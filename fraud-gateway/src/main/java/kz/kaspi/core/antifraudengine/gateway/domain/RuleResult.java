package kz.kaspi.core.antifraudengine.gateway.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleResult {
    private String ruleName;
    private int riskScorePenalty;
    private String reason;
}
