Absolutely. Based on the **DEXIAN L1 – Java Developer** interview and your **Pay10 Wallet/Payment Microservices experience**, here is a comprehensive mock-interview question bank.

## DEXIAN L1 Java Developer — Complete Mock Interview

### Round 1 — Introduction & Project

1. Tell me about yourself.
2. Explain your current project.
3. What is your role in the project?
4. What are your day-to-day responsibilities?
5. Explain your project architecture.
6. Why did you choose microservices architecture?
7. How many microservices are there in your project?
8. Which service did you work on most?
9. How does a request flow through your application?
10. What is the role of API Gateway?
11. How do services communicate with each other?
12. Why do you use REST?
13. Where do you use Kafka?
14. Why did you use Kafka instead of synchronous communication?
15. Where do you use Redis?
16. Where do you use MySQL?
17. Where do you use MongoDB?
18. What AWS services do you use?
19. Explain one complete payment flow.
20. Explain one production issue you handled.

---

# 2. Core Java

21. What are the main features of Java?
22. Why is Java platform independent?
23. What is JVM?
24. Difference between JDK, JRE and JVM?
25. Explain JVM architecture.
26. What is bytecode?
27. What is class loading?
28. What is the difference between stack and heap?
29. What is garbage collection?
30. How does garbage collection work?
31. What are strong, weak and soft references?
32. What is the difference between `==` and `.equals()`?
33. What is `hashCode()`?
34. Why should we override `hashCode()` when overriding `equals()`?
35. What is an immutable class?
36. How do you create an immutable class?
37. Why is String immutable?
38. Difference between String, StringBuilder and StringBuffer.
39. What is String Pool?
40. What is the difference between `final`, `finally`, and `finalize()`?
41. What is a static variable?
42. Can we override a static method?
43. Can we overload a static method?
44. Can a constructor be final?
45. Can an abstract class have a constructor?
46. Can an interface have a constructor?
47. What is an abstract class?
48. What is an interface?
49. Abstract class vs interface.
50. What are default and static methods in interfaces?
51. What is multiple inheritance?
52. How does Java solve the diamond problem?
53. What is method overloading?
54. What is method overriding?
55. Overloading vs overriding.
56. What is compile-time polymorphism?
57. What is runtime polymorphism?
58. Explain encapsulation.
59. Explain inheritance.
60. Explain abstraction.
61. Explain polymorphism.
62. Composition vs inheritance.
63. What is a marker interface?
64. What is a functional interface?
65. What is a lambda expression?
66. What is a method reference?
67. What is Optional?
68. Why should we avoid unnecessary use of Optional?
69. Checked vs unchecked exceptions.
70. `throw` vs `throws`.
71. Can we have multiple catch blocks?
72. Can finally block be skipped?
73. What happens if an exception occurs inside finally?
74. Custom exception — why and how?
75. What is try-with-resources?

---

# 3. Java Collections

76. What is Collection Framework?
77. Difference between Collection and Collections.
78. List vs Set vs Map.
79. ArrayList vs LinkedList.
80. ArrayList vs Vector.
81. HashSet vs LinkedHashSet vs TreeSet.
82. HashMap vs Hashtable.
83. HashMap vs ConcurrentHashMap.
84. HashMap vs LinkedHashMap.
85. TreeMap vs HashMap.
86. How does HashMap work internally?
87. How does HashMap handle collisions?
88. What happens when two keys have the same hashcode?
89. What is bucket in HashMap?
90. What is load factor?
91. What is the default HashMap capacity?
92. What happens when HashMap reaches its threshold?
93. Why are immutable objects preferred as HashMap keys?
94. Can HashMap have null key?
95. Can ConcurrentHashMap have null key?
96. How does HashSet work internally?
97. How does TreeSet maintain sorting?
98. Comparable vs Comparator.
99. Fail-fast vs fail-safe iterator.
100. Iterator vs ListIterator.
101. How can you make a collection thread-safe?
102. What is CopyOnWriteArrayList?
103. When would you use ConcurrentHashMap?

---

# 4. Java 8 / Streams

104. What are the important features introduced in Java 8?
105. What is Stream API?
106. Collection vs Stream.
107. Intermediate vs terminal operations.
108. `map()` vs `flatMap()`.
109. `filter()` vs `map()`.
110. `findFirst()` vs `findAny()`.
111. `forEach()` vs `forEachOrdered()`.
112. What is `reduce()`?
113. What is `collect()`?
114. What is Collectors?
115. How do you convert List to Map?
116. How do you remove duplicates using streams?
117. How do you sort a list using streams?
118. How do you find the second-highest salary?
119. How do you group employees by department?
120. How do you count employees by department?
121. How do you find duplicate elements?
122. How do you find the first non-repeated character?
123. Sequential stream vs parallel stream.
124. When should you avoid parallel streams?
125. What is lazy evaluation in streams?

### Coding questions

126. Reverse a String.
127. Reverse an integer.
128. Check palindrome.
129. Find factorial.
130. Find Fibonacci series.
131. Find prime number.
132. Find duplicate elements in an array.
133. Find missing number.
134. Find second-largest number.
135. Find frequency of characters.
136. Find first non-repeated character.
137. Remove duplicates from ArrayList.
138. Sort an array.
139. Merge two sorted arrays.
140. Two Sum.
141. Two Pointer problem.
142. Move zeroes to the end.
143. Find maximum subarray sum.
144. Reverse an array.
145. Find intersection of two arrays.
146. Find common elements between two lists.
147. Find longest substring without repeating characters.
148. Check whether two strings are anagrams.
149. Find duplicate number.
150. Binary search.

---

# 5. Multithreading

151. What is a thread?
152. Process vs thread.
153. How do you create a thread?
154. Thread vs Runnable.
155. Runnable vs Callable.
156. What is ExecutorService?
157. Types of thread pools.
158. FixedThreadPool vs CachedThreadPool.
159. What is ScheduledExecutorService?
160. What is Future?
161. What is CompletableFuture?
162. Future vs CompletableFuture.
163. What is synchronization?
164. What is synchronized method?
165. Synchronized block vs method.
166. What is a race condition?
167. How do you prevent race conditions?
168. What is deadlock?
169. How do you prevent deadlock?
170. What is livelock?
171. What is starvation?
172. What is volatile?
173. Can volatile maintain data consistency?
174. AtomicInteger vs synchronized.
175. What is ReentrantLock?
176. ReentrantLock vs synchronized.
177. CountDownLatch.
178. CyclicBarrier.
179. Semaphore.
180. What is thread-safe code?
181. What is ThreadLocal?
182. What is ConcurrentHashMap?
183. Explain producer-consumer problem.
184. How would you process 10,000 records concurrently?
185. How do you handle exceptions in CompletableFuture?

---

# 6. Spring Framework

186. What is Spring Framework?
187. What is IoC?
188. What is Dependency Injection?
189. Constructor injection vs field injection.
190. What is Spring Bean?
191. Bean lifecycle.
192. Bean scopes.
193. Singleton vs prototype.
194. What is ApplicationContext?
195. ApplicationContext vs BeanFactory.
196. What is `@Component`?
197. `@Service` vs `@Repository` vs `@Component`.
198. What is `@Autowired`?
199. How does Spring resolve dependencies?
200. What is `@Qualifier`?
201. What is `@Primary`?
202. What is `@Configuration`?
203. What is `@Bean`?
204. What is component scanning?
205. What is Spring AOP?
206. What is cross-cutting concern?
207. What is an Aspect?
208. What is a Pointcut?
209. What is Advice?
210. What is Spring transaction management?

---

# 7. Spring Boot

211. What is Spring Boot?
212. Spring vs Spring Boot.
213. Advantages of Spring Boot.
214. What is auto-configuration?
215. How does auto-configuration work?
216. What is `@SpringBootApplication`?
217. Explain the three annotations inside `@SpringBootApplication`.
218. What is starter dependency?
219. What is embedded Tomcat?
220. How do you change the server port?
221. `application.properties` vs `application.yml`.
222. What are Spring Profiles?
223. How do you configure different environments?
224. What is Actuator?
225. What are Actuator endpoints?
226. How do you implement global exception handling?
227. What is `@ControllerAdvice`?
228. `@RestController` vs `@Controller`.
229. `@RequestParam` vs `@PathVariable`.
230. `@RequestBody` vs `@RequestParam`.
231. How do you validate request data?
232. What is `@Valid`?
233. How do you create custom validation?
234. How do you implement pagination?
235. How do you implement sorting?
236. How do you handle API errors consistently?
237. How do you implement logging?
238. How do you configure external properties?
239. How do you secure sensitive configuration?

---

# 8. Spring Security / JWT

240. What is Spring Security?
241. Authentication vs authorization.
242. What is JWT?
243. Explain JWT structure.
244. Header vs payload vs signature.
245. How does JWT authentication work?
246. Where should JWT be stored?
247. What is access token?
248. What is refresh token?
249. How do you validate JWT?
250. What happens when JWT expires?
251. How do you implement role-based authorization?
252. What is SecurityFilterChain?
253. What is OncePerRequestFilter?
254. Why do we use BCrypt?
255. How do you implement password encryption?
256. What is OAuth 2.0?
257. OAuth vs JWT.
258. Authentication flow in your project.
259. How do you prevent unauthorized API access?
260. How do you handle CORS?

---

# 9. REST API

261. What is REST?
262. REST vs SOAP.
263. GET vs POST.
264. PUT vs PATCH.
265. DELETE.
266. What are HTTP status codes?
267. 200 vs 201.
268. 400 vs 401 vs 403.
269. 404 vs 409.
270. 500 vs 502 vs 503.
271. What is idempotency?
272. Which HTTP methods are idempotent?
273. How do you make a payment API idempotent?
274. What is API versioning?
275. What is pagination?
276. What is rate limiting?
277. What is request validation?
278. What is content negotiation?
279. How do you design a good REST API?
280. How do you handle duplicate requests?

---

# 10. Microservices

281. What is Microservices architecture?
282. Monolith vs Microservices.
283. Advantages of microservices.
284. Disadvantages of microservices.
285. What is service discovery?
286. What is Eureka?
287. What is API Gateway?
288. API Gateway vs Load Balancer.
289. What is centralized configuration?
290. What is Config Server?
291. What is Circuit Breaker?
292. Explain Resilience4j.
293. What happens when downstream service fails?
294. Retry vs Circuit Breaker.
295. What is timeout?
296. What is Bulkhead pattern?
297. What is Rate Limiter?
298. What is distributed tracing?
299. What is correlation ID?
300. How do you monitor microservices?
301. How do you handle distributed transactions?
302. What is Saga pattern?
303. Choreography vs orchestration.
304. What is eventual consistency?
305. How do you maintain data consistency?
306. How do microservices communicate?
307. REST vs gRPC.
308. When would you use synchronous communication?
309. When would you use asynchronous communication?
310. How do you handle service failure?

---

# 11. Kafka

311. What is Kafka?
312. Why Kafka?
313. Kafka architecture.
314. What is Broker?
315. What is Topic?
316. What is Partition?
317. What is Offset?
318. What is Producer?
319. What is Consumer?
320. What is Consumer Group?
321. How does Kafka achieve scalability?
322. How does Kafka achieve fault tolerance?
323. What is replication factor?
324. Leader vs follower partition.
325. What happens when a Kafka broker goes down?
326. What happens when a consumer goes down?
327. What is consumer rebalancing?
328. What is partition ordering?
329. Does Kafka guarantee message ordering?
330. How do you maintain ordering?
331. What is acknowledgment?
332. `acks=0`, `acks=1`, `acks=all`.
333. What is Kafka offset commit?
334. Auto commit vs manual commit.
335. At-most-once vs at-least-once vs exactly-once.
336. What is idempotent Kafka producer?
337. What is retry topic?
338. What is Dead Letter Topic?
339. How do you handle failed Kafka messages?
340. Kafka vs RabbitMQ.
341. How does your transaction service use Kafka?
342. What happens if DB transaction succeeds but Kafka publishing fails?
343. What is the Outbox Pattern?
344. How would you prevent duplicate Kafka processing?

---

# 12. Redis

345. What is Redis?
346. Why use Redis?
347. Redis vs database.
348. What is caching?
349. Cache-aside pattern.
350. Write-through vs write-behind.
351. What is TTL?
352. How do you store OTP in Redis?
353. How do you expire OTP?
354. How do you prevent repeated OTP requests?
355. What happens if Redis goes down?
356. Redis vs Memcached.
357. What is Redis distributed locking?
358. How would you prevent duplicate payment processing using Redis?

---

# 13. MySQL / SQL

359. What is normalization?
360. Normalization vs denormalization.
361. Primary key vs unique key.
362. Foreign key.
363. Index.
364. Clustered vs non-clustered index.
365. What is composite index?
366. What is query optimization?
367. INNER JOIN vs LEFT JOIN.
368. WHERE vs HAVING.
369. GROUP BY.
370. ORDER BY.
371. UNION vs UNION ALL.
372. Subquery vs JOIN.
373. What is transaction?
374. ACID properties.
375. Isolation levels.
376. Dirty read.
377. Non-repeatable read.
378. Phantom read.
379. Optimistic vs pessimistic locking.
380. What causes deadlock in MySQL?
381. How do you troubleshoot a slow query?
382. How do you optimize database performance?
383. What is connection pooling?
384. What is HikariCP?

### SQL coding

385. Find second-highest salary.
386. Find duplicate records.
387. Find employees with maximum salary.
388. Find department-wise maximum salary.
389. Find employees who don't have a department.
390. Find duplicate email IDs.
391. Find nth-highest salary.
392. Count employees department-wise.
393. Delete duplicate records.
394. Find records created in the last 7 days.

---

# 14. Hibernate / JPA

395. What is JPA?
396. JPA vs Hibernate.
397. What is Entity?
398. What is `@Entity`?
399. `@Id` and `@GeneratedValue`.
400. One-to-One relationship.
401. One-to-Many.
402. Many-to-One.
403. Many-to-Many.
404. Lazy vs eager loading.
405. What is N+1 problem?
406. How do you solve N+1?
407. What is cascade?
408. What is orphanRemoval?
409. First-level cache.
410. Second-level cache.
411. JPQL vs native query.
412. What is EntityManager?
413. What is persistence context?
414. What is dirty checking?
415. Optimistic locking using `@Version`.

---

# 15. MongoDB

416. What is MongoDB?
417. SQL vs MongoDB.
418. Collection vs table.
419. Document vs row.
420. What is BSON?
421. Why MongoDB?
422. What is MongoDB index?
423. What is aggregation?
424. `$match`.
425. `$group`.
426. `$lookup`.
427. `$project`.
428. `$sort`.
429. `$skip` and `$limit`.
430. MongoDB transactions.
431. Embedded vs referenced documents.
432. How do you optimize MongoDB queries?
433. What is MongoTemplate?
434. Repository vs MongoTemplate.

---

# 16. Payment Domain — Very Important for You

435. Explain your payment architecture.

436. Explain merchant onboarding.

437. What happens when a merchant is onboarded?

438. Explain Merchant Hosted Payment Flow.

439. Explain Server-to-Server Payment Flow.

440. What is an acquirer?

441. What is a payment gateway?

442. Gateway vs acquirer.

443. Explain the complete payment transaction lifecycle.

444. What happens if payment request is duplicated?

445. How do you implement idempotency?

446. What happens if payment succeeds at the bank but your service receives a timeout?

447. How do you reconcile transactions?

448. How do you handle callback/webhook failures?

449. How do you secure payment APIs?

450. Why do you use encryption?

451. AES vs RSA.

452. Symmetric vs asymmetric encryption.

453. What is JWT?

454. How do you protect sensitive payment data?

455. How do you handle transaction timeout?

456. How do you handle partial failure?

457. How do you prevent double debit?

458. What happens if Kafka fails during payment processing?

459. What happens if the database goes down?

460. How do you trace one payment transaction across multiple microservices?

---

# 17. Production Scenario Questions

461. API suddenly starts returning 500. What will you check?

462. API response time increases from 200 ms to 5 seconds. How will you debug?

463. CPU reaches 100%. What will you do?

464. Memory reaches 95%. What will you check?

465. Application gets `OutOfMemoryError`. How do you investigate?

466. Database CPU is high. What will you check?

467. Kafka consumer lag is increasing. What will you do?

468. Kafka producer is failing. What will you check?

469. Redis is unavailable. What happens?

470. One microservice is down. How will you prevent the whole system from failing?

471. A downstream API is responding slowly. What will you implement?

472. Users are receiving duplicate notifications. How will you debug?

473. Duplicate payment transactions are being created. How will you fix them?

474. DB transaction succeeds but API returns failure. How do you handle it?

475. API returns 403 in production but works locally. What will you check?

476. Application cannot connect to database. What will you check?

477. Port is already in use. How do you fix it?

478. Kafka messages are being processed twice. Why?

479. Production deployment breaks the application. What steps will you take?

480. How do you rollback a deployment?

---

# 18. AWS

481. What AWS services have you used?
482. What is EC2?
483. What is S3?
484. What is RDS?
485. RDS vs DynamoDB.
486. What is Lambda?
487. What is CloudWatch?
488. How do you monitor an application on AWS?
489. How do you check application logs?
490. How do you secure AWS resources?
491. What is IAM?
492. IAM user vs role.
493. What is Security Group?
494. What is VPC?
495. Public vs private subnet.
496. What is Load Balancer?
497. ALB vs NLB.
498. How would you deploy Spring Boot on AWS?
499. How would you scale your application?
500. How do you store secrets in AWS?

---

# 19. Docker / CI-CD

501. What is Docker?
502. VM vs Docker.
503. What is Docker image?
504. What is Docker container?
505. Dockerfile?
506. Docker Compose?
507. What is a Docker volume?
508. How do you expose a port?
509. How do you reduce Docker image size?
510. Explain your Jenkins pipeline.
511. What happens after you push code?
512. What is CI/CD?
513. Continuous Integration vs Continuous Deployment.
514. How do you handle failed builds?
515. What is SonarQube?
516. How do you deploy Spring Boot application?

---

# 20. Git

517. What is Git?
518. Git vs GitHub.
519. `git clone` vs `git pull`.
520. `git fetch` vs `git pull`.
521. `git merge` vs `git rebase`.
522. What is conflict?
523. How do you resolve merge conflicts?
524. What is cherry-pick?
525. What is stash?
526. How do you revert a commit?
527. Reset vs revert.
528. How do you create a branch?
529. How do you delete a branch?
530. What is origin?
531. How do you checkout a remote branch?

---

# 21. System Design — L1/L1+ Level

532. Design a payment system.
533. Design a wallet system.
534. Design an OTP service.
535. Design a notification service.
536. Design a URL shortener.
537. Design an employee management system.
538. Design a transaction processing system.
539. Design a bill-payment system.
540. Design a scalable REST API.
541. How would you make your system highly available?
542. How would you scale a microservice?
543. How would you handle millions of transactions?
544. How would you handle database failure?
545. How would you handle Kafka failure?
546. How would you handle Redis failure?
547. How would you implement monitoring?
548. How would you implement distributed tracing?
549. How would you handle duplicate requests?
550. How would you design fault-tolerant payment processing?

---

# 22. HR / Managerial Questions

551. Tell me about yourself.
552. Why are you looking for a change?
553. Why DEXIAN?
554. Why should we hire you?
555. What are your strengths?
556. What is your weakness?
557. Where do you see yourself in 5 years?
558. Tell me about a difficult production issue.
559. Tell me about a conflict with a team member.
560. How do you handle pressure?
561. How do you prioritize tasks?
562. Have you worked independently?
563. Have you mentored junior developers?
564. How do you review code?
565. How do you ensure code quality?
566. How do you handle tight deadlines?
567. What happens if you disagree with your senior?
568. Are you comfortable working with clients?
569. Are you comfortable with relocation?
570. What is your notice period?
571. What is your current CTC?
572. What are your salary expectations?
573. Are you interviewing with other companies?
574. Why should we select you?

---

# 🔥 Most Important 30 Questions for Your DEXIAN L1

If the interviewer has limited time, **prepare these first**:

1. Tell me about yourself.
2. Explain your Pay10 project.
3. Explain your project architecture.
4. What is your role?
5. Explain one complete payment flow.
6. Why microservices?
7. API Gateway — why?
8. REST vs gRPC.
9. Circuit Breaker and Resilience4j.
10. Kafka architecture.
11. How does Kafka communicate between services?
12. Kafka consumer group.
13. Kafka partition and offset.
14. How do you handle duplicate Kafka messages?
15. Idempotency — explain with payment example.
16. Redis — why did you use it?
17. Explain JWT authentication.
18. Authentication vs authorization.
19. HashMap internal working.
20. HashMap vs ConcurrentHashMap.
21. ArrayList vs LinkedList.
22. HashSet internal working.
23. Java 8 Streams.
24. `map()` vs `flatMap()`.
25. CompletableFuture.
26. Race condition and prevention.
27. SQL joins and indexes.
28. JPA lazy vs eager loading.
29. N+1 problem.
30. Explain one production issue and how you solved it.

### 🎯 Interviewer follow-up chain you should practice

For almost every project answer, expect:

**What? → Why? → How? → Alternative? → Failure scenario? → Production example?**

For example:

> **Interviewer:** Why Kafka?  
> **You:** We use Kafka for asynchronous communication.
>
> **Interviewer:** Why asynchronous?  
> **You:** It reduces coupling and allows the producer to continue without waiting for the consumer.
>
> **Interviewer:** What if Kafka is down?  
> **You:** We use retries and failure handling/DLT depending on the event. Critical transaction state is persisted so that the event can be retried safely.
>
> **Interviewer:** What if the same message comes twice?  
> **You:** We make the consumer idempotent using a unique transaction/event identifier and verify whether that event has already been processed.

This **follow-up style** is particularly important for an L1 interview because knowing definitions alone is usually not enough; you need to explain **why you used the technology and what happens when it fails**.