package com.bubble.buubleforprofessor.chatroom.service.impl;

import com.bubble.buubleforprofessor.chatroom.doc.MessageMongo;
import com.bubble.buubleforprofessor.chatroom.dto.MessageRequestDto;
import com.bubble.buubleforprofessor.chatroom.dto.MessageSimpleDto;
import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.buubleforprofessor.chatroom.entity.Message;
import com.bubble.buubleforprofessor.chatroom.entity.MessageImage;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.buubleforprofessor.chatroom.repository.MessageImageRepository;
import com.bubble.buubleforprofessor.chatroom.repository.MessageMongoRepository;
import com.bubble.buubleforprofessor.chatroom.repository.MessageRepository;
import com.bubble.buubleforprofessor.chatroom.service.ChatroomUserService;
import com.bubble.buubleforprofessor.chatroom.service.MessageService;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import com.vane.badwordfiltering.BadWordFiltering;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatroomUserRepository chatroomUserRepository;

    private final ChatroomUserService chatroomUserService;

    private final MessageMongoRepository messageMongoRepository;

    private final MessageImageRepository messageImageRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final BadWordFiltering badWordFiltering;


    @Override
    public MessageMongo save(MessageRequestDto message) {
        String content = message.getContent();
        Message.MessageType messageType=Message.MessageType.valueOf("TEXT");
        //object스토리지로 저장해야할거같음
        // content가 이미지 파일 데이터 (Base64 인코딩된 문자열)인지 확인합니다.
        if (content != null && content.startsWith("data:image/")) {
            try {
                // "data:image/png;base64,..." 형태에서 comma 위치 찾기
                int commaIndex = content.indexOf(",");
                if (commaIndex > 0) {
                    String metadata = content.substring(0, commaIndex);  // 예: "data:image/png;base64"
                    String base64Data = content.substring(commaIndex + 1);

                    // 확장자 결정 (기본값은 png)
                    String extension = ".png";
                    if (metadata.contains("image/jpeg")) {
                        extension = ".jpg";
                    } else if (metadata.contains("image/gif")) {
                        extension = ".gif";
                    }

                    // Base64 데이터 디코딩
                    byte[] fileBytes = Base64.getDecoder().decode(base64Data);

                    // 고유 파일명 생성
                    String uniqueFileName = UUID.randomUUID().toString() + extension;

                    // 파일 저장 경로 설정 (예: uploads/ 디렉토리)
                    String uploadDir = Paths.get("uploads").toAbsolutePath().toString() + File.separator;
                    File uploadDirectory = new File(uploadDir);
                    if (!uploadDirectory.exists()) {
                        uploadDirectory.mkdirs();
                    }
                    File saveFile = new File(uploadDir + uniqueFileName);
                    Files.write(saveFile.toPath(), fileBytes);

                    //임시로 서버에 저장하고 , 상대경로로 가져오도록 해둠.
                    content = "/uploads/" + uniqueFileName;
                    messageType=Message.MessageType.valueOf("IMAGE");
                }
            } catch (IOException e) {
                throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }

        // 메세지마다 쿼리 날리는현상. 캐싱으로 해결. EnableCaching 또한 붙여주어야함.
        ChatroomUser chatroomUser = chatroomUserService.getUserByUserIdAndChatroomId(message.getUserId(),message.getChatRoomId());

        //MongoDB에 저장
        MessageMongo messageMongo = MessageMongo.builder()
                .userId(chatroomUser.getUser().getId())
                .chatroomId(chatroomUser.getChatroom().getId())
                .userName(chatroomUser.getUser().getName())
                .messageType(messageType)
                .sendTime(LocalDateTime.now())
                .content(content)
                .build();

        return messageMongoRepository.save(messageMongo);
    }

    @Override
    public void send(int chatroomId, String userRole,MessageSimpleDto simpleDto) {
        //유저는 채팅방 구독 , 자기자신 구독.
        //교수는 자기자신 구독
        if(userRole.equals("PROFESSOR"))
        {
            //교수가 보내는곳. 채팅방과(유저 전체) 교수(자신)
            messagingTemplate.convertAndSend("/sub/chatroom/" + chatroomId, simpleDto);
            messagingTemplate.convertAndSend("/sub/chatroom/" + chatroomId+"/professor", simpleDto);
        }
        else
        {
            //유저가 보내는곳.교수 유저(자신)
            messagingTemplate.convertAndSend("/sub/chatroom/" + chatroomId+"/professor", simpleDto);
            messagingTemplate.convertAndSend("/sub/user/" +simpleDto.getUserId(),simpleDto);
        }
    }
    @Override
    public MessageRequestDto filtering(MessageRequestDto messageRequestDto)
    {
        if(badWordFiltering.blankCheck(messageRequestDto.getContent()))
        {
            String changeContent= badWordFiltering.change(messageRequestDto.getContent());
            messageRequestDto.setContent(changeContent);
        }
        return messageRequestDto;
    }
    @Scheduled(fixedRate = 300000)
    public void transferMessagesToRdb() {
        log.info("transferMessagesToRdb 시작: {}", LocalDateTime.now());
        // 최근 5분 전부터 현재까지의 MongoDB 메시지 조회
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        LocalDateTime now = LocalDateTime.now();
        List<MessageMongo> recentMessages = messageMongoRepository.findBySendTimeBetween(fiveMinutesAgo, now);

        if (recentMessages.isEmpty()) {
            return; // 조회된 메시지가 없으면 바로 종료
        }

        // 1. 최근 메시지에서 필요한 userId와 chatroomId를 모두 추출 (중복 제거)
        Set<UUID> userIds = recentMessages.stream()
                .map(MessageMongo::getUserId)
                .collect(Collectors.toSet());
        Set<Integer> chatroomIds = recentMessages.stream()
                .map(MessageMongo::getChatroomId)
                .collect(Collectors.toSet());

        // 2. userId와 chatroomId에 해당하는 ChatroomUser들을 한 번에 조회
        // repository에 아래와 같이 메서드를 추가했다고 가정합니다.
        // List<ChatroomUser> findByUserIdInAndChatroomIdIn(Set<UUID> userIds, Set<Integer> chatroomIds);
        List<ChatroomUser> chatroomUsers = chatroomUserRepository.findByUserIdInAndChatroomIdIn(userIds, chatroomIds);

        // 3. ChatroomUser를 Map으로 변환 (키: "userId_chatroomId")
        Map<String, ChatroomUser> chatroomUserMap = chatroomUsers.stream()
                .collect(Collectors.toMap(
                        cu -> cu.getUser().getId().toString() + "_" + cu.getChatroom().getId(),
                        Function.identity()
                ));
        List<MessageImage> messageImages =new ArrayList<>();
        // 4. 각 MessageMongo를 Message 엔티티로 변환 (매핑 과정에서 Map을 사용)
        List<Message> messages = recentMessages.stream()
                .map(messageMongo -> {
                    String key = messageMongo.getUserId().toString() + "_" + messageMongo.getChatroomId();
                    ChatroomUser chatroomUser = chatroomUserMap.get(key);
                    if (chatroomUser == null) {
                        throw new CustomException(ErrorCode.NON_EXISTENT_CHATROOM_USER);
                    }
                    Message message = Message.builder()
                            .chatroomUser(chatroomUser)
                            .messageType(messageMongo.getMessageType())
                            .sendTime(messageMongo.getSendTime())
                            .content(messageMongo.getContent())
                            .build();
                    if(messageMongo.getMessageType()==Message.MessageType.IMAGE)
                    {
                        messageImages.add(MessageImage.builder()
                                .message(message)
                                .url(message.getContent())
                                .build());
                    }
                    return message;
                }).toList();


        // 5. 배치로 RDB에 저장 (한 번에 saveAll 호출)
        messageRepository.saveAll(messages);

        messageImageRepository.saveAll(messageImages);
    }



}
