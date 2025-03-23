package com.bubble.buubleforprofessor.domain.university.controller;

import com.bubble.buubleforprofessor.domain.university.dto.request.UniversityApiRequest;
import com.bubble.buubleforprofessor.domain.university.service.UniversityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name="대학교 검색 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/university")
public class UniversityController {

    private final UniversityService universityService;

    @GetMapping("/list")
    public ResponseEntity<String> universityList() {
        log.info("컨트롤러  : universityList 시작");
        UniversityApiRequest uniRequest = new UniversityApiRequest();
        universityService.saveAllUniversities(uniRequest);
        log.info("컨트롤러  : universityList 완료");
        return ResponseEntity.ok("성공");
    }

    @GetMapping("/totalCount")
    public Mono<ResponseEntity<Integer>> test() {
        UniversityApiRequest request = new UniversityApiRequest();
        log.info("컨트롤러  : university 총 갯수 테스트");
        return universityService.onelist(request)
                .map(totalCount -> ResponseEntity.ok(totalCount));
    }
}
