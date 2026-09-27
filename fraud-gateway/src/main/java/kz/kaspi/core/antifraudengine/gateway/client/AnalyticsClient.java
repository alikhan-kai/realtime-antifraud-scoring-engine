package kz.kaspi.core.antifraudengine.gateway.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyticsClient {

    private final RestTemplate restTemplate;
    private final String analyticsUrl = "http://localhost:8081/api/v1/analytics/users/{userId}/velocity";

    public long getUserTransactionCount(String senderId) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Number> response = restTemplate.getForObject(analyticsUrl, Map.class, senderId);
            if (response != null && response.containsKey("transactionCount")) {
                return response.get("transactionCount").longValue();
            }
        } catch (Exception e) {
            log.error("Сбой stream-analytics для {}: {}", senderId, e.getMessage());
        }
        return 0;
    }
}
