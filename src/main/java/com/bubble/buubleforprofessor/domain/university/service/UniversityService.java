package com.bubble.buubleforprofessor.domain.university.service;

import com.bubble.buubleforprofessor.domain.university.dto.request.UniversityApiRequest;
import com.bubble.buubleforprofessor.domain.university.entity.University;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UniversityService {
    Mono<Void> saveAllUniversities(UniversityApiRequest uniRequest);
    Mono<Integer> onelist(UniversityApiRequest request);

    List<University> searchUniversity(String uniname);
}
