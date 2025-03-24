package com.bubble.buubleforprofessor.user.repository;

import com.bubble.buubleforprofessor.user.entity.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, UUID> {
    Page<Professor> findAllByIsApprovedFalse(Pageable pageable);
}
