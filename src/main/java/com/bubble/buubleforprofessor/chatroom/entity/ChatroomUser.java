package com.bubble.buubleforprofessor.chatroom.entity;

import com.bubble.buubleforprofessor.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chatroom_user")
public class ChatroomUser {

    @EmbeddedId
    private ChatroomUserId id;

    @ManyToOne
    @MapsId("chatroomId")
    @JoinColumn(name = "chatroom_id",referencedColumnName = "chatroom_id")
    private Chatroom chatroom;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private User user;

    public ChatroomUser(Chatroom chatroom, User user) {
        this.chatroom = chatroom;
        this.user = user;
        this.id = new ChatroomUserId(chatroom.getId(), user.getId());  // 복합 키 생성
    }
}

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ChatroomUserId implements Serializable {
    private long chatroomId;
    private UUID userId;

    public ChatroomUserId(long chatroomId, UUID userId) {
        this.chatroomId = chatroomId;
        this.userId = userId;
    }

    // equals와 hashCode 구현 (복합 키 비교를 위해 필수)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatroomUserId that = (ChatroomUserId) o;
        return chatroomId == that.chatroomId && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return 31 * Long.hashCode(chatroomId) + userId.hashCode();
    }
}

