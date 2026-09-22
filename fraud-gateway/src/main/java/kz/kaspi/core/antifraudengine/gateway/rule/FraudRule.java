package kz.kaspi.core.antifraudengine.gateway.rule;

import kz.kaspi.core.antifraudengine.gateway.domain.RuleResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;

public interface FraudRule {

    // Возвращает уникальное имя правила (например, "BLACKLIST_IP_RULE")
    String ruleName();

    // Основная логика проверки транзакции
    RuleResult evaluate(TransactionEvent event);
}
