package com.bubble.buubleforprofessor.domain.university.repository.es;

import com.bubble.buubleforprofessor.domain.university.entity.University;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("ElasticSearchRepository")
public interface ElasticSearchRepository extends ElasticsearchRepository<University, Long> {
    List<University> findByUniversityNameContaining(String name);


    //todo 부분 일치 검색으로 변경
    // 기존. findByUniversityName은 정확한 값이 필요했음
    // findByUniversityNameContaining -> 부분 일치 검색으로 "서울" 이라고만 검색해도 포함된 검색어가 다나옴
}

