package com.bubble.bubbleforprofessor.skin.repository;

import com.bubble.bubbleforprofessor.skin.entity.UserSkin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserSkinRepository extends JpaRepository<UserSkin, Integer> {
    @EntityGraph(attributePaths = {"skin","skin.category","skin.skinImages"})
    Page<UserSkin> getSkinsByUserId(UUID userID, Pageable pageable);

    UserSkin findByUserIdAndSkinId(UUID userId, int skinId);
}
