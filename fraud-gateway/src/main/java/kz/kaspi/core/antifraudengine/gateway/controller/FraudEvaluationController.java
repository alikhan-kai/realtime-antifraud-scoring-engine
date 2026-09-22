package kz.kaspi.core.antifraudengine.gateway.controller;

import kz.kaspi.core.antifraudengine.gateway.domain.ScoringResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.engine.FraudEvaluationEngine;
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

    @PostMapping("/evaluate")
    public ResponseEntity<ScoringResult> evaluateTransaction(@RequestBody TransactionEvent event) {
        // Передаем пришедшую транзакцию в наш движок
        ScoringResult result = engine.evaluate(event);

        // Возвращаем результат (JSON) обратно клиенту
        return ResponseEntity.ok(result);
    }
}
