# Anti-Fraud Engine Roadmap

🚀 **Future Enhancements (Level-Up for Senior / FinTech Leads)**

These features aim to solve real enterprise challenges and take the project to the next level:

### 1. "Outbox" Pattern (Guaranteed Delivery)
**Concept:** If Kafka goes down for a millisecond, transactions shouldn't be lost.
**Implementation:** Transactions will be saved in PostgreSQL into an `outbox_events` table (ACID), while a background process (Debezium or Spring Scheduler) reads this table and guarantees pushing to Kafka.

### 2. Idempotency
**Concept:** Protection against duplicated HTTP requests (e.g., if the network blinks and the app sends a request twice).
**Implementation:** Save `transactionId` in Redis for 24 hours. Upon a duplicate request, return the cached result without running the rules again.

### 3. Impossible Travel Rule
**Concept:** Advanced geo-analytics.
**Implementation:** Utilize the MaxMind (GeoIP) database. If a transfer is from Almaty, and 5 minutes later from London — block it.

### 4. Shadow Mode for Rules
**Concept:** Safe testing of new rules in production without actually blocking clients.
**Implementation:** Add an `isShadowMode = true` flag to the `FraudRule` interface. The rule calculates the score and writes to logs (ELK), but does not affect the `totalRiskScore`.

### 5. Real-Time Metrics (Prometheus + Grafana)
**Concept:** Business monitoring.
**Implementation:** Integrate Micrometer to collect TPS, % of blocks, and latency. Set up Grafana for visualizing intuitive dashboards.

### 6. Money Laundering Chain Detection (Graph Database - Neo4j)
**Concept:** Finding circular transfers (A -> B -> C -> A).
**Implementation:** Use a graph database like Neo4j, as relational databases struggle with complex relationship traversals.
