package kz.kaspi.core.antifraudengine.gateway.service;

import kz.kaspi.core.antifraudengine.gateway.domain.TransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "transactions-events";

    public void publish(TransactionEvent event) {
        try {
            // Ключ - senderId, чтобы все события юзера попали в одну партицию Kafka
            kafkaTemplate.send(TOPIC, event.getSenderId(), event);
        } catch (Exception e) {
            log.error("Ошибка при отправке в Kafka: {}", e.getMessage());
        }
    }
}
