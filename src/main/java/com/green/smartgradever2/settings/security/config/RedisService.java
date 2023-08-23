package com.green.smartgradever2.settings.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
//@Transactional(readOnly = true) //트랜잭션 읽기 전용 (JPA 때 유용)
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    // 영구저장 방법
    public void setValues(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    // 만료시간 설정 -> 자동 삭제
    public void setValuesWithTimeout(String key, String value, long timeout){
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    public String getValues(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
