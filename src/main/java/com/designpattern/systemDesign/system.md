# System Design Interview — 0 to 7 Years

For a **5+ year Java/Spring Boot developer**, prepare system design progressively from basic concepts to advanced distributed-system design.

## 0–1 Year — System Design Fundamentals

### Topics

* What is System Design?
* Functional requirements
* Non-functional requirements
* Scalability
* Availability
* Reliability
* Performance
* Maintainability
* Latency
* Throughput
* Capacity
* Single point of failure
* Client-server architecture
* Monolithic architecture
* Basic database concepts
* Basic caching
* Basic load balancing

### Questions

1. What is system design?
2. What is the difference between functional and non-functional requirements?
3. What is scalability?
4. Horizontal vs vertical scaling?
5. What is availability?
6. What is reliability?
7. What is latency?
8. What is throughput?
9. What is a bottleneck?
10. What is a Single Point of Failure?
11. What is load balancing?
12. What is caching?
13. Why do we need a database?
14. SQL vs NoSQL?
15. What is an API?

---

# 1–2 Years — Basic Architecture

### Topics

* Client
* Server
* REST API
* Database
* Load Balancer
* Reverse Proxy
* Web Server
* Application Server
* Cache
* CDN
* Database indexing
* Replication
* Sharding basics
* Message Queue basics

### Questions

16. Explain a basic web application architecture.
17. What happens when a user calls an API?
18. What is the role of a Load Balancer?
19. Load Balancer vs Reverse Proxy?
20. What is a CDN?
21. Why do we use CDN?
22. What is database indexing?
23. What is database replication?
24. Primary vs Replica database?
25. What is database sharding?
26. What is a message queue?
27. Kafka vs traditional message queue?
28. When should you use asynchronous processing?
29. What is a cache?
30. What happens when cache is unavailable?

---

# 2–3 Years — Intermediate System Design

### Topics

* Microservices
* API Gateway
* Service Discovery
* Database-per-service
* Redis
* Kafka
* Message queues
* Rate limiting
* Authentication
* Authorization
* JWT
* API versioning
* Retry
* Timeout
* Circuit Breaker
* Distributed logging

### Questions

31. Monolith vs Microservices?
32. How would you break a monolith into Microservices?
33. What is API Gateway?
34. What is Service Discovery?
35. How do Microservices communicate?
36. REST vs Kafka?
37. When would you choose synchronous communication?
38. When would you choose asynchronous communication?
39. How would you design authentication?
40. How would you design authorization?
41. Where would you use Redis?
42. How would you design rate limiting?
43. How would you handle service failure?
44. What is Circuit Breaker?
45. How would you prevent cascading failures?
46. How would you handle API timeout?
47. How would you handle retry?
48. How would you design centralized logging?

---

# 3–4 Years — Distributed Systems

### Topics

* Distributed systems
* CAP Theorem
* Consistency
* Availability
* Partition tolerance
* Eventual consistency
* Distributed transactions
* Saga
* Idempotency
* Distributed locking
* Concurrency
* Race conditions
* Event-driven architecture
* CQRS
* Event Sourcing

### Questions

49. What is a distributed system?
50. Explain CAP Theorem.
51. What is eventual consistency?
52. Strong consistency vs eventual consistency?
53. What is a distributed transaction?
54. Why are distributed transactions difficult?
55. Explain Saga Pattern.
56. Choreography vs Orchestration?
57. What is a compensating transaction?
58. What is idempotency?
59. How do you prevent duplicate payment?
60. How do you handle concurrent requests?
61. What is distributed locking?
62. How would you implement distributed locking using Redis?
63. What is race condition?
64. What is CQRS?
65. What is Event Sourcing?
66. When would you use event-driven architecture?

---

# 4–5 Years — Advanced System Design

### Topics

* High-level design
* Low-level design
* Scalability
* High availability
* Fault tolerance
* Data partitioning
* Database sharding
* Replication
* Read replicas
* Distributed cache
* Kafka partitioning
* Consumer groups
* Backpressure
* Rate limiting
* Circuit breaker
* Bulkhead
* Disaster recovery

### Questions

67. How would you design a highly available system?
68. How would you design a system for millions of users?
69. How would you handle 100K requests per second?
70. How would you scale the database?
71. What is database sharding?
72. What is a shard key?
73. How do you choose a shard key?
74. What is database replication?
75. What is read/write splitting?
76. How would you scale Redis?
77. How would you scale Kafka?
78. How do Kafka partitions improve scalability?
79. What happens when a Kafka consumer fails?
80. How do you handle Kafka consumer lag?
81. What is backpressure?
82. How do you handle traffic spikes?
83. How do you design rate limiting?
84. How do you design fault tolerance?
85. How do you design disaster recovery?

---

# 5–6 Years — Senior System Design

### Topics

* Capacity estimation
* HLD
* LLD
* Distributed transactions
* Data consistency
* Event-driven architecture
* Reliability
* Observability
* Security
* Multi-region architecture
* Disaster recovery
* Failover
* Auto-scaling
* Containerization
* Kubernetes
* Cloud architecture
* AWS

### Questions

86. How do you start a system-design interview?
87. How do you gather requirements?
88. How do you estimate traffic?
89. How do you calculate storage requirements?
90. How do you estimate database capacity?
91. How do you identify bottlenecks?
92. How do you design for high availability?
93. How do you design for fault tolerance?
94. How do you design multi-region architecture?
95. Active-Active vs Active-Passive?
96. How do you handle region failure?
97. How do you design disaster recovery?
98. What is RTO?
99. What is RPO?
100. How do you monitor a distributed system?
101. How do you implement distributed tracing?
102. How do you design secure Microservices?
103. How do you design zero-downtime deployment?
104. Blue-Green vs Canary deployment?
105. How would you autoscale Microservices?

---

# 6–7 Years — Architect-Level System Design

### Topics

* Large-scale distributed systems
* Multi-region systems
* Global traffic management
* Active-Active architecture
* Data consistency
* Consensus
* Distributed locking
* Distributed ID generation
* Event-driven architecture
* CQRS
* Event Sourcing
* Saga
* Outbox Pattern
* Service Mesh
* Kubernetes
* Multi-cloud
* Disaster recovery
* Observability
* Security architecture
* Cost optimization

### Questions

106. Design a globally distributed system.
107. How would you design Active-Active architecture?
108. How would you handle global traffic?
109. How would you handle cross-region data replication?
110. How do you solve distributed consistency problems?
111. How do you generate unique IDs across multiple services?
112. UUID vs Snowflake ID?
113. How do you design distributed locking?
114. How do you prevent duplicate events?
115. How do you guarantee reliable event delivery?
116. Explain Transactional Outbox Pattern.
117. Explain Saga at large scale.
118. How would you design an event-driven architecture?
119. How would you design a multi-region payment system?
120. How would you design a highly available banking system?
121. How would you design a globally scalable notification system?
122. How would you handle a complete database failure?
123. How would you handle a complete AWS region failure?
124. How would you design zero-downtime migration?
125. How would you migrate a monolith to Microservices without downtime?
126. How would you design a system with 100 million users?
127. How would you optimize system cost without reducing reliability?
128. How would you design observability for 100+ Microservices?

---

# 7+ Years / Architect-Level Design Problems

These are excellent **end-to-end interview problems**:

### Beginner

1. Design URL Shortener
2. Design Library Management System
3. Design Parking Lot
4. Design Employee Management System
5. Design File Upload System

### Intermediate

6. Design Food Delivery System
7. Design E-commerce System
8. Design Order Management System
9. Design Notification System
10. Design Chat Application
11. Design Ride Booking System
12. Design Hotel Booking System

### Senior — 5+ Years

13. **Design Payment Gateway**
14. **Design Digital Wallet**
15. **Design Money Transfer System**
16. **Design Bill Payment System**
17. **Design Merchant Onboarding System**
18. **Design Transaction Processing System**
19. **Design Banking System**
20. **Design Notification Platform**
21. **Design Order + Payment System**
22. **Design API Gateway**
23. **Design Distributed Rate Limiter**
24. **Design Distributed Job Scheduler**

### Advanced — 6–7+ Years

25. Design Instagram
26. Design YouTube
27. Design WhatsApp
28. Design Uber
29. Design Netflix
30. Design Amazon
31. Design Google Drive
32. Design Distributed Cache
33. Design Distributed Message Queue
34. Design Global Payment Platform

---

# ⭐ For Your 5+ Years Interview

Based on your **Java + Spring Boot + Microservices + Kafka + Redis + MySQL/MongoDB + AWS + payment-domain** background, concentrate particularly on these:

| Priority | System Design Topic             |
| -------- | ------------------------------- |
| 🔴       | Microservices Architecture      |
| 🔴       | Payment Gateway Design          |
| 🔴       | Wallet System                   |
| 🔴       | Transaction Processing          |
| 🔴       | Kafka/Event-driven Architecture |
| 🔴       | Saga & Distributed Transactions |
| 🔴       | Idempotency                     |
| 🔴       | Database Design & Sharding      |
| 🔴       | Redis/Caching                   |
| 🔴       | API Gateway                     |
| 🔴       | Scalability & High Availability |
| 🔴       | Failure Handling                |
| 🟠       | CAP Theorem                     |
| 🟠       | CQRS/Event Sourcing             |
| 🟠       | Rate Limiting                   |
| 🟠       | Distributed Locking             |
| 🟠       | AWS Architecture                |
| 🟠       | Kubernetes                      |
| 🟠       | Observability                   |
| 🟠       | Disaster Recovery               |

### The interview flow you should practice

For every **5+ year system-design question**, answer in this sequence:

**1. Requirements → 2. Scale estimation → 3. APIs → 4. High-level architecture → 5. Database → 6. Cache → 7. Kafka/async processing → 8. Security → 9. Scalability → 10. Failure handling → 11. Monitoring → 12. Trade-offs**

That structure is much more important for a senior interview than memorizing architecture diagrams.
