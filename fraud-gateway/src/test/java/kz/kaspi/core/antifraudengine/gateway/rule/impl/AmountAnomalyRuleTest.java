package kz.kaspi.core.antifraudengine.gateway.rule.impl;

import kz.kaspi.core.antifraudengine.gateway.domain.RuleResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AmountAnomalyRuleTest {

    private final AmountAnomalyRule rule = new AmountAnomalyRule();

    @Test
    void shouldReturnPenaltyWhenAmountExceedsLimit() {
        TransactionEvent event = new TransactionEvent();
        event.setAmount(new BigDecimal("600000"));
        
        RuleResult result = rule.evaluate(event);
        assertEquals(30, result.getRiskScorePenalty());
    }

    @Test
    void shouldReturnZeroPenaltyWhenAmountIsWithinLimit() {
        TransactionEvent event = new TransactionEvent();
        event.setAmount(new BigDecimal("100000"));
        
        RuleResult result = rule.evaluate(event);
        assertEquals(0, result.getRiskScorePenalty());
    }
}
