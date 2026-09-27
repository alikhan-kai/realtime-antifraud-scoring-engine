package kz.kaspi.core.antifraudengine.analytics.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    public static final String TRANSACTIONS_TOPIC = "transactions-events";

    @Bean
    public NewTopic transactionsTopic() {
        return TopicBuilder.name(TRANSACTIONS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
