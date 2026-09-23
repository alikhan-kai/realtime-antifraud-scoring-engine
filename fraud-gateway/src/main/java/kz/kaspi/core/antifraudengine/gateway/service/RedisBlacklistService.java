package kz.kaspi.core.antifraudengine.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisBlacklistService {

    private final StringRedisTemplate redisTemplate;

    public boolean isBlacklisted(String filterName, String value) {
        if (value == null || value.isEmpty())
            return false;

        try {
            // Вызываем команду модуля безопасно через встроенный Lua-движок Redis
            String script = "return redis.call('BF.EXISTS', KEYS[1], ARGV[1])";
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

            // Отправляем скрипт (KEYS[1] = filterName, ARGV[1] = value)
            Long result = redisTemplate.execute(redisScript, Collections.singletonList(filterName), value);

            return result != null && result == 1L;
        } catch (Exception e) {
            log.error("Error checking Redis Bloom Filter {}: {}", filterName, e.getMessage());
            return false;
        }
    }
}