package com.bubble.buubleforprofessor.domain.university.controller;

import com.bubble.buubleforprofessor.domain.university.dto.request.UniversityApiRequest;
import com.bubble.buubleforprofessor.domain.university.entity.University;
import com.bubble.buubleforprofessor.domain.university.service.UniversityService;
import com.bubble.buubleforprofessor.global.config.CustomException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name="대학교 검색 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/university")
public class UniversityController {

    private final UniversityService universityService;

    @GetMapping("/list")
    public Mono<ResponseEntity<String>> universityList() {
        log.info("컨트롤러  : universityList 시작");
        UniversityApiRequest uniRequest = new UniversityApiRequest();
        return universityService.saveAllUniversities(uniRequest)
                .doOnSuccess(v -> log.info("컨트롤러  : universityList 완료"))
                .thenReturn(ResponseEntity.ok("성공"));
    }




    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> universitySearch(@RequestParam String uniname) {
        try {
            List<University> universities = universityService.searchUniversity(uniname);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 200);
            response.put("message", "대학교 조회 성공");
            response.put("data", universities);
            response.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC).toString());
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", e.getErrorCode().getStatus().value());
            errorResponse.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC).toString());
            Map<String, String> error = new HashMap<>();
            error.put("errorCode", e.getErrorCode().name());
            error.put("errorMessage", e.getErrorCode().getMsg());
            errorResponse.put("error", error);
            return ResponseEntity.status(e.getErrorCode().getStatus())
                    .body(errorResponse);
        }
    }

    //테스트 용
    @GetMapping("/totalCount")
    public Mono<ResponseEntity<Integer>> test() {
        UniversityApiRequest request = new UniversityApiRequest();
        log.info("컨트롤러  : university 총 갯수 테스트");
        return universityService.onelist(request)
                .map(totalCount -> ResponseEntity.ok(totalCount));
    }
}
