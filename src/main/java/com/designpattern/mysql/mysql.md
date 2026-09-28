# MySQL Interview Topics — 0 to 7 Years

For a **5+ year Java/Spring Boot developer**, prepare MySQL from basic SQL through database design, performance, transactions, and production troubleshooting.

---

## 0–1 Year — SQL Fundamentals

### Topics

1. Database basics
2. DBMS vs RDBMS
3. Tables, rows, columns
4. Primary Key
5. Foreign Key
6. Candidate Key
7. Unique Key
8. Composite Key
9. Constraints
10. `NULL`
11. `NOT NULL`
12. `DEFAULT`
13. `UNIQUE`
14. `CHECK`
15. `AUTO_INCREMENT`
16. `SELECT`
17. `INSERT`
18. `UPDATE`
19. `DELETE`
20. `WHERE`
21. `ORDER BY`
22. `GROUP BY`
23. `HAVING`
24. `DISTINCT`
25. `LIMIT`

### Questions

* What is MySQL?
* DBMS vs RDBMS?
* Primary Key vs Unique Key?
* Primary Key vs Foreign Key?
* What is a composite key?
* What is `NULL`?
* `WHERE` vs `HAVING`?
* `DELETE` vs `TRUNCATE` vs `DROP`?
* `GROUP BY` vs `ORDER BY`?
* What are constraints?

---

# 1–2 Years — SQL Queries

### Topics

* Aggregate functions
* `COUNT()`
* `SUM()`
* `AVG()`
* `MIN()`
* `MAX()`
* String functions
* Date functions
* Conditional expressions
* `CASE`
* Subqueries
* Correlated subqueries
* `IN`
* `EXISTS`
* `BETWEEN`
* `LIKE`
* `UNION`
* `UNION ALL`

### Questions

1. Find the second-highest salary.
2. Find the third-highest salary.
3. Find duplicate records.
4. Delete duplicate records.
5. Find employees with salary greater than average.
6. Find employees who don't have a department.
7. Find the highest salary in each department.
8. Find the number of employees in each department.
9. Find employees whose name starts with `A`.
10. Find employees who joined in the last 6 months.
11. `IN` vs `EXISTS`?
12. `UNION` vs `UNION ALL`?
13. Subquery vs JOIN?
14. What is a correlated subquery?

---

# 2–3 Years — Joins

### Topics

* INNER JOIN
* LEFT JOIN
* RIGHT JOIN
* CROSS JOIN
* SELF JOIN
* Multiple joins
* Join conditions
* Join performance

### Questions

1. What is a JOIN?
2. INNER JOIN vs LEFT JOIN?
3. LEFT JOIN vs RIGHT JOIN?
4. What is SELF JOIN?
5. What is CROSS JOIN?
6. Write a query using three tables.
7. Find employees without departments.
8. Find departments without employees.
9. Find customers who never placed an order.
10. Explain JOIN vs subquery.

### Must Practice

```sql
SELECT u.id, u.name, w.balance
FROM user u
LEFT JOIN wallet_master w
    ON u.id = w.user_id;
```

---

# 3–4 Years — Database Design

### Topics

* Normalization
* Denormalization
* 1NF
* 2NF
* 3NF
* BCNF
* Relationships
* One-to-One
* One-to-Many
* Many-to-Many
* ER diagrams
* Foreign keys
* Referential integrity
* Cascading

### Questions

1. What is normalization?
2. Explain 1NF, 2NF and 3NF.
3. Why do we normalize databases?
4. What is denormalization?
5. When should you denormalize?
6. One-to-One vs One-to-Many?
7. How do you model Many-to-Many?
8. What is referential integrity?
9. What is cascading delete?
10. Design a database for an e-commerce application.

---

# 4–5 Years — Indexing & Query Optimization

### Topics

* Index
* Clustered index
* Non-clustered index
* Composite index
* Unique index
* Covering index
* Prefix index
* Index selectivity
* B-Tree
* Query execution plan
* `EXPLAIN`
* Slow queries
* Full table scan
* Index scan
* Query optimization

### Questions

1. What is an index?
2. Why does an index improve query performance?
3. When can an index hurt performance?
4. What is a composite index?
5. How does a composite index work?
6. What is the leftmost-prefix rule?
7. What is a covering index?
8. What is index selectivity?
9. What is a full table scan?
10. How do you identify a slow query?
11. How does `EXPLAIN` help?
12. Why isn't MySQL using my index?
13. How many indexes should a table have?
14. Index on one column vs multiple columns?

### Very Important

Learn to analyze:

```sql
EXPLAIN
SELECT *
FROM transactions
WHERE merchant_id = 1001
AND status = 'SUCCESS'
AND created_at >= '2026-01-01';
```

For a **5+ year interview**, don't just say "add an index." Explain **which columns, index order, selectivity, query pattern, and execution plan**.

---

# 5–6 Years — Transactions & Concurrency

### Topics

* ACID
* Transactions
* `COMMIT`
* `ROLLBACK`
* Savepoints
* Isolation levels
* READ UNCOMMITTED
* READ COMMITTED
* REPEATABLE READ
* SERIALIZABLE
* Dirty Read
* Non-Repeatable Read
* Phantom Read
* Locks
* Row-level locking
* Table-level locking
* Deadlocks
* Optimistic locking
* Pessimistic locking

### Questions

1. Explain ACID.
2. What is a transaction?
3. What is `COMMIT`?
4. What is `ROLLBACK`?
5. Explain isolation levels.
6. What is a Dirty Read?
7. What is a Non-Repeatable Read?
8. What is a Phantom Read?
9. What is a deadlock?
10. How do you detect a deadlock?
11. How do you prevent deadlocks?
12. Row-level vs table-level locking?
13. Optimistic vs pessimistic locking?
14. How would you handle concurrent wallet transactions?
15. How would you prevent double payment?

---

# 6–7 Years — Advanced MySQL

### Topics

### Database Scaling

* Read replicas
* Replication
* Primary/Replica architecture
* Read/write splitting
* Horizontal scaling
* Vertical scaling
* Sharding
* Partitioning
* Database clustering

### Partitioning

* Range partitioning
* List partitioning
* Hash partitioning
* Partition pruning

### High Availability

* Failover
* Replication
* Backup
* Point-in-time recovery
* Disaster recovery
* RTO
* RPO

### Performance

* Query optimization
* Connection pooling
* Slow Query Log
* Buffer Pool
* Temporary tables
* Sorting
* Lock contention
* CPU/memory bottlenecks

### Questions

1. How do you scale MySQL?
2. Vertical vs horizontal scaling?
3. What is replication?
4. How does MySQL replication work?
5. What is read/write splitting?
6. What is database sharding?
7. Sharding vs partitioning?
8. How do you choose a shard key?
9. What happens when the primary database goes down?
10. How do you design MySQL for high availability?
11. How do you perform zero-downtime database migration?
12. How do you handle millions of transactions?
13. How do you optimize a slow production query?
14. How do you troubleshoot database CPU at 100%?
15. How do you troubleshoot connection pool exhaustion?
16. How do you handle deadlocks in production?
17. How do you design backup and recovery?
18. What are RTO and RPO?

---

# 🔥 SQL Coding Questions — Must Practice

For your **5+ years interview**, practice these without looking at solutions:

### Basic

1. Find second-highest salary.
2. Find Nth-highest salary.
3. Find duplicate records.
4. Remove duplicate records.
5. Find employees with salary greater than average.
6. Find maximum salary by department.
7. Find top 3 salaries per department.
8. Find employees who joined this year.
9. Find employees who have never placed an order.
10. Find customers with more than 5 orders.

### Intermediate

11. Find the second-highest salary using `DENSE_RANK()`.
12. Find top N records per group.
13. Find duplicate transactions.
14. Find consecutive transaction dates.
15. Find customers with transactions on consecutive days.
16. Find monthly transaction totals.
17. Find month-over-month growth.
18. Calculate running balance.
19. Calculate cumulative transaction amount.
20. Find the latest transaction for each customer.

### Advanced

21. Find the highest transaction per merchant.
22. Find the latest successful transaction per user.
23. Find users whose transaction amount exceeds their monthly average.
24. Find merchants with no transactions in the last 30 days.
25. Find users who completed transactions in every month.
26. Find the percentage of successful transactions.
27. Calculate rolling 7-day transaction volume.
28. Find gaps in transaction sequences.
29. Find duplicate payment requests using business keys.
30. Design a query for a large transaction-reporting table.

---

# ⭐ MySQL Topics You Should Prioritize for 5+ Years

| Priority | Topic                      |
| -------- | -------------------------- |
| 🔴       | Joins                      |
| 🔴       | Subqueries                 |
| 🔴       | CTE                        |
| 🔴       | Window Functions           |
| 🔴       | Indexing                   |
| 🔴       | Composite Index            |
| 🔴       | `EXPLAIN`                  |
| 🔴       | Query Optimization         |
| 🔴       | Transactions               |
| 🔴       | ACID                       |
| 🔴       | Isolation Levels           |
| 🔴       | Locks                      |
| 🔴       | Deadlocks                  |
| 🔴       | Normalization              |
| 🔴       | Database Design            |
| 🔴       | Pagination                 |
| 🔴       | Stored Procedures          |
| 🟠       | Views                      |
| 🟠       | Triggers                   |
| 🟠       | Replication                |
| 🟠       | Partitioning               |
| 🟠       | Sharding                   |
| 🟠       | Read Replicas              |
| 🟠       | High Availability          |
| 🟠       | Backup/Recovery            |
| 🟠       | Connection Pooling         |
| 🟠       | Production Troubleshooting |

### For your Java + Spring Boot + payment-domain interviews

The **most important combination** is:

**SQL Queries → JOINs → CTE → Window Functions → Indexing → EXPLAIN → Transactions → Isolation → Locks → Deadlocks → JPA/Hibernate → Query Optimization → Database Design → Scaling → Sharding/Replication.**

Especially practice **concurrent wallet/payment transactions, duplicate payments, transaction consistency, and large transaction-reporting queries**, because these connect MySQL knowledge directly to your Microservices experience.
