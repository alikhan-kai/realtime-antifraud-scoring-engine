package kz.kaspi.core.antifraudengine.gateway.rule.impl;

import kz.kaspi.core.antifraudengine.gateway.domain.RuleResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.repository.neo4j.GraphUserRepository;
import kz.kaspi.core.antifraudengine.gateway.rule.FraudRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MoneyLaunderingRingRule implements FraudRule {

    private final GraphUserRepository graphUserRepository;

    @Override
    public String ruleName() {
        return "MONEY_LAUNDERING_RING_RULE";
    }

    @Override
    public RuleResult evaluate(TransactionEvent event) {
        try {
            // 1. Проверяем, не участвовал ли отправитель в подозрительных кольцах
            boolean isRingDetected = graphUserRepository.isPartOfMoneyLaunderingRing(event.getSenderId());

            // 2. В любом случае фиксируем новую транзакцию в графе
            graphUserRepository.recordTransaction(event.getSenderId(), event.getReceiverId());

            if (isRingDetected) {
                return new RuleResult(
                        ruleName(),
                        100, // Мгновенный отказ (DECLINE)
                        "Detected cyclic transaction pattern (Money Laundering Ring)",
                        isShadowMode()
                );
            }
        } catch (Exception e) {
            log.error("Failed to evaluate graph rule: {}", e.getMessage());
        }

        return new RuleResult(ruleName(), 0, "No suspicious graph patterns", isShadowMode());
    }
}
