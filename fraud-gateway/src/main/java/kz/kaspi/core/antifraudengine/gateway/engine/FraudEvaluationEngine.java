package kz.kaspi.core.antifraudengine.gateway.engine;

import java.util.concurrent.Executors;
import kz.kaspi.core.antifraudengine.gateway.domain.RiskVerdict;
import kz.kaspi.core.antifraudengine.gateway.domain.RuleResult;
import kz.kaspi.core.antifraudengine.gateway.domain.ScoringResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.rule.FraudRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FraudEvaluationEngine {

    private final List<FraudRule> rules;

    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

        public ScoringResult evaluate(TransactionEvent event) {
        log.info("Scoring started for transaction: {}", event.getTransactionId());

        // 1. Запускаем все правила параллельно (Virtual Threads)
        List<CompletableFuture<RuleResult>> futures = rules.stream()
                .map(rule -> CompletableFuture.supplyAsync(() -> {
                    RuleResult result = rule.evaluate(event);
                    // Проставляем флаг Shadow Mode в результат, чтобы сохранить это в логи/БД
                    result.setShadowMode(rule.isShadowMode());
                    return result;
                }, virtualThreadExecutor))
                .toList();

        // 2. Собираем все сработавшие правила (штраф > 0)
        List<RuleResult> ruleResults = futures.stream()
                .map(CompletableFuture::join)
                .filter(result -> result.getRiskScorePenalty() > 0)
                .toList();

        // 3. Считаем сумму баллов ТОЛЬКО ДЛЯ БОЕВЫХ ПРАВИЛ (Игнорируем Shadow Mode!)
        int totalScore = ruleResults.stream()
                .filter(result -> !result.isShadowMode()) // <--- ВОТ НАША МАГИЯ
                .mapToInt(RuleResult::getRiskScorePenalty)
                .sum();

        RiskVerdict verdict = calculateVerdict(totalScore);

        // 4. Но при этом в список сработавших мы передаем ВСЕ правила (даже теневые)
        return ScoringResult.builder()
                .transactionId(event.getTransactionId())
                .verdict(verdict)
                .totalRiskScore(totalScore)
                .triggeredRules(ruleResults)
                .build();
    }

    private RiskVerdict calculateVerdict(int score) {
        if (score >= 80)
            return RiskVerdict.DECLINE;
        if (score >= 40)
            return RiskVerdict.CHALLENGE;
        return RiskVerdict.ALLOW;
    }
}
