package com.bubble.buubleforprofessor.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentRedisServiceImpl implements PaymentReidsService{

    private final RedisTemplate<String, Integer> redisTemplate;

    //redis에 주문번호, 금액, TTL시간 저장
    @Override
    public void saveOrderAmount(String orderId, Integer amount, long timeoutInSeconds) {
        redisTemplate.opsForValue().set(orderId, amount, timeoutInSeconds, TimeUnit.SECONDS);
        //void set(K key, V value, long timeout, TimeUnit unit) 메서드 순서, ctrl+set 클릭시 확인가능
    }

    //redis에서 주문번호번호로 금액 조회
    @Override
    public Integer getOrderAmount(String orderId) {
        return redisTemplate.opsForValue().get(orderId);
    }

    @Override
    public void deleteOrderAmount(String orderId) {
        redisTemplate.delete(orderId);
    }

}
