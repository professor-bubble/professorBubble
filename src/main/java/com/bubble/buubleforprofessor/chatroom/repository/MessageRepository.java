package com.bubble.buubleforprofessor.chatroom.repository;

import com.bubble.buubleforprofessor.chatroom.entity.Chatroom;
import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;
import com.bubble.buubleforprofessor.chatroom.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatroomUser_Chatroom(Chatroom chatroom);
    Optional<Message> findByChatroomUser(ChatroomUser chatroomUser);
}
