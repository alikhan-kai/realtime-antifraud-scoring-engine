package kz.kaspi.core.antifraudengine.analytics.domain;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionEvent {
    private String transactionId;
    private String senderId;
    private BigDecimal amount;
}
