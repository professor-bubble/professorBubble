package com.bubble.bubbleforprofessor.university.repository.es;

import com.bubble.bubbleforprofessor.university.document.UniversityDocument;
import com.bubble.bubbleforprofessor.university.entity.University;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("ElasticSearchRepository")
public interface UniversityElasticSearchRepository extends ElasticsearchRepository<UniversityDocument, Long> {
    List<UniversityDocument> findByUniversityNameContaining(String name);


    // 부분 일치 검색으로 변경
    // 기존. findByUniversityName은 정확한 값이 필요했음
    // findByUniversityNameContaining -> 부분 일치 검색으로 "서울" 이라고만 검색해도 포함된 검색어가 다나옴
}