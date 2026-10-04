package kz.kaspi.core.antifraudengine.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kaspi.core.antifraudengine.gateway.domain.ScoringResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Slf4j 
@Service 
@RequiredArgsConstructor 
public class IdempotencyService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Префикс для ключей в редис
    private static final String IDEMPOTENCY_PREFIX = "idempotency:txn:";
    // Храним дубликаты 24 часа
    private static final Duration TTL = Duration.ofHours(24);

    // Проверяет, была ли уже обработана такая транзакция
    public ScoringResult getCachedResult(String transactionId){
        try{
            String key = IDEMPOTENCY_PREFIX + transactionId;
            String cachedJson = redisTemplate.opsForValue().get(key);

            if(cachedJson != null){
                log.warn("Duplicate transactionId detected: {}", transactionId);
                return objectMapper.readValue(cachedJson, ScoringResult.class);
            }
        } catch(Exception e){
            log.error("Failed to read cached result for {}: {}", e.getMessage());
        }
        return null;
    }

    // Сохраняет результат проверки в Redis, чтобы защититься от будущих дубликатов
    public void cacheResult(String transactionId, ScoringResult result){
        try{
            String key = IDEMPOTENCY_PREFIX + transactionId;
            String jsonResult = objectMapper.writeValueAsString(result);
            redisTemplate.opsForValue().set(key, jsonResult, TTL);
        } catch(Exception e){
            log.error("Failed to cache result for {}", e.getMessage());
        }
    }
}
