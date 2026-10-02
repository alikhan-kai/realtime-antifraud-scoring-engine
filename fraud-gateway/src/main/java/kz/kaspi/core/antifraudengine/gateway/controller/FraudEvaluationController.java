package kz.kaspi.core.antifraudengine.gateway.controller;

import jakarta.validation.Valid;
import kz.kaspi.core.antifraudengine.gateway.domain.ScoringResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.engine.FraudEvaluationEngine;
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
    private final OutboxService outboxService; // Подключили наш новый сервис
    private final ScoringHistoryService historyService;

    @PostMapping("/evaluate")
    public ResponseEntity<ScoringResult> evaluateTransaction(@Valid @RequestBody TransactionEvent event) {

        // 1. Паттерн OUTBOX: Сохраняем в PostgreSQL вместо прямой отправки в Kafka
        outboxService.saveEvent(event);

        // 2. Расчет фрод-скоринга (Virtual Threads)
        ScoringResult result = engine.evaluate(event);

        // 3. Асинхронное сохранение результата в Postgres
        historyService.saveResultAsync(event.getTransactionId(), result);

        return ResponseEntity.ok(result);
    }
}