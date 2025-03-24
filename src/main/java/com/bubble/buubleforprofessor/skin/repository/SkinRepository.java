package com.bubble.buubleforprofessor.skin.repository;

import com.bubble.buubleforprofessor.skin.entity.Skin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SkinRepository extends JpaRepository<Skin, Integer> {

}
