# Kafka Interview Topics & Questions — 0 to 7 Years

For your **5+ years Java + Spring Boot + Microservices** profile, Kafka is a high-priority topic. Prepare it from basic messaging to production architecture and failure scenarios.

## 🟢 0–1 Year — Kafka Fundamentals

### Topics

* What is Apache Kafka?
* Event streaming
* Producer
* Consumer
* Broker
* Topic
* Partition
* Offset
* Consumer Group
* Message/Record
* Key
* Leader
* Follower
* Replication

### Questions

1. What is Kafka?
2. Why do we use Kafka?
3. Kafka vs REST API?
4. Kafka vs RabbitMQ?
5. What is a Kafka Broker?
6. What is a Topic?
7. What is a Partition?
8. What is an Offset?
9. What is a Producer?
10. What is a Consumer?
11. What is a Consumer Group?
12. What is a Kafka Key?
13. What is Replication Factor?
14. What are Leader and Follower replicas?
15. How does a Kafka message flow from Producer to Consumer?

### Basic Flow

```text
Producer
   |
   v
 Kafka Topic
   |
+--+---------+
|            |
P0           P1
|            |
Consumer     Consumer
```

---

# 🟢 1–2 Years — Producer & Consumer

### Topics

* Producer configuration
* Consumer configuration
* Serialization
* Deserialization
* StringSerializer
* JSON Serializer
* JSON Deserializer
* Consumer offset
* Auto commit
* Manual commit
* Consumer group
* Partition assignment

### Questions

16. How do you create a Kafka Producer in Spring Boot?
17. How do you create a Kafka Consumer?
18. What is serialization?
19. What is deserialization?
20. Why do we need serializers?
21. What is `StringSerializer`?
22. What is `JsonSerializer`?
23. What is `JsonDeserializer`?
24. Auto commit vs manual commit?
25. What happens when a consumer crashes?
26. What happens to the unprocessed message?
27. Can two consumers in the same group consume the same partition?
28. Can multiple consumers consume one partition simultaneously?

---

# 🟡 2–3 Years — Partitions & Consumer Groups

### Topics

* Partitioning
* Partition key
* Ordering
* Consumer groups
* Rebalancing
* Offset
* Consumer lag
* Parallelism

### Questions

29. Why does Kafka use partitions?
30. How does Kafka achieve parallel processing?
31. How is a message assigned to a partition?
32. What happens if no key is provided?
33. How does Kafka maintain ordering?
34. Is ordering guaranteed across the entire topic?
35. How do you guarantee ordering for a particular user?
36. What happens when consumers are fewer than partitions?
37. What happens when consumers are more than partitions?
38. What is consumer rebalancing?
39. What causes consumer rebalancing?
40. What is consumer lag?
41. How do you reduce consumer lag?

### Important Rule

If you need ordering for a particular entity:

```text
key = userId
```

Then messages for that key are routed to the same partition, preserving partition-level ordering.

---

# 🟡 2–3 Years — Kafka Reliability

### Topics

* Acknowledgments
* `acks=0`
* `acks=1`
* `acks=all`
* Replication
* ISR
* Leader election
* Retention
* Delivery semantics

### Questions

42. What is `acks`?
43. `acks=0` vs `acks=1` vs `acks=all`?
44. What is ISR?
45. What happens when a broker goes down?
46. How does Kafka recover from broker failure?
47. What is replication factor?
48. What is message retention?
49. Does Kafka delete a message after consumption?
50. How long can Kafka retain messages?
51. At-most-once vs at-least-once vs exactly-once?

---

# 🟠 3–4 Years — Spring Boot + Kafka

### Topics

* Spring Kafka
* `KafkaTemplate`
* `@KafkaListener`
* Consumer Factory
* Producer Factory
* Listener Container
* Error Handler
* Retry
* Dead Letter Topic
* Batch consumption
* Manual acknowledgment

### Questions

52. How do you integrate Kafka with Spring Boot?
53. What is `KafkaTemplate`?
54. What is `@KafkaListener`?
55. How do you configure ProducerFactory?
56. How do you configure ConsumerFactory?
57. How do you handle Kafka exceptions?
58. How do you implement retry?
59. What is a Dead Letter Topic?
60. How do you send failed messages to DLT?
61. How do you manually acknowledge Kafka messages?
62. How do you configure multiple consumers?
63. How do you process Kafka messages in batches?

### Typical Spring Boot Flow

```text
Spring Boot Service
       |
       v
 KafkaTemplate
       |
       v
    Kafka
       |
       v
@KafkaListener
       |
       v
Business Logic
```

---

# 🔴 4–5 Years — Kafka Failure Scenarios

These are **very important for your experience level**.

### 64. Consumer processes the same message twice

```text
Kafka
  ↓
Consumer
  ↓
DB Update ✅
  ↓
Consumer crashes
  ↓
Offset not committed
  ↓
Message consumed again
```

**Questions:**

* Why did duplicate processing happen?
* How do you make processing idempotent?
* How do you prevent duplicate business transactions?
* Can you use a unique transaction ID?
* Can you use a database constraint?

---

### 65. Consumer is slower than Producer

```text
Producer: 20,000 msg/sec
Consumer:  5,000 msg/sec
```

**Questions:**

* What happens?
* What is consumer lag?
* How do you increase throughput?
* Increase partitions?
* Add consumers?
* Batch processing?
* Optimize database calls?

---

### 66. Kafka message processing fails

**Questions:**

* Should you retry?
* How many times?
* What happens after retry exhaustion?
* What is DLT?
* How do you investigate failed messages?

---

### 67. Database update succeeds but Kafka offset isn't committed

**Questions:**

* What happens?
* Can the message be processed again?
* How do you make the operation idempotent?
* How can Kafka + DB consistency be handled?

---

### 68. Kafka publish succeeds but database transaction fails

```text
DB Transaction ❌
Kafka Event    ✅
```

**Questions:**

* What inconsistency is created?
* How can you solve it?
* Explain Transactional Outbox Pattern.
* Why is Outbox useful?

---

# 🔴 5–6 Years — Advanced Kafka

### Topics

* Exactly-once semantics
* Idempotent Producer
* Kafka Transactions
* Transactional Outbox
* CDC
* Debezium
* Schema Registry
* Avro
* Protobuf
* Consumer lag monitoring
* Partition strategy
* Rebalancing
* Backpressure
* Batch processing
* Compaction
* Retention policies

### Questions

69. What is exactly-once processing?
70. What is an idempotent producer?
71. What is Kafka transaction?
72. Kafka transaction vs database transaction?
73. What is Transactional Outbox?
74. How does Outbox solve DB + Kafka consistency?
75. What is CDC?
76. What is Debezium?
77. What is Schema Registry?
78. Avro vs JSON?
79. What is log compaction?
80. Retention vs compaction?
81. How do you evolve Kafka message schemas?
82. How do you handle backward compatibility?
83. How do you monitor Kafka consumer lag?
84. How do you handle millions of Kafka messages?

---

# 🔴 6–7 Years — Kafka Architecture

### Topics

* Kafka cluster design
* Partition strategy
* Replication
* Multi-broker architecture
* Multi-datacenter
* Disaster recovery
* MirrorMaker
* Kafka security
* SASL
* SSL/TLS
* ACLs
* Capacity planning
* Performance tuning
* High availability
* Event-driven architecture

### Questions

85. How would you design a Kafka cluster for high availability?
86. How do you choose the number of partitions?
87. How do you choose replication factor?
88. How do you scale Kafka?
89. How do you handle broker failure?
90. How do you handle a partition leader failure?
91. How do you design Kafka for millions of events per second?
92. How do you handle cross-region Kafka replication?
93. What is MirrorMaker?
94. How do you secure Kafka?
95. SASL vs SSL?
96. What are Kafka ACLs?
97. How do you monitor Kafka in production?
98. How do you troubleshoot high consumer lag?
99. How do you troubleshoot producer latency?
100. How do you handle Kafka cluster failure?

---

# 🔥 Scenario-Based Kafka Questions

### Scenario 1 — Duplicate Payment

```text
Payment Service
      ↓
Kafka
      ↓
Transaction Service
```

The same payment event is received twice.

**Question:** How do you prevent the transaction from being recorded twice?

Expected areas:

* Idempotency key
* Transaction ID
* Unique DB constraint
* Consumer-side deduplication
* Transaction handling

---

### Scenario 2 — Ordering

A user performs:

```text
DEBIT
CREDIT
REFUND
```

The events must be processed in this order.

**Question:** How will you guarantee ordering?

Expected:

* Same partition
* Same message key, e.g. `walletId`
* Partition-level ordering

---

### Scenario 3 — Consumer Lag

```text
Incoming: 100,000/sec
Processing: 20,000/sec
```

**Question:** How do you fix it?

Consider:

* More partitions
* More consumers
* Consumer group scaling
* Batch processing
* Async DB operations
* Reduce expensive processing
* Optimize downstream services

---

### Scenario 4 — Poison Message

One message always causes an exception.

**Question:** How do you prevent it from blocking the consumer?

Expected:

* Retry
* Retry limit
* DLT
* Monitoring
* Manual replay after correction

---

### Scenario 5 — DB + Kafka Consistency

```text
Save Payment → DB
Publish PaymentCreated → Kafka
```

DB succeeds but Kafka publish fails.

**Question:** How do you prevent event loss?

**Answer area:** Transactional Outbox Pattern.

```text
Application
   |
   +---- DB Transaction
   |       |
   |       +-- Payment
   |       +-- Outbox Event
   |
   +---- Outbox Publisher
             |
             ↓
           Kafka
```

---

# ⭐ Kafka Coding Questions

Prepare these in Spring Boot:

1. Create Kafka Producer.
2. Create Kafka Consumer.
3. Send JSON object using `KafkaTemplate`.
4. Consume JSON object using `@KafkaListener`.
5. Configure multiple partitions.
6. Configure consumer group.
7. Implement manual acknowledgment.
8. Implement retry.
9. Implement DLT.
10. Handle deserialization errors.
11. Implement idempotent consumer.
12. Implement Kafka + DB transaction handling.
13. Implement Transactional Outbox.
14. Process Kafka messages in batches.
15. Monitor consumer lag.

---

# 📌 Kafka 0–7 Year Roadmap

| Experience | Topics                                                      |
| ---------- | ----------------------------------------------------------- |
| **0–1**    | Kafka, Broker, Topic, Producer, Consumer, Partition, Offset |
| **1–2**    | Consumer Groups, Serialization, Deserialization, Commit     |
| **2–3**    | Partitioning, Ordering, Replication, Consumer Lag           |
| **3–4**    | Spring Kafka, KafkaTemplate, Listener, Retry, DLT           |
| **4–5**    | Idempotency, Failure Handling, Outbox, DB + Kafka           |
| **5–6**    | Exactly Once, Transactions, CDC, Schema Registry            |
| **6–7**    | Cluster Design, Scaling, HA, Security, Multi-Region         |

## 🔥 For your 5+ years interview, master these first

**Producer → Consumer → Topic → Partition → Offset → Consumer Group → Ordering → Replication → Consumer Lag → Spring Kafka → Retry → DLT → Idempotency → Duplicate Messages → Kafka + DB Consistency → Transactional Outbox → Exactly-Once → Kafka Scaling → Production Troubleshooting.**
