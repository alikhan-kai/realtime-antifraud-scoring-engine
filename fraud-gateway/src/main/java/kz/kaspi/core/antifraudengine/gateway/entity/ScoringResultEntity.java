package kz.kaspi.core.antifraudengine.gateway.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
@Table(name = "scoring_results")
public class ScoringResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "verdict", nullable = false)
    private String verdict;

    @Column(name = "total_score")
    private int totalScore;

    @Column(name = "triggered_rules", length = 1000)
    private String triggeredRulesJson;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
}