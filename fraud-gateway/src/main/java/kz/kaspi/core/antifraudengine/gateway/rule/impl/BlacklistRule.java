package kz.kaspi.core.antifraudengine.gateway.rule.impl;

import org.springframework.stereotype.Component;

import kz.kaspi.core.antifraudengine.gateway.domain.RuleResult;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.rule.FraudRule;
import kz.kaspi.core.antifraudengine.gateway.service.RedisBlacklistService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BlacklistRule implements FraudRule {

    private final RedisBlacklistService blacklistService;
    private static final String IP_BLACKLIST_KEY = "blacklist:ip";

    @Override
    public String ruleName() {
        return "IP_BLACKLICT_RULE";
    }

    @Override
    public RuleResult evaluate(TransactionEvent event) {
        boolean isFraud = blacklistService.isBlacklisted(IP_BLACKLIST_KEY, event.getIpAddress());

        if (isFraud) {
            return new RuleResult(
                    ruleName(),
                    100,
                    "IP find in global blacklist");
        }

        return new RuleResult(ruleName(), 0, "OK - IP is clean");
    }
}
