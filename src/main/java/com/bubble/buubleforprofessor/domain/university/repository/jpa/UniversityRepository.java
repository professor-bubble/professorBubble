package com.bubble.buubleforprofessor.domain.university.repository.jpa;

import com.bubble.buubleforprofessor.domain.university.entity.University;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("UniversityRepository")
public interface UniversityRepository extends JpaRepository<University, Long> {
}

