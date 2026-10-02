package kz.kaspi.core.antifraudengine.gateway.repository;

import kz.kaspi.core.antifraudengine.gateway.domain.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEventEntity, UUID> {
    // Метод для поиска всех необработанных событий
    List<OutboxEventEntity> findByStatusOrderByCreatedAtAsc(OutboxEventEntity.OutboxStatus status);
}