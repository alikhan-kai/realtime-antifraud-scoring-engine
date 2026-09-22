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
        log.info("Scoring comple ted for transaction: {}", event.getTransactionId());

        List<CompletableFuture<RuleResult>> futures = rules.stream()
                .map(rule -> CompletableFuture.supplyAsync(() -> rule.evaluate(event), virtualThreadExecutor))
                .toList();

        List<RuleResult> ruleResults = futures.stream()
                .map(CompletableFuture::join)
                .filter(result -> result.getRiskScorePenalty() > 0)
                .toList();

        int totalScore = ruleResults.stream()
                .mapToInt(RuleResult::getRiskScorePenalty)
                .sum();

        RiskVerdict verdict = calculateVerdict(totalScore);

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
