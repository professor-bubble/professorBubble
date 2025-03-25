package com.bubble.buubleforprofessor.domain.university.service;

import com.bubble.buubleforprofessor.domain.university.dto.request.UniversityApiRequest;

import com.bubble.buubleforprofessor.domain.university.dto.response.Body;
import com.bubble.buubleforprofessor.domain.university.dto.response.UniversityApiResponse;
import com.bubble.buubleforprofessor.domain.university.dto.response.UniversityList;
import com.bubble.buubleforprofessor.domain.university.repository.jpa.UniversityRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

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
        UniversityApiRequest request = new UniversityApiRequest();
        request.setServiceKey("test");
        request.setPageNo(1);
        request.setNumOfRows(10);
        request.setDataType("xml");
        request.setFcltyCd("50112");

        //가짜 응답 준비
        UniversityApiResponse response = new UniversityApiResponse();
        Body body = new Body();
        body.setTotalCount(666);
        response.setBody(body);
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
        //give
        //1. totalcount 값 얻어오기
        UniversityApiRequest getTotalCount = new UniversityApiRequest();
        getTotalCount.setServiceKey("test");
        getTotalCount.setPageNo(1);
        getTotalCount.setDataType("xml");
        getTotalCount.setFcltyCd("50112");

        //2. 가짜 TotalCount 준비
        UniversityApiResponse initResponse = new UniversityApiResponse();
        Body initBody = new Body();
        initBody.setTotalCount(666);
        initResponse.setBody(initBody);

        //todo 가짜 데이터를 왜 아래 when을 사용할까?
        //      그 데이터를 saveAllUniversities매서드에 전달할려면 webClient가 데이터를 넣어주도록 설정위해 필요 (비동기 처리위함)
        //      responseSpec : 응답을 처리하는 객체, WebClient를 통해 HTTP 요청을 보내고, 그에 대한 HTTP 응답을 받을 때 사용
        //      bodyToMono() :  HTTP 응답 본문을 특정 Java 객체로 변환하는 메소드
        when(responseSpec.bodyToMono(UniversityApiResponse.class)).thenReturn(Mono.just(initResponse));

        doNothing().when(universityRepository).deleteAll();

        //3. 전체 대학교 데이터 요청준비
        UniversityApiRequest allUniRequest = new UniversityApiRequest();
        allUniRequest.setServiceKey("test");
        allUniRequest.setPageNo(1);
        allUniRequest.setDataType("xml");
        allUniRequest.setNumOfRows(initBody.getTotalCount());
        allUniRequest.setFcltyCd("50112");


        //4. 전체 데이터 응답
        UniversityApiResponse fullResponse = new UniversityApiResponse();
        Body fullBody = new Body();
        UniversityList items = new UniversityList();
        items.setUniversityName("한국대학교");
        fullBody.setItems(List.of(items));
        fullResponse.setBody(fullBody);

        when(responseSpec.bodyToMono(UniversityApiResponse.class)).thenReturn(Mono.just(fullResponse));
        when(universityRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        // When
        Mono<Void> result = universityServiceImplTest.saveAllUniversities(allUniRequest);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
        verify(universityRepository, times(1)).deleteAll(); // deleteAll 호출 확인
        verify(universityRepository, times(1)).saveAll(anyList()); // saveAll 호출 확인
    }


}
