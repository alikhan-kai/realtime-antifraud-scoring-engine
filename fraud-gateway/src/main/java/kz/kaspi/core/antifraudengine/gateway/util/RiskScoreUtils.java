package kz.kaspi.core.antifraudengine.gateway.util;

public class RiskScoreUtils {
    
    private RiskScoreUtils() {}

    public static boolean isCritical(int score) {
        return score >= 80;
    }
}
