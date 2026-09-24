package kz.kaspi.core.antifraudengine.gateway.rule.impl;

import kz.kaspi.core.antifraudengine.gateway.domain.RuleResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VelocityRuleTest {

    private final VelocityRule rule = new VelocityRule();

    @Test
    void shouldReturnZeroPenaltyWhileDisabled() {
        TransactionEvent event = new TransactionEvent();
        RuleResult result = rule.evaluate(event);
        assertEquals(0, result.getRiskScorePenalty());
    }
}
