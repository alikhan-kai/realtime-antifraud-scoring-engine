package kz.kaspi.core.antifraudengine.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kaspi.core.antifraudengine.gateway.domain.ScoringResult;
import kz.kaspi.core.antifraudengine.gateway.entity.ScoringResultEntity;
import kz.kaspi.core.antifraudengine.gateway.entity.ScoringResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringHistoryService {

    private final ScoringResultRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Async
    public void saveResultAsync(String transactionId, ScoringResult result) {
        try {
            ScoringResultEntity entity = new ScoringResultEntity();
            entity.setTransactionId(transactionId);
            entity.setVerdict(result.getVerdict().name());
            entity.setTotalScore(result.getTotalRiskScore());
            entity.setTriggeredRulesJson(objectMapper.writeValueAsString(result.getTriggeredRules()));
            
            repository.save(entity);
            log.debug("Результат скоринга для {} сохранен в Postgres", transactionId);
        } catch (Exception e) {
            log.error("Ошибка сохранения в Postgres: {}", e.getMessage());
        }
    }
}
