package kz.kaspi.core.antifraudengine.gateway.domain;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ScoringResult {
    private String transactionId;
    private RiskVerdict verdict;
    private int totalRiskScore;
    private List<RuleResult> triggeredRules;
}
