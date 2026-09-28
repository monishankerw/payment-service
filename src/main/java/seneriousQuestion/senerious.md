# Scenario-Based Java + Spring Boot + Microservices Interview Questions

For **5+ years experience**, scenario-based questions are more important than simple definitions. Interviewers usually ask: **“Your production system has this problem. How will you solve it?”**

Below is a **0–7 year progression**, with extra focus on the scenarios expected around 5+ years.

---

## 🟢 0–1 Year — Java + Spring Boot Scenarios

### 1. REST API is returning `400 Bad Request`

**Scenario:** A client sends JSON, but Spring Boot cannot deserialize it.

**Questions:**

* How will you identify the issue?
* How do you validate the request?
* How do you handle `HttpMessageNotReadableException`?
* How do you return a proper error response?

### 2. API returns `500 Internal Server Error`

* How do you troubleshoot?
* Where will you check logs?
* How will you implement global exception handling?
* `@ExceptionHandler` vs `@ControllerAdvice`?

### 3. Spring Boot application doesn't start

* What will you check first?
* How do you identify bean creation failures?
* What is a circular dependency?
* How would you resolve it?

### 4. Two beans of the same type exist

* What happens with `@Autowired`?
* How do `@Primary` and `@Qualifier` solve it?

### 5. Application works locally but not in UAT

* What will you check?
* Profiles?
* Environment variables?
* Database configuration?
* External service URL?
* Secrets?

---

# 🟢 1–2 Years — Database & REST Scenarios

### 6. API suddenly becomes slow

Your API normally takes **200 ms**, but now takes **5 seconds**.

**Questions:**

* How will you troubleshoot?
* Is the problem Java, database, network, or another service?
* How will you identify the slow SQL query?
* How will `EXPLAIN` help?
* Would you add caching?

---

### 7. Database connection pool is exhausted

```text
HikariPool - Connection is not available
```

**Questions:**

* Why does this happen?
* How do you investigate?
* Could long-running transactions cause it?
* How would you fix it?
* Should you simply increase the pool size?

---

### 8. JPA is executing hundreds of queries

**Scenario:**

You fetch 100 employees and see 101 SQL queries.

**Question:**

* What is the N+1 problem?
* How do you fix it?
* JOIN FETCH?
* EntityGraph?
* DTO projection?

---

### 9. Duplicate database records are created

Two requests arrive simultaneously.

**Questions:**

* Why can this happen?
* How do you prevent duplicates?
* Unique constraint?
* Idempotency?
* Locking?
* Transaction?

---

# 🟡 2–3 Years — Microservices Scenarios

### 10. Payment Service calls Wallet Service

```text
Payment Service
      |
      ↓
Wallet Service
```

Wallet Service takes 10 seconds.

**Questions:**

* What happens to Payment Service?
* How do you configure timeout?
* Should you retry?
* When should you use Circuit Breaker?
* What fallback would you implement?

---

### 11. One Microservice is down

```text
Order → Payment → Notification
```

Payment Service is unavailable.

**Questions:**

* Should Order fail immediately?
* How do you prevent cascading failure?
* Circuit Breaker?
* Retry?
* Queue?
* Fallback?

---

### 12. Service discovery fails

**Scenario:** Eureka is unavailable.

**Questions:**

* Can services continue communicating?
* What happens to already registered services?
* How would you design for Service Discovery failure?

---

### 13. API Gateway is down

```text
Client
  ↓
API Gateway ❌
  ↓
Microservices
```

**Questions:**

* What is the impact?
* How do you make Gateway highly available?
* Multiple Gateway instances?
* Load Balancer?

---

# 🟡 3–4 Years — Kafka Scenarios

### 14. Kafka message is processed twice

```text
PaymentCreated
     ↓
Kafka
     ↓
Consumer
```

The consumer processes the same payment twice.

**Questions:**

* Why can duplicate processing happen?
* How do you make the consumer idempotent?
* How can you use a transaction/business key?
* How do you handle retries?

---

### 15. Kafka consumer is slower than producer

```text
Producer → 10,000 msg/sec
Consumer → 2,000 msg/sec
```

**Questions:**

* What happens?
* What is consumer lag?
* How do you increase consumer throughput?
* Can you increase partitions?
* Can you increase consumers?
* What is the Consumer Group?

---

### 16. Kafka consumer crashes after database update

```text
Kafka Message
     ↓
Update DB ✅
     ↓
Consumer crashes ❌
     ↓
Offset not committed
```

Message is consumed again.

**Question:**
How do you prevent duplicate business processing?

Possible discussion:

* Idempotency
* DB unique constraint
* Transactional processing
* Kafka transactions
* Outbox pattern

---

### 17. Kafka message cannot be processed

**Questions:**

* Should the consumer keep retrying forever?
* What is a Dead Letter Topic?
* How do you handle poison messages?
* How do you monitor failed messages?

---

# 🔴 4–5 Years — Production Scenarios

### 18. Payment is deducted twice

This is a **very important scenario** for payment-domain interviews.

```text
Client
  ↓
Payment API
  ↓
Payment Service
  ↓
Bank/Acquirer
```

Client sends the same request twice.

**Questions:**

* How do you prevent double payment?
* What is an Idempotency Key?
* Where do you store it?
* How do you handle concurrent requests?
* Database unique constraint?
* Redis lock?
* Distributed lock?
* What happens if the external payment provider times out?

---

### 19. Payment succeeds but your service gets timeout

```text
Your Service → Bank
                  ↓
               Payment SUCCESS
                  ↓
              Network timeout
```

Your service doesn't know whether payment succeeded.

**Questions:**

* Should you retry?
* Could retry cause double payment?
* How do you reconcile the transaction?
* Would you use transaction status enquiry?
* How would you design the payment state machine?

---

### 20. Wallet debit succeeds but transaction DB update fails

```text
Wallet Debit ✅
      ↓
Transaction DB ❌
```

**Questions:**

* How do you maintain consistency?
* Can a normal `@Transactional` solve this?
* Should you use Saga?
* What is a compensating transaction?
* How would you reconcile the wallet?

---

### 21. Database is down

**Scenario:**

Spring Boot services cannot connect to MySQL.

**Questions:**

* What happens to APIs?
* Should you retry database connections?
* How should Circuit Breaker be used?
* What should the API return?
* How do you prevent request overload after DB recovery?

---

### 22. Redis goes down

Your application uses Redis for caching.

**Questions:**

* Should the application stop?
* Can it fall back to MySQL?
* What happens to Redis locks?
* How do you avoid a database traffic spike?

---

# 🔴 5–6 Years — Senior Scenarios

### 23. API receives 10× normal traffic

Normal:

```text
1,000 requests/sec
```

Suddenly:

```text
10,000 requests/sec
```

**Questions:**

* How do you scale?
* Horizontal scaling?
* Load balancer?
* Auto Scaling?
* Redis?
* Kafka?
* Rate limiting?
* Database bottleneck?

---

### 24. Database CPU reaches 100%

**Questions:**

* How do you investigate?
* Slow query log?
* `EXPLAIN`?
* Missing index?
* Full table scan?
* Connection pool?
* Read replicas?
* Caching?

---

### 25. One API is slow but database is fast

**Questions:**

* What could be causing the problem?
* External API?
* Network?
* Thread pool?
* Lock contention?
* Serialization?
* Garbage collection?
* How do you use distributed tracing?

---

### 26. Memory usage keeps increasing

Spring Boot application:

```text
Memory: 40%
       ↓
       60%
       ↓
       80%
       ↓
       95%
       ↓
       OOM
```

**Questions:**

* How do you investigate?
* Heap dump?
* GC logs?
* Memory leak?
* Large collections?
* Cache?
* Thread leak?
* How do you fix it?

---

### 27. Thread pool is exhausted

**Questions:**

* What causes thread starvation?
* How do you identify it?
* What is `ThreadPoolTaskExecutor`?
* How do you configure core/max pool size?
* Why shouldn't you simply increase the pool indefinitely?

---

# 🔴 6–7 Years — Architecture Scenarios

### 28. Design a Payment System

Requirements:

* Merchant payment
* Wallet
* Bank transfer
* Payment status
* Retry
* Notifications
* Audit
* High availability

**Questions:**

* What Microservices will you create?
* Where will Kafka be used?
* Where will Redis be used?
* Database design?
* How will you prevent duplicate payment?
* How will you handle failures?
* How will you achieve idempotency?
* How will you monitor it?

---

### 29. Design a Wallet System

```text
Client
 ↓
API Gateway
 ↓
Wallet Service
 ↓
Transaction Service
 ↓
Kafka
 ↓
Notification Service
```

**Questions:**

* How do you maintain wallet balance consistency?
* How do you handle concurrent debit?
* How do you prevent negative balance?
* Optimistic vs pessimistic locking?
* How do you handle failed transactions?
* How do you maintain an audit trail?

---

### 30. Design a Bill Payment System

**Scenario:**

Millions of users pay utility bills.

**Questions:**

* How do you handle bill fetch?
* How do you handle payment?
* How do you handle provider timeout?
* How do you retry?
* How do you prevent duplicate payment?
* How do you reconcile failed/unknown transactions?
* How do you handle provider downtime?

---

# 🔥 Rapid-Fire Senior Scenarios

These are excellent questions to practice:

31. Your API gets duplicate requests. What will you do?

32. Kafka sends duplicate messages. How will you handle them?

33. A downstream API takes 30 seconds. What will you do?

34. A downstream service is completely unavailable. What will you do?

35. Your database is slow. How will you find the root cause?

36. Redis is unavailable. What happens?

37. Kafka is unavailable. What happens?

38. Your application has high CPU. How will you troubleshoot?

39. Your application has high memory. How will you troubleshoot?

40. Your API returns intermittent 500 errors. How will you investigate?

41. Two users update the same wallet simultaneously. What happens?

42. Payment succeeds externally but your database update fails. How do you recover?

43. Database transaction succeeds but Kafka publishing fails. What do you do?

44. Kafka publishing succeeds but database transaction fails. What do you do?

45. How do you guarantee an event is not lost?

46. How do you make an API idempotent?

47. How do you implement distributed locking?

48. How do you handle a production deployment with zero downtime?

49. How do you rollback a bad deployment?

50. How would you migrate a monolith to Microservices?

---

# ⭐ Best 15 for Your 5+ Year Interview

I recommend mastering these first:

1. **Duplicate payment — idempotency**
2. **Payment timeout — unknown transaction status**
3. **Wallet concurrent debit**
4. **Kafka duplicate message**
5. **Kafka consumer lag**
6. **Database connection pool exhausted**
7. **Slow API troubleshooting**
8. **Slow SQL query / indexing**
9. **Redis failure**
10. **Downstream service failure — Circuit Breaker**
11. **Database failure**
12. **High CPU / memory in Spring Boot**
13. **Distributed transaction — Saga**
14. **DB update + Kafka event consistency — Outbox**
15. **Complete Payment/Wallet Microservices system design**

### Senior answer structure

For every scenario, answer using:

**Problem → Root Cause → Immediate Mitigation → Permanent Solution → Failure Cases → Monitoring → Trade-off**

That structure will make your answers sound much more like a **5+ year production Java/Spring Boot engineer** rather than someone giving only textbook definitions.
