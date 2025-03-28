package com.bubble.buubleforprofessor.chatroom.repository;

import com.bubble.buubleforprofessor.chatroom.entity.ChatroomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatroomUserRepository extends JpaRepository<ChatroomUser, Long> {
    boolean existsByUserIdAndChatroomId(UUID userId, int chatroomId);
    List<ChatroomUser> findByChatroomId(int chatroomId);
    Optional<ChatroomUser> findByUserIdAndChatroomId(UUID userId, int chatroomId);
}
