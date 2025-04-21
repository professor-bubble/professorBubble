package com.bubble.bubbleforprofessor.skin.repository;

import com.bubble.bubbleforprofessor.skin.entity.Skin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkinRepository extends JpaRepository<Skin, Integer> {

}
