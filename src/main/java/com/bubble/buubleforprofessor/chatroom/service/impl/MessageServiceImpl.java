package com.bubble.buubleforprofessor.chatroom.service.impl;

import com.bubble.buubleforprofessor.chatroom.dto.MessageRequestDto;
import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.buubleforprofessor.chatroom.entity.Message;
import com.bubble.buubleforprofessor.chatroom.repository.ChatroomUserRepository;
import com.bubble.buubleforprofessor.chatroom.repository.MessageRepository;
import com.bubble.buubleforprofessor.chatroom.service.MessageService;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatroomUserRepository chatroomUserRepository;

    @Override
    public Message findByUserIdAndChatroomId(UUID userId, int chatroomId) {
        ChatroomUser chatroomUser = chatroomUserRepository.findByUserIdAndChatroomId(userId,chatroomId).orElseThrow(()->new CustomException(ErrorCode.NON_EXISTENT_CHATROOM_USER));
        Message message=messageRepository.findByChatroomUser(chatroomUser).orElseThrow(()->new CustomException(ErrorCode.NON_EXISTENT_MESSAGE));
        return message;
    }

    @Override
    public Message save(MessageRequestDto message) {
        String content = message.getContent();
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
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new CustomException(ErrorCode.NON_EXISTENT_MESSAGE);
                //todo 에러코드 만들것
            }
        }

        // chatroomUser 조회 및 메시지 엔티티 생성
        Message messageEntity = Message.builder()
                .chatroomUser(chatroomUserRepository.findByUserIdAndChatroomId(message.getUserId(), message.getChatRoomId())
                        .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_CHATROOM_USER)))
                .sendTime(LocalDateTime.now())
                .content(content)
                .build();

        return messageRepository.save(messageEntity);
    }
}
