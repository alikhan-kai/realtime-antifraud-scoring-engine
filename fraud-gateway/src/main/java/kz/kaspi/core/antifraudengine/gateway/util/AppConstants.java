package kz.kaspi.core.antifraudengine.gateway.util;

public final class AppConstants {
    private AppConstants() {}

    public static final String RULE_IP_BLACKLIST = "IP_BLACKLIST_RULE";
    public static final String RULE_AMOUNT_ANOMALY = "AMOUNT_ANOMALY_RULE";
    public static final String RULE_VELOCITY_SURGE = "VELOCITY_SURGE_RULE";
    
    public static final int SCORE_BLOCK = 100;
    public static final int SCORE_CHALLENGE = 40;
}
