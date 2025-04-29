package com.bubble.bubbleforprofessor.chatroom.repository;

import com.bubble.bubbleforprofessor.chatroom.entity.Chatroom;
import com.bubble.bubbleforprofessor.chatroom.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatroomUser_Chatroom(Chatroom chatroom);
    List<Message> findByChatroomUser_ChatroomAndChatroomUser_User_IdIn(Chatroom chatroom, List<UUID> userIds);

}
