package com.bubble.buubleforprofessor.domain.university.repository.elasticsearch;

import com.bubble.buubleforprofessor.domain.university.entity.University;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("UniversitySearchRepository")
public interface UniversitySearchRepository extends ElasticsearchRepository<University, Long> {
    List<University> findByUniversityName(String name);
}

