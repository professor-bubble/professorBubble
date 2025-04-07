package com.bubble.buubleforprofessor.domain.payment.service;


import com.bubble.buubleforprofessor.domain.payment.dto.request.OrderRequestDto;
import com.bubble.buubleforprofessor.domain.payment.dto.response.InitPaymentResponseDto;

public interface PaymentService {
    InitPaymentResponseDto payment(OrderRequestDto orderRequestDTO);
    boolean confirmPayment(String paymentId, String orderId, int amount);
    void savePayment(String paymentKey, String orderId, int amount, String paymentMethod);
    Pay
}
