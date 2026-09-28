# MongoDB Interview Topics & Questions — 0 to 5 Years

For a **5+ year Java/Spring Boot developer**, prepare MongoDB progressively from CRUD fundamentals to aggregation, indexing, transactions, replication, and production design.

---

## 🟢 0–1 Year — MongoDB Fundamentals

### Topics

* What is MongoDB?
* NoSQL databases
* SQL vs NoSQL
* Document database
* Database
* Collection
* Document
* Field
* BSON
* JSON vs BSON
* `_id`
* ObjectId
* Embedded documents
* Arrays
* MongoDB data types

### Questions

1. What is MongoDB?
2. Why is MongoDB called a NoSQL database?
3. MongoDB vs MySQL?
4. What is a document?
5. What is a collection?
6. What is BSON?
7. JSON vs BSON?
8. What is `_id`?
9. What is ObjectId?
10. Can MongoDB documents have different fields?
11. What is an embedded document?
12. Embedded document vs separate collection?
13. What are MongoDB data types?

---

# 🟢 1–2 Years — CRUD Operations

### Topics

* `insertOne()`
* `insertMany()`
* `find()`
* `findOne()`
* `updateOne()`
* `updateMany()`
* `replaceOne()`
* `deleteOne()`
* `deleteMany()`
* Query operators
* Projection
* Sorting
* Pagination

### Questions

14. How do you insert a document?
15. `insertOne()` vs `insertMany()`?
16. How do you find a document?
17. `find()` vs `findOne()`?
18. How do you update a document?
19. `updateOne()` vs `updateMany()`?
20. How do you delete documents?
21. What is projection?
22. How do you sort MongoDB results?
23. How do you implement pagination?
24. What is `$set`?
25. What is `$unset`?
26. What is `$inc`?
27. What is `$push`?
28. What is `$pull`?
29. What is `$addToSet`?

### Example

```javascript
db.users.find({
    status: "ACTIVE",
    age: { $gte: 18 }
});
```

---

# 🟡 2–3 Years — Query Operators

### Topics

* Comparison operators
* `$eq`
* `$ne`
* `$gt`
* `$gte`
* `$lt`
* `$lte`
* Logical operators
* `$and`
* `$or`
* `$not`
* `$nor`
* `$in`
* `$nin`
* Array operators
* `$elemMatch`
* `$all`
* `$size`
* `$exists`
* `$regex`

### Questions

30. Explain MongoDB comparison operators.
31. `$in` vs `$nin`?
32. `$or` vs `$and`?
33. How do you search inside an array?
34. What is `$elemMatch`?
35. How do you check whether a field exists?
36. How do you search using regex?
37. How do you query nested documents?
38. How do you query an array of objects?

---

# 🟡 2–3 Years — MongoDB Aggregation

### Topics

* Aggregation Pipeline
* `$match`
* `$project`
* `$group`
* `$sort`
* `$limit`
* `$skip`
* `$unwind`
* `$lookup`
* `$count`
* `$sum`
* `$avg`
* `$min`
* `$max`
* `$addFields`
* `$replaceRoot`
* `$facet`

### Questions

39. What is MongoDB Aggregation?
40. What is an aggregation pipeline?
41. `$match` vs `$project`?
42. What does `$group` do?
43. What is `$unwind`?
44. What is `$lookup`?
45. MongoDB `$lookup` vs SQL JOIN?
46. How do you calculate total transaction amount?
47. How do you find the highest transaction?
48. How do you group transactions by merchant?
49. How do you generate a monthly report?
50. How do you paginate aggregation results?
51. How do you optimize an aggregation pipeline?

### Example

```javascript
db.transactions.aggregate([
    {
        $match: {
            status: "SUCCESS"
        }
    },
    {
        $group: {
            _id: "$merchantId",
            totalAmount: { $sum: "$amount" }
        }
    },
    {
        $sort: {
            totalAmount: -1
        }
    }
]);
```

---

# 🟡 3–4 Years — Indexing & Performance

### Topics

* MongoDB indexes
* Single-field index
* Compound index
* Multikey index
* Unique index
* Text index
* TTL index
* Sparse index
* Partial index
* Index selectivity
* Query optimization
* `explain()`
* Collection scan
* Index scan

### Questions

52. What is an index in MongoDB?
53. Why do we need indexes?
54. What is a compound index?
55. What is a multikey index?
56. What is a unique index?
57. What is a TTL index?
58. What is a text index?
59. What is a partial index?
60. What is index selectivity?
61. How do you check whether MongoDB uses an index?
62. What is `COLLSCAN`?
63. What is `IXSCAN`?
64. How do you optimize a slow MongoDB query?
65. How do you decide the order of fields in a compound index?
66. Can too many indexes cause problems?
67. How do indexes affect write performance?

### Very Important

Practice:

```javascript
db.transactions
  .find({
      merchantId: "M1001",
      status: "SUCCESS"
  })
  .explain("executionStats");
```

Know how to interpret:

* `COLLSCAN`
* `IXSCAN`
* `totalDocsExamined`
* `totalKeysExamined`
* `executionTimeMillis`

---

# 🟠 3–4 Years — Data Modeling

### Topics

* Embedded documents
* References
* One-to-One
* One-to-Many
* Many-to-Many
* Denormalization
* Data duplication
* Schema design
* Schema validation
* Document size considerations

### Questions

68. How do you design MongoDB schema?
69. Embedding vs referencing?
70. When should you embed documents?
71. When should you use references?
72. How do you model One-to-Many?
73. How do you model Many-to-Many?
74. How do you avoid excessively large documents?
75. What are the advantages of denormalization?
76. When can denormalization become a problem?
77. How would you design a transaction collection?
78. How would you design an employee mapping collection?

---

# 🔴 4–5 Years — Transactions & Consistency

### Topics

* MongoDB transactions
* Single-document atomicity
* Multi-document transactions
* Sessions
* ACID
* Read concern
* Write concern
* Read preference
* Consistency
* Atomic operations
* Optimistic concurrency

### Questions

79. Are MongoDB operations atomic?
80. What is single-document atomicity?
81. What are multi-document transactions?
82. When should you use MongoDB transactions?
83. How do MongoDB transactions work?
84. What is a MongoDB session?
85. What is Read Concern?
86. What is Write Concern?
87. What is Read Preference?
88. How do you handle concurrent updates?
89. How do you prevent lost updates?
90. MongoDB transaction vs MySQL transaction?
91. When should you avoid MongoDB transactions?

---

# 🔴 4–5 Years — Replication & High Availability

### Topics

* Replica Set
* Primary
* Secondary
* Arbiter
* Election
* Failover
* Replication
* Oplog
* Read preference
* Write concern
* High availability

### Questions

92. What is a Replica Set?
93. Primary vs Secondary?
94. What happens when Primary goes down?
95. How does election work?
96. What is failover?
97. What is oplog?
98. How does replication work?
99. Can applications read from Secondary?
100. What is `readPreference`?
101. What is `writeConcern`?
102. How do you design MongoDB for high availability?

---

# 🔴 4–5 Years — Sharding & Scalability

### Topics

* Sharding
* Shard
* Shard Key
* Config Server
* Mongos
* Horizontal scaling
* Data distribution
* Chunk
* Balancer
* Range-based sharding
* Hashed sharding

### Questions

103. What is MongoDB sharding?
104. Why do we need sharding?
105. What is a shard key?
106. How do you choose a shard key?
107. What makes a good shard key?
108. What is a poor shard key?
109. What is `mongos`?
110. What is Config Server?
111. What is a chunk?
112. What is the balancer?
113. Hashed vs range-based sharding?
114. Replication vs sharding?
115. How would you scale MongoDB for billions of documents?

---

# 🔥 MongoDB Production Questions — 5 Years

These are particularly important for senior interviews:

1. A MongoDB query takes 10 seconds. How do you troubleshoot it?
2. MongoDB CPU reaches 100%. What will you check?
3. MongoDB memory usage is very high. What will you check?
4. How do you identify slow queries?
5. How do you use `explain()`?
6. How do you choose indexes?
7. How do you optimize aggregation?
8. How do you handle millions of documents?
9. How do you archive old transactions?
10. How do you implement TTL?
11. How do you handle duplicate records?
12. How do you ensure idempotency?
13. How do you handle concurrent updates?
14. How do you handle MongoDB node failure?
15. How do you design MongoDB for high availability?
16. How do you scale MongoDB horizontally?
17. How do you choose a shard key?
18. How do you migrate a large MongoDB collection?
19. How do you back up MongoDB?
20. How do you recover from database failure?

---

# ⭐ MongoDB Coding Questions

Practice these with a **transaction collection**, because they map well to your payment experience:

### Basic

1. Find all successful transactions.
2. Find transactions greater than ₹10,000.
3. Find transactions for a specific merchant.
4. Find transactions created today.
5. Update transaction status.
6. Delete failed transactions.
7. Find transactions where a field doesn't exist.

### Intermediate

8. Find total transaction amount by merchant.
9. Find transaction count by status.
10. Find the highest transaction for each merchant.
11. Find the latest transaction for each user.
12. Find daily transaction totals.
13. Find monthly transaction totals.
14. Find users with more than 5 transactions.
15. `$lookup` users with transactions.
16. `$unwind` transaction arrays.
17. Build a transaction dashboard using aggregation.

### Advanced

18. Optimize a slow aggregation query.
19. Find duplicate payment transactions.
20. Find the latest status for every transaction.
21. Calculate success/failure percentage.
22. Calculate merchant-wise transaction volume.
23. Calculate running/cumulative transaction totals.
24. Build a dashboard using `$facet`.
25. Design indexes for a high-volume transaction collection.

---

# 📌 0–5 Year Roadmap

| Experience | Main MongoDB Topics                                                 |
| ---------- | ------------------------------------------------------------------- |
| **0–1**    | MongoDB basics, BSON, collections, CRUD                             |
| **1–2**    | Queries, operators, arrays, nested documents                        |
| **2–3**    | Aggregation, `$lookup`, `$group`, `$unwind`                         |
| **3–4**    | Indexes, `explain()`, schema design, performance                    |
| **4–5**    | Transactions, replication, sharding, HA, production troubleshooting |

### 🔥 For your 5+ year interview, prioritize

**CRUD → Query Operators → Aggregation → `$lookup` → Indexing → `explain()` → Schema Design → Embedding vs Referencing → Transactions → Read/Write Concern → Replica Sets → Sharding → Performance Optimization → Production Troubleshooting.**
