package com.bubble.buubleforprofessor.user.repository;

import com.bubble.buubleforprofessor.user.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, UUID> {
    List<Professor> findAllByIsApprovedFalse();
}
