package com.bubble.buubleforprofessor.chatroom.entity;

import com.bubble.buubleforprofessor.user.entity.Professor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chatrooms")
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private int id;

    private LocalDateTime createdAt=LocalDateTime.now();

    private Timestamp lastSeenAt = Timestamp.valueOf(LocalDateTime.now());

    @OneToOne
    @JoinColumn(name = "professor_id",referencedColumnName = "professor_id")
    private Professor professor;

    public Chatroom(Professor professor) {
        this.professor = professor;
    }
}
