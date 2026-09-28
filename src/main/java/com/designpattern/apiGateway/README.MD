# API Gateway — Complete Interview Notes

## 1. What Is an API Gateway?

An **API Gateway** is a single entry point between external clients and backend microservices.

Instead of clients directly calling multiple services:

```text
Client
  |
  +----> Order Service
  |
  +----> Inventory Service
  |
  +----> Payment Service
  |
  +----> User Service
```

we introduce an API Gateway:

```text
                         API Gateway
                              |
              +---------------+---------------+
              |               |               |
              ↓               ↓               ↓
        Order Service   Inventory Service   Payment Service
```

The client only knows the Gateway URL.

```text
Client
   |
   ↓
API Gateway
   |
   ├── /orders/**     → Order Service
   ├── /inventory/**  → Inventory Service
   ├── /payments/**   → Payment Service
   └── /users/**      → User Service
```

---

# 2. Why Do We Need an API Gateway?

Imagine you have 20 microservices.

Without Gateway:

```text
Mobile App
   |
   +--> User Service
   +--> Order Service
   +--> Payment Service
   +--> Inventory Service
   +--> Notification Service
   +--> Wallet Service
   +--> ...
```

The client needs to know:

* Service URLs
* Service ports
* Authentication details
* Which service handles which request
* How to handle failures

This makes the client tightly coupled to the microservices.

With Gateway:

```text
Mobile/Web Client
       |
       ↓
API Gateway
       |
       +--> User Service
       +--> Order Service
       +--> Payment Service
       +--> Inventory Service
```

The client only communicates with:

```text
API Gateway
```

---

# 3. Main Responsibilities

An API Gateway commonly handles:

```text
API Gateway
     |
     +── Routing
     |
     +── Authentication
     |
     +── Authorization
     |
     +── Rate Limiting
     |
     +── Logging
     |
     +── CORS
     |
     +── API Versioning
     |
     +── Request/Response transformation
     |
     +── Circuit Breaker
     |
     +── Load Balancing
```

---

# 4. Routing

The most basic responsibility is routing.

Example:

```text
/orders/**

       ↓

Order Service
```

and:

```text
/inventory/**

       ↓

Inventory Service
```

Example request:

```text
GET /orders/1001
```

Gateway determines:

```text
/orders/**
      ↓
Order Service
```

---

# 5. Spring Cloud Gateway

For Spring Boot applications, a common choice is:

**Spring Cloud Gateway**

Architecture:

```text
Client
  |
  ↓
Spring Cloud Gateway
  |
  +----> Order Service
  |
  +----> Inventory Service
  |
  +----> Payment Service
```

---

# 6. `lb://` Meaning

You may see:

```text
lb://ORDER-SERVICE
```

The important part is:

```text
lb://
```

It means **load-balanced routing**.

The Gateway doesn't need to hardcode:

```text
http://192.168.1.10:8081
```

Instead:

```text
lb://ORDER-SERVICE
```

means:

> Find an available instance of `ORDER-SERVICE` through the configured service discovery/load-balancing mechanism and route the request to it.

For example:

```text
                    Service Registry
                         |
             +-----------+-----------+
             |                       |
      ORDER-SERVICE            INVENTORY-SERVICE
        |      |                    |      |
        ↓      ↓                    ↓      ↓
      :8081  :8082                :8091  :8092
```

Gateway:

```text
lb://ORDER-SERVICE
```

can route to one of the available instances.

---

# 7. Basic Spring Cloud Gateway Example

```java
@Bean
public RouteLocator routes(
        RouteLocatorBuilder builder) {

    return builder.routes()

            .route("order-service", r -> r
                    .path("/orders/**")
                    .uri("lb://ORDER-SERVICE"))

            .route("inventory-service", r -> r
                    .path("/inventory/**")
                    .uri("lb://INVENTORY-SERVICE"))

            .build();
}
```

Flow:

```text
GET /orders/123
       |
       ↓
API Gateway
       |
       ↓
/orders/**
       |
       ↓
lb://ORDER-SERVICE
       |
       ↓
Order Service
```

---

# 8. Gateway With Filters

You can add filters to routes.

Example:

```java
.route("order-service", r -> r
        .path("/orders/**")
        .filters(f -> f
                .stripPrefix(1)
        )
        .uri("lb://ORDER-SERVICE"))
```

Suppose the client calls:

```text
/orders/123
```

`stripPrefix(1)` removes:

```text
/orders
```

before forwarding.

The downstream service may receive:

```text
/123
```

---

# 9. Circuit Breaker

Suppose Order Service is down:

```text
Client
  ↓
Gateway
  ↓
Order Service ❌
```

Without a circuit breaker, the Gateway may continue sending requests to a failing service.

With a circuit breaker:

```text
Client
  ↓
Gateway
  ↓
Circuit Breaker
  ↓
Order Service ❌
  ↓
Fallback
```

Example:

```java
.filters(f -> f
        .circuitBreaker(c -> c
                .setName("orderCB")
                .setFallbackUri("/fallback/orders")
        )
)
```

---

# 10. Why Circuit Breaker?

Suppose:

```text
Order Service
    ↓
Slow / Down
```

Thousands of requests keep coming:

```text
Request
Request
Request
Request
Request
...
```

This can cause cascading failures.

Circuit breaker helps stop repeatedly calling an unhealthy service.

Conceptually:

```text
CLOSED
  |
  | failures increase
  ↓
OPEN
  |
  | requests blocked
  ↓
HALF-OPEN
  |
  | test request
  ↓
CLOSED
```

---

# 11. API Gateway vs Load Balancer

This is a common **2–4 year interview question**.

### Load Balancer

Main responsibility:

> Distribute traffic across multiple instances.

```text
Load Balancer
      |
   +--+--+
   ↓     ↓
Order1 Order2
```

### API Gateway

Has broader responsibilities:

```text
API Gateway
     |
     +── Routing
     +── Authentication
     +── Rate limiting
     +── Logging
     +── API versioning
     +── Request transformation
     +── Circuit breaker
     +── Load balancing
```

### Simple answer

> **A load balancer primarily distributes traffic among service instances, while an API Gateway is an application-level entry point that provides routing and cross-cutting API policies such as authentication, rate limiting, logging, versioning and sometimes load balancing.**

---

# 12. Gateway Authentication

A common architecture:

```text
Client
   |
   | JWT
   ↓
API Gateway
   |
   ↓
Validate JWT
   |
   ↓
Forward request
   |
   ↓
Microservice
```

For example:

```text
Authorization: Bearer eyJhbGci...
```

Gateway validates:

```text
JWT
 |
 +-- Signature
 +-- Expiration
 +-- Issuer
 +-- Claims
```

If invalid:

```text
401 Unauthorized
```

If valid:

```text
Gateway
   ↓
Microservice
```

---

# 13. Important Point: Should Services Still Validate JWT?

Don't blindly say:

> "Gateway validates JWT once, therefore services don't need authentication."

In production, the exact trust boundary depends on architecture.

For example:

```text
Internet
   ↓
Gateway
   ↓
Private Services
```

You may centralize some authentication at the Gateway.

But services should still enforce **service-level authorization and trust boundaries** appropriate to the system.

For example:

```text
Gateway
  ↓
Authentication
  ↓
Order Service
  ↓
Authorization
  ↓
Can this user access this order?
```

---

# 14. Rate Limiting

Suppose one client sends:

```text
10,000 requests/second
```

It could overload the services.

Gateway can enforce:

```text
100 requests/minute/client
```

Flow:

```text
Client
  ↓
Gateway
  ↓
Rate Limiter
  |
  +--- Allowed → Service
  |
  +--- Exceeded → 429
```

Response:

```text
HTTP 429 Too Many Requests
```

---

# 15. Rate Limiting Per Client

For example:

```text
Client A → 100 requests/min
Client B → 100 requests/min
Client C → 100 requests/min
```

You can identify clients using:

```text
API Key
JWT subject
Client ID
IP address
```

For distributed gateways, the rate-limit state often needs shared infrastructure such as Redis rather than local in-memory counters.

---

# 16. Request Logging

Gateway is a good place for common request logging.

Example:

```text
Request:
GET /orders/123

User:
USER1001

Correlation ID:
CORR-12345

Response:
200

Latency:
120 ms
```

Flow:

```text
Client
 ↓
Gateway
 ↓
Generate/propagate Correlation ID
 ↓
Service
 ↓
Response
 ↓
Gateway logs response
```

This makes tracing requests across microservices easier.

---

# 17. API Versioning

Gateway can route different API versions:

```text
/api/v1/orders
       ↓
Order Service V1
```

and:

```text
/api/v2/orders
       ↓
Order Service V2
```

Architecture:

```text
                 Gateway
                    |
             +------+------+
             |             |
             ↓             ↓
           V1 API        V2 API
```

This is useful during gradual migrations.

---

# 18. Canary Routing

At senior level, you may need **canary releases**.

Suppose you have:

```text
Order Service V1
Order Service V2
```

Instead of moving everyone to V2 immediately:

```text
95% → V1
5%  → V2
```

Then gradually:

```text
90% → V1
10% → V2
```

Then:

```text
50% → V1
50% → V2
```

Eventually:

```text
0% → V1
100% → V2
```

The exact implementation can use Gateway routing rules, service discovery, deployment infrastructure, or a service mesh.

---

# 19. Gateway as a BFF

BFF means:

> **Backend for Frontend**

Suppose mobile needs data from:

```text
User Service
Order Service
Payment Service
```

Instead of mobile making three calls:

```text
Mobile
  |
  +--> User
  +--> Order
  +--> Payment
```

a BFF can aggregate:

```text
Mobile
  |
  ↓
Mobile BFF
  |
  +--> User Service
  +--> Order Service
  +--> Payment Service
```

Response:

```text
{
    "user": {...},
    "orders": [...],
    "payment": {...}
}
```

This can reduce client-side complexity.

---

# 20. Gateway Single Point of Failure

This is a very important **4–7 year question**.

### Question

> "How do you prevent the API Gateway from becoming a bottleneck or single point of failure?"

Don't deploy only one Gateway:

```text
             Client
                |
                ↓
          Gateway  ❌
```

Instead deploy multiple Gateway instances:

```text
                    Client
                      |
                      ↓
                 Load Balancer
                  /         \
                 /           \
                ↓             ↓
          Gateway 1       Gateway 2
                |             |
                +------+------+
                       |
                       ↓
                  Microservices
```

For more scale:

```text
                    Load Balancer
                  /      |       \
                 ↓       ↓        ↓
             Gateway1 Gateway2 Gateway3
```

Use:

* Multiple Gateway instances
* Load balancer
* Health checks
* Autoscaling
* Stateless Gateway design where possible
* Proper timeouts
* Rate limiting
* Monitoring
* Circuit breakers

---

# 21. Gateway Autoscaling

If traffic increases:

```text
Normal traffic:

Gateway 1
Gateway 2
```

High traffic:

```text
Gateway 1
Gateway 2
Gateway 3
Gateway 4
Gateway 5
```

Autoscaling can be based on metrics such as:

```text
CPU
Memory
Request rate
Latency
```

The Gateway should ideally be stateless so another instance can be added easily.

---

# 22. What Should Gateway NOT Do?

Don't put business logic into the Gateway.

Bad:

```text
Gateway
   |
   +── Calculate payment
   +── Update wallet
   +── Validate merchant business rules
   +── Update order
```

Better:

```text
Gateway
   |
   +── Authentication
   +── Routing
   +── Rate limiting
   +── Logging
   +── Policies
   |
   ↓
Business Service
   |
   +── Payment logic
   +── Wallet logic
   +── Order logic
```

Gateway should generally handle **cross-cutting concerns**, not domain business logic.

---

# 23. Gateway vs Service Mesh

This is a common senior-level question.

### API Gateway

Primarily handles:

```text
External traffic
        ↓
Internet / Client
        ↓
Gateway
        ↓
Services
```

It is mainly concerned with **north-south traffic**.

### Service Mesh

Primarily handles:

```text
Service A
    ↕
Service B
    ↕
Service C
```

It is mainly concerned with **east-west service-to-service traffic**.

---

# 24. Simple Difference

```text
                  Internet
                     |
                     ↓
                API Gateway
                     |
          +----------+----------+
          |          |          |
          ↓          ↓          ↓
       Service A  Service B  Service C
          ↕          ↕          ↕
       Service-to-service communication
                  Service Mesh
```

### Gateway responsibilities

```text
Client authentication
External routing
Rate limiting
API versioning
CORS
External API policies
```

### Service Mesh responsibilities

```text
Service-to-service mTLS
Service discovery
Traffic management
Retries
Circuit breaking
Observability
East-west traffic policies
```

The exact boundary depends on the platform architecture.

---

# 25. Gateway vs Service Mesh — Interview Answer

> "I generally use the API Gateway for north-south traffic, meaning external client-to-service communication. It handles things like external authentication, routing, rate limiting and API policies. A service mesh is more appropriate for east-west service-to-service communication, where it can provide mTLS, traffic management, retries, observability and service-to-service policies. I avoid putting domain business logic into either layer."

---

# 26. Complete Architecture

A mature architecture could look like:

```text
                         Internet
                            |
                            ↓
                     Cloud Load Balancer
                            |
                  +---------+---------+
                  |                   |
                  ↓                   ↓
             Gateway 1           Gateway 2
                  |                   |
                  +---------+---------+
                            |
                     Service Mesh
                            |
           +----------------+----------------+
           |                |                |
           ↓                ↓                ↓
      Order Service   Payment Service   Inventory Service
           |                |                |
           ↓                ↓                ↓
         MySQL            MySQL            MongoDB
                            |
                          Kafka
                            |
           +----------------+----------------+
           |                                 |
           ↓                                 ↓
    Notification Service             Reporting Service
```

---

# 27. Experience-Level Expectations

## 0–2 Years

Know:

```text
What is API Gateway?
Why do we need it?
What is routing?
```

### Common Question

**What problem does an API Gateway solve?**

Answer:

> "It provides a single entry point for clients and routes requests to the appropriate microservice. It also centralizes common concerns such as authentication, logging and rate limiting instead of implementing them separately in every client or service."

---

# 28. 2–4 Years

You should know:

```text
Spring Cloud Gateway
Routes
Predicates
Filters
JWT validation
Rate limiting
Circuit breaker
Service discovery
Load balancing
```

### Question

**How do you route requests based on path?**

Answer:

```text
/orders/**     → ORDER-SERVICE

/inventory/**  → INVENTORY-SERVICE

/payments/**   → PAYMENT-SERVICE
```

With Spring Cloud Gateway:

```text
path predicate
      ↓
route
      ↓
lb://SERVICE-NAME
```

---

# 29. 4–7 Years

You should be able to discuss:

```text
High Availability
Autoscaling
Rate limiting
Canary routing
API versioning
BFF
Gateway vs Service Mesh
Observability
Circuit breakers
Timeouts
Failure isolation
```

---

# 30. Senior Interview Question

### How do you prevent Gateway from becoming a bottleneck?

Answer:

> "I would run multiple stateless Gateway instances behind a load balancer, configure health checks and autoscaling, and monitor request rate, latency, error rate and resource utilization. I would also use proper connection and timeout settings and avoid putting expensive business logic in the Gateway. For rate limiting or shared state, I would use distributed infrastructure rather than relying on local in-memory state."

---

# 31. Senior Interview Question

### Why shouldn't business logic be placed in the Gateway?

Answer:

> "The Gateway should handle cross-cutting concerns such as routing, authentication, rate limiting and API policies. Domain logic belongs in the respective microservice. Putting business logic in the Gateway creates tight coupling, makes deployments harder and turns the Gateway into a central bottleneck."

---

# 32. Senior Interview Question

### Where do you draw the Gateway vs Service Mesh boundary?

Answer:

> "I generally keep client-facing concerns at the Gateway: external authentication, API routing, rate limiting, CORS and API versioning. I use the service mesh for service-to-service concerns such as mTLS, east-west traffic management, retries, service-level observability and traffic policies. The exact boundary depends on the organization's infrastructure and security model."

---

# 33. Interview Whiteboard Flow

Remember this:

```text
                    CLIENT
                       |
                       ↓
                Load Balancer
                       |
              +--------+--------+
              |                 |
              ↓                 ↓
         API Gateway 1     API Gateway 2
              |                 |
              +--------+--------+
                       |
                 Authentication
                       |
                  Rate Limiting
                       |
                     Routing
                       |
             +---------+---------+
             |         |         |
             ↓         ↓         ↓
           Order    Payment   Inventory
             |         |         |
             +---------+---------+
                       |
                  Service Mesh
                       |
              Service-to-Service
```

---

# 34. Final 1-Minute Interview Answer

> **"API Gateway is a single entry point for external clients in a microservices architecture. Instead of clients directly calling multiple microservices, they call the Gateway, which routes requests based on paths or other rules. In Spring Cloud Gateway, I can use routes such as `/orders/**` to `lb://ORDER-SERVICE`, where `lb://` represents load-balanced service routing rather than hardcoded instance addresses.**
>
> **The Gateway can centralize cross-cutting concerns such as JWT authentication, rate limiting, logging, CORS, API versioning and circuit breaking. I keep business logic inside the downstream services.**
>
> **For high availability, I deploy multiple stateless Gateway instances behind a load balancer with health checks and autoscaling, so the Gateway isn't a single point of failure. For service-to-service communication, I would consider a service mesh for concerns such as mTLS, retries, traffic management and observability, while keeping external API concerns at the Gateway."**

### Easy formula to remember

```text
API Gateway
    ↓
Single Entry Point
    ↓
Routing
    ↓
Authentication
    ↓
Rate Limiting
    ↓
Logging
    ↓
Circuit Breaker
    ↓
Versioning
    ↓
Multiple Instances + Load Balancer
    ↓
High Availability
```

**For your 5+ years Java/Spring Boot profile, focus especially on:** `lb://`, predicates vs filters, JWT, rate limiting, circuit breaker, Gateway HA/autoscaling, Gateway vs Load Balancer, Gateway vs Service Mesh, and the failure scenario where the Gateway itself becomes unavailable.
