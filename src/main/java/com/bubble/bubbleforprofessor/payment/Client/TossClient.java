package com.bubble.bubbleforprofessor.payment.Client;

import com.bubble.bubbleforprofessor.payment.dto.response.TossConfirmResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TossClient {
    private final RestTemplate restTemplate;

    @Value("${toss.secret-key}")
    private String secretKey;


    //결제 승인
    public TossConfirmResponseDto confirm(String paymentKey, String orderId, int amount) {
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        //  결제 확인 요청을 위한 본문
        Map<String, Object> body = Map.of(
                "paymentKey", paymentKey,
                "orderId", orderId,
                "amount", amount
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 전송
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<TossConfirmResponseDto> response = restTemplate
                .postForEntity(url, request, TossConfirmResponseDto.class); //응답 타입 지정

        return response.getBody();
    }

    public void cancel(String paymentKey, String reason) {
        String url = "https://api.tosspayments.com/v1/payments/" + paymentKey+ "/cancel";

        Map<String, String> body = Map.of("cancelReason", reason);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 멱등키 (paymentKey 자체로 설정, 중복 취소 방지)
        headers.set("Idempotency-Key", paymentKey);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, request, Void.class);
    }

}


