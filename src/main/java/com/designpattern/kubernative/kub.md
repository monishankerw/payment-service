# Kubernetes Interview Topics & Questions — 0 to 3 Years

For a **5+ year Java/Spring Boot developer**, you don't necessarily need Kubernetes at an expert DevOps level. You should be able to **deploy, configure, scale, troubleshoot, and explain Spring Boot Microservices on Kubernetes**.

---

# 🟢 0–1 Year — Kubernetes Fundamentals

## Topics

1. What is Kubernetes?
2. Why Kubernetes?
3. Container vs Kubernetes
4. Docker vs Kubernetes
5. Kubernetes Architecture
6. Kubernetes Cluster
7. Control Plane
8. Worker Node
9. Pod
10. Container
11. Namespace
12. Label
13. Selector
14. Annotation
15. `kubectl`

## Questions

1. What is Kubernetes?
2. Why do we need Kubernetes?
3. Docker vs Kubernetes?
4. What is a Kubernetes Cluster?
5. What is a Node?
6. Control Plane vs Worker Node?
7. What is a Pod?
8. Can a Pod contain multiple containers?
9. What is a Namespace?
10. What is a Label?
11. What is a Selector?
12. What is `kubectl`?
13. How do you check running Pods?

### Important Commands

```bash
kubectl get pods
kubectl get nodes
kubectl get services
kubectl get deployments
kubectl get namespaces
```

---

# 🟢 1 Year — Pods & Deployments

## Topics

* Pod
* Deployment
* ReplicaSet
* Replicas
* Rolling Update
* Rollback
* Container Image
* Docker Registry
* Environment Variables
* Resource Requests
* Resource Limits

## Questions

14. What is a Pod?
15. Pod vs Container?
16. What is a Deployment?
17. What is ReplicaSet?
18. Deployment vs ReplicaSet?
19. How does Deployment maintain replicas?
20. What happens when a Pod crashes?
21. What happens when a Node crashes?
22. How do you scale a Deployment?
23. How do you perform a rolling deployment?
24. How do you rollback a deployment?
25. What are resource requests?
26. What are resource limits?

### Commands

```bash
kubectl create deployment myapp --image=myapp:1.0

kubectl scale deployment myapp --replicas=3

kubectl rollout status deployment/myapp

kubectl rollout history deployment/myapp

kubectl rollout undo deployment/myapp
```

---

# 🟢 1–2 Years — Kubernetes Services

## Topics

* Kubernetes Service
* ClusterIP
* NodePort
* LoadBalancer
* Service Discovery
* DNS
* Internal communication
* Ingress

## Questions

27. What is a Kubernetes Service?
28. Why do we need Services?
29. What is ClusterIP?
30. What is NodePort?
31. What is LoadBalancer?
32. ClusterIP vs NodePort vs LoadBalancer?
33. How do Pods communicate with each other?
34. What happens when a Pod IP changes?
35. How does Kubernetes service discovery work?
36. What is Kubernetes DNS?
37. What is Ingress?
38. Ingress vs LoadBalancer?
39. How do you expose a Spring Boot application?

### Typical Architecture

```text
Internet
   |
Ingress / Load Balancer
   |
Kubernetes Service
   |
+--------+--------+
|        |        |
Pod      Pod      Pod
|        |        |
Spring Boot Application
```

---

# 🟡 1–2 Years — Configuration & Secrets

## Topics

* ConfigMap
* Secret
* Environment variables
* Volumes
* PersistentVolume
* PersistentVolumeClaim
* StorageClass

## Questions

40. What is ConfigMap?
41. What is Secret?
42. ConfigMap vs Secret?
43. How do you pass environment variables to Spring Boot?
44. How do you store database credentials?
45. Should passwords be stored in ConfigMap?
46. What is PersistentVolume?
47. What is PersistentVolumeClaim?
48. Why do containers need persistent storage?
49. How would you configure a Spring Boot application using ConfigMap?

### Spring Boot Example

```yaml
env:
  - name: SPRING_PROFILES_ACTIVE
    value: "prod"
```

---

# 🟡 2 Years — Health Checks & Scaling

## Topics

* Liveness Probe
* Readiness Probe
* Startup Probe
* Health checks
* Horizontal Pod Autoscaler
* CPU utilization
* Memory utilization
* Replica scaling

## Questions

50. What is Liveness Probe?
51. What is Readiness Probe?
52. Liveness vs Readiness?
53. What is Startup Probe?
54. When should you use Startup Probe?
55. What happens when a readiness probe fails?
56. What happens when a liveness probe fails?
57. What is HPA?
58. How does Kubernetes autoscale Pods?
59. CPU-based vs memory-based scaling?
60. How do you configure Spring Boot health checks?

### Spring Boot + Kubernetes

```text
Spring Boot Actuator
        |
        +---- /actuator/health
                    |
              Kubernetes Probe
                    |
          Ready / Not Ready
```

---

# 🟡 2–3 Years — Networking

## Topics

* Pod Network
* Service Network
* ClusterIP
* DNS
* Ingress
* Network Policy
* CNI basics
* Internal vs external communication

## Questions

61. How does Pod-to-Pod communication work?
62. How does Service-to-Pod communication work?
63. What is ClusterIP?
64. What is Kubernetes DNS?
65. How does one Microservice call another?
66. What is NetworkPolicy?
67. How do you restrict communication between Microservices?
68. How does Ingress route requests?

---

# 🟠 2–3 Years — Kubernetes Security

## Topics

* RBAC
* Role
* ClusterRole
* RoleBinding
* ServiceAccount
* Secrets
* NetworkPolicy
* Security Context
* Least privilege

## Questions

69. What is RBAC?
70. Role vs ClusterRole?
71. RoleBinding vs ClusterRoleBinding?
72. What is ServiceAccount?
73. How does a Pod authenticate with Kubernetes?
74. How do you restrict user permissions?
75. How do you secure Kubernetes Secrets?
76. What is the principle of least privilege?

---

# 🔴 3 Years — Production Kubernetes

## Topics

* Production deployments
* Rolling updates
* Blue-Green deployment
* Canary deployment
* Resource management
* HPA
* Cluster Autoscaler
* Pod Disruption Budget
* Affinity/Anti-affinity
* Taints
* Tolerations
* Monitoring
* Logging
* Troubleshooting
* Helm
* EKS basics

## Questions

77. How do you deploy Spring Boot Microservices to Kubernetes?
78. How do you perform zero-downtime deployment?
79. Rolling vs Blue-Green deployment?
80. What is Canary deployment?
81. What is Helm?
82. Why do we use Helm?
83. What is a Helm Chart?
84. What is Pod Disruption Budget?
85. What is Node Affinity?
86. What is Pod Affinity?
87. What are Taints and Tolerations?
88. How do you prevent two replicas from running on the same Node?
89. How do you monitor Kubernetes?
90. How do you collect container logs?

---

# 🔥 Kubernetes Troubleshooting Questions

These are **very important for 3+ years and especially 5+ year interviews**.

### Scenario 1

**Pod is stuck in `Pending`. What will you check?**

Topics:

* Node resources
* CPU/memory requests
* Taints
* Node selectors
* Affinity
* PVC

---

### Scenario 2

**Pod is in `CrashLoopBackOff`. What will you check?**

```bash
kubectl logs <pod>
kubectl describe pod <pod>
```

Check:

* Application exception
* Configuration
* Environment variables
* Secret
* Database connection
* Memory
* Liveness probe

---

### Scenario 3

**Pod is `Running` but API isn't accessible.**

Check:

```text
Pod
 ↓
Service
 ↓
Ingress
 ↓
Load Balancer
```

Verify:

* Pod port
* Container port
* Service targetPort
* Service selector
* Ingress configuration
* Network policy

---

### Scenario 4

**Application is restarting frequently.**

Check:

* Liveness probe
* OOMKilled
* CPU limits
* Memory limits
* Application logs
* Startup time

---

### Scenario 5

**Spring Boot application cannot connect to MySQL.**

Check:

* Kubernetes Secret
* Database URL
* DNS
* NetworkPolicy
* Security Group
* RDS accessibility
* Port
* Credentials

---

# ⭐ Kubernetes Commands You Should Know

```bash
kubectl get pods
kubectl get nodes
kubectl get svc
kubectl get deployments

kubectl describe pod <pod>

kubectl logs <pod>

kubectl logs -f <pod>

kubectl exec -it <pod> -- /bin/sh

kubectl apply -f deployment.yaml

kubectl delete pod <pod>

kubectl scale deployment <name> --replicas=5

kubectl rollout status deployment/<name>

kubectl rollout undo deployment/<name>

kubectl get events

kubectl top pods

kubectl top nodes
```

---

# 📌 Kubernetes 0–3 Year Roadmap

| Level         | Main Topics                                                               |
| ------------- | ------------------------------------------------------------------------- |
| **0–1 Year**  | Cluster, Node, Pod, Container, Namespace, kubectl                         |
| **1–2 Years** | Deployment, ReplicaSet, Service, Ingress, ConfigMap, Secret, Storage      |
| **2–3 Years** | Probes, HPA, Networking, RBAC, Helm, Security, Troubleshooting            |
| **3+ Years**  | Production architecture, HA, Canary, Blue-Green, advanced scheduling, EKS |

## 🔥 For Your Java Developer Interview

Prioritize:

**Pod → Deployment → Service → Ingress → ConfigMap → Secret → Liveness/Readiness → HPA → Rolling Deployment → Helm → RBAC → Networking → Docker → EKS → Monitoring → Troubleshooting.**

For a **5+ year Java/Microservices interview**, be able to explain this complete flow:

**Git → Jenkins/CI → Docker → ECR → Kubernetes/EKS → Deployment → Pods → Service → Ingress/ALB → Spring Boot → Redis/MySQL/Kafka → CloudWatch/Prometheus/Grafana.**
