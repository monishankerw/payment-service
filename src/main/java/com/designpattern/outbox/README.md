# Dual Write Problem — Complete Setup Guide

This guide explains how to set up the **Transactional Outbox Pattern** using:

* Java 17
* Spring Boot
* Gradle
* MySQL
* Kafka
* Spring Data JPA
* Lombok
* Spring Scheduler

The goal is to understand and implement the **Dual Write Problem** from start to finish.

---

## 1. What are we solving?

Suppose a payment service needs to perform two operations:

1. Save payment in MySQL
2. Publish payment event to Kafka

The naive implementation is:

```text
Payment API
    |
    v
Payment Service
    |
    +------> MySQL
    |
    +------> Kafka
```

This creates the **Dual Write Problem**.

### Failure scenario

```text
Save Payment → MySQL ✅

Publish Event → Kafka ❌
```

Now:

```text
MySQL = Payment SUCCESS

Kafka = No Event
```

The database and Kafka are inconsistent.

---

# 2. Solution — Transactional Outbox Pattern

Instead of directly writing to MySQL and Kafka:

```text
Payment Service
     |
     +----> MySQL
     |
     +----> Kafka
```

we use an **Outbox Table**:

```text
                    MySQL
                      |
             +--------+--------+
             |                 |
             v                 v
       Payment Table      Outbox Table
                               |
                               v
                       Outbox Publisher
                               |
                               v
                             Kafka
```

The important concept is:

> Payment data and the Kafka event are saved in the same MySQL transaction.

Then a separate publisher sends the outbox event to Kafka.

---

# 3. Technology Stack

Use:

```text
Java 17
Spring Boot
Gradle
Spring Web
Spring Data JPA
MySQL
Apache Kafka
Lombok
Spring Scheduler
```

---

# 4. Create Spring Boot Project

Create a Spring Boot project with:

```text
Project: payment-service

Language: Java

Build Tool: Gradle

Java: 17
```

Add dependencies:

```text
Spring Web
Spring Data JPA
Spring for Apache Kafka
MySQL Driver
Lombok
```

---

# 5. Project Structure

Create the following structure:

```text
payment-service
│
├── build.gradle
├── settings.gradle
│
└── src
    │
    └── main
        │
        ├── java
        │   │
        │   └── com
        │       │
        │       └── designpattern
        │           │
        │           ├── DesignPatternApplication.java
        │           │
        │           ├── controller
        │           │   └── PaymentController.java
        │           │
        │           ├── dto
        │           │   ├── PaymentRequest.java
        │           │   └── PaymentEvent.java
        │           │
        │           ├── entity
        │           │   ├── Payment.java
        │           │   ├── OutboxEvent.java
        │           │   └── OutboxStatus.java
        │           │
        │           ├── repository
        │           │   ├── PaymentRepository.java
        │           │   └── OutboxEventRepository.java
        │           │
        │           ├── service
        │           │   ├── PaymentService.java
        │           │   └── OutboxPublisher.java
        │           │
        │           └── kafka
        │               ├── PaymentKafkaProducer.java
        │               └── PaymentConsumer.java
        │
        └── resources
            └── application.yml
```

---

# 6. Configure Gradle

Your `build.gradle` should contain these major dependencies:

```text
Spring Boot
Spring Data JPA
Spring Kafka
Spring Web MVC
MySQL Connector
Lombok
```

Important MySQL dependency:

```text
runtimeOnly 'com.mysql:mysql-connector-j'
```

You already have this dependency, so you don't need to add another MySQL driver.

---

# 7. Configure MySQL

Start MySQL.

Default local configuration for this example:

```text
Host     = localhost
Port     = 3306
Username = root
Password = root
```

Create the database:

```text
payment_db
```

You don't need to manually create the tables because Hibernate can create them.

---

# 8. Configure application.yml

The most important configuration is:

```text
spring
 ├── application
 │    └── name
 │
 ├── datasource
 │    ├── url
 │    ├── username
 │    ├── password
 │    └── driver-class-name
 │
 ├── jpa
 │
 └── kafka
```

Your `application.yml` should have:

```text
spring.datasource.url
spring.datasource.username
spring.datasource.password
spring.datasource.driver-class-name
```

For your current project, use:

```text
jdbc:mysql://localhost:3306/payment_db
```

Also configure:

```text
spring.jpa.hibernate.ddl-auto=update
```

This allows Hibernate to create/update the tables.

---

# 9. Important YAML Indentation

Your previous error was caused by this structure:

```text
spring
 └── application
      ├── name
      ├── server
      └── spring
           └── datasource
```

This is incorrect.

The correct structure is:

```text
spring
 ├── application
 │    └── name
 │
 ├── datasource
 │
 ├── jpa
 │
 └── kafka

server

outbox
```

This is very important because Spring Boot expects:

```text
spring.datasource.url
```

not:

```text
spring.application.spring.datasource.url
```

---

# 10. Create Payment Entity

Create:

```text
entity/Payment.java
```

The payment table should contain:

```text
id
transactionId
customerId
amount
status
createdAt
```

Example database:

```text
payments

+----+----------------+------------+--------+---------+
| id | transaction_id | customer_id| amount | status  |
+----+----------------+------------+--------+---------+
| 1  | TXN-1001       | CUST-101   | 5000   | SUCCESS |
+----+----------------+------------+--------+---------+
```

---

# 11. Create Payment Repository

Create:

```text
repository/PaymentRepository.java
```

It should extend:

```text
JpaRepository<Payment, Long>
```

This gives you standard operations:

```text
save()
findById()
findAll()
delete()
```

---

# 12. Create Outbox Status

Create:

```text
entity/OutboxStatus.java
```

Use three states:

```text
NEW
PUBLISHED
FAILED
```

Meaning:

### NEW

Event is stored in database but has not yet been published.

### PUBLISHED

Kafka successfully received the event.

### FAILED

Publishing failed and the event needs retry handling.

---

# 13. Create Outbox Entity

Create:

```text
entity/OutboxEvent.java
```

The outbox table should contain:

```text
id
eventId
eventType
payload
status
retryCount
createdAt
processedAt
```

Example:

```text
outbox_event

+----+----------+-----------------+--------+------------+
| id | event_id | event_type      | status | retryCount |
+----+----------+-----------------+--------+------------+
| 1  | EVT-001  | PAYMENT_SUCCESS | NEW    | 0          |
+----+----------+-----------------+--------+------------+
```

The `payload` contains the event that needs to be published to Kafka.

---

# 14. Create Outbox Repository

Create:

```text
repository/OutboxEventRepository.java
```

You need a query that finds pending events.

Conceptually:

```text
Find first 100 events
where status = NEW
order by createdAt
```

This prevents the publisher from loading thousands or millions of events at once.

---

# 15. Create Payment Request DTO

Create:

```text
dto/PaymentRequest.java
```

Request fields:

```text
customerId
amount
```

Example request:

```text
{
    customerId: "CUST1001",
    amount: 5000
}
```

---

# 16. Create Payment Event DTO

Create:

```text
dto/PaymentEvent.java
```

The Kafka event contains:

```text
eventId
transactionId
customerId
amount
status
```

Example:

```text
{
    eventId: "EVT-1001",
    transactionId: "TXN-1001",
    customerId: "CUST1001",
    amount: 5000,
    status: "SUCCESS"
}
```

---

# 17. Create Payment Service

Create:

```text
service/PaymentService.java
```

This is the most important part.

The flow should be:

```text
createPayment()
      |
      v
Generate transactionId
      |
      v
Create Payment
      |
      v
Save Payment
      |
      v
Create PaymentEvent
      |
      v
Convert event to JSON
      |
      v
Create OutboxEvent
      |
      v
Save OutboxEvent
      |
      v
COMMIT
```

The service method should use:

```text
@Transactional
```

---

# 18. Why `@Transactional`?

Because we want these two operations to be atomic:

```text
Payment Table
      +
Outbox Table
```

For example:

```text
Transaction starts

Save Payment       ✅

Save Outbox Event  ✅

COMMIT
```

Both succeed.

But if:

```text
Save Payment       ✅

Save Outbox Event  ❌
```

then the transaction rolls back:

```text
Payment            ❌
Outbox Event       ❌
```

This prevents inconsistent database state.

---

# 19. Don't Publish Kafka Directly from Payment Service

Avoid this approach:

```text
@Transactional
createPayment()

    Save Payment
        ↓
    Publish Kafka
```

Instead:

```text
@Transactional
createPayment()

    Save Payment
        +
    Save Outbox Event
```

Then:

```text
Outbox Publisher
      ↓
Kafka
```

This separation is the main idea of the Outbox Pattern.

---

# 20. Create Payment Controller

Create:

```text
controller/PaymentController.java
```

Endpoint:

```text
POST /payments
```

Request:

```text
{
    customerId: "CUST1001",
    amount: 5000
}
```

Controller calls:

```text
PaymentService.createPayment()
```

and returns the transaction ID.

---

# 21. Create Kafka Producer

Create:

```text
kafka/PaymentKafkaProducer.java
```

Its responsibility is only:

```text
Receive event
      ↓
Send event to Kafka
```

Kafka topic:

```text
payment-topic
```

Use:

```text
eventId
```

as the Kafka message key.

This is useful because the same event can be identified later.

---

# 22. Create Outbox Publisher

Create:

```text
service/OutboxPublisher.java
```

Use Spring Scheduler.

Run the publisher every:

```text
5 seconds
```

The process is:

```text
Every 5 seconds
      ↓
Find NEW outbox events
      ↓
For each event
      ↓
Publish to Kafka
      ↓
Kafka acknowledgement
      ↓
Mark PUBLISHED
```

---

# 23. Why Scheduler?

Imagine Kafka is temporarily unavailable.

Database:

```text
Payment       = SUCCESS
Outbox Event  = NEW
```

The event isn't lost.

When Kafka becomes available:

```text
Scheduler
    ↓
Find NEW event
    ↓
Publish Kafka
    ↓
Success
    ↓
PUBLISHED
```

This provides reliable event delivery.

---

# 24. Important Kafka Acknowledgement

Don't consider an event successfully published merely because:

```text
kafkaTemplate.send()
```

was called.

Kafka publishing is asynchronous.

The safer flow is:

```text
Send Event
    ↓
Wait for Kafka acknowledgement
    ↓
Success?
   / \
 YES  NO
 |     |
 ↓     ↓
PUBLISHED
      RETRY
```

Only mark the outbox event as:

```text
PUBLISHED
```

after successful Kafka acknowledgement.

---

# 25. Create Kafka Consumer

Create:

```text
kafka/PaymentConsumer.java
```

The consumer listens to:

```text
payment-topic
```

Flow:

```text
Kafka
  ↓
payment-topic
  ↓
PaymentConsumer
  ↓
Process Event
```

In a real microservices architecture, this could be:

```text
Payment Service
      ↓
Kafka
      ↓
Notification Service
      ↓
Send SMS/Email
```

or:

```text
Payment Service
      ↓
Kafka
      ↓
Transaction Service
      ↓
Update transaction processing
```

---

# 26. Enable Scheduling

In the main Spring Boot application, enable:

```text
@EnableScheduling
```

Then Spring will execute the Outbox Publisher periodically.

---

# 27. Start MySQL

Verify MySQL is running on:

```text
localhost:3306
```

Check:

```text
3306
```

is listening.

Then verify:

```text
payment_db
```

exists.

---

# 28. Start Kafka

Start your Kafka server.

Verify Kafka is available on:

```text
localhost:9092
```

Your Spring Boot application uses:

```text
localhost:9092
```

as the Kafka bootstrap server.

---

# 29. Create Kafka Topic

Create:

```text
payment-topic
```

Recommended local setup:

```text
Topic: payment-topic
Partitions: 3
Replication Factor: 1
```

For production, replication factor would normally be greater than 1 depending on the Kafka cluster.

---

# 30. Start Spring Boot

Using Gradle:

```text
gradlew clean build
```

Then:

```text
gradlew bootRun
```

Expected flow:

```text
Spring Boot
    ↓
DataSource
    ↓
MySQL
    ↓
JPA
    ↓
Kafka configuration
    ↓
Tomcat
    ↓
Application Started
```

---

# 31. Test Payment API

Call:

```text
POST http://localhost:8080/payments
```

Request:

```text
{
    "customerId": "CUST1001",
    "amount": 5000
}
```

Expected response:

```text
Payment created successfully
TransactionId = <generated-id>
```

---

# 32. Check Payment Table

Immediately after API call:

```text
payments

transactionId = generated ID
customerId    = CUST1001
amount        = 5000
status        = SUCCESS
```

---

# 33. Check Outbox Table

Immediately after payment:

```text
outbox_event

eventId       = generated ID
eventType     = PAYMENT_SUCCESS
status        = NEW
retryCount    = 0
```

At this point Kafka doesn't need to have received anything yet.

The important thing is that the event is safely stored in MySQL.

---

# 34. Outbox Publisher Runs

After approximately 5 seconds:

```text
Outbox Publisher
      ↓
Find NEW events
      ↓
Get event
      ↓
Publish Kafka
```

Kafka receives:

```text
payment-topic
```

with the event payload.

---

# 35. Event Becomes PUBLISHED

After successful Kafka acknowledgement:

```text
outbox_event

status = PUBLISHED
```

So the final flow becomes:

```text
NEW
 ↓
Kafka Publish
 ↓
Kafka ACK
 ↓
PUBLISHED
```

---

# 36. Kafka Failure Scenario

Stop Kafka.

Now create a payment.

You get:

```text
Payment Table       SUCCESS
Outbox Table        NEW
Kafka               DOWN
```

The important thing:

> The payment is not lost and the Kafka event is not lost.

The event remains in the outbox table.

---

# 37. Start Kafka Again

Start Kafka.

The publisher sees:

```text
status = NEW
```

and publishes it:

```text
Outbox
   ↓
Kafka
   ↓
ACK
   ↓
PUBLISHED
```

---

# 38. Duplicate Event Problem

There is another important distributed-system issue.

Suppose:

```text
Outbox
   ↓
Kafka
   ↓
Consumer
```

Kafka successfully delivers:

```text
EVT-1001
```

Consumer processes it.

But before the consumer successfully commits its offset, something fails.

Kafka may deliver:

```text
EVT-1001
```

again.

So the consumer receives:

```text
EVT-1001
EVT-1001
```

This means your consumer must be **idempotent**.

---

# 39. Implement Consumer Idempotency

Maintain processed event IDs.

Conceptually:

```text
processed_events

+----+----------+
| id | event_id |
+----+----------+
| 1  | EVT-1001 |
+----+----------+
```

When an event arrives:

```text
EVT-1001
   ↓
Check processed_events
   ↓
Already exists?
   ↓
YES
   ↓
Ignore duplicate
```

For a new event:

```text
EVT-1002
   ↓
Doesn't exist
   ↓
Process
   ↓
Save EVT-1002
```

---

# 40. Complete Architecture

The final architecture is:

```text
                         Client
                           |
                           v
                    Payment Controller
                           |
                           v
                    Payment Service
                           |
                     @Transactional
                           |
                +----------+----------+
                |                     |
                v                     v
          Payment Table          Outbox Table
                |                     |
                +----------+----------+
                           |
                         COMMIT
                           |
                           v
                    Outbox Publisher
                           |
                           v
                         Kafka
                           |
                    payment-topic
                           |
                +----------+----------+
                |          |          |
                v          v          v
          Notification  Transaction  Reporting
             Service      Service      Service
                |
                v
           Idempotency
```

---

# 41. Complete Data Flow

```text
1. Client sends payment
          ↓
2. Controller receives request
          ↓
3. Payment Service starts transaction
          ↓
4. Generate transactionId
          ↓
5. Save Payment
          ↓
6. Create Payment Event
          ↓
7. Save Outbox Event
          ↓
8. Database COMMIT
          ↓
9. Outbox Publisher finds NEW event
          ↓
10. Publish event to Kafka
          ↓
11. Kafka sends acknowledgement
          ↓
12. Mark Outbox as PUBLISHED
          ↓
13. Consumer receives event
          ↓
14. Check eventId
          ↓
15. Process event
```

---

# 42. Failure Flow

```text
Payment Request
      ↓
Save Payment       ✅
      ↓
Save Outbox        ✅
      ↓
COMMIT             ✅
      ↓
Kafka              ❌
      ↓
Outbox remains NEW
      ↓
Retry
      ↓
Kafka available
      ↓
Publish
      ↓
PUBLISHED
```

---

# 43. Why This Solves Dual Write

Without Outbox:

```text
MySQL ✅
Kafka  ❌
```

or:

```text
MySQL ❌
Kafka  ✅
```

With Outbox:

```text
MySQL Transaction
       |
       +--- Payment
       |
       +--- Outbox Event
       |
      COMMIT
```

Kafka becomes a separate reliable delivery step:

```text
Outbox
   ↓
Kafka
   ↓
Retry if required
```

Therefore, the business event isn't lost simply because Kafka is temporarily unavailable.

---

# 44. Interview Explanation

For a 5-year Java/Spring Boot interview, explain it in this order:

### Step 1 — Definition

> "Dual write problem occurs when one business operation needs to write to two independent systems, such as MySQL and Kafka."

### Step 2 — Problem

> "These systems don't automatically participate in the same transaction, so one operation can succeed while the other fails."

### Step 3 — Example

> "For example, payment can be successfully saved in MySQL but Kafka publishing can fail."

### Step 4 — Solution

> "I can solve this using the Transactional Outbox Pattern."

### Step 5 — Implementation

> "I save the payment and the Kafka event in an outbox table within the same database transaction using `@Transactional`."

### Step 6 — Publisher

> "A background scheduler or worker reads pending outbox records and publishes them to Kafka."

### Step 7 — Retry

> "If Kafka is unavailable, the event remains pending and the publisher retries it."

### Step 8 — Idempotency

> "Because retries or Kafka redelivery can result in duplicates, consumers should use an event ID or transaction ID to make processing idempotent."

---

# 45. One-Minute Interview Answer

> "In a payment microservice, suppose I need to save a payment into MySQL and publish a payment-success event to Kafka. This creates a dual-write problem because MySQL and Kafka are independent systems. MySQL may commit successfully while Kafka publishing fails, resulting in inconsistency.
>
> To solve this, I use the Transactional Outbox Pattern. Inside a single database transaction, I save both the payment record and an outbox event. A separate outbox publisher periodically reads pending events and publishes them to Kafka. Once Kafka acknowledges the event, I mark it as published. If Kafka is down, the event remains in the outbox and can be retried. On the consumer side, I use the event ID for idempotency so duplicate events don't cause duplicate processing."

---

# 46. Important Interview Questions

After explaining Dual Write, the interviewer may ask:

### Q1. Why can't `@Transactional` solve MySQL + Kafka?

Because the normal database transaction and Kafka operation are not automatically one atomic transaction.

### Q2. What is Transactional Outbox?

A pattern where the business data and event are saved together in the same database transaction.

### Q3. What happens if Kafka is down?

The event remains in the outbox and is retried later.

### Q4. How do you handle duplicate Kafka events?

Use idempotency using:

```text
eventId
transactionId
unique database constraint
```

### Q5. Why use an Outbox table?

To reliably persist events before attempting asynchronous delivery.

### Q6. How do you know Kafka successfully received the event?

Wait for/handle the Kafka producer acknowledgement before marking the outbox record as `PUBLISHED`.

### Q7. What happens if the application crashes after Kafka publish but before marking PUBLISHED?

The event can be published again during retry. Therefore, consumers must be idempotent.

### Q8. Is exactly-once delivery guaranteed?

Not simply by using an outbox. The practical design should tolerate duplicate delivery through idempotent consumers and appropriate Kafka/database transaction strategies.

---

# 47. Final Mental Model

Remember just this:

```text
                 DUAL WRITE
                     |
          +----------+----------+
          |                     |
        MySQL                  Kafka
          |                     |
       Problem:            Problem:
       Commit              Publish
       succeeds            fails
          |                     |
          +----------+----------+
                     |
                  OUTBOX
                     |
          +----------+----------+
          |                     |
     Payment Table        Outbox Table
          |                     |
          +----------+----------+
                     |
                  COMMIT
                     |
                     v
              Outbox Publisher
                     |
                     v
                   Kafka
                     |
                     v
              Idempotent Consumer
```

**Key words to remember for the interview:**

```text
Dual Write
    ↓
Data Inconsistency
    ↓
Transactional Outbox
    ↓
@Transaction
    ↓
Outbox Table
    ↓
Publisher
    ↓
Kafka
    ↓
Retry
    ↓
Idempotency
    ↓
Duplicate Handling
```

This is the complete **Gradle + Spring Boot + MySQL + Kafka Transactional Outbox setup flow** you can use to build the project and explain it in an interview.
