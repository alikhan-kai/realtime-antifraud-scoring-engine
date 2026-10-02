package kz.kaspi.core.antifraudengine.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kaspi.core.antifraudengine.gateway.domain.OutboxEventEntity;
import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import kz.kaspi.core.antifraudengine.gateway.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxEventRepository outboxRepository;
    private final KafkaEventPublisher kafkaPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Запускаем проверку базы каждые 5 секунд
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutboxEvents() {
        // Достаем все события, которые ожидают отправки (PENDING)
        List<OutboxEventEntity> pendingEvents = outboxRepository
                .findByStatusOrderByCreatedAtAsc(OutboxEventEntity.OutboxStatus.PENDING);

        if (pendingEvents.isEmpty()) {
            return; // Если пустых нет, просто ничего не делаем
        }

        log.info("Найдено {} событий в Outbox для отправки в Kafka", pendingEvents.size());

        for (OutboxEventEntity eventEntity : pendingEvents) {
            try {
                // Превращаем JSON из базы обратно в Java-объект
                TransactionEvent transactionEvent = objectMapper
                        .readValue(eventEntity.getPayload(), TransactionEvent.class);

                // Отправляем в Kafka (с гарантией!)
                kafkaPublisher.publish(transactionEvent);

                // Меняем статус на PROCESSED и сохраняем
                eventEntity.setStatus(OutboxEventEntity.OutboxStatus.PROCESSED);
                outboxRepository.save(eventEntity);

                log.info("Событие {} успешно отправлено в Kafka и помечено PROCESSED", eventEntity.getId());

            } catch (Exception e) {
                log.error("Ошибка при обработке Outbox события {}: {}", eventEntity.getId(), e.getMessage());
            }
        }
    }
}