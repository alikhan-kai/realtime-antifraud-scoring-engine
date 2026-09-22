package kz.kaspi.core.antifraudengine.gateway.domain;

public enum RiskVerdict {
    ALLOW,     // Одобрено (Score < 40)
    CHALLENGE, // Доп. проверка, например Face ID (40 <= Score < 80)
    DECLINE    // Блокировка (Score >= 80)
}
