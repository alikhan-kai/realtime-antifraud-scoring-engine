package kz.kaspi.core.antifraudengine.gateway.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class TransactionEvent {
    private String transactionId;
    private String senderId;
    private String receiverId;
    private BigDecimal amount;
    private String currency;
    private String ipAddress;
    private String deviceId;
    private Double latitude;
    private Double longitude;
    private Instant timestamp;
}
