# 10. Service Registry & Discovery — Step-by-Step Coding

This is an important **Spring Boot Microservices** topic for your 5+ years interview preparation.

The easiest way to understand it is:

> **Service Discovery means services find each other dynamically instead of using hardcoded IP addresses.**

---

# 1. Without Service Discovery

Suppose you have:

```text
Order Service
     |
     ↓
http://192.168.1.20:8082
     |
     ↓
Inventory Service
```

Problem: Inventory service may restart.

Its IP can change:

```text
Old:
192.168.1.20

New:
192.168.1.35
```

Then this breaks:

```java
http://192.168.1.20:8082
```

---

# 2. With Service Discovery

Use Eureka:

```text
                    Eureka Server
                    localhost:8761
                         |
          ┌──────────────┼──────────────┐
          ↓              ↓              ↓
    Order Service   Inventory Service  Payment
       :8081              :8082          :8083
          |                 |
          └────────────┬────┘
                       ↓
              Service Discovery
```

Order Service says:

```text
"I need inventory-service"
```

Eureka tells it:

```text
inventory-service
    ↓
192.168.1.35:8082
```

---

# 3. Create 3 applications

For learning, create:

```text
service-discovery-demo/
│
├── discovery-server
│
├── order-service
│
└── inventory-service
```

Flow:

```text
Client
  |
  ↓
Order Service
  |
  | "Where is inventory-service?"
  ↓
Eureka
  |
  | "Use 8082"
  ↓
Inventory Service
```

---

# 4. Step 1 — Create Eureka Server

Create Spring Boot project:

```text
discovery-server
```

Dependency:

```groovy
implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
```

---

# 5. Enable Eureka Server

Create:

```java
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                DiscoveryServerApplication.class,
                args
        );
    }
}
```

Important:

```java
@EnableEurekaServer
```

means:

> This application acts as the Eureka Service Registry.

---

# 6. Configure Eureka Server

`application.yml`

```yaml
server:
  port: 8761

spring:
  application:
    name: discovery-server

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

Start it.

Open:

```text
http://localhost:8761
```

You should see the Eureka dashboard.

---

# 7. Step 2 — Create Inventory Service

Create:

```text
inventory-service
```

Dependency:

```groovy
implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
implementation 'org.springframework.boot:spring-boot-starter-web'
```

---

# 8. Configure Inventory Service

```yaml
server:
  port: 8082

spring:
  application:
    name: inventory-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

The important part:

```yaml
spring:
  application:
    name: inventory-service
```

This becomes the **service name** in Eureka.

---

# 9. Inventory Controller

```java
@RestController
@RequestMapping("/stock")
public class InventoryController {

    @GetMapping("/{sku}")
    public String getStock(
            @PathVariable String sku) {

        return "Stock available for " + sku;
    }
}
```

Test directly:

```text
GET http://localhost:8082/stock/SKU100
```

Response:

```text
Stock available for SKU100
```

---

# 10. Start Inventory Service

Start:

```text
Discovery Server
      ↓
Inventory Service
```

Go to:

```text
http://localhost:8761
```

You should see:

```text
Instances currently registered with Eureka

INVENTORY-SERVICE
```

This means:

```text
Inventory Service
       ↓
registered itself
       ↓
Eureka
```

---

# 11. Step 3 — Create Order Service

Create:

```text
order-service
```

Dependencies:

```groovy
implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
implementation 'org.springframework.boot:spring-boot-starter-web'
```

---

# 12. Configure Order Service

```yaml
server:
  port: 8081

spring:
  application:
    name: order-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

Now:

```text
Order Service
      ↓
registers
      ↓
Eureka
```

---

# 13. Enable Feign

Main class:

```java
@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                OrderServiceApplication.class,
                args
        );
    }
}
```

`@EnableFeignClients` tells Spring:

> Find my Feign client interfaces and create implementations for them.

---

# 14. Create Feign Client

```java
@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/stock/{sku}")
    String getStock(
            @PathVariable("sku") String sku
    );
}
```

Notice:

```java
@FeignClient(name = "inventory-service")
```

We didn't write:

```text
http://localhost:8082
```

That's the important part.

---

# 15. How does Feign find the service?

This:

```java
@FeignClient(name = "inventory-service")
```

means:

```text
Feign
  |
  ↓
Eureka
  |
  ↓
Find inventory-service
  |
  ↓
192.168.x.x:8082
  |
  ↓
Inventory Service
```

So the application doesn't care about the physical IP.

---

# 16. Create Order Service

```java
@Service
@RequiredArgsConstructor
public class OrderService {

    private final InventoryClient inventoryClient;

    public String createOrder(String sku) {

        String stock =
                inventoryClient.getStock(sku);

        return "Order created. " + stock;
    }
}
```

---

# 17. Create Controller

```java
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{sku}")
    public String createOrder(
            @PathVariable String sku) {

        return orderService.createOrder(sku);
    }
}
```

---

# 18. Test the complete flow

Start all 3:

```text
1. Eureka       :8761
2. Order        :8081
3. Inventory    :8082
```

Then call:

```http
POST http://localhost:8081/orders/SKU100
```

Flow:

```text
Client
  |
  ↓
Order Service :8081
  |
  ↓
Feign Client
  |
  ↓
Eureka
  |
  ↓
inventory-service
  |
  ↓
Inventory :8082
```

Response:

```text
Order created. Stock available for SKU100
```

---

# 19. What happens if Inventory has 2 instances?

This is where service discovery becomes really useful.

Run:

```text
Inventory Instance 1
192.168.1.10:8082

Inventory Instance 2
192.168.1.11:8082
```

Both register:

```text
              Eureka
                 |
        ┌────────┴────────┐
        ↓                 ↓
 Inventory :8082     Inventory :8083
```

Feign asks for:

```text
inventory-service
```

The discovery/load-balancing mechanism can select an available instance.

So you don't write:

```text
192.168.1.10
```

or:

```text
192.168.1.11
```

---

# 20. Important Interview Question

### How does Feign know which physical instance to call?

Answer:

> The Feign client uses the logical service name, such as `inventory-service`. The service discovery/load-balancing infrastructure resolves that name to an available service instance and selects an instance for the request. Therefore, the client doesn't need to hardcode the physical IP or hostname.

---

# 21. What happens when a service goes down?

Suppose:

```text
Eureka
   |
   ├── Inventory :8082 ✅
   └── Inventory :8083 ❌
```

The failed instance should eventually be removed or considered unavailable through Eureka's registration/heartbeat mechanism.

Then traffic can go to:

```text
Inventory :8082
```

Health checks and graceful shutdown are important during deployments.

---

# 22. Eureka Registration Flow

Remember this:

```text
              START
                |
                ↓
        Inventory Service
                |
                ↓
       Register with Eureka
                |
                ↓
       Send heartbeat/lease
                |
                ↓
             Eureka
```

If the service shuts down gracefully:

```text
Service
   ↓
Deregister
   ↓
Eureka
```

If it crashes:

```text
Service
   X
No heartbeat
   ↓
Eureka eventually
marks/removes instance
```

---

# 23. Kubernetes is different

If you're running on Kubernetes, you often don't need Eureka.

Kubernetes provides:

```text
Service
   +
CoreDNS
   +
kube-proxy / networking
```

Architecture:

```text
Order Pod
    |
    | inventory-service
    ↓
Kubernetes Service
    |
    ├── Inventory Pod 1
    ├── Inventory Pod 2
    └── Inventory Pod 3
```

DNS can be:

```text
inventoryservice.default.svc.cluster.local
```

Or commonly just:

```text
inventoryservice
```

inside the appropriate Kubernetes namespace.

---

# 24. Eureka vs Kubernetes

| Eureka                                | Kubernetes                               |
| ------------------------------------- | ---------------------------------------- |
| Application-level discovery           | Platform-native discovery                |
| Spring Cloud ecosystem                | Kubernetes ecosystem                     |
| Service registers with Eureka         | Pods are selected by Service             |
| Eureka dashboard                      | Kubernetes APIs/tools                    |
| Useful outside Kubernetes             | Native for K8s                           |
| Client-side discovery patterns common | Service/networking layer handles routing |

If you're already fully running on Kubernetes, adding Eureka may duplicate capabilities and operational complexity unless there's a specific architectural reason.

---

# 25. Production Architecture

For your type of backend:

```text
                       API Gateway
                            |
                            ↓
                  ┌─────────────────┐
                  │ Service Discovery│
                  └────────┬────────┘
                           |
        ┌──────────────────┼──────────────────┐
        ↓                  ↓                  ↓
   User Service      Payment Service    Order Service
        |                  |                  |
        ↓                  ↓                  ↓
      MySQL             MySQL              MySQL
        |
        └────────── Kafka / Redis ────────────┘
```

If deployed on Kubernetes:

```text
                    Kubernetes
                         |
              ┌──────────┴──────────┐
              ↓                     ↓
       API Gateway Service     Internal Services
                                      |
                         ┌────────────┼────────────┐
                         ↓            ↓            ↓
                       Order       Payment      Inventory
                         |            |            |
                       Pods         Pods         Pods
```

---

# 26. Service Discovery + Circuit Breaker

These are different responsibilities.

```text
Feign
  ↓
Service Discovery
  ↓
Find Inventory Instance
  ↓
Circuit Breaker
  ↓
Inventory Service
```

### Service Discovery

> **Where is the service?**

### Circuit Breaker

> **Should I call the service right now?**

### Load Balancer

> **Which available instance should receive the request?**

Very important distinction for interviews.

---

# 27. Complete Project Structure

```text
service-discovery-demo/
│
├── discovery-server/
│   └── src/main/java/
│       └── DiscoveryServerApplication.java
│
├── inventory-service/
│   └── src/main/java/
│       ├── InventoryServiceApplication.java
│       └── controller/
│           └── InventoryController.java
│
└── order-service/
    └── src/main/java/
        ├── OrderServiceApplication.java
        ├── controller/
        │   └── OrderController.java
        ├── service/
        │   └── OrderService.java
        └── client/
            └── InventoryClient.java
```

---

# 28. 5+ Years Interview Answer

> **Service Discovery solves the problem of dynamically locating microservice instances. In a traditional setup, hardcoded IPs are unreliable because instances can scale, restart, or move. With Eureka, services register themselves with the Eureka server and clients use a logical service name such as `inventory-service`. Feign can then resolve that service name through the discovery and load-balancing infrastructure. In Kubernetes, I would normally prefer Kubernetes Service and DNS-based discovery because it is native to the platform, and I would avoid adding Eureka unless there is a specific requirement. During rolling deployments, I would also use health checks, graceful shutdown and appropriate deregistration/readiness handling so traffic isn't sent to terminating instances.**

### Remember these 4 questions:

```text
Service Discovery
       |
       ├── WHERE is the service?
       |
       ├── Eureka → register/discover
       |
       ├── Feign → call using service name
       |
       └── Kubernetes → Service + DNS
```

**One-line interview definition:**

> **Service Discovery allows microservices to dynamically find healthy service instances without hardcoding their IP addresses or hostnames.**
