# Transactional Outbox Pattern — Interview Questions & Solutions

## 1. 0–2 Years Experience

### Q1. What is the Dual-Write Problem?

The **dual-write problem** happens when one business operation needs to write to two different systems.

For example:

```text
Payment Service
      |
      +----> MySQL
      |
      +----> Kafka
```

Example:

```java
paymentRepository.save(payment);

kafkaTemplate.send("payment-topic", event);
```

There are two separate operations.

### What could go wrong?

Suppose:

```text
paymentRepository.save(payment);    // SUCCESS
kafkaTemplate.send(...);            // FAILED
```

Result:

```text
MySQL
Payment = SUCCESS

Kafka
Event = NOT SENT
```

The database contains the payment, but other services don't know about it.

This causes **data inconsistency**.

The opposite can also be problematic:

```text
DB write      → FAILED
Kafka publish → SUCCESS
```

Now Kafka consumers may receive an event for data that was never committed to the database.

---

### Interview Answer

> "If I save to the database and then call `kafkaTemplate.send()` as two separate operations, the first operation can succeed while the second fails. For example, the payment can be committed to MySQL but the Kafka event may not be published. This creates inconsistency between the database and downstream services. This is known as the dual-write problem."

---

# 2. 2–4 Years Experience

At this level, you should be able to **implement a basic Outbox Pattern**.

## Q2. How does the Outbox Pattern solve the Dual-Write Problem?

Instead of:

```text
DB
 +
Kafka
```

we use:

```text
DB
 |
 +-- Business Table
 |
 +-- Outbox Table
```

Both are updated in the **same database transaction**.

Then:

```text
Outbox Table
     |
     ↓
Scheduled Publisher
     |
     ↓
Kafka
```

---

## Architecture

```text
                    Payment Request
                          |
                          ↓
                   Payment Service
                          |
                     @Transactional
                          |
                 +--------+--------+
                 |                 |
                 ↓                 ↓
          Payment Table      Outbox Table
                 |                 |
                 +--------+--------+
                          |
                        COMMIT
                          |
                          ↓
                  Outbox Publisher
                          |
                          ↓
                        Kafka
                          |
                          ↓
                 Consumer Services
```

---

# 3. Basic Outbox Table

A typical table:

```text
outbox_event
------------------------------------------------
id
event_id
event_type
payload
status
retry_count
created_at
processed_at
------------------------------------------------
```

Example:

```text
id = 1
event_id = EVT-1001
event_type = PAYMENT_SUCCESS
payload = {...}
status = NEW
retry_count = 0
```

---

# 4. Basic Implementation

## Payment Entity

```java
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;

    private String customerId;

    private BigDecimal amount;

    private String status;

    private LocalDateTime createdAt;
}
```

---

## Outbox Entity

```java
@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId;

    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String status;

    private int retryCount;

    private LocalDateTime createdAt;

    private LocalDateTime processedAt;
}
```

---

# 5. Transactional Service

The important part is:

```java
@Transactional
public String createPayment(PaymentRequest request) {

    String transactionId =
            UUID.randomUUID().toString();

    Payment payment = new Payment();

    payment.setTransactionId(transactionId);
    payment.setCustomerId(request.getCustomerId());
    payment.setAmount(request.getAmount());
    payment.setStatus("SUCCESS");
    payment.setCreatedAt(LocalDateTime.now());

    paymentRepository.save(payment);

    PaymentEvent event = new PaymentEvent();

    event.setEventId(UUID.randomUUID().toString());
    event.setTransactionId(transactionId);
    event.setCustomerId(request.getCustomerId());
    event.setAmount(request.getAmount());
    event.setStatus("SUCCESS");

    OutboxEvent outbox = new OutboxEvent();

    outbox.setEventId(event.getEventId());
    outbox.setEventType("PAYMENT_SUCCESS");
    outbox.setPayload(toJson(event));
    outbox.setStatus("NEW");
    outbox.setRetryCount(0);
    outbox.setCreatedAt(LocalDateTime.now());

    outboxRepository.save(outbox);

    return transactionId;
}
```

The important point is:

```text
@Transactional
       |
       +---- Payment Save
       |
       +---- Outbox Save
       |
      COMMIT
```

---

# 6. Why Does This Guarantee the Event Is Not Lost?

This is a common interview question.

### Question

> "How does the Outbox pattern guarantee the event is only lost if the DB write itself also failed?"

### Answer

Because the business data and event are written in the **same database transaction**.

### Successful case

```text
Save Payment       ✅
Save Outbox Event  ✅
       |
     COMMIT
       |
       ↓
Data + Event safely stored
```

Now even if Kafka is down:

```text
MySQL       ✅
Outbox      ✅
Kafka       ❌
```

The event still exists in the database.

The publisher can retry later.

---

### Database failure

If the transaction fails:

```text
Save Payment       ❌
Save Outbox Event  ❌
       |
     ROLLBACK
```

No event is committed.

Therefore:

```text
DB transaction failed
        ↓
Business data not committed
        ↓
Outbox event not committed
```

The important guarantee is:

> **If the database transaction commits, the outbox event is durably stored with the business data.**

---

# 7. Scheduled Publisher

The publisher periodically checks:

```text
status = NEW
```

For example:

```java
@Scheduled(fixedDelay = 5000)
public void publishEvents() {

    List<OutboxEvent> events =
            outboxRepository
                    .findTop100ByStatusOrderByCreatedAtAsc(
                            "NEW"
                    );

    for (OutboxEvent event : events) {

        // Publish to Kafka

        // If successful:
        // status = PUBLISHED

        // If failed:
        // retry later
    }
}
```

---

# 8. Publishing Flow

```text
Outbox Table
     |
     ↓
Find NEW events
     |
     ↓
Publish to Kafka
     |
     ↓
Kafka ACK?
    / \
  YES  NO
   |    |
   ↓    ↓
PUBLISHED
        |
        ↓
      RETRY
```

---

# 9. What Delivery Guarantee Does Outbox Provide?

The basic Outbox Pattern provides:

> **At-least-once delivery**

It does **not** automatically provide exactly-once delivery.

Why?

Consider:

```text
Outbox
   |
   ↓
Kafka Publish
   |
   ↓
Kafka receives event ✅
   |
   ↓
Application crashes ❌
   |
   ↓
Outbox still says NEW
```

When the application restarts:

```text
NEW
 ↓
Publish again
 ↓
Kafka
```

Kafka may now receive the same event twice.

```text
EVT-1001
EVT-1001
```

Therefore, duplicates are possible.

---

# 10. 4–7 Years Experience

At the 4–7 year level, interviewers may expect a more scalable design.

Instead of:

```text
Outbox Table
     ↓
Polling every 5 seconds
     ↓
Kafka
```

you can use:

```text
Outbox Table
     ↓
CDC
     ↓
Debezium
     ↓
Kafka Connect
     ↓
Kafka
```

---

# 11. CDC-Based Outbox

CDC means:

> **Change Data Capture**

Instead of continuously polling the database:

```text
SELECT *
FROM outbox_event
WHERE status = 'NEW';
```

a CDC system watches database changes.

Architecture:

```text
                    Application
                         |
                         ↓
                   MySQL Transaction
                         |
                +--------+--------+
                |                 |
                ↓                 ↓
           Payment Table     Outbox Table
                                  |
                                  ↓
                             MySQL Binlog
                                  |
                                  ↓
                              Debezium
                                  |
                                  ↓
                           Kafka Connect
                                  |
                                  ↓
                                Kafka
```

---

# 12. Why CDC?

Polling has overhead.

For example:

```text
Every 5 seconds:

SELECT ...
FROM outbox_event
WHERE status = 'NEW';
```

If you have many services and large outbox tables, this creates:

```text
Database queries
       ↓
CPU usage
       ↓
IO
       ↓
Polling overhead
```

CDC instead reads database changes from the database transaction log/binlog.

Advantages:

```text
Lower latency
Less polling
Better scalability
Near-real-time event delivery
```

---

# 13. Debezium

[Debezium documentation](https://debezium.io/documentation/?utm_source=chatgpt.com)

Debezium captures database changes.

For MySQL:

```text
MySQL
  ↓
Binlog
  ↓
Debezium
  ↓
Kafka Connect
  ↓
Kafka
```

For example:

```text
INSERT INTO outbox_event
```

happens.

Debezium detects the change:

```text
INSERT
  ↓
Debezium
  ↓
Kafka
```

---

# 14. Outbox + Idempotent Consumer

This is extremely important for a **4–7 year interview**.

Outbox gives:

```text
At-least-once delivery
```

Therefore:

```text
Event
  ↓
Kafka
  ↓
Consumer
```

may result in:

```text
EVT-1001
EVT-1001
```

The consumer must handle duplicates.

---

# 15. Idempotent Consumer Pattern

Consumer receives:

```text
eventId = EVT-1001
```

First time:

```text
EVT-1001
   ↓
Not processed
   ↓
Process
   ↓
Save event ID
```

Second time:

```text
EVT-1001
   ↓
Already processed
   ↓
Ignore
```

Database:

```text
processed_events
---------------------
event_id
---------------------
EVT-1001
```

You can enforce uniqueness:

```text
event_id UNIQUE
```

This prevents the same event from being processed twice.

---

# 16. Why Outbox Doesn't Guarantee Exactly-Once

### Interview Question

> "Why does Outbox alone not guarantee exactly-once delivery?"

### Answer

Because there is a failure window between:

```text
Kafka Publish
     ↓
Mark Outbox as PUBLISHED
```

Suppose:

```text
1. Publish to Kafka       ✅

2. Application crashes    ❌

3. Mark PUBLISHED         never happens
```

The outbox record still looks like:

```text
NEW
```

After restart:

```text
NEW
 ↓
Publish again
```

Therefore Kafka may receive:

```text
EVT-1001
EVT-1001
```

So:

```text
Outbox
  =
At-least-once
```

not:

```text
Outbox
  =
Exactly-once
```

---

# 17. What Pattern Do You Pair With Outbox?

Answer:

> **Idempotent Consumer Pattern**

Architecture:

```text
Outbox
   ↓
CDC / Publisher
   ↓
Kafka
   ↓
Consumer
   ↓
Check eventId
   ↓
Already processed?
   /       \
 YES       NO
  |         |
Ignore    Process
            |
            ↓
      Save eventId
```

---

# 18. Outbox Table Growth

Another senior-level concern is:

> "What happens when the Outbox table becomes very large?"

If you continuously insert events:

```text
outbox_event

1 million
10 million
100 million
```

then queries and storage can become problematic.

You need a cleanup strategy.

---

# 19. Outbox Cleanup

Possible approaches:

### Option 1 — Delete published events

Periodically:

```text
DELETE
FROM outbox_event
WHERE status = 'PUBLISHED'
AND processed_at < retention period;
```

For example:

```text
Keep 7 days
Delete older records
```

---

### Option 2 — Archive

Move old records:

```text
outbox_event
     ↓
archive_outbox_event
```

Then delete from the active table.

---

### Option 3 — Partitioning

Partition the table based on:

```text
created_at
```

For very large systems, partitioning can make cleanup more manageable.

---

# 20. Monitor Outbox Publishing Lag

A production system should monitor:

```text
Number of NEW events
Number of FAILED events
Publishing latency
Oldest unpublished event
Retry count
Kafka publishing failures
Consumer lag
```

One particularly useful metric:

```text
Outbox Publishing Lag
```

Example:

```text
Current Time:
10:30:00

Oldest NEW event:
10:25:00

Publishing Lag:
5 minutes
```

If this suddenly becomes:

```text
30 minutes
1 hour
2 hours
```

you likely have a problem with the publisher, Kafka, or database.

---

# 21. Polling Outbox vs CDC Outbox

| Feature        | Polling Outbox                     | CDC Outbox               |
| -------------- | ---------------------------------- | ------------------------ |
| Mechanism      | Scheduled DB queries               | Database transaction log |
| Latency        | Usually polling interval dependent | Near real-time           |
| DB polling     | Yes                                | No continuous polling    |
| Setup          | Simple                             | More complex             |
| Scalability    | Moderate                           | Better for high scale    |
| Infrastructure | Application scheduler              | Debezium + Kafka Connect |
| Good for       | Small/medium systems               | High-scale systems       |

---

# 22. Migration From Polling to CDC Without Downtime

This is a very good **4–7 year interview question**.

### Question

> "How would you migrate from a polling-based Outbox Publisher to CDC without downtime?"

Don't immediately remove the existing publisher.

Use a gradual migration.

---

## Step 1 — Existing System

Currently:

```text
Application
    ↓
MySQL
    ↓
Outbox
    ↓
Polling Publisher
    ↓
Kafka
```

Everything is working.

---

## Step 2 — Introduce CDC

Add:

```text
MySQL
  ↓
Binlog
  ↓
Debezium
  ↓
Kafka Connect
```

while keeping the existing polling publisher.

Now:

```text
                 MySQL
                   |
              Outbox Table
                   |
           +-------+-------+
           |               |
           ↓               ↓
      Polling          Debezium
      Publisher            |
           |               |
           ↓               ↓
             Kafka
```

---

# 23. Problem: Duplicate Events During Migration

Both publishers can send the same event.

Example:

```text
Polling Publisher
      ↓
EVT-1001
      ↓
Kafka

Debezium
      ↓
EVT-1001
      ↓
Kafka
```

Consumer might receive:

```text
EVT-1001
EVT-1001
```

Therefore, make the consumer idempotent **before enabling both publishers**.

Use:

```text
eventId
```

with a unique constraint.

---

# 24. Step 3 — Validate CDC

Run both systems for a period:

```text
Polling Publisher
       +
Debezium CDC
```

Monitor:

```text
Event counts
Kafka records
Failures
Duplicates
Publishing latency
Consumer processing
```

Make sure CDC is successfully capturing all expected outbox events.

---

# 25. Step 4 — Reduce Polling

Once CDC is stable:

```text
Polling
100%
```

reduce/disable it carefully.

For example:

```text
CDC = Primary

Polling = Fallback
```

Monitor the system.

---

# 26. Step 5 — Remove Polling Publisher

Once you're confident:

```text
Polling Publisher → Removed
```

Final architecture:

```text
Application
    |
    ↓
MySQL
    |
Outbox Table
    |
MySQL Binlog
    |
Debezium
    |
Kafka Connect
    |
Kafka
    |
Consumer
    |
Idempotency
```

There is no downtime because CDC was introduced and validated **before** the polling publisher was removed.

---

# 27. Senior-Level Final Architecture

```text
                         Payment API
                              |
                              ↓
                       Payment Service
                              |
                        @Transactional
                              |
                   +----------+----------+
                   |                     |
                   ↓                     ↓
             Payment Table          Outbox Table
                                         |
                                         ↓
                                    MySQL Binlog
                                         |
                                         ↓
                                     Debezium
                                         |
                                         ↓
                                  Kafka Connect
                                         |
                                         ↓
                                       Kafka
                                         |
                         +---------------+---------------+
                         |               |               |
                         ↓               ↓               ↓
                  Notification      Transaction      Reporting
                     Service           Service         Service
                         |
                         ↓
                Idempotent Consumer
                         |
                         ↓
                  Processed Event ID
```

---

# 28. Complete Interview Answer — 2–4 Years

> "The Transactional Outbox Pattern solves the dual-write problem between a database and a message broker. Instead of saving the business data to MySQL and directly publishing to Kafka, I save both the business data and an outbox event in the same database transaction using `@Transactional`. A scheduled publisher reads pending outbox events and publishes them to Kafka. If Kafka is unavailable, the event remains in the database and can be retried. Therefore, once the database transaction commits, the event isn't lost even if Kafka is temporarily unavailable. The delivery guarantee is at-least-once."

---

# 29. Complete Interview Answer — 4–7 Years

> "At scale, I can implement the Outbox pattern using CDC rather than polling. The application writes the business record and outbox event in the same MySQL transaction. Debezium reads the MySQL binlog and Kafka Connect publishes the outbox changes to Kafka, which reduces polling overhead and provides lower latency.
>
> However, Outbox provides at-least-once delivery, not exactly-once delivery. There can be a failure between Kafka publishing and marking the event as processed, resulting in duplicate events. Therefore, I pair Outbox with an Idempotent Consumer using a unique event ID or transaction ID.
>
> I also need operational concerns such as outbox table cleanup or partitioning, monitoring publishing lag, retry counts, failed events, and Kafka consumer lag.
>
> For migration from polling to CDC, I would first introduce Debezium alongside the existing publisher, make consumers idempotent, validate CDC event completeness and latency, run both during a controlled period, then switch CDC to primary and gradually remove the polling publisher. This allows migration without downtime."

---

# 30. What to Remember for the Interview

```text
                    DUAL WRITE
                        |
                        ↓
              DB + Kafka separately
                        |
                        ↓
                 Inconsistency
                        |
                        ↓
                 OUTBOX PATTERN
                        |
                        ↓
          DB + Outbox same transaction
                        |
                        ↓
              At-least-once delivery
                        |
                        ↓
                    Kafka
                        |
                        ↓
              Possible duplicates
                        |
                        ↓
             IDEMPOTENT CONSUMER
                        |
                        ↓
                 Duplicate safe
```

For **0–2 years**, know the problem.

For **2–4 years**, know how to build **Outbox + Scheduler + Retry**.

For **4–7 years**, know **CDC + Debezium + Kafka Connect + Idempotent Consumer + cleanup + monitoring + zero-downtime migration**.
