package kz.kaspi.core.antifraudengine.gateway.controller;

import jakarta.validation.Valid;
import kz.kaspi.core.antifraudengine.gateway.domain.ScoringResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.engine.FraudEvaluationEngine;
import kz.kaspi.core.antifraudengine.gateway.service.KafkaEventPublisher;
import kz.kaspi.core.antifraudengine.gateway.service.ScoringHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fraud")
@RequiredArgsConstructor
public class FraudEvaluationController {

    private final FraudEvaluationEngine engine;
    private final KafkaEventPublisher eventPublisher;
    private final ScoringHistoryService historyService;

    @PostMapping("/evaluate")
    public ResponseEntity<ScoringResult> evaluateTransaction(@Valid @RequestBody TransactionEvent event) {
        // Отправка в Kafka
        eventPublisher.publish(event);
        
        // Мгновенный скоринг (Virtual Threads)
        ScoringResult result = engine.evaluate(event);
        
        // Асинхронное сохранение в Postgres (без блокировки)
        historyService.saveResultAsync(event.getTransactionId(), result);
        
        return ResponseEntity.ok(result);
    }
}
