package com.bubble.buubleforprofessor.domain.university.service;

import com.bubble.buubleforprofessor.domain.university.dto.request.UniversityApiRequest;

import com.bubble.buubleforprofessor.domain.university.dto.response.Body;
import com.bubble.buubleforprofessor.domain.university.dto.response.UniversityApiResponse;
import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UniversityServiceImplTest {

    /*Mock :  가짜로 만든 객체
    * InjectMoks : 테스트 대상에 Mock 주입*/

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

    }
