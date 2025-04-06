package com.bubble.buubleforprofessor.domain.payment.service;

public interface PaymentReidsService {
    void saveOrderAmount(String orderId, Integer amount, long timeoutInSeconds);
    Integer getOrderAmount(String orderId);
    void deleteOrderAmount(String orderId);
}
