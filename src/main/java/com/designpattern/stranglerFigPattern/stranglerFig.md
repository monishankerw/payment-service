# 9. Strangler Fig Pattern — Step-by-Step

This is a very important **microservices migration pattern** for a 4–7 year Java/Spring Boot developer.

## 1. What is Strangler Fig?

Suppose you have a large monolith:

```text id="z6x2qv"
              Client
                 |
                 ↓
        ┌─────────────────┐
        │  Legacy Monolith │
        │                 │
        │ Order            │
        │ Payment          │
        │ User             │
        │ Inventory        │
        │ Reports          │
        └─────────────────┘
```

You don't rewrite everything at once.

Instead:

```text id="y1x0cz"
                  Client
                    |
                    ↓
              API Gateway
                    |
          ┌─────────┴─────────┐
          ↓                   ↓
    New Microservice      Legacy Monolith
       Order Service
```

Then gradually migrate functionality:

```text id="k3n7wa"
Phase 1

Order     → New Service
Payment   → Monolith
User      → Monolith
Inventory → Monolith
```

Then:

```text id="2e4c2d"
Phase 2

Order     → New Service
Payment   → New Service
User      → Monolith
Inventory → Monolith
```

Finally:

```text id="5f8b4h"
Phase 3

Order     → New Service
Payment   → New Service
User      → New Service
Inventory → New Service

Legacy Monolith
      ↓
   Retired
```

That's why it is called **Strangler Fig**: the new system gradually grows around and replaces the old system.

---

# 2. Why not rewrite everything?

### Big Bang

```text id="f5t9yt"
Monolith
   ↓
Rewrite everything
   ↓
Deploy new system
```

Problems:

```text
❌ Huge release
❌ High migration risk
❌ Difficult rollback
❌ Long development time
❌ Business disruption
❌ Unknown legacy dependencies
```

### Strangler Fig

```text id="l0h7h2"
Monolith
   ↓
Extract Order
   ↓
Extract Payment
   ↓
Extract User
   ↓
Extract Inventory
   ↓
Retire Monolith
```

You can validate each migration incrementally.

---

# 3. Step 1 — Create the Gateway

Create a Spring Cloud Gateway application.

```text id="6j6r6n"
api-gateway
```

The Gateway decides:

```text id="q5tx1n"
Request
   |
   ↓
Gateway
   |
   ├── New functionality → Microservice
   |
   └── Old functionality → Monolith
```

---

# 4. Step 2 — Configure Routing

Example `application.yml`:

```yaml
spring:
  cloud:
    gateway:
      routes:

        - id: order-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/v2/orders/**

        - id: legacy-monolith
          uri: http://localhost:8080
          predicates:
            - Path=/api/v1/**
```

Now:

```text
/api/v2/orders/**
       ↓
Order Microservice
```

while:

```text
/api/v1/**
       ↓
Legacy Monolith
```

---

# 5. Step 3 — Move Order functionality

Suppose the monolith currently has:

```text id="tq6uvk"
POST /api/v1/orders
GET  /api/v1/orders/{id}
```

Create a new service:

```text id="7qg0mw"
order-service
```

New APIs:

```text
POST /api/v2/orders
GET  /api/v2/orders/{id}
```

Architecture:

```text id="r5a3mk"
                    Client
                      |
                      ↓
                 API Gateway
                      |
             ┌────────┴────────┐
             ↓                 ↓
       /api/v2/orders      /api/v1/**
             ↓                 ↓
      Order Service      Legacy Monolith
```

---

# 6. Step 4 — Create the new Order Service

Controller:

```java id="5u6b7p"
@RestController
@RequestMapping("/api/v2/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public OrderResponse getOrder(
            @PathVariable Long id) {

        return orderService.getOrder(id);
    }

    @PostMapping
    public OrderResponse createOrder(
            @RequestBody CreateOrderRequest request) {

        return orderService.createOrder(request);
    }
}
```

Now new functionality lives in:

```text id="v4x4d9"
Order Service
```

---

# 7. Step 5 — Problem: Legacy Data

This is where migration becomes more interesting.

Suppose the old monolith has:

```text id="j7b0cx"
legacy_order
-----------------------
order_id
cust_id
order_amt
order_status
```

But the new service expects:

```text id="0f3h91"
Order
-----------------------
id
customerId
amount
status
```

Don't spread legacy-format knowledge everywhere.

Use an **Adapter / Anti-Corruption Layer**.

---

# 8. Step 6 — Create Legacy Adapter

```java id="t1z2j5"
@Component
@RequiredArgsConstructor
public class LegacyOrderAdapter {

    private final LegacyOrderClient legacyOrderClient;

    public Order fetchOrder(Long orderId) {

        LegacyOrderDto legacyOrder =
                legacyOrderClient.getOrder(orderId);

        return mapToNewOrder(legacyOrder);
    }

    private Order mapToNewOrder(
            LegacyOrderDto legacyOrder) {

        Order order = new Order();

        order.setId(legacyOrder.getOrderId());
        order.setCustomerId(
                legacyOrder.getCustId()
        );
        order.setAmount(
                legacyOrder.getOrderAmt()
        );
        order.setStatus(
                legacyOrder.getOrderStatus()
        );

        return order;
    }
}
```

The new service doesn't need to understand:

```text
cust_id
order_amt
order_status
```

It works with:

```text
customerId
amount
status
```

---

# 9. What is Anti-Corruption Layer?

Think:

```text id="j9z8jh"
Legacy World
     |
     | legacy format
     ↓
┌──────────────────────┐
│ Anti-Corruption Layer│
│                      │
│ Adapter              │
│ Mapper               │
│ Translator           │
└──────────────────────┘
     |
     | new domain model
     ↓
New Microservice
```

Its job is to **protect the new service from legacy design and data formats**.

---

# 10. Step 7 — Gradually move traffic

Initially:

```text id="d4j6pg"
Order traffic
   |
   └── 100% Monolith
```

After migration:

```text id="4n1w4f"
Order traffic
   |
   ├── New Order Service → 10%
   └── Monolith           → 90%
```

Later:

```text id="spm9kt"
Order traffic
   |
   ├── New Service → 50%
   └── Monolith    → 50%
```

Eventually:

```text id="x6v7gb"
Order traffic
   |
   └── New Service → 100%
```

Then remove the old Order functionality.

---

# 11. Path-based migration

The simplest approach is path-based routing.

```yaml
spring:
  cloud:
    gateway:
      routes:

        - id: new-order-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/v2/orders/**

        - id: legacy
          uri: http://localhost:8080
          predicates:
            - Path=/api/v1/**
```

Example:

```text id="m7r2vl"
/api/v2/orders/1001
        ↓
Order Service
```

```text id="6u1m1m"
/api/v1/payment/1001
        ↓
Legacy Monolith
```

---

# 12. Step 8 — Database Migration

This is one of the hardest parts.

Initially:

```text id="t4v0hm"
                  Monolith
                     |
                     ↓
                Legacy DB
                     |
              ┌──────┴──────┐
              ↓             ↓
         Order tables   Payment tables
```

After Order extraction:

```text id="d4v0ca"
Monolith                    Order Service
    |                            |
    ↓                            ↓
Legacy DB                   Order DB
```

Now we need to decide:

> **Where is the source of truth?**

This must be explicitly defined during migration.

---

# 13. Dual Write

One temporary approach:

```text id="7t9j5q"
             Order Request
                   |
                   ↓
             New Order Service
                /       \
               ↓         ↓
          New Order DB  Legacy DB
```

But dual writes are dangerous.

Example:

```text id="c9k5g3"
New DB → SUCCESS
Legacy DB → FAILED
```

Now databases are inconsistent.

So don't blindly use dual writes.

Possible approaches include:

```text
Outbox
CDC
Change Data Capture
Event-based synchronization
One-way ownership migration
```

---

# 14. Better migration approach

A common strategy is:

```text id="l7k3dp"
Legacy DB
   |
   | CDC / synchronization
   ↓
New Order DB
   |
   ↓
Order Service
```

Then after validation:

```text id="2s4yq7"
New Order DB
     ↓
Source of Truth
```

And finally:

```text id="x9m0by"
Legacy Order DB
       ↓
No longer used
```

---

# 15. Step 9 — Migration phases

A practical migration roadmap:

```text id="w6r9qk"
PHASE 1
------
Understand monolith
      ↓
Identify dependencies
      ↓
Identify Order module


PHASE 2
------
Create Order Service
      ↓
Create new DB
      ↓
Create APIs


PHASE 3
------
Build Adapter
      ↓
Synchronize legacy data
      ↓
Validate data


PHASE 4
------
Gateway routing
      ↓
Small traffic
      ↓
Monitor


PHASE 5
------
Increase traffic
      ↓
100% New Service


PHASE 6
------
Remove old Order code
      ↓
Remove old DB dependencies
      ↓
Retire monolith
```

---

# 16. How do you decide what to migrate first?

For a real project, don't simply choose randomly.

Look at:

```text id="a4q1uk"
                Candidate Service
                       |
        ┌──────────────┼──────────────┐
        ↓              ↓              ↓
   Coupling         Business       Technical
   level            value          complexity
```

You generally want to evaluate:

* Dependency/coupling with the monolith
* Business value
* Change frequency
* Team ownership
* Data ownership complexity
* Operational risk
* Migration effort

A highly coupled module with many shared tables may be much harder to extract safely than a more self-contained capability.

---

# 17. Complete Architecture

```text id="o5t6cn"
                         CLIENT
                           |
                           ↓
                    ┌─────────────┐
                    │ API Gateway │
                    └──────┬──────┘
                           |
             ┌─────────────┼─────────────┐
             ↓             ↓             ↓
       Order Service   Payment Service   User Service
             |             |             |
             ↓             ↓             ↓
        Order DB       Payment DB      User DB


             Other functionality
                    |
                    ↓
             Legacy Monolith
                    |
                    ↓
               Legacy DB
```

Over time:

```text id="i5s1p7"
           NEW SERVICES
               ↓
      ┌────────┼────────┐
      ↓        ↓        ↓
    Order    Payment    User
      ↓        ↓        ↓
    DB       DB         DB

               +

        Legacy Monolith
          ↓
       decreasing
       functionality
          ↓
        RETIRED
```

---

# 18. Strangler Fig vs Big Bang

| Strangler Fig                  | Big Bang                    |
| ------------------------------ | --------------------------- |
| Incremental migration          | Complete rewrite            |
| Lower change per release       | Huge release                |
| Easier rollback per capability | Difficult rollback          |
| Old + new coexist              | Old replaced at once        |
| Gradual traffic migration      | Single cutover              |
| Requires temporary integration | Simpler target architecture |
| Migration can take longer      | High upfront effort         |

---

# 19. Interview Answer — 5+ Years

> **Strangler Fig is an incremental migration pattern used to replace a legacy monolith with microservices. Instead of rewriting the entire application, we introduce a routing layer such as an API Gateway and gradually move individual business capabilities to new services. During the transition, some requests go to the new microservices while others continue to the monolith. I would use an Adapter or Anti-Corruption Layer to isolate the new service from legacy APIs and data models. For data migration, I would explicitly define the source of truth and use mechanisms such as Outbox or CDC where appropriate rather than relying blindly on dual writes. After validating the new service and gradually moving traffic to it, we remove the corresponding functionality from the monolith and eventually decommission it.**

### One-line answer

> **Strangler Fig = gradually replace a monolith capability-by-capability instead of doing a risky big-bang rewrite.**

And remember:

```text id="7x6q2p"
Gateway
   ↓
Route
   ↓
New Service OR Legacy
   ↓
Gradually migrate
   ↓
100% New Service
   ↓
Remove Legacy
```
