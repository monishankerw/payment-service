# 2. Circuit Breaker — Complete Interview Explanation

A **Circuit Breaker** protects your service from repeatedly calling a failing downstream service.

For example:

```text
Payment Service
      |
      | calls
      ↓
Inventory Service
      |
      X DOWN / TIMEOUT
```

Without Circuit Breaker:

```text
Request
  ↓
Inventory
  ↓
Timeout
  ↓
Retry
  ↓
Timeout
  ↓
Retry
  ↓
Timeout
```

This can create a **cascading failure** and overload the Inventory Service even more.

With Circuit Breaker:

```text
Payment Service
      |
      ↓
Circuit Breaker
      |
      X Inventory failing
      |
      ↓
Fallback
```

Once the failure threshold is reached, the Circuit Breaker **opens** and subsequent calls fail fast instead of calling Inventory.

---

# 1. Three Main States

```text
              Failure threshold reached
          ┌───────────────────────────────┐
          │                               ↓
      CLOSED ─────────────────────────→ OPEN
        ↑                                |
        |                                | wait duration
        |                                ↓
        └────────────── HALF_OPEN ←──────┘
                       |
                  Test requests
                   /       \
                SUCCESS    FAILURE
                   |          |
                   ↓          ↓
                CLOSED       OPEN
```

## CLOSED

Normal state.

```text
Request
   ↓
Circuit Breaker
   ↓
Inventory Service
```

Calls are allowed.

Circuit Breaker records:

```text
Success
Failure
Slow call
```

---

## OPEN

Failure rate crosses the configured threshold.

Example:

```text
20 calls
12 failed
8 successful

Failure rate = 60%
```

Configuration:

```yaml
failure-rate-threshold: 50
```

Since:

```text
60% > 50%
```

Circuit becomes:

```text
OPEN
```

Now:

```text
Request
   ↓
Circuit Breaker
   ↓
OPEN
   ↓
❌ Don't call Inventory
   ↓
Fallback
```

This is **fail fast**.

---

# 2. How long does OPEN stay?

This configuration controls it:

```yaml
wait-duration-in-open-state: 10s
```

Meaning:

```text
OPEN
 |
 | 10 seconds
 ↓
HALF_OPEN
```

You could use:

```yaml
wait-duration-in-open-state: 30s
```

or:

```yaml
wait-duration-in-open-state: 1m
```

The right value depends on the downstream service's recovery characteristics.

---

# 3. HALF_OPEN

After the wait duration, the Circuit Breaker allows a limited number of test calls.

Example:

```yaml
permitted-number-of-calls-in-half-open-state: 5
```

Then:

```text
HALF_OPEN
   |
   ├── Test 1 → SUCCESS
   ├── Test 2 → SUCCESS
   ├── Test 3 → SUCCESS
   ├── Test 4 → SUCCESS
   └── Test 5 → SUCCESS
                  |
                  ↓
                CLOSED
```

If the test calls fail:

```text
HALF_OPEN
     |
     ↓
  FAILURE
     |
     ↓
    OPEN
```

---

# 4. Resilience4j Configuration

A more complete configuration:

```yaml
resilience4j:
  circuitbreaker:
    instances:

      inventoryService:

        # Circuit opens when failure rate reaches 50%
        failure-rate-threshold: 50

        # Don't calculate failure rate until
        # minimum number of calls has occurred
        minimum-number-of-calls: 20

        # Evaluate the latest 20 calls
        sliding-window-size: 20

        # Stay OPEN for 10 seconds
        wait-duration-in-open-state: 10s

        # Allow 5 test calls in HALF_OPEN
        permitted-number-of-calls-in-half-open-state: 5

        # Automatically transition OPEN → HALF_OPEN
        automatic-transition-from-open-to-half-open-enabled: true
```

---

# 5. What is Sliding Window?

This is important for your **4–7 year interview**.

Suppose:

```yaml
sliding-window-size: 20
```

Circuit Breaker evaluates the latest 20 calls.

Example:

```text
Latest 20 calls

✓ ✓ ✗ ✗ ✓ ✗ ✗ ✓ ✗ ✗
✓ ✓ ✗ ✗ ✗ ✓ ✗ ✓ ✗ ✗
```

Suppose 12 failed:

```text
12 / 20 = 60%
```

Threshold:

```yaml
failure-rate-threshold: 50
```

Therefore:

```text
60% > 50%
       ↓
Circuit OPEN
```

---

# 6. Minimum Number of Calls

Suppose:

```yaml
minimum-number-of-calls: 20
```

This prevents the Circuit Breaker from making a decision based on too few requests.

For example:

```text
Only 2 requests happened

Request 1 → failure
Request 2 → failure

Failure = 100%
```

You generally don't want to conclude that the dependency is unhealthy based only on two requests.

So:

```text
minimum-number-of-calls = 20
```

means the failure-rate calculation won't be meaningful until the configured minimum number of calls has been reached within the relevant window.

---

# 7. Java Code

```java
@Service
public class InventoryClientService {

    @CircuitBreaker(
        name = "inventoryService",
        fallbackMethod = "fallback"
    )
    public Inventory checkStock(String skuId) {

        return inventoryClient.getStock(skuId);
    }

    public Inventory fallback(String skuId, Throwable throwable) {

        return Inventory.unavailable(skuId);
    }
}
```

Normal:

```text
checkStock()
    ↓
Inventory Service
    ↓
Response
```

When Circuit Breaker is OPEN:

```text
checkStock()
    ↓
Circuit Breaker
    ↓
fallback()
```

---

# 8. What should the fallback return?

There is no single answer.

It depends on the business operation.

### Cached data

Good for:

```text
Product details
Configuration
Read-only information
```

Example:

```text
Inventory Service unavailable
       ↓
Redis cache
       ↓
Return last known inventory
```

### Default value

Useful when a safe default exists.

Example:

```text
Recommendation Service unavailable
       ↓
Return empty recommendations
```

### Error response

For critical operations, don't pretend the operation succeeded.

For example, a payment operation should generally not return:

```text
Payment SUCCESS
```

when the payment dependency is unavailable.

Instead:

```text
Payment temporarily unavailable
```

or an appropriate retryable/business response.

---

# 9. Circuit Breaker vs Retry

This is a very common interview question.

### Retry

Means:

> "The failure might be temporary, so try again."

```text
Request
  ↓
Service
  ↓
Failure
  ↓
Retry
  ↓
Service
```

### Circuit Breaker

Means:

> "The dependency appears unhealthy, so stop calling it for a while."

```text
Request
  ↓
Circuit Breaker
  ↓
OPEN
  ↓
Don't call dependency
```

So:

```text
Retry = try again

Circuit Breaker = stop calling temporarily
```

They solve different problems.

---

# 10. Retry + Circuit Breaker

This is the important **senior-level question**:

> "In what order do you apply Retry and Circuit Breaker?"

The answer depends on what behavior you want, because decorator order changes what the Circuit Breaker observes.

A common resilience design is:

```text
Request
   ↓
Retry
   ↓
Circuit Breaker
   ↓
Inventory Service
```

So:

```text
Request
   ↓
Retry
   ↓
Circuit Breaker
   ↓
Inventory
   X
   ↓
Retry
   ↓
Circuit Breaker
   ↓
Inventory
```

Here, each actual attempt passes through the Circuit Breaker, so repeated failed attempts can contribute to opening the circuit.

But you must be careful: **Retry can multiply traffic to an already-failing dependency.**

---

# 11. Retry Storm

Imagine:

```text
1000 requests
```

and Retry configuration:

```text
max-attempts: 3
```

Worst-case traffic can approach:

```text
1000 × 3
       ↓
3000 attempts
```

Now imagine the Inventory Service is already overloaded.

Retry makes it worse:

```text
Inventory overloaded
       ↓
Failures
       ↓
Retry
       ↓
More traffic
       ↓
More failures
       ↓
More retry
       ↓
💥 Retry storm
```

That's why you should use:

```text
Retry
+
Circuit Breaker
+
Timeout
+
Bulkhead
```

carefully.

---

# 12. How to avoid Retry Storm

Use:

### Limited retries

```yaml
max-attempts: 3
```

Don't retry forever.

### Exponential backoff

Instead of:

```text
retry immediately
retry immediately
retry immediately
```

use:

```text
Attempt 1
   ↓
100ms
   ↓
Attempt 2
   ↓
200ms
   ↓
Attempt 3
```

### Jitter

Add randomness:

```text
100ms + random
200ms + random
400ms + random
```

This prevents thousands of clients from retrying simultaneously.

### Retry only transient failures

Good candidates:

```text
temporary network failure
timeout
HTTP 503
```

Usually don't blindly retry:

```text
400 Bad Request
401 Unauthorized
403 Forbidden
validation failure
business rule failure
```

For payment systems, also consider **idempotency** before retrying operations that could create duplicate side effects.

---

# 13. Circuit Breaker + Bulkhead

Bulkhead limits concurrent calls.

Example:

```text
Payment Service
      |
      ├── Request 1
      ├── Request 2
      ├── Request 3
      ├── Request 4
      └── Request 5
             ↓
        Bulkhead limit
             ↓
        Other requests rejected
```

This prevents one dependency from consuming all application threads/connections.

Think:

```text
Timeout
   ↓
Retry
   ↓
Circuit Breaker
   ↓
Bulkhead
   ↓
Downstream
```

The **exact decorator order should be chosen based on the failure semantics you want and verified against your Resilience4j configuration/version**; don't memorize one universal order as the only correct answer.

---

# 14. Production Monitoring

At 4–7 years, mention monitoring.

Monitor:

```text
Circuit state
Failure rate
Slow-call rate
Number of calls
Fallback count
Retry count
Retry failures
Response time
```

Example:

```text
Inventory Circuit Breaker

CLOSED → normal
OPEN → alert
HALF_OPEN → recovery testing
```

With metrics:

```text
Prometheus
     ↓
Grafana
     ↓
Circuit Breaker Dashboard
```

You can alert when:

```text
Circuit = OPEN
```

for a sustained period.

---

# 15. Interview answer — 30 seconds

> **"Circuit Breaker protects our service from a failing downstream dependency. Resilience4j maintains three main states: CLOSED, OPEN and HALF_OPEN. In CLOSED, calls pass normally and failures are recorded. When the configured failure rate crosses the threshold after the minimum number of calls, the circuit opens and requests fail fast instead of calling the dependency. After the configured wait duration, it moves to HALF_OPEN and permits a few test calls. If they succeed, it returns to CLOSED; otherwise it goes back to OPEN. In production, I tune the sliding window, minimum calls, failure threshold and wait duration based on traffic patterns, and combine Circuit Breaker with bounded Retry, Timeout and Bulkhead while monitoring the state and fallback metrics."**

### One-line memory trick

```text
CLOSED  = CALL
OPEN    = DON'T CALL
HALF_OPEN = TEST
```

And:

```text
Retry      → "Try again"
Circuit    → "Stop calling"
Bulkhead   → "Limit concurrency"
Timeout    → "Don't wait forever"
Fallback   → "Degrade gracefully"
```
