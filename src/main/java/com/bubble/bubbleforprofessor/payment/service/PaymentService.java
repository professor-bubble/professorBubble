package com.bubble.bubbleforprofessor.payment.service;


import com.bubble.bubbleforprofessor.payment.dto.request.OrderRequestDto;
import com.bubble.bubbleforprofessor.payment.dto.response.InitPaymentResponseDto;

public interface PaymentService {
    InitPaymentResponseDto initPayment(OrderRequestDto orderRequest);
    void completePayment(String paymentKey, String orderId, int amount);
    void failPayment(String paymentKey, String orderId, int amount);
}
