package kz.kaspi.core.antifraudengine.gateway.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoringResultRepository extends JpaRepository<ScoringResultEntity, Long> {
}