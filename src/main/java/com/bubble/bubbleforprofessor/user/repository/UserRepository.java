package com.bubble.bubbleforprofessor.user.repository;


import com.bubble.bubbleforprofessor.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByLoginId(String loginId);

    User findByLoginId(String loginId);

    boolean existsByEmail(String email);
}
