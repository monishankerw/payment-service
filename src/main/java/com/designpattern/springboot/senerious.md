# Spring Boot Scenario-Based Interview Questions

For a **5+ year Java/Spring Boot developer**, interviewers usually focus less on definitions and more on **production problems, debugging, performance, transactions, security, and microservices**.

## 🔥 Top 30 Spring Boot Scenarios

### 1. API Suddenly Becomes Slow

**Scenario:**
Your Spring Boot API normally responds in 200 ms, but suddenly takes 5–10 seconds.

**Questions:**

* How will you investigate?
* How will you identify whether the problem is Java, DB, network, or another service?
* What metrics/logs will you check?
* How would you fix it?

**Expected areas:**

```text
Actuator
Logs
APM
DB query performance
Thread pool
Connection pool
External API latency
CPU / Memory
GC
```

---

### 2. Database Connection Pool Exhausted

**Scenario:**

```text
HikariPool - Connection is not available,
request timed out after 30000ms
```

**Questions:**

* What does this error mean?
* Why can it happen?
* How do you troubleshoot it?
* Would you simply increase `maximum-pool-size`?

**Expected topics:**

* HikariCP
* Long-running queries
* Connection leaks
* Transactions
* DB max connections
* Pool sizing

---

### 3. `@Transactional` Is Not Working

```java
@Transactional
public void transferMoney() {
    debitWallet();
    creditWallet();
}
```

**Scenario:**
`debitWallet()` succeeds, `creditWallet()` fails, but the debit isn't rolled back as expected.

**Questions:**

* Why?
* Does `@Transactional` work for private methods?
* What happens with checked exceptions?
* What happens when one `@Transactional` method calls another?
* What is self-invocation?

---

### 4. Duplicate Payment Transaction

**Scenario:**

A customer clicks **Pay** twice because the first request appears stuck.

```text
Request 1 → Payment → SUCCESS
Request 2 → Payment → SUCCESS
```

**Questions:**

* How would you prevent duplicate payment?
* Where would you implement idempotency?
* Would Redis alone be sufficient?
* How would you handle concurrent requests?

**Expected:**

```text
Idempotency Key
      +
Unique DB Constraint
      +
Transaction
      +
Distributed Lock where appropriate
```

---

### 5. API Returns 500 Error in Production

**Scenario:**

```text
Production API → HTTP 500
Development → Works correctly
```

**Questions:**

* How will you troubleshoot?
* What logs do you check?
* How do you avoid exposing stack traces to clients?
* How do you correlate one request across services?

**Expected topics:**

* Centralized logging
* Correlation ID
* Exception handling
* Profiles
* Environment variables
* Actuator

---

# 🔥 Database + JPA Scenarios

### 6. N+1 Query Problem

**Scenario:**

One API generates:

```text
1 query → fetch users
100 queries → fetch orders
```

**Questions:**

* What is N+1?
* How do you identify it?
* How can you solve it?

Possible solutions:

```text
JOIN FETCH
@EntityGraph
DTO projection
Batch fetching
```

---

### 7. JPA API Is Very Slow

**Scenario:**

```text
GET /transactions
```

takes 8 seconds.

Database contains millions of records.

**Questions:**

* What would you check?
* Would you fetch the entire entity?
* Would you use pagination?
* What indexes would you add?

Expected:

```text
Pagination
Projection
Indexes
EXPLAIN
Query optimization
Avoid unnecessary relationships
```

---

### 8. LazyInitializationException

**Scenario:**

```text
org.hibernate.LazyInitializationException
```

occurs when returning an entity from a REST API.

**Questions:**

* Why does this happen?
* What is lazy loading?
* Should you simply change everything to `FetchType.EAGER`?
* What's the better solution?

---

### 9. Deadlock

**Scenario:**

Two payment requests occasionally fail because MySQL reports a deadlock.

**Questions:**

* What causes a deadlock?
* How do you identify it?
* How can you reduce deadlocks?
* Would retry help?

Expected:

```text
Consistent lock ordering
Short transactions
Proper indexes
Retry with backoff
```

---

### 10. Optimistic Locking

**Scenario:**

Two requests update the same wallet simultaneously.

```text
Request A → Balance = 1000
Request B → Balance = 1000

A → 900
B → 800
```

**Questions:**

* How do you prevent lost updates?
* What is `@Version`?
* Optimistic vs pessimistic locking?

---

# 🔥 Spring Security Scenarios

### 11. JWT Expired

**Scenario:**

Client sends an expired JWT.

**Questions:**

* How should the API respond?
* Where should JWT validation happen?
* Should every controller validate JWT manually?

Expected:

```text
Request
 ↓
Security Filter
 ↓
JWT Validation
 ↓
Authentication
 ↓
Controller
```

---

### 12. Some APIs Must Be Public

**Scenario:**

You have:

```text
POST /login       → Public
POST /register    → Public
GET /users        → Authenticated
GET /admin/report → ADMIN only
```

**Question:**

How would you configure Spring Security?

---

### 13. Authentication Works but Authorization Fails

**Scenario:**

User successfully logs in but receives:

```text
403 Forbidden
```

**Questions:**

* Difference between `401` and `403`?
* How do roles/authorities work?
* Where would you debug?

---

# 🔥 Exception Handling

### 14. Different Errors Need Different Responses

**Scenario:**

Your API can generate:

```text
UserNotFoundException
ValidationException
PaymentException
DatabaseException
```

You want consistent responses.

Example:

```json
{
  "timestamp": "...",
  "status": 400,
  "message": "User not found",
  "path": "/users/101"
}
```

**Question:**

How would you implement this?

Expected:

```java
@RestControllerAdvice
@ExceptionHandler
```

---

### 15. Validation Failure

Request:

```json
{
  "email": "",
  "age": -5
}
```

**Questions:**

* How do you validate it?
* How do you return all validation errors?
* Difference between `@Valid` and `@Validated`?

---

# 🔥 Microservices Scenarios

### 16. Downstream Service Is Down

```text
Payment Service
      ↓
Bank Service ❌
```

**Questions:**

* Should Payment Service wait indefinitely?
* How do you prevent cascading failures?
* What happens to the transaction?

Expected:

```text
Timeout
Circuit Breaker
Retry
Fallback
Idempotency
```

---

### 17. External API Takes 30 Seconds

**Scenario:**

Your Spring Boot application calls a third-party payment provider.

Sometimes it takes 30 seconds.

**Questions:**

* How would you configure timeout?
* Would you retry?
* How many retries?
* What if the payment actually succeeded but the response was lost?

This is a **very important payment-domain scenario**.

---

### 18. Service-to-Service Communication

```text
Order Service
      ↓
Payment Service
```

**Question:**

Would you use:

```text
REST
```

or:

```text
Kafka
```

or both?

Explain your decision based on:

* synchronous response requirement
* reliability
* latency
* coupling
* eventual consistency

---

### 19. Distributed Transaction

```text
Order Service → Payment Service → Inventory Service
```

Payment succeeds but Inventory fails.

**Questions:**

* How do you maintain consistency?
* Would you use one database transaction?
* What is Saga?
* Orchestration vs choreography?

---

# 🔥 Kafka + Spring Boot

### 20. Kafka Message Is Processed Twice

```text
PaymentCreated
      ↓
Consumer
      ↓
DB update
      ↓
Consumer crashes
      ↓
Message consumed again
```

**Question:**

How do you make the consumer idempotent?

Expected:

```text
Event ID
+
Processed-event table / unique constraint
+
Idempotent DB operation
```

---

### 21. Kafka Consumer Is Slow

**Scenario:**

```text
Producer → 10,000 msg/sec
Consumer → 2,000 msg/sec
```

Lag keeps increasing.

**Questions:**

* How do you troubleshoot?
* How do you increase consumer throughput?
* What happens if consumers > partitions?

---

### 22. DB Update + Kafka Publish

**Scenario:**

```text
DB UPDATE → SUCCESS
Kafka SEND → FAILED
```

Now database and Kafka are inconsistent.

**Question:**

How would you solve this?

Expected senior answer:

```text
Transactional Outbox Pattern
```

or appropriate Kafka transaction architecture depending on the requirements.

---

# 🔥 Redis Scenarios

### 23. Redis Is Down

Your application uses Redis for caching.

```text
Spring Boot
    ↓
Redis ❌
```

**Questions:**

* Should the complete API fail?
* How should fallback work?
* What happens to database load?

Expected:

```text
Cache failure → fallback to DB
```

with appropriate timeouts and protection against a cache stampede.

---

### 24. Cache Contains Old Data

**Scenario:**

DB:

```text
balance = 1000
```

Redis:

```text
balance = 800
```

**Questions:**

* How do you handle stale cache?
* Cache-aside?
* When do you invalidate/update cache?

---

# 🔥 Spring Boot Production Scenarios

### 25. High CPU

```text
CPU = 95%
Memory = normal
```

**Questions:**

* What will you check first?
* How do you identify the problematic thread?
* Could an infinite loop cause it?
* Could excessive GC cause it?

Expected tools:

```text
Actuator
JVM metrics
Thread dump
JFR
APM
Application logs
```

---

### 26. OutOfMemoryError

```text
java.lang.OutOfMemoryError: Java heap space
```

**Questions:**

* What causes it?
* How would you investigate?
* Would increasing `-Xmx` solve it permanently?

Expected:

```text
Heap dump
GC logs
Memory profiling
Large collections
Caches
Leaks
Object retention
```

---

### 27. Application Doesn't Start

Error:

```text
APPLICATION FAILED TO START
```

**Question:**

How do you systematically troubleshoot?

Use:

```text
1. Read root cause
2. Check nested exception
3. Check configuration
4. Check dependency versions
5. Check bean creation
6. Check environment variables
7. Check DB/external connectivity
```

---

### 28. Bean Creation Failure

```text
NoSuchBeanDefinitionException
```

**Questions:**

* Why does this happen?
* Component scanning issue?
* Missing `@Bean`?
* Wrong package?
* Profile issue?
* Dependency missing?

---

# 🔥 Advanced Spring Boot Scenarios

### 29. Application Works Locally but Not in Docker

```text
Local:
localhost:3306 → works

Docker:
Connection refused
```

**Question:**

What is wrong?

Important point:

```text
Inside container:
localhost = current container
```

Not your MySQL container.

With Docker Compose:

```text
jdbc:mysql://mysql:3306/payment
```

where `mysql` is the service name.

---

### 30. Production Deployment Without Downtime

**Scenario:**

You have:

```text
Spring Boot Payment Service
```

and need to deploy a new version while users continue making payments.

**Questions:**

* How would you deploy?
* How would you handle database migrations?
* How would you rollback?
* What happens to in-flight requests?

Expected areas:

```text
Load Balancer
Multiple instances
Rolling deployment
Blue/Green
Canary
Backward-compatible DB changes
Health checks
Graceful shutdown
```

---

# ⭐ Top 15 You Should Prepare First

For your **Java + Spring Boot + Microservices + payment-domain experience**, I would prioritize these:

| #  | Scenario                       | Priority |
| -- | ------------------------------ | -------- |
| 1  | Slow API troubleshooting       | ⭐⭐⭐⭐⭐    |
| 2  | `@Transactional` failure       | ⭐⭐⭐⭐⭐    |
| 3  | Duplicate payment              | ⭐⭐⭐⭐⭐    |
| 4  | DB connection pool exhausted   | ⭐⭐⭐⭐⭐    |
| 5  | Downstream service failure     | ⭐⭐⭐⭐⭐    |
| 6  | External payment API timeout   | ⭐⭐⭐⭐⭐    |
| 7  | Distributed transaction / Saga | ⭐⭐⭐⭐⭐    |
| 8  | Kafka duplicate message        | ⭐⭐⭐⭐⭐    |
| 9  | DB + Kafka consistency         | ⭐⭐⭐⭐⭐    |
| 10 | Redis failure                  | ⭐⭐⭐⭐     |
| 11 | JPA N+1                        | ⭐⭐⭐⭐     |
| 12 | Optimistic/pessimistic locking | ⭐⭐⭐⭐     |
| 13 | JWT/security failure           | ⭐⭐⭐⭐     |
| 14 | High CPU / memory              | ⭐⭐⭐⭐     |
| 15 | Production deployment/rollback | ⭐⭐⭐⭐     |

### Senior interview answer structure

For almost every scenario, answer in this order:

```text
1. Identify the problem
        ↓
2. Check logs + metrics
        ↓
3. Find root cause
        ↓
4. Immediate mitigation
        ↓
5. Permanent solution
        ↓
6. Failure/edge cases
        ↓
7. Monitoring & alerting
```

For example, don't just say **“I'll add a retry.”**

Say:

> “First I would identify whether the failure is transient or permanent using logs, metrics and downstream response codes. For transient failures, I would use a bounded retry with exponential backoff and a timeout. I would combine it with a circuit breaker to prevent cascading failures. For payment operations, I would also ensure idempotency because retrying a request must not result in duplicate transactions.”
