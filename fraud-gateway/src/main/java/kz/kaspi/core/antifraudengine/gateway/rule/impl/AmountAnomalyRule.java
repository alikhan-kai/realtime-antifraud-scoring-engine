package kz.kaspi.core.antifraudengine.gateway.rule.impl;

import kz.kaspi.core.antifraudengine.gateway.domain.RuleResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.rule.FraudRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AmountAnomalyRule implements FraudRule {

    private static final BigDecimal AMOUNT_LIMIT = new BigDecimal("500000");

    @Override
    public String ruleName() {
        return "AMOUNT_ANOMALY_RULE";
    }

    @Override
    public RuleResult evaluate(TransactionEvent event) {
        if (event.getAmount() != null && event.getAmount().compareTo(AMOUNT_LIMIT) > 0) {
            return new RuleResult(
                    ruleName(),
                    30,
                    "Сумма перевода превышает базовый лимит (500 000)");
        }

        return new RuleResult(ruleName(), 0, "Сумма в пределах нормы");
    }
}