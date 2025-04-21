package com.bubble.bubbleforprofessor.domain.payment.service;

public interface PaymentRedisService {
    void saveOrderAmount(String orderId, Integer amount, long timeoutInSeconds);
    Integer getOrderAmount(String orderId);
    void deleteOrderAmount(String orderId);
}
