# Real-Time Anti-Fraud Scoring Engine 🛡️

Enterprise-grade fraud detection engine built with Java 21, Spring Boot, Kafka, Redis, Neo4j, and PostgreSQL.

## Features
- **Transactional Outbox Pattern** for guaranteed event delivery to Kafka.
- **Idempotency** via Redis to prevent double-spending and duplicate evaluations.
- **Shadow Mode** for safely evaluating experimental machine learning rules in production.
- **Graph Analysis (Neo4j)** to detect complex money laundering rings and cyclic transaction patterns.
- **Real-Time Observability** with Prometheus and Grafana.
