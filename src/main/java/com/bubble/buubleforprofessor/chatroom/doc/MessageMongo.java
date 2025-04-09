package com.bubble.buubleforprofessor.chatroom.doc;

import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.buubleforprofessor.chatroom.entity.Message;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "messages")
@NoArgsConstructor
@Getter
public class MessageMongo {
    //Mongodb에서는 String 타입으로 해야 자동생성
    @Id
    private String id;

    private UUID userId;
    private int chatroomId;
    private String userName;

    private Message.MessageType messageType;
    private LocalDateTime sendTime;

    private String content;

    @Builder
    public MessageMongo(UUID userId,int chatroomId, String userName,Message.MessageType messageType,LocalDateTime sendTime, String content) {
        this.userId = userId;
        this.chatroomId = chatroomId;
        this.userName = userName;
        this.messageType = messageType;
        this.sendTime = sendTime;
        this.content = content;
    }

}
