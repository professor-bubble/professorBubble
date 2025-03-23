package com.bubble.buubleforprofessor.domain.university.service;

import com.bubble.buubleforprofessor.domain.university.dto.request.UniversityApiRequest;
import com.bubble.buubleforprofessor.domain.university.dto.response.UniversityApiResponse;
import com.bubble.buubleforprofessor.domain.university.entity.University;
import com.bubble.buubleforprofessor.domain.university.repository.UniversityRepository;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;
    private final WebClient webClient;


    @Transactional
    @Override
    public Mono<Void> saveAllUniversities(UniversityApiRequest uniRequest) {
        // Step 1: 초기 값 요청으로 totalCount 가져오기
        uniRequest.setNumOfRows(1);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/getUniversity.do")
                        .queryParam("serviceKey", uniRequest.getServiceKey())
                        .queryParam("pageNo", uniRequest.getPageNo())
                        .queryParam("numOfRows", uniRequest.getNumOfRows())
                        .queryParam("dataType", uniRequest.getDataType())
                        .queryParam("Fclty_Cd", uniRequest.getFcltyCd())
                        .build())
                .retrieve()
                .bodyToMono(UniversityApiResponse.class)
                .flatMap(initResponse -> {
                    // null 체크
                    if (initResponse == null) {
                        return Mono.error(new CustomException(ErrorCode.UNI_API_RESPONSE_NULL));
                    }
                    if (initResponse.getBody() == null) {
                        return Mono.error(new CustomException(ErrorCode.BODY_MISSING));
                    }

                    // totalCount 추출
                    int totalCount = initResponse.getBody().getTotalCount();
                    log.info("Total count: {}", totalCount);

                    // Step 2: numOfRows를 totalCount로 설정
                    uniRequest.setNumOfRows(totalCount);

                    // Step 3: 전체 데이터 요청
                    return webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/data/getUniversity.do")
                                    .queryParam("serviceKey", uniRequest.getServiceKey())
                                    .queryParam("pageNo", uniRequest.getPageNo())
                                    .queryParam("numOfRows", uniRequest.getNumOfRows())
                                    .queryParam("dataType", uniRequest.getDataType())
                                    .queryParam("Fclty_Cd", uniRequest.getFcltyCd())
                                    .build())
                            .retrieve()
                            .bodyToMono(UniversityApiResponse.class);
                })
                .flatMap(fullResponse -> {
                    // null 체크
                    if (fullResponse == null) {
                        return Mono.error(new CustomException(ErrorCode.UNI_API_RESPONSE_NULL));
                    }
                    if (fullResponse.getBody() == null) {
                        return Mono.error(new CustomException(ErrorCode.BODY_MISSING));
                    }

                    // Step 4: DB에 저장
                    List<University> universities = fullResponse.getBody().getItems().stream()
                            .map(items -> University.builder()
                                    .universityName(items.getUniversityName())
                                    .build())
                            .collect(Collectors.toList());

                    // JPA 동기 호출을 비동기화
                    return Mono.fromCallable(() -> {
                                universityRepository.saveAll(universities); // 동기 JPA 호출
                                return null; // Void 반환을 위해
                            })
                            .subscribeOn(Schedulers.boundedElastic()) // 블록킹 작업 별도 스레드에서 실행
                            .doOnSuccess(v -> log.info("저장갯수 {} universities to DB", universities.size()))
                            .then();
                });
    }

    @Override
    public Mono<Integer> onelist(UniversityApiRequest request) {
        request.setNumOfRows(1);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/getUniversity.do")
                        .queryParam("serviceKey", request.getServiceKey())
                        .queryParam("pageNo", request.getPageNo())
                        .queryParam("numOfRows", request.getNumOfRows())
                        .queryParam("dataType", request.getDataType())
                        .queryParam("Fclty_Cd", request.getFcltyCd())
                        .build())
                .retrieve()
                .bodyToMono(UniversityApiResponse.class)
                .map(response -> {
                    if (response == null) {
                        throw new CustomException(ErrorCode.UNI_API_RESPONSE_NULL);
                    }
                    if (response.getBody() == null) {
                        throw new CustomException(ErrorCode.BODY_MISSING);
                    }
                    return response.getBody().getTotalCount();
                });
    }
}