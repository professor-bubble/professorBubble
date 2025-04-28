package com.bubble.bubbleforprofessor.payment.Client;

import com.bubble.bubbleforprofessor.payment.dto.response.TossInitResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TossClient {
    private final RestTemplate restTemplate;

    @Value("${toss.secret-key}")
    private String secretKey;

    public TossInitResponseDto ready(String orderId, int amount) {
        String url = "https://api.tosspayments.com/v1/payments/ready";

        // 1) 요청 바디 준비
        Map<String, Object> body = Map.of(
                "orderId", orderId,
                "amount", amount
        );

        // 2) HTTP 헤더 준비 (Basic Auth) -> 아이디랑 비번 합쳐서 만듦 하지만 비번없이 조합할꺼라서 ""빈문자열 입력
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // 3) 호출 & 응답 받기
        ResponseEntity<TossInitResponseDto> response = restTemplate
                .postForEntity(url, request, TossInitResponseDto.class);

        return response.getBody();
    }
}


