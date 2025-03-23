package com.bubble.buubleforprofessor.domain.university.repository;

import com.bubble.buubleforprofessor.domain.university.entity.University;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
}

