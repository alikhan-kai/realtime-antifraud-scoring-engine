package kz.kaspi.core.antifraudengine.analytics.api;

import kz.kaspi.core.antifraudengine.analytics.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final TransactionHistoryService historyService;

    @GetMapping("/users/{userId}/velocity")
    public ResponseEntity<Map<String, Object>> getVelocity(@PathVariable String userId) {
        long count = historyService.countTransactionsInWindow(userId, 1);
        return ResponseEntity.ok(Map.of("senderId", userId, "transactionCount", count));
    }
}
