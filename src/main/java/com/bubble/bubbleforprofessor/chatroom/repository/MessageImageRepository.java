package com.bubble.bubbleforprofessor.chatroom.repository;

import com.bubble.bubbleforprofessor.chatroom.entity.MessageImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageImageRepository extends JpaRepository<MessageImage, Integer> {
}
