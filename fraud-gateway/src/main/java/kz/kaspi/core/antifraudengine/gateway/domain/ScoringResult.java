package kz.kaspi.core.antifraudengine.gateway.domain;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * Encapsulates the final decision made by the FraudEvaluationEngine.
 * Contains the aggregated risk score and the specific rules that were triggered.
 */
@Data
@Builder
public class ScoringResult {
    private String transactionId;
    private RiskVerdict verdict;
    private int totalRiskScore;
    private List<RuleResult> triggeredRules;
}
