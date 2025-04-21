package com.bubble.bubbleforprofessor.domain.payment.service;


import com.bubble.bubbleforprofessor.domain.payment.dto.request.OrderRequestDto;
import com.bubble.bubbleforprofessor.domain.payment.dto.response.InitPaymentResponseDto;

public interface PaymentService {
    InitPaymentResponseDto initPayment(OrderRequestDto orderRequest);
    void completePayment(String paymentKey, String orderId, int amount);
    void failPayment(String paymentKey, String orderId, int amount);
}
