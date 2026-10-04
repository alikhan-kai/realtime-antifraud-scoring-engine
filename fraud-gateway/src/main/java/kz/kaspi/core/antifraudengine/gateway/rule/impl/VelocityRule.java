package kz.kaspi.core.antifraudengine.gateway.rule.impl;

import kz.kaspi.core.antifraudengine.gateway.client.AnalyticsClient;
import kz.kaspi.core.antifraudengine.gateway.domain.RuleResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.rule.FraudRule;
import kz.kaspi.core.antifraudengine.gateway.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VelocityRule implements FraudRule {

    private final AnalyticsClient analyticsClient;
    private static final long MAX_TRANSACTIONS_PER_MINUTE = 4;

    @Override
    public String ruleName() {
        return AppConstants.RULE_VELOCITY_SURGE;
    }

    @Override
    public RuleResult evaluate(TransactionEvent event) {
        if (event.getSenderId() == null) return new RuleResult(ruleName(), 0, "No sender ID", isShadowMode());

        long count = analyticsClient.getUserTransactionCount(event.getSenderId());
        
        if (count >= MAX_TRANSACTIONS_PER_MINUTE) {
            return new RuleResult(ruleName(), 50, "РћР±РЅР°СЂСѓР¶РµРЅР° РІРµРµСЂРЅР°СЏ СЂР°СЃСЃС‹Р»РєР°: " + count + " С‚СЂР°РЅР·Р°РєС†РёР№ Р·Р° РјРёРЅСѓС‚Сѓ", isShadowMode());
        }
        return new RuleResult(ruleName(), 0, "Р§Р°СЃС‚РѕС‚Р° РїРµСЂРµРІРѕРґРѕРІ РІ РЅРѕСЂРјРµ: " + count, isShadowMode());
    }
}

