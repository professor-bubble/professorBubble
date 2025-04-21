package com.bubble.bubbleforprofessor.chatroom.repository;

import com.bubble.bubbleforprofessor.chatroom.entity.Chatroom;
import com.bubble.bubbleforprofessor.user.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Integer> {
    boolean existsChatroomByProfessor(Professor professor);
}
