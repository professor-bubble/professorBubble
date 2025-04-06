package com.bubble.buubleforprofessor.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentRedisServiceImpl implements PaymentReidsService{

    private final RedisTemplate<String, Integer> redisTemplate;

}
