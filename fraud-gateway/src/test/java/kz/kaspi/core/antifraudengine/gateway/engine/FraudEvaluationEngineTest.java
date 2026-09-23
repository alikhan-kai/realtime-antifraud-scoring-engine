package kz.kaspi.core.antifraudengine.gateway.engine;

import kz.kaspi.core.antifraudengine.gateway.domain.RiskVerdict;
import kz.kaspi.core.antifraudengine.gateway.domain.RuleResult;
import kz.kaspi.core.antifraudengine.gateway.domain.ScoringResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.rule.FraudRule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FraudEvaluationEngineTest {

    @Test
    void shouldEvaluateRulesAndReturnVerdict() {
        // Создаем простое фейковое правило, которое всегда дает 50 штрафных баллов
        FraudRule mockRule = event -> new RuleResult("MOCK_RULE", 50, "Mock reason");
        FraudEvaluationEngine engine = new FraudEvaluationEngine(List.of(mockRule));

        TransactionEvent event = new TransactionEvent();
        event.setTransactionId("TXN-TEST");

        ScoringResult result = engine.evaluate(event);

        // 50 баллов означает Желтая зона (CHALLENGE)
        assertEquals(RiskVerdict.CHALLENGE, result.getVerdict());
        assertEquals(50, result.getTotalRiskScore());
    }
}
