# Real-Time Anti-Fraud Scoring Engine

A high-performance, enterprise-grade fraud detection system designed to evaluate financial transactions in real-time. Built with a modern microservices architecture, this engine calculates a dynamic risk score for every incoming transaction and issues a final verdict (ALLOW, CHALLENGE, DECLINE) based on complex rule sets.

## Project Goal
The primary objective of this project is to provide a highly scalable, fault-tolerant, and idempotent system capable of intercepting and analyzing thousands of transactions per second. It mitigates financial risks such as money laundering, account takeovers, and high-velocity brute-force attacks while maintaining a strict sub-100ms SLA for synchronous API responses.

## Key Features & Architecture

* **Rule-Based Evaluation Engine:** A concurrent evaluation engine leveraging Java 21 Virtual Threads to run multiple fraud checks simultaneously without blocking OS threads.
* **Idempotency Guarantee:** Prevents duplicate transaction processing (e.g., due to client retries or network failures) by caching scoring results in Redis for 24 hours.
* **Transactional Outbox Pattern:** Guarantees at-least-once delivery of transaction events to Apache Kafka. Transactions are synchronously saved to PostgreSQL and asynchronously polled by a scheduler to prevent data loss during Kafka outages.
* **Anti-Money Laundering (AML) Graph Detection:** Utilizes Neo4j to detect cyclic transaction patterns (e.g., A -> B -> C -> A) up to 3 hops deep, effectively identifying money laundering rings.
* **Shadow Mode for Rule Testing:** Allows data scientists to deploy new experimental rules into production. Shadow rules execute and log their results but are excluded from the final risk score calculation, ensuring zero impact on real customers.
* **Velocity & Anomaly Rules:** Tracks user transaction frequency and flags abnormally high amounts using in-memory limits.
* **Global Blacklist (RedisBloom):** Uses RedisBloom (Probabilistic Data Structures) for ultra-fast, memory-efficient IP and device blacklisting.
* **Real-Time Observability:** Emits JVM and application metrics via Micrometer to Prometheus and Grafana for live monitoring.

## Technology Stack

* **Language/Framework:** Java 21, Spring Boot 3.3
* **Databases:** PostgreSQL 16 (Relational), Redis Stack (Caching, Bloom Filters), Neo4j (Graph)
* **Message Broker:** Apache Kafka (KRaft mode)
* **Observability:** Prometheus, Grafana, ELK Stack (Elasticsearch, Logstash, Kibana)
* **Build Tool:** Gradle

## API & Integration

### Evaluate Transaction Endpoint
\POST /api/v1/fraud/evaluate\

**Request Body:**
\\\json
{
  "transactionId": "TXN-12345",
  "senderId": "USER-999",
  "receiverId": "MERCH-001",
  "amount": 600000.00,
  "currency": "KZT",
  "ipAddress": "192.168.1.100"
}
\\\

**Response Body (Example of a Declined Transaction):**
\\\json
{
  "transactionId": "TXN-12345",
  "verdict": "DECLINE",
  "totalRiskScore": 130,
  "triggeredRules": [
    {
      "ruleName": "AMOUNT_ANOMALY_RULE",
      "riskScorePenalty": 30,
      "reason": "Amount exceeds limit (500 000)",
      "shadowMode": false
    },
    {
      "ruleName": "EXPERIMENTAL_AI_MODEL_RULE",
      "riskScorePenalty": 100,
      "reason": "Experimental AI model flagged the transaction",
      "shadowMode": true
    }
  ]
}
\\\
*(Note: The shadow rule penalty is ignored in the total score calculation).*

## Infrastructure Setup

The entire infrastructure can be spun up locally using the provided \docker-compose.yml\ file:
\\\ash
docker compose up -d
\\\

**Containers include:**
* \postgres\ (Port 5433)
* \edis\ (Port 6379)
* \
eo4j\ (Ports 7474, 7687)
* \kafka\ (Port 9092)
* \prometheus\ & \grafana\ (Ports 9090, 3000)
* \elasticsearch\ & \kibana\ (Ports 9200, 5601)
