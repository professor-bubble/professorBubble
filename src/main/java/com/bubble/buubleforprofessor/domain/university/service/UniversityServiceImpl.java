package com.bubble.buubleforprofessor.domain.university.service;

import com.bubble.buubleforprofessor.domain.university.dto.request.UniversityApiRequest;
import com.bubble.buubleforprofessor.domain.university.dto.response.UniversityApiResponse;
import com.bubble.buubleforprofessor.domain.university.entity.University;
import com.bubble.buubleforprofessor.domain.university.repository.es.ElasticSearchRepository;
import com.bubble.buubleforprofessor.domain.university.repository.jpa.UniversityRepository;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/*
Mono<T> :  최대 1개의 데이터를 비동기로 감싸는 컨테이너
subscribeOn : 오래걸리는 작업을 메인스레드가 별도의 스레드(boundedElastic)에서 실행하게 함
flatMap : Mono 안에 또 다른 Mono를 반환하는 함수, 비동기 작업 안에서 또 다른 비동기 작업을 할 때 사용
 */


@Service
@Slf4j
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    private final WebClient webClient;

    private final ElasticSearchRepository esSearchRepository;


    @PostConstruct
    public void init(){
        log.info("초기 실행시 대학교 데이터 초기 로드 시작");
        saveAllUniversities(new UniversityApiRequest())
                .subscribe(null,  // onSuccess: Void이므로 null
                        error -> log.error("초기 로드 중 오류 발생: {}", error.getMessage()));
    }

    @Scheduled(cron ="0 0 0 22 11 *") //매년 11월 22일 00:00에 실행
    public void universityUpdate(){
        LocalDate today = LocalDate.now();
        if(today.getMonthValue() == 11 && today.getDayOfMonth() == 22){
            log.info("11월 22일 대학교 데이터 업데이트 시작");
            saveAllUniversities(new UniversityApiRequest()).subscribe(null,
                    error -> log.error("업데이트 중 오류 발생: {}", error.getMessage())  // 오류 처리
            );
        }
    }

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
                .retrieve()//비동기시작, 응답 받아오기
                .bodyToMono(UniversityApiResponse.class)
                .flatMap(initResponse -> {
                    // null 체크 비동기 요청이 완료료 되면 호출
                    if (initResponse == null) {
                        return Mono.error(new CustomException(ErrorCode.UNI_API_RESPONSE_NULL));
                    }
                    if (initResponse.getBody() == null) {
                        return Mono.error(new CustomException(ErrorCode.BODY_MISSING));
                    }

                    // totalCount 추출
                    int totalCount = initResponse.getBody().getTotalCount();
                    log.info("Total count: {}", totalCount);
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
                                    .universityId(items.getObjectId())
                                    .universityName(items.getUniversityName())
                                    .isDeleted(false)
                                    .build())
                            .collect(Collectors.toList());

                    return Mono.fromRunnable(() -> saveToDb(universities))
                            .subscribeOn(Schedulers.boundedElastic())
                            .onErrorResume(e -> {
                                log.error("DB 저장 중 오류 발생: {}", e.getMessage());
                                return Mono.empty();
                            });
                })
                .then();
    }

    @Transactional
    protected void saveToDb(List<University> newUniversities){
        Set<Long> apiUniversityIds = newUniversities.stream()
                .map(University::getUniversityId)
                .collect(Collectors.toSet());

        List<University> existUniversities = universityRepository.findAll();
        Map<Long, University> dbUniversityMap = existUniversities.stream()
                .collect(Collectors.toMap(University::getUniversityId, Function.identity()));

        List<University> universitiesToSave = new ArrayList<>();


        for (University apiUniversity : newUniversities) {
            University dbUniversity = dbUniversityMap.get(apiUniversity.getUniversityId());

            if (dbUniversity == null) {
                universitiesToSave.add(apiUniversity);
            } else {
                // 대학이름 변경될시 업데이트
                if (!dbUniversity.getUniversityName().equals(apiUniversity.getUniversityName())) {
                    dbUniversity.setUniversityName(apiUniversity.getUniversityName());
                }
                // Soft Delete 복구 (만약 삭제되었다면 다시 활성화)
                if (dbUniversity.isDeleted()) {
                    dbUniversity.setDeleted(false);
                }
                universitiesToSave.add(dbUniversity);
                dbUniversityMap.remove(apiUniversity.getUniversityId());
            }
        }

        // DB에만 존재하고 API에서 사라진 데이터 → Soft Delete 적용
        for (University deletedUniversity : dbUniversityMap.values()) {
            deletedUniversity.setDeleted(true);
            universitiesToSave.add(deletedUniversity);
        }

        universityRepository.saveAll(universitiesToSave);
        indexUniversityies(universitiesToSave);

        log.info("저장된 대학교 갯수: {}, 삭제된 대학교 갯수: {}",
                universitiesToSave.size(), dbUniversityMap.size());
    }




    //대학교list DB -> Elasticsearch에 저장
    private void indexUniversityies(List<University> universities){
        esSearchRepository.saveAll(universities);
        log.info("색인된 대학교 갯수 : {} ", universities.size());
    }


    //검색 메서드
    public List<University> searchUniversity(String uniname){
        if (uniname == null || uniname.trim().isEmpty()) { //null 확인 및  앞뒤 공백을 제거한 후 빈 문자열인지 확인
            throw new CustomException(ErrorCode.UNIVERSITYNAME_INVALID_REQUEST);
        }
        List<University> universities = esSearchRepository.findByUniversityNameContaining(uniname);
        if (universities.isEmpty()) {
            throw new CustomException(ErrorCode.UNIVERSITY_NOT_FOUND);
        }
        return universities;
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