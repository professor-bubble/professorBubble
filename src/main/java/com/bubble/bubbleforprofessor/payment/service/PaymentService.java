package com.bubble.bubbleforprofessor.payment.service;


import com.bubble.bubbleforprofessor.payment.dto.request.OrderRequestDto;
import com.bubble.bubbleforprofessor.payment.dto.response.InitTossResponseDto;
import com.bubble.bubbleforprofessor.payment.dto.response.SuccessResponseDto;
import com.bubble.bubbleforprofessor.user.dto.CustomPrincipal;

public interface PaymentService {
    InitTossResponseDto initPayment(OrderRequestDto orderRequest, CustomPrincipal principal);
    SuccessResponseDto completePayment(String paymentKey, String orderId, int amount);
    void failPayment(String paymentKey, String orderId, int amount);
}
