package com.bubble.buubleforprofessor.chatroom.controller;

import com.bubble.buubleforprofessor.chatroom.entity.Message;
import com.bubble.buubleforprofessor.chatroom.entity.MessageImage;
import com.bubble.buubleforprofessor.chatroom.service.MessageImageService;
import com.bubble.buubleforprofessor.chatroom.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MessageImageController {

    private final MessageImageService messageImageService;
    private final MessageService messageService;
    private final String uploadDir = "uploads/";

    @PostMapping(value="/api/upload/message-image", produces = "application/json")
    public ResponseEntity<Map<String, Object>> uploadMessageImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") UUID userId,
            @RequestParam("chatroomId") int chatroomId) throws IOException {

        // 해당 채팅방에 이미 메시지가 존재하는지 또는 새 메시지를 생성할 것인지 결정
        Message message = messageService.findByUserIdAndChatroomId(userId, chatroomId);
        if(message == null) {
            // 예를 들어, 메시지가 없다면 새 메시지를 생성할 수도 있습니다.
            // 이 부분은 설계에 따라 달라질 수 있습니다.
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Message not found"));
        }

        // 업로드 디렉토리 생성
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        // 원본 파일명과 확장자 처리
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        // 파일 저장
        File saveFile = new File(uploadDir + uniqueFileName);
        file.transferTo(saveFile);

        // 이미지 URL 생성 (예: "/uploads/uuid_filename.jpg")
        String imageUrl = "/" + uploadDir + uniqueFileName;

        // MessageImage 엔티티 생성 및 저장 (원하는 경우)
        MessageImage messageImage = MessageImage.builder()
                .message(message)
                .url(imageUrl)
                .build();
        messageImageService.save(messageImage);

        // 반환값 구성
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("imageUrl", imageUrl);
        return ResponseEntity.ok(response);
    }

}
