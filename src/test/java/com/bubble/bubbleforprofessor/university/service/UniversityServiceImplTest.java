package com.bubble.bubbleforprofessor.university.service;

import com.bubble.bubbleforprofessor.university.document.UniversityDocument;
import com.bubble.bubbleforprofessor.university.dto.request.UniversityApiRequest;

import com.bubble.bubbleforprofessor.university.dto.response.Body;
import com.bubble.bubbleforprofessor.university.dto.response.UniversityApiResponse;
import com.bubble.bubbleforprofessor.university.dto.response.UniversityList;
import com.bubble.bubbleforprofessor.university.entity.University;
import com.bubble.bubbleforprofessor.university.repository.es.UniversityElasticSearchRepository;
import com.bubble.bubbleforprofessor.university.repository.jpa.UniversityRepository;

import com.bubble.bubbleforprofessor.university.service.UniversityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UniversityServiceImplTest {

    @Mock
    UniversityRepository universityRepository;

    /*Mock :  가짜로 만든 객체
     * InjectMoks : 테스트 대상에 Mock 주입
     * Mockito는 when 설정을 테스트가 시작되기 전에 모두 메모리에 저장*/

    @Mock
    private WebClient webClient;

    // WebClient의 내부 객체들도 모킹
    /*WebClient.RequestHeadersUriSpec, RequestHeadersSpec, ResponseSpec: WebClient 내부에서 HTTP 요청을 처리하는 인터페이스
     * uriSpec: URL을 설정하는 가짜 객체
     * headersSpec: 헤더를 설정하고 요청을 준비하는 가짜 객체
     * responseSpec: 응답을 처리하는 가짜 객체
     * */

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private UniversityElasticSearchRepository esSearchRepository;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    public void setUp() {
        // WebClient 동작을 모킹하기 위한 설정
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @InjectMocks
    private UniversityServiceImpl universityServiceImplTest;


    @Test
    void onelist() {
        //give
        UniversityApiRequest request = UniversityApiRequest.builder()
                .serviceKey("test")
                .pageNo(1)
                .numOfRows(666)
                .dataType("xml")
                .fcltyCd("50112")
                .build();

        // given: 응답 DTO (Body + UniversityApiResponse) 도 Builder로 생성
        Body body = Body.builder()
                .totalCount(666)
                .build();

        UniversityApiResponse response = UniversityApiResponse.builder()
                .body(body)
                .build();
        when(responseSpec.bodyToMono(UniversityApiResponse.class)).thenReturn(Mono.just(response));

        //when (실행)
        Mono<Integer> result = universityServiceImplTest.onelist(request);

        //then (검증)
        StepVerifier.create(result)
                .expectNext(666) //totalCount가 66인지 확인
                .verifyComplete(); //Mono가 끝났는지 확인
    }


    @Test
    void saveAllUniversities() {
        // given
        // 1. totalCount 응답
        Body initBody = Body.builder()
                .totalCount(666)
                .build();
        UniversityApiResponse initResponse = UniversityApiResponse.builder()
                .body(initBody)
                .build();
        when(responseSpec.bodyToMono(UniversityApiResponse.class)).thenReturn(Mono.just(initResponse));

        // 2. 기존 저장된 대학 목록 (1건 있음)
        University existingUniversity = University.builder()
                .universityId(1L)
                .universityName("한국대학교")
                .isDeleted(false)
                .build();
        when(universityRepository.findAll()).thenReturn(List.of(existingUniversity));

        // 3. 새로 받아올 데이터 (기존과 다른 이름으로 덮어쓰기 유도)
        UniversityList newItem = UniversityList.builder()
                .objectId(1L)
                .universityName("조선대학교") // 이름 변경됨
                .build();
        Body fullBody = Body.builder()
                .items(List.of(newItem))
                .build();
        UniversityApiResponse fullResponse = UniversityApiResponse.builder()
                .body(fullBody)
                .build();
        when(responseSpec.bodyToMono(UniversityApiResponse.class)).thenReturn(Mono.just(fullResponse));
        when(universityRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        UniversityApiRequest request = UniversityApiRequest.builder()
                .serviceKey("test")
                .pageNo(1)
                .numOfRows(666)
                .dataType("xml")
                .fcltyCd("50112")
                .build();

        // when
        Mono<Void> result = universityServiceImplTest.saveAllUniversities(request);

        // then
        StepVerifier.create(result).verifyComplete();

        // DB 저장 검증
        ArgumentCaptor<List<University>> universityCaptor = ArgumentCaptor.forClass(List.class);
        verify(universityRepository, times(1)).saveAll(universityCaptor.capture());
        List<University> savedUniversities = universityCaptor.getValue();
        assertThat(savedUniversities).hasSize(1);
        assertThat(savedUniversities.get(0).getUniversityName()).isEqualTo("조선대학교");

        // Elasticsearch 색인 검증
        ArgumentCaptor<List<UniversityDocument>> esCaptor = ArgumentCaptor.forClass(List.class);
        verify(esSearchRepository, times(1)).saveAll(esCaptor.capture());
        List<UniversityDocument> indexedDocuments = esCaptor.getValue();
        assertThat(indexedDocuments).hasSize(1);
        assertThat(indexedDocuments.get(0).getUniversityName()).isEqualTo("조선대학교");
    }
    }


