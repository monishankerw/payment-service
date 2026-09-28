# Spring Boot Scenario-Based Questions — 0 to 7 Years

Here is a **complete 0–7 year progression**. The difficulty increases from basic troubleshooting to senior architecture and production scenarios.

---

## 🟢 0–1 Year — Basic Spring Boot Scenarios

### Topics

* Spring Boot basics
* Dependency Injection
* Beans
* REST API
* Controller/Service/Repository
* Configuration
* Profiles
* Basic exception handling
* Validation
* Spring Boot startup

### Questions

1. Your Spring Boot application is not starting. How will you find the error?
2. You get `NoSuchBeanDefinitionException`. What could be the reason?
3. A REST API returns `404`. How will you troubleshoot it?
4. Your API returns `400 Bad Request`. What could cause it?
5. Your API returns `500 Internal Server Error`. How do you debug it?
6. You created `@Service`, but Spring cannot inject it. Why?
7. What happens if `@ComponentScan` doesn't include your package?
8. Why would you use constructor injection?
9. Your application works locally but fails with a missing environment variable. How do you fix it?
10. How do you configure different properties for DEV, UAT and PROD?
11. How do you validate request fields?
12. How do you create a global exception handler?
13. How do you change the server port?
14. How do you return a proper HTTP status from a REST API?
15. What happens when Spring Boot starts?

### Example

```text
GET /users/10
      ↓
Controller
      ↓
Service
      ↓
Repository
      ↓
Database
```

---

# 🟢 1–2 Years — REST + Database Scenarios

### Topics

* Spring MVC
* REST
* DTO
* Validation
* JPA
* Hibernate
* Transactions basics
* MySQL
* Exception handling
* Profiles
* Actuator

### Questions

16. Your `POST /users` API creates duplicate users. How do you prevent it?

17. An API is returning database entities directly. What problems can this cause?

18. Your JPA query is taking 5 seconds. How would you investigate?

19. Your application gets:

```text
LazyInitializationException
```

What does it mean and how do you solve it?

20. Your database connection fails only in production. How do you troubleshoot?

21. An API returns thousands of records and becomes slow. What would you change?

22. How would you implement pagination?

23. Two users update the same record at the same time. What problems can occur?

24. How would you handle database exceptions?

25. How would you configure multiple databases?

26. What happens if an exception occurs inside a `@Transactional` method?

27. Why shouldn't you put all business logic inside the controller?

28. How do you implement request validation?

29. How do you expose application health information?

30. How do you externalize database credentials?

---

# 🟡 2–3 Years — JPA + Security + Performance

### Topics

```text
JPA/Hibernate
Transactions
Spring Security
JWT
Caching
Redis
Connection Pool
Async
Scheduling
Actuator
```

### Questions

31. Your API is slow because of an N+1 query problem. How do you fix it?

32. Your HikariCP connection pool is exhausted. What could be the reason?

33. What happens when a database connection is not released?

34. How would you optimize a slow JPA query?

35. When would you use `JOIN FETCH`?

36. When would you use DTO projection?

37. Two requests update the same wallet simultaneously. How would you prevent lost updates?

38. Explain optimistic locking using `@Version`.

39. When would you use pessimistic locking?

40. Your JWT is expired. What should your API return?

41. Difference between:

```text
401 Unauthorized
403 Forbidden
```

42. How would you make `/login` public but `/payment` authenticated?

43. How would you implement role-based authorization?

44. Your Redis cache contains stale data. How do you solve it?

45. Redis is down. Should your application completely fail?

46. How would you use `@Cacheable`?

47. When would you use `@Async`?

48. What happens if an `@Async` method throws an exception?

49. How would you implement scheduled jobs in Spring Boot?

50. How would you monitor a Spring Boot application?

---

# 🟡 3–4 Years — Microservices Scenarios

### Topics

```text
Microservices
API Gateway
Service Discovery
Feign
REST
Resilience4j
Circuit Breaker
Retry
Timeout
Kafka
Redis
Distributed Transactions
```

### Questions

51. Payment Service calls Bank Service, but Bank Service is down. What happens?

52. One microservice takes 30 seconds to respond. How do you protect your application?

53. How would you implement a timeout?

54. Should you retry every failed API request?

55. What is the danger of unlimited retries?

56. How does a Circuit Breaker help?

57. What happens when the Circuit Breaker is OPEN?

58. How would you handle:

```text
Payment Service
      ↓
Bank API
      ↓
Timeout
```

59. Payment succeeded at the bank but your application received a timeout. What would you do?

60. How do you prevent duplicate payment when the client retries?

61. How would you implement idempotency?

62. How would you pass correlation IDs between microservices?

63. How would you handle distributed logging?

64. How would you handle configuration for multiple microservices?

65. What happens if API Gateway goes down?

66. How would you scale one Spring Boot microservice independently?

67. How would you handle service-to-service authentication?

68. REST vs Kafka — when would you use each?

69. How would you handle a slow downstream service without blocking all application threads?

70. How would you implement graceful degradation?

---

# 🟠 4–5 Years — Advanced Production Scenarios

### Topics

```text
Distributed Transactions
Saga
Kafka
Idempotency
Outbox
Concurrency
Performance
JVM
Thread Pools
Caching
Database Locking
Observability
```

### Questions

71. Payment succeeds but transaction-service database update fails. What will you do?

72. Database update succeeds but Kafka message publishing fails. How do you solve it?

73. Explain the Transactional Outbox Pattern.

74. How would you implement Saga for:

```text
Order → Payment → Inventory
```

75. Kafka consumer receives the same payment event twice. How do you prevent duplicate processing?

76. Kafka consumer crashes after updating the database but before acknowledging the message. What happens?

77. Kafka consumer lag keeps increasing. How do you troubleshoot?

78. Your Spring Boot application has 95% CPU usage. How do you investigate?

79. Your application throws:

```text
OutOfMemoryError
```

How do you troubleshoot it?

80. Application threads are blocked. How would you investigate?

81. How do you analyze a thread dump?

82. How would you tune HikariCP?

83. How would you identify a slow database query from production?

84. How would you design a cache for frequently accessed payment data?

85. How do you prevent cache stampede?

86. Database CPU reaches 100% after deployment. What would you check?

87. How would you implement distributed locking?

88. Redis lock vs database lock — when would you use each?

89. How would you handle concurrent wallet debits?

90. How would you guarantee that a wallet never becomes negative?

---

# 🔴 5–6 Years — Senior Spring Boot Scenarios

At this level, interviewers expect **design + trade-offs + production experience**.

### Topics

```text
High Availability
Scalability
Distributed Systems
Kafka
Redis
Database
Observability
Security
Performance
Failure Handling
Deployment
Cloud
```

### Questions

91. Design a highly scalable payment service using Spring Boot.

92. Design a wallet service that supports:

```text
Add Money
Debit
Credit
W2W Transfer
Withdraw
```

93. How would you guarantee wallet transaction consistency?

94. Two simultaneous requests try to debit ₹500 from a ₹700 wallet. What happens?

95. How would you prevent double spending?

96. Design an idempotent payment API.

97. Design:

```text
POST /payments
```

for millions of requests per day.

98. Payment provider is intermittently failing. Design the retry/fallback strategy.

99. Payment API receives a timeout, but the payment provider processed the transaction. How do you reconcile the status?

100. How would you design a reconciliation system?

101. How would you handle a sudden 10x traffic spike?

102. Database cannot handle the increased traffic. What options do you have?

103. How would you scale a Spring Boot application horizontally?

104. How would you make your service stateless?

105. How would you design centralized logging?

106. How would you implement distributed tracing?

107. How would you monitor:

```text
Latency
Error Rate
Throughput
CPU
Memory
DB
Kafka Lag
```

108. How would you handle a production incident without restarting the service?

109. How would you perform zero-downtime deployment?

110. How would you rollback a failed deployment?

---

# 🔴 6–7 Years — Architect-Level Scenarios

At this level, expect **architecture, trade-offs, failure handling and large-scale system design**.

### Topics

```text
Distributed Architecture
Event-Driven Architecture
DDD
CQRS
Event Sourcing
Saga
Outbox
Multi-Region
High Availability
Disaster Recovery
Service Mesh
Cloud Architecture
Security
Observability
Capacity Planning
```

### Questions

111. Design a payment platform using Spring Boot microservices.

```text
API Gateway
     ↓
Auth Service
     ↓
Payment Service
     ↓
Transaction Service
     ↓
Kafka
     ↓
Notification Service
```

Explain every component.

---

112. Design a wallet system supporting **10 million users**.

How would you handle:

```text
High traffic
Concurrency
Consistency
Database scaling
Caching
Kafka
Failures
Monitoring
```

---

113. How would you design multi-region Spring Boot services?

```text
India Region
      +
Singapore Region
      +
Global Load Balancer
```

Discuss:

* Data replication
* Failover
* Consistency
* Disaster recovery

---

114. Your primary database goes down during a payment transaction. What happens?

How do you recover safely?

---

115. Kafka cluster goes down while payments are being processed. How does your system behave?

---

116. Redis cluster goes down during peak traffic. How do you prevent database overload?

---

117. Design a distributed rate limiter for Spring Boot APIs.

---

118. Design an idempotency system that works across multiple Spring Boot instances.

---

119. Design a centralized configuration system for 100 microservices.

---

120. How would you migrate a monolithic Spring application to microservices without stopping the business?

Expected concept:

```text
Strangler Fig Pattern
```

---

# 🔥 7-Year Senior Production Scenarios

These are especially useful for **senior Java/Spring Boot interviews**.

### 121. Payment Duplicate

```text
Client
  ↓
Payment API
  ↓
Payment Provider
  ↓
SUCCESS

Response lost
  ↓
Client retries
```

**Question:** How do you guarantee the retry doesn't create another payment?

---

### 122. DB + Kafka Consistency

```text
DB UPDATE       → SUCCESS
Kafka PUBLISH   → FAILED
```

**Question:** How do you guarantee eventual event delivery?

**Expected:** Transactional Outbox.

---

### 123. Kafka + DB Failure

```text
Kafka message
      ↓
Consumer
      ↓
DB UPDATE
      ↓
Application crashes
```

**Question:** When the Kafka message is delivered again, how do you prevent duplicate DB processing?

---

### 124. High Traffic

```text
Normal traffic: 5,000 req/sec

Peak traffic: 50,000 req/sec
```

**Question:** How would you scale the system?

Discuss:

```text
Load Balancer
Horizontal Scaling
Redis
Kafka
DB Read Replicas
Connection Pool
Caching
Async Processing
Rate Limiting
Auto Scaling
```

---

### 125. Production Memory Problem

```text
Memory: 92%
GC: Very High
Latency: Increasing
```

**Question:** What would you investigate?

```text
Heap
GC
Thread dump
Heap dump
Object allocation
Cache
Large collections
Memory leak
```

---

# ⭐ Experience-Wise Priority

| Experience | Main Focus                                      |
| ---------- | ----------------------------------------------- |
| **0–1**    | Spring Boot basics + REST + DI                  |
| **1–2**    | JPA + DB + validation + exceptions              |
| **2–3**    | Security + JWT + Redis + transactions           |
| **3–4**    | Microservices + Gateway + Resilience4j + Kafka  |
| **4–5**    | Distributed transactions + Outbox + concurrency |
| **5–6**    | Scalability + performance + production          |
| **6–7**    | Architecture + HA + distributed systems         |
| **7+**     | System design + architecture + trade-offs       |

## 🔥 Most Important 20 for Your Preparation

If you are preparing for **5+ year Java/Spring Boot interviews**, make these your first priority:

1. Slow API troubleshooting
2. `@Transactional` behavior
3. HikariCP connection pool exhaustion
4. JPA N+1 problem
5. Optimistic vs pessimistic locking
6. Duplicate payment prevention
7. Idempotency
8. External API timeout
9. Circuit Breaker + Retry
10. Kafka duplicate messages
11. Kafka consumer failure
12. Kafka lag
13. DB + Kafka consistency
14. Transactional Outbox
15. Saga pattern
16. Redis failure
17. High CPU
18. OutOfMemoryError
19. High-traffic scalability
20. Payment/wallet system design

### Senior answer pattern

For every scenario, structure your answer like this:

```text
Problem
   ↓
Root Cause
   ↓
How I investigate
   ↓
Immediate Fix
   ↓
Permanent Solution
   ↓
Failure Cases
   ↓
Monitoring
   ↓
Trade-offs
```

That structure makes your answer sound like **real production experience**, rather than only theoretical Spring Boot knowledge.
