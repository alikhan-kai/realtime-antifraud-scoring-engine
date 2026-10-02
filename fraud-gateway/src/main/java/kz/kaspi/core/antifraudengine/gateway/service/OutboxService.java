package kz.kaspi.core.antifraudengine.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kaspi.core.antifraudengine.gateway.domain.OutboxEventEntity;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void saveEvent(TransactionEvent event) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(event);
            OutboxEventEntity outboxEvent = OutboxEventEntity.builder()
                    .aggregateId(event.getSenderId())
                    .payload(jsonPayload)
                    .status(OutboxEventEntity.OutboxStatus.PENDING)
                    .createdAt(Instant.now())
                    .build();

            outboxEventRepository.save(outboxEvent);
            log.info("Транзакция {} успешно сохранена в Outbox (БД)", event.getTransactionId());

        } catch (Exception e) {
            log.error("Ошибка при сохранении события в Outbox: {}", e.getMessage());
        }
    }
}