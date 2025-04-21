package com.bubble.bubbleforprofessor.payment.service;

public interface PaymentRedisService {
    void saveOrderAmount(String orderId, Integer amount, long timeoutInSeconds);
    Integer getOrderAmount(String orderId);
    void deleteOrderAmount(String orderId);
}
