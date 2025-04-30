package com.bubble.bubbleforprofessor.university.repository.jpa;

import com.bubble.bubbleforprofessor.university.entity.University;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository("UniversityRepository")
public interface UniversityRepository extends JpaRepository<University, Long> {
}