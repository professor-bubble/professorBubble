package com.bubble.buubleforprofessor.domain.university.service;

import com.bubble.buubleforprofessor.domain.university.dto.request.UniversityApiRequest;
import com.bubble.buubleforprofessor.domain.university.dto.response.UniversityApiResponse;
import com.bubble.buubleforprofessor.domain.university.entity.University;
import com.bubble.buubleforprofessor.domain.university.repositoy.UniversityRepository;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
    public void saveAllUniversities(UniversityApiRequest uniRequest) {
        // Step 1: 초기 값 요청으로 totalCount 가져오기
        uniRequest.setNumOfRows(1);

        UniversityApiResponse initResponse = webClient.get()
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
                .block(); // 동기처리

        if (initResponse == null) {
            throw new CustomException(ErrorCode.UNI_API_RESPONSE_NULL);
        }

        if (initResponse.getBody() == null) {
            throw new CustomException(ErrorCode.BODY_MISSING);
        }

        // totalCount 추출
        int totalCount = initResponse.getBody().getTotalCount();
        log.info("Total count: {}", totalCount);

        // Step 2: numOfRows를 totalCount로 설정
        uniRequest.setNumOfRows(totalCount);

        // Step 3: 전체 데이터 요청
        UniversityApiResponse fullResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/getUniversity.do")
                        .queryParam("serviceKey", uniRequest.getServiceKey())
                        .queryParam("pageNo", uniRequest.getPageNo())
                        .queryParam("numOfRows", uniRequest.getNumOfRows())
                        .queryParam("dataType", uniRequest.getDataType()) // 오타 수정
                        .queryParam("Fclty_Cd", uniRequest.getFcltyCd())
                        .build())
                .retrieve()
                .bodyToMono(UniversityApiResponse.class)
                .block();

        if (fullResponse == null) {
            throw new CustomException(ErrorCode.UNI_API_RESPONSE_NULL);
        }

        if (fullResponse.getBody() == null) {
            throw new CustomException(ErrorCode.BODY_MISSING);
        }

        // Step 4: DB에 저장 (UniversityList -> University 변환, 타입불일치 문제필요)
        List<University> universities = fullResponse.getBody().getItems().stream()
                .map(items -> University.builder()
                        .universityName(items.getUniversityName())
                        .build())
                .collect(Collectors.toList()); // 변환된 결과 다시 리스트로 모음
        universityRepository.saveAll(universities);
        log.info("저장갯수 {} universities to DB", universities.size());
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