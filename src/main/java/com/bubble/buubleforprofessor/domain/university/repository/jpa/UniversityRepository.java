package com.bubble.buubleforprofessor.domain.university.repository.jpa;

import com.bubble.buubleforprofessor.domain.university.entity.University;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("UniversityRepository")
public interface UniversityRepository extends JpaRepository<University, Long> {
    @Modifying
    @Query("UPDATE University u SET u.isDeleted = true WHERE u.universityId NOT IN :apiIds")
    int deleteNotIn(@Param("apiIds") Set<Long> apiIds);
}

