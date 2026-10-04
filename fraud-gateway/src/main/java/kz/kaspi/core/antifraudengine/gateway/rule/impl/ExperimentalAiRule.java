package kz.kaspi.core.antifraudengine.gateway.rule.impl;

import kz.kaspi.core.antifraudengine.gateway.domain.RuleResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.rule.FraudRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExperimentalAiRule implements FraudRule {

    @Override
    public String ruleName() {
        return "EXPERIMENTAL_AI_MODEL_RULE";
    }

    @Override
    public boolean isShadowMode() {
        return true;
    }

    @Override
    public RuleResult evaluate(TransactionEvent event) {
        log.info("Оцениваем транзакцию {} с помощью экспериментальной нейросети...", event.getTransactionId());
        
        return new RuleResult(
                ruleName(),
                100,
                "Экспериментальная AI модель заподозрила неладное",
                isShadowMode()
        );
    }
}
