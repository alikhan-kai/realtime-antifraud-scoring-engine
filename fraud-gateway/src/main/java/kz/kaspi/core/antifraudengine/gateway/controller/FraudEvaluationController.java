package kz.kaspi.core.antifraudengine.gateway.controller;

import jakarta.validation.Valid;
import kz.kaspi.core.antifraudengine.gateway.domain.ScoringResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.engine.FraudEvaluationEngine;
import kz.kaspi.core.antifraudengine.gateway.service.IdempotencyService;
import kz.kaspi.core.antifraudengine.gateway.service.OutboxService;
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
    private final OutboxService outboxService;
    private final ScoringHistoryService historyService;
    private final IdempotencyService idempotencyService; // <-- Наш новый сервис

    @PostMapping("/evaluate")
    public ResponseEntity<ScoringResult> evaluateTransaction(@Valid @RequestBody TransactionEvent event) {
        
        // 1.ИДЕМПОТЕНТНОСТЬ: Проверяем, не дубликат ли это?
        ScoringResult cachedResult = idempotencyService.getCachedResult(event.getTransactionId());
        if (cachedResult != null) {
            // Возвращаем сохраненный ответ моментально, не нагружая систему!
            return ResponseEntity.ok(cachedResult);
        }

        // 2.Паттерн OUTBOX: Сохраняем в PostgreSQL (для Kafka)
        outboxService.saveEvent(event);
        
        // 3.Расчет фрод-скоринга (Virtual Threads)
        ScoringResult result = engine.evaluate(event);
        
        // 4.ИДЕМПОТЕНТНОСТЬ: Сохраняем результат в Redis на 24 часа
        idempotencyService.cacheResult(event.getTransactionId(), result);

        // 5.Асинхронное сохранение результата в Postgres
        historyService.saveResultAsync(event.getTransactionId(), result);
        
        return ResponseEntity.ok(result);
    }
}