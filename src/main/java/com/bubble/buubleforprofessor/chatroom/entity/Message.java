package com.bubble.buubleforprofessor.chatroom.entity;

import com.bubble.buubleforprofessor.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="messages")
@Getter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="message_id")
    private long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="chatroom_id",referencedColumnName = "chatroom_id"),
            @JoinColumn(name="user_id",referencedColumnName = "user_id")
    })
    private ChatroomUser chatroomUser;

    private LocalDateTime sendTime;

    private String content;

    @Builder
    public Message(ChatroomUser chatroomUser, LocalDateTime sendTime, String content) {
        this.chatroomUser = chatroomUser;
        this.sendTime = sendTime;
        this.content = content;
    }
}
