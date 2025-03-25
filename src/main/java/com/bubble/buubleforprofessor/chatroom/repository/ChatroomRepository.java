package com.bubble.buubleforprofessor.chatroom.repository;

import com.bubble.buubleforprofessor.chatroom.entity.Chatroom;
import com.bubble.buubleforprofessor.user.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    boolean existsChatroomByProfessor(Professor professor);
}
