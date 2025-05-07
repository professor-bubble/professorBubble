package com.bubble.bubbleforprofessor.chatroom.repository;

import com.bubble.bubbleforprofessor.chatroom.entity.Chatroom;
import com.bubble.bubbleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.bubbleforprofessor.chatroom.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatroomUser_Chatroom(Chatroom chatroom);
    Optional<Message> findByChatroomUser(ChatroomUser chatroomUser);
}
