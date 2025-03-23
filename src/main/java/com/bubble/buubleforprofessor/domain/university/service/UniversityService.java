package com.bubble.buubleforprofessor.domain.university.service;

import com.bubble.buubleforprofessor.domain.university.dto.request.UniversityApiRequest;
import reactor.core.publisher.Mono;

public interface UniversityService {
    void saveAllUniversities(UniversityApiRequest uniRequest);
    Mono<Integer> onelist(UniversityApiRequest request);
}
