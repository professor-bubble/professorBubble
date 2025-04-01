package com.bubble.buubleforprofessor.chatroom.repository;

import com.bubble.buubleforprofessor.chatroom.entity.MessageImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageImageRepository extends JpaRepository<MessageImage, Integer> {
}
