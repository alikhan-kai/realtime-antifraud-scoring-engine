package kz.kaspi.core.antifraudengine.gateway.rule.impl;

import kz.kaspi.core.antifraudengine.gateway.domain.RuleResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.rule.FraudRule;
import org.springframework.stereotype.Component;

@Component
public class VelocityRule implements FraudRule {

    @Override
    public String ruleName() {
        return "VELOCITY_SURGE_RULE";
    }

    @Override
    public RuleResult evaluate(TransactionEvent event) {
        // TODO: Реализовать обращение к Kafka Streams для подсчета транзакций за 60
        // секунд.
        return new RuleResult(ruleName(), 0, "Проверка частоты временно отключена");
    }
}
