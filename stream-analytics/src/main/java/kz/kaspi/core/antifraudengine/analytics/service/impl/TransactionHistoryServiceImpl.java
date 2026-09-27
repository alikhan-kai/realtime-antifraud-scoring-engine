package kz.kaspi.core.antifraudengine.analytics.service.impl;

import kz.kaspi.core.antifraudengine.analytics.processor.TransactionTopology;
import kz.kaspi.core.antifraudengine.analytics.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.apache.kafka.streams.state.WindowStoreIterator;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    private final StreamsBuilderFactoryBean factoryBean;

    @Override
    public long countTransactionsInWindow(String userId, int minutes) {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        if (kafkaStreams == null || kafkaStreams.state() != KafkaStreams.State.RUNNING) {
            log.warn("Kafka Streams не готов.");
            return 0;
        }
        try {
            ReadOnlyWindowStore<String, Long> store = kafkaStreams.store(
                    StoreQueryParameters.fromNameAndType(TransactionTopology.TRANSACTION_COUNT_STORE, QueryableStoreTypes.windowStore())
            );
            long totalCount = 0;
            Instant timeFrom = Instant.now().minusSeconds(minutes * 60L);
            Instant timeTo = Instant.now();
            
            try (WindowStoreIterator<Long> iterator = store.fetch(userId, timeFrom, timeTo)) {
                while (iterator.hasNext()) {
                    totalCount += iterator.next().value;
                }
            }
            return totalCount;
        } catch (Exception e) {
            log.error("Ошибка при запросе к State Store: {}", e.getMessage());
            return 0;
        }
    }
}
