Yes. Since your current project uses **Spring Boot 4.1.1 + Gradle + Java 17**, we can set up a complete **Resilience4j Circuit Breaker** in that project. Spring Cloud's current compatibility table says the **2025.1.x** release train supports Spring Boot 4.1.x starting with 2025.1.2. ([Home][1])

## 1. Project structure

We'll create:

```text
payment-service
│
├── src/main/java/com/designpattern
│   │
│   ├── DesignPatternApplication.java
│   │
│   ├── controller
│   │   └── PaymentController.java
│   │
│   ├── service
│   │   └── PaymentService.java
│   │
│   ├── client
│   │   └── InventoryClient.java
│   │
│   └── model
│       └── Inventory.java
│
└── src/main/resources
    └── application.yml
```

The flow will be:

```text
Client
   |
   ↓
Payment Controller
   |
   ↓
Payment Service
   |
   ↓
Circuit Breaker
   |
   ↓
Inventory Service
   |
   ├── SUCCESS → return inventory
   |
   └── FAILURE
          ↓
       Fallback
```

---

# 2. `build.gradle`

Because your application is Spring Boot 4.1.1, use Spring Cloud 2025.1.2. Spring Cloud CircuitBreaker provides a Resilience4j starter for non-reactive applications. ([Home][2])

Update your `build.gradle`:

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '4.1.1'
    id 'io.spring.dependency-management' version '1.1.7'
}

group = 'com.designpattern'
version = '0.0.1-SNAPSHOT'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

ext {
    set('springCloudVersion', "2025.1.2")
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}

dependencies {

    // REST API
    implementation 'org.springframework.boot:spring-boot-starter-webmvc'

    // Circuit Breaker - Resilience4j
    implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j'

    // Actuator - health/metrics
    implementation 'org.springframework.boot:spring-boot-starter-actuator'

    // Resilience4j metrics
    implementation 'io.github.resilience4j:resilience4j-micrometer'

    // Lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    // Tests
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') {
    useJUnitPlatform()
}
```

The Actuator + `resilience4j-micrometer` combination enables Resilience4j metrics. ([Home][3])

### If this is only a Circuit Breaker demo

You don't need:

```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-kafka'
runtimeOnly 'com.mysql:mysql-connector-j'
```

Add those back later when you connect this to your actual Payment/Outbox project.

---

# 3. `application.yml`

Create:

```text
src/main/resources/application.yml
```

Use:

```yaml
spring:
  application:
    name: payment-service

server:
  port: 8080

resilience4j:

  circuitbreaker:

    instances:

      inventoryService:

        # Open circuit when failure rate >= 50%
        failure-rate-threshold: 50

        # Don't calculate failure rate until
        # at least 10 calls have happened
        minimum-number-of-calls: 10

        # Evaluate the latest 10 calls
        sliding-window-size: 10

        # Stay OPEN for 10 seconds
        wait-duration-in-open-state: 10s

        # Allow 3 test calls in HALF_OPEN
        permitted-number-of-calls-in-half-open-state: 3

        # Automatically move OPEN -> HALF_OPEN
        automatic-transition-from-open-to-half-open-enabled: true

        # Consider calls taking > 2 seconds as slow
        slow-call-duration-threshold: 2s

        # Open if 50% of calls are slow
        slow-call-rate-threshold: 50

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus

  endpoint:
    health:
      show-details: always

  health:
    circuitbreakers:
      enabled: true
```

Resilience4j supports count-based and time-based sliding windows, and its Circuit Breaker state machine includes `CLOSED`, `OPEN`, and `HALF_OPEN`, along with special states. ([resilience4j][4])

---

# 4. Main application

```java
package com.designpattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesignPatternApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                DesignPatternApplication.class,
                args
        );
    }
}
```

---

# 5. Inventory model

Create:

```text
model/Inventory.java
```

```java
package com.designpattern.model;

public record Inventory(
        String skuId,
        int availableQuantity,
        boolean available
) {

    public static Inventory unavailable(String skuId) {

        return new Inventory(
                skuId,
                0,
                false
        );
    }
}
```

---

# 6. Inventory Client

This represents calling another microservice.

Create:

```text
client/InventoryClient.java
```

```java
package com.designpattern.client;

import com.designpattern.model.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryClient {

    public Inventory getStock(String skuId) {

        System.out.println(
                "Calling Inventory Service for SKU: " + skuId
        );

        return new Inventory(
                skuId,
                100,
                true
        );
    }
}
```

For now, this is a fake downstream service.

Later, replace it with:

```text
RestClient
WebClient
OpenFeign
```

or another HTTP client.

---

# 7. Payment Service with Circuit Breaker

This is the most important class.

```java
package com.designpattern.service;

import com.designpattern.client.InventoryClient;
import com.designpattern.model.Inventory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final InventoryClient inventoryClient;

    public PaymentService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @CircuitBreaker(
            name = "inventoryService",
            fallbackMethod = "inventoryFallback"
    )
    public Inventory checkInventory(String skuId) {

        System.out.println(
                "Circuit Breaker: Calling Inventory Service"
        );

        return inventoryClient.getStock(skuId);
    }

    public Inventory inventoryFallback(
            String skuId,
            Throwable throwable
    ) {

        System.out.println(
                "Circuit Breaker Fallback triggered"
        );

        System.out.println(
                "Reason: " + throwable.getMessage()
        );

        return Inventory.unavailable(skuId);
    }
}
```

The Resilience4j starter provides the Spring annotations/AOP integration for this style of configuration. ([resilience4j][5])

---

# 8. Controller

Create:

```text
controller/PaymentController.java
```

```java
package com.designpattern.controller;

import com.designpattern.model.Inventory;
import com.designpattern.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/inventory/{skuId}")
    public Inventory checkInventory(
            @PathVariable String skuId
    ) {

        return paymentService.checkInventory(skuId);
    }
}
```

---

# 9. Start application

Run:

```cmd
gradlew clean bootRun
```

Application:

```text
http://localhost:8080
```

---

# 10. Test successful request

Call:

```cmd
curl http://localhost:8080/payments/inventory/SKU100
```

Response:

```json
{
  "skuId": "SKU100",
  "availableQuantity": 100,
  "available": true
}
```

Flow:

```text
Client
  ↓
/payments/inventory/SKU100
  ↓
PaymentController
  ↓
PaymentService
  ↓
CircuitBreaker
  ↓
InventoryClient
  ↓
SUCCESS
```

---

# 11. Now simulate Inventory failure

Change:

```java
public Inventory getStock(String skuId) {

    throw new RuntimeException(
            "Inventory Service is DOWN"
    );
}
```

Now call:

```cmd
curl http://localhost:8080/payments/inventory/SKU100
```

Instead of propagating the exception, Circuit Breaker invokes:

```java
inventoryFallback(...)
```

Response:

```json
{
  "skuId": "SKU100",
  "availableQuantity": 0,
  "available": false
}
```

Flow:

```text
Client
   ↓
Payment Service
   ↓
Circuit Breaker
   ↓
Inventory Service
   X
   ↓
Fallback
   ↓
Inventory unavailable
```

---

# 12. How does the Circuit actually OPEN?

Our configuration:

```yaml
minimum-number-of-calls: 10
sliding-window-size: 10
failure-rate-threshold: 50
```

Suppose:

```text
Call 1 → FAIL
Call 2 → FAIL
Call 3 → SUCCESS
Call 4 → FAIL
Call 5 → SUCCESS
Call 6 → FAIL
Call 7 → FAIL
Call 8 → SUCCESS
Call 9 → SUCCESS
Call 10 → FAIL
```

Failures:

```text
6 / 10 = 60%
```

Threshold:

```text
50%
```

Therefore:

```text
60% > 50%
```

Circuit:

```text
CLOSED
   ↓
OPEN
```

---

# 13. What happens when OPEN?

Suppose:

```yaml
wait-duration-in-open-state: 10s
```

Then:

```text
OPEN
 |
 | 10 seconds
 ↓
HALF_OPEN
```

During OPEN:

```text
Request 1
   ↓
Circuit Breaker
   ↓
OPEN
   ↓
Fallback

Request 2
   ↓
Circuit Breaker
   ↓
OPEN
   ↓
Fallback

Request 3
   ↓
Circuit Breaker
   ↓
OPEN
   ↓
Fallback
```

Inventory isn't repeatedly called.

---

# 14. HALF_OPEN

We configured:

```yaml
permitted-number-of-calls-in-half-open-state: 3
```

So:

```text
HALF_OPEN

Test 1 → SUCCESS
Test 2 → SUCCESS
Test 3 → SUCCESS

        ↓

      CLOSED
```

If tests fail:

```text
HALF_OPEN

Test 1 → FAIL
Test 2 → FAIL
Test 3 → FAIL

        ↓

       OPEN
```

---

# 15. See Circuit Breaker health

Because we enabled Actuator:

```yaml
management:
  endpoint:
    health:
      show-details: always

  health:
    circuitbreakers:
      enabled: true
```

Call:

```cmd
curl http://localhost:8080/actuator/health
```

You can inspect the Circuit Breaker health information when health indicators are registered/enabled. Resilience4j maps `CLOSED` to UP, `OPEN` to DOWN, and `HALF_OPEN` to UNKNOWN for its health indicator. ([resilience4j][5])

---

# 16. Metrics

You can inspect metrics through:

```text
http://localhost:8080/actuator/metrics
```

And specific Circuit Breaker metrics can be exposed through the Resilience4j/Micrometer integration. ([Home][3])

Typical things to monitor:

```text
failure rate
successful calls
failed calls
slow calls
not permitted calls
state transitions
```

---

# 17. Production architecture

For your actual microservices project:

```text
                         Client
                           |
                           ↓
                    API Gateway
                           |
                           ↓
                    Payment Service
                           |
                           ↓
                ┌─────────────────────┐
                │    Circuit Breaker  │
                │  inventoryService   │
                └──────────┬──────────┘
                           |
                    ┌──────┴──────┐
                    ↓             ↓
                Inventory      Fallback
                 Service
                    |
                    ↓
                  MySQL
```

If Inventory is unavailable:

```text
Payment Service
      |
      ↓
Circuit Breaker
      |
      X
Inventory
      |
      ↓
Fallback
```

---

# 18. Circuit Breaker + Retry + Timeout

For a production application, don't use Circuit Breaker alone.

A common resilience stack is:

```text
Request
   ↓
Timeout
   ↓
Retry
   ↓
Circuit Breaker
   ↓
Bulkhead
   ↓
Downstream Service
```

But **ordering is a design choice**, not a universal rule; the important point is understanding what each layer sees and avoiding retry amplification.

For example:

```text
100 requests
      ↓
Retry × 3
      ↓
potentially up to 300 attempts
```

Therefore don't configure unlimited retries.

Use:

```text
limited retries
+
exponential backoff
+
jitter
+
circuit breaker
+
timeout
+
bulkhead
```

---

# 19. Important payment-system point

For your fintech/payment project, be especially careful with retries.

Suppose:

```text
Payment Service
      |
      ↓
Bank/Acquirer
```

Request succeeds at the bank but the response is lost:

```text
Payment Service
      ↓
Bank
      ↓
SUCCESS
      X
Response lost
```

If you blindly retry:

```text
Payment Service
      ↓
Retry
      ↓
Bank
```

you could potentially create a duplicate business operation.

So use:

```text
Idempotency Key
       +
Retry
       +
Circuit Breaker
       +
Timeout
```

For example:

```text
Idempotency-Key: PAY-100001
```

The downstream payment operation should recognize the same key and avoid processing the same business request twice.

---

# 20. Interview explanation

For your **5+ years Java/Spring Boot interview**, answer:

> "We use Resilience4j Circuit Breaker to protect our service from failing downstream dependencies. For example, when Payment Service calls Inventory Service, the Circuit Breaker monitors the calls using a sliding window. If the configured failure rate crosses the threshold after the minimum number of calls, the circuit moves from CLOSED to OPEN and subsequent calls fail fast and execute a fallback. After the configured wait duration, it moves to HALF_OPEN and allows a limited number of test calls. If the dependency recovers, it returns to CLOSED; otherwise it goes back to OPEN. In production, I tune failure thresholds, sliding-window size, minimum calls and wait duration based on traffic patterns, and combine Circuit Breaker with bounded Retry, Timeout and Bulkhead while monitoring metrics and state transitions."

### Remember this:

```text
CLOSED
   ↓ failure threshold
OPEN
   ↓ wait duration
HALF_OPEN
   ↓
SUCCESS → CLOSED
FAILURE → OPEN
```

And the five resilience concepts:

```text
Timeout       → Don't wait forever
Retry         → Try again carefully
Circuit Breaker → Stop calling a failing service
Bulkhead      → Limit concurrent damage
Fallback      → Graceful degradation
```

This is the complete basic-to-production setup you can implement in your current Gradle Spring Boot project.

[1]: https://spring.io/blog/2026/06/11/spring-cloud-2025-1-2-aka-oakwood-has-been-released/?utm_source=chatgpt.com "Spring Cloud 2025.1.2 (aka Oakwood) Has Been Released"
[2]: https://spring.io/projects/spring-cloud/?utm_source=chatgpt.com "Spring Cloud"
[3]: https://docs.spring.io/spring-cloud-circuitbreaker/reference/spring-cloud-circuitbreaker-resilience4j/collecting-metrics.html?utm_source=chatgpt.com "Collecting Metrics :: Spring Cloud Circuitbreaker"
[4]: https://resilience4j.readme.io/docs/circuitbreaker?utm_source=chatgpt.com "CircuitBreaker"
[5]: https://resilience4j.readme.io/docs/getting-started-3?utm_source=chatgpt.com "Getting Started"
