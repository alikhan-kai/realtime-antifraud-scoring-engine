# Real-Time Anti-Fraud & Transaction Scoring Engine

Архитектурный концепт масштабируемой системы антифрода для оценки транзакций в реальном времени (SLA < 40ms). 
Проект построен на микросервисной архитектуре с использованием Java 21, Spring Boot, Kafka Streams и RedisBloom.

## Микросервисы
1. **fraud-gateway** - Высокопроизводительный шлюз для синхронной оценки транзакций.
2. **stream-analytics** - Асинхронный процессор Kafka Streams для вычисления скользящих окон.

## Инфраструктура
Для локального запуска используется Docker Compose (Kafka, Redis, Postgres, ELK).
