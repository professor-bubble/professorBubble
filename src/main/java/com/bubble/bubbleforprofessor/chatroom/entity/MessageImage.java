package com.bubble.bubbleforprofessor.chatroom.entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_image_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "message_id",referencedColumnName = "message_id")
    private Message message;

    private String url;

    @Builder
    public MessageImage(Message message, String url) {
        this.message = message;
        this.url = url;
    }
}