package com.bubble.bubbleforprofessor.payment.service;

import java.util.List;

public interface PaymentRedisService {
    void saveOrderAmount(String orderId, Integer amount, long timeoutInSeconds);
    Integer getOrderAmount(String orderId);
    void deleteRedisOrder(String orderId);
}
