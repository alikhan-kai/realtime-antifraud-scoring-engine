package kz.kaspi.core.antifraudengine.analytics.processor;

import kz.kaspi.core.antifraudengine.analytics.config.KafkaStreamsConfig;
import kz.kaspi.core.antifraudengine.analytics.domain.TransactionEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
public class TransactionTopology {
    public static final String TRANSACTION_COUNT_STORE = "transaction-count-store";

    @Autowired
    public void buildPipeline(StreamsBuilder streamsBuilder) {
        JsonSerde<TransactionEvent> jsonSerde = new JsonSerde<>(TransactionEvent.class);
        KStream<String, TransactionEvent> stream = streamsBuilder.stream(
                KafkaStreamsConfig.TRANSACTIONS_TOPIC, Consumed.with(Serdes.String(), jsonSerde));

        stream.selectKey((key, event) -> event.getSenderId())
              .groupByKey(Grouped.with(Serdes.String(), jsonSerde))
              .windowedBy(SlidingWindows.ofTimeDifferenceAndGrace(Duration.ofSeconds(60), Duration.ofSeconds(5)))
              .count(Materialized.as(TRANSACTION_COUNT_STORE));
    }
}
