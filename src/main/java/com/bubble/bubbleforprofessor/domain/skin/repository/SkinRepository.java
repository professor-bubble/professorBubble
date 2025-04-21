package com.bubble.bubbleforprofessor.domain.skin.repository;

import com.bubble.bubbleforprofessor.domain.skin.entity.Skin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkinRepository extends JpaRepository<Skin, Long> {

}