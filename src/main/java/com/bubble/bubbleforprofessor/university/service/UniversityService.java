package com.bubble.bubbleforprofessor.university.service;

import com.bubble.bubbleforprofessor.university.dto.request.UniversityApiRequest;
import com.bubble.bubbleforprofessor.university.entity.University;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UniversityService {
    Mono<Void> saveAllUniversities(UniversityApiRequest uniRequest);
    Mono<Integer> onelist(UniversityApiRequest request);

    List<University> searchUniversity(String uniname);
}
