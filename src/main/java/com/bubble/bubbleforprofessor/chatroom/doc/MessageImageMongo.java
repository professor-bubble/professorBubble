package com.bubble.buubleforprofessor.chatroom.doc;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "message_images")
@NoArgsConstructor
@Getter
public class MessageImageMongo {
    @Id
    private String id;
    @DBRef
    private MessageMongo messageMongo;
    private String imageUrl;
}
