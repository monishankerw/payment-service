# Transactional Outbox Pattern — Easy Explanation

The **Outbox Pattern** is a design pattern used in microservices to reliably send events/messages to systems like **Kafka** while keeping database data consistent.

For your **Spring Boot + MySQL + Kafka payment service**, think of it like this:

```text
Client
  ↓
Payment Service
  ↓
MySQL
  ├── Payment Table
  └── Outbox Table
          ↓
     Outbox Publisher
          ↓
        Kafka
          ↓
   Other Microservices
```

## 1. What problem does it solve?

Suppose your Payment Service has to do two things:

```text
1. Save payment in MySQL
2. Send PAYMENT_SUCCESS event to Kafka
```

Without Outbox:

```text
Payment Service
     |
     +----> MySQL ✅
     |
     +----> Kafka ❌
```

Example:

```text
MySQL:
Payment = SUCCESS

Kafka:
Event = NOT SENT
```

Now your systems are inconsistent.

This is called the **Dual Write Problem**.

---

# 2. What does Outbox Pattern do?

Instead of directly sending to Kafka, we first save the event in an **Outbox Table**.

```text
Payment Service
      |
      ↓
 MySQL Transaction
      |
      ├── Payment Table
      |
      └── Outbox Table
              |
             COMMIT
              |
              ↓
       Outbox Publisher
              |
              ↓
            Kafka
```

The key idea is:

> **Store the business data and the event in the same database transaction.**

---

# 3. Example

User sends:

```json
{
  "customerId": "CUST1001",
  "amount": 5000
}
```

Payment Service starts a transaction.

### Step 1 — Save Payment

```text
payments

transactionId = TXN123
customerId    = CUST1001
amount        = 5000
status        = SUCCESS
```

### Step 2 — Save Event

At the same time:

```text
outbox_event

eventId       = EVT123
eventType     = PAYMENT_SUCCESS
payload       = {...}
status        = NEW
```

Both happen inside:

```text
@Transactional
```

Then:

```text
COMMIT
```

---

# 4. Why is this reliable?

Suppose Kafka is down.

```text
Payment → MySQL       ✅
Outbox → MySQL        ✅
Kafka                  ❌
```

That's okay.

The event is safely stored:

```text
Outbox Event
status = NEW
```

When Kafka becomes available:

```text
Outbox
   ↓
Publisher
   ↓
Kafka
   ↓
SUCCESS
   ↓
status = PUBLISHED
```

So we don't lose the event.

---

# 5. What is Outbox Publisher?

The **Outbox Publisher** is responsible for reading pending events from the database and sending them to Kafka.

For example, every 5 seconds:

```text
Scheduler
    ↓
Find events where status = NEW
    ↓
Publish to Kafka
    ↓
Kafka ACK
    ↓
Update status = PUBLISHED
```

Example:

```text
NEW
 ↓
Publish Kafka
 ↓
Kafka ACK
 ↓
PUBLISHED
```

---

# 6. What if Kafka fails?

```text
Outbox
   ↓
Publish
   ↓
Kafka ❌
```

Don't delete the event.

Keep it:

```text
status = NEW
retryCount = 1
```

Next retry:

```text
Outbox
   ↓
Retry
   ↓
Kafka
   ↓
Success
   ↓
PUBLISHED
```

---

# 7. Important problem: Duplicate Events

There is one more issue.

Imagine:

```text
Outbox
  ↓
Kafka
  ↓
Consumer
```

Kafka receives:

```text
EVT123
```

Consumer processes it.

But before the consumer commits its offset, the application crashes.

Kafka may send:

```text
EVT123
```

again.

So consumer gets:

```text
EVT123
EVT123
```

Therefore, the consumer should be **idempotent**.

Use:

```text
eventId
```

to detect duplicates.

```text
EVT123
   ↓
Already processed?
   ↓
YES
   ↓
Ignore
```

---

# 8. Outbox Table

A typical table:

```text
outbox_event
--------------------------------
id
event_id
event_type
payload
status
retry_count
created_at
processed_at
```

Example:

```text
+----+----------+------------------+-----------+
| id | event_id | event_type       | status    |
+----+----------+------------------+-----------+
| 1  | EVT123   | PAYMENT_SUCCESS  | NEW       |
+----+----------+------------------+-----------+
```

After Kafka succeeds:

```text
+----+----------+------------------+-----------+
| id | event_id | event_type       | status    |
+----+----------+------------------+-----------+
| 1  | EVT123   | PAYMENT_SUCCESS  | PUBLISHED |
+----+----------+------------------+-----------+
```

---

# 9. Complete Flow

Remember this flow for interviews:

```text
             Client
                |
                ↓
        Payment Controller
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
       Other Microservices
```

---

# 10. Why not directly Kafka?

### Without Outbox

```text
Save DB
   ↓
Publish Kafka
```

Failure:

```text
DB ✅
Kafka ❌
```

### With Outbox

```text
Save DB
   +
Save Outbox
   ↓
COMMIT
   ↓
Publish Kafka
```

Failure:

```text
DB ✅
Outbox ✅
Kafka ❌
```

The event remains safely stored and can be retried.

---

# 11. Interview Answer

If the interviewer asks:

**"What is the Outbox Pattern?"**

Say:

> **"The Transactional Outbox Pattern is used to solve the dual-write problem in microservices. Suppose a payment service needs to save payment data in MySQL and publish an event to Kafka. Instead of directly performing both operations, I save the payment and the Kafka event in an outbox table within the same database transaction. After the transaction commits, a separate publisher reads pending outbox events and publishes them to Kafka. If Kafka is unavailable, the event remains in the outbox and can be retried. After successful publishing, the event is marked as published. On the consumer side, I use event IDs for idempotency to handle duplicate messages."**

### Easy keywords to remember

```text
Outbox Pattern
      ↓
Dual Write Problem
      ↓
Same DB Transaction
      ↓
Payment + Outbox Event
      ↓
Commit
      ↓
Publisher
      ↓
Kafka
      ↓
Retry
      ↓
Idempotency
```

**One-line definition:**

> **"Outbox Pattern means saving the database change and the event together in the same transaction, then asynchronously publishing that event to Kafka."**
