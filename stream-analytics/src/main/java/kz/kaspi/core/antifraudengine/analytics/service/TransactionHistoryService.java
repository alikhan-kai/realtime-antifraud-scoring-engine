package kz.kaspi.core.antifraudengine.analytics.service;

public interface TransactionHistoryService {
    /**
     * Возвращает количество транзакций пользователя за последние N минут.
     */
    long countTransactionsInWindow(String userId, int minutes);
}
