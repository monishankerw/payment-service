# Jenkins Interview Topics & Questions — 0 to 5 Years

For your **5+ year Java + Spring Boot + Microservices** profile, focus on Jenkins as a **CI/CD automation tool**: build, test, SonarQube, Docker, deployment, rollback, and production troubleshooting.

---

# 🟢 0–1 Year — Jenkins Fundamentals

## Topics

* What is Jenkins?
* CI — Continuous Integration
* CD — Continuous Delivery
* CD — Continuous Deployment
* Jenkins Controller
* Jenkins Agent
* Job
* Build
* Workspace
* Plugin
* Credentials
* Jenkins Dashboard
* Build history

## Questions

1. What is Jenkins?
2. Why do we use Jenkins?
3. What is CI/CD?
4. Continuous Delivery vs Continuous Deployment?
5. What is a Jenkins Job?
6. What is a Jenkins Build?
7. What is Jenkins Workspace?
8. What is a Jenkins Plugin?
9. What is Jenkins Controller?
10. What is a Jenkins Agent?
11. Controller vs Agent?
12. Why do we use Jenkins Agents?
13. What are Jenkins Credentials?
14. How do you configure Git in Jenkins?

---

# 🟢 1–2 Years — Jenkins + Java

## Topics

* Git integration
* Maven
* Gradle
* JDK
* Build
* Unit tests
* Artifacts
* Build triggers
* Git webhook
* Environment variables

## Questions

15. How do you create a Jenkins pipeline for a Spring Boot project?
16. How do you connect Jenkins with GitHub/GitLab?
17. How does Jenkins trigger after a Git push?
18. What is a webhook?
19. How do you build a Maven project?
20. How do you run unit tests?
21. How do you generate a JAR?
22. Where is the JAR stored?
23. What are Jenkins environment variables?
24. How do you pass parameters to a Jenkins build?
25. How do you schedule a Jenkins job?
26. What is Jenkins Poll SCM?

### Typical Flow

```text id="4n2x3j"
Developer
    ↓
Git Push
    ↓
Webhook
    ↓
Jenkins
    ↓
Maven/Gradle Build
    ↓
Unit Tests
    ↓
JAR
```

---

# 🟡 2–3 Years — Jenkins Pipeline

## Topics

* Pipeline
* Declarative Pipeline
* Scripted Pipeline
* Jenkinsfile
* Stages
* Steps
* Agent
* Environment
* Parameters
* Post
* `when`
* Credentials
* Artifacts

## Questions

27. What is Jenkins Pipeline?
28. What is Jenkinsfile?
29. Declarative vs Scripted Pipeline?
30. What is a Stage?
31. What is a Step?
32. What is `agent`?
33. What is `environment`?
34. What is `parameters`?
35. What is `post`?
36. What is `when`?
37. How do you skip a stage conditionally?
38. How do you archive artifacts?
39. How do you handle pipeline failures?
40. How do you use credentials inside Jenkinsfile?

### Basic Jenkinsfile

```groovy id="g4j8xk"
pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/example/project.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
}
```

---

# 🟡 2–3 Years — Jenkins + Docker

## Topics

* Docker build
* Docker image
* Docker Registry
* Docker Hub
* AWS ECR
* Image tagging
* Push image
* Pull image
* Container deployment

## Questions

41. How do you build a Docker image using Jenkins?
42. How do you tag a Docker image?
43. How do you push an image to ECR?
44. How do you authenticate Jenkins with ECR?
45. How do you deploy a Docker container from Jenkins?
46. How do you handle Docker build failures?
47. Why should image tags not always be `latest`?
48. How do you version Docker images?

### Pipeline

```text id="kwm0gz"
Git
 ↓
Jenkins
 ↓
Maven Build
 ↓
Unit Test
 ↓
Docker Build
 ↓
Docker Image
 ↓
ECR
 ↓
ECS / Kubernetes
```

---

# 🟠 3–4 Years — CI/CD Pipeline

## Topics

* Checkout
* Build
* Test
* SonarQube
* Security Scan
* Docker Build
* Image Push
* Deployment
* Smoke Test
* Approval
* Rollback
* Notifications

## Questions

49. Design a complete CI/CD pipeline.
50. Where would you run unit tests?
51. Where would you run SonarQube?
52. Where would you perform security scanning?
53. How do you deploy to DEV?
54. How do you deploy to UAT?
55. How do you deploy to Production?
56. How do you add manual approval?
57. How do you rollback a failed deployment?
58. How do you send Slack/email notifications?
59. How do you prevent two deployments from running simultaneously?
60. How do you maintain build history?

### Production Pipeline

```text id="1wzvtt"
Git Push
   ↓
Checkout
   ↓
Compile
   ↓
Unit Tests
   ↓
SonarQube
   ↓
Package JAR
   ↓
Docker Build
   ↓
Security Scan
   ↓
Push → ECR
   ↓
Deploy → ECS/EKS
   ↓
Smoke Test
   ↓
Production
```

---

# 🔴 4–5 Years — Jenkins Production

## Topics

* Shared Libraries
* Pipeline as Code
* Multibranch Pipeline
* Parallel stages
* Manual approval
* Credentials
* Secret management
* Agent management
* Distributed builds
* Blue-Green deployment
* Canary deployment
* Rollback
* Deployment strategies
* Jenkins security
* Backup
* Monitoring

## Questions

61. What is Pipeline as Code?
62. What is Multibranch Pipeline?
63. What are Jenkins Shared Libraries?
64. Why use Shared Libraries?
65. How do you run stages in parallel?
66. How do you restrict production deployment?
67. How do you implement manual approval?
68. How do you manage secrets securely?
69. How do you prevent secrets from appearing in logs?
70. How do you manage multiple Jenkins Agents?
71. How do you scale Jenkins?
72. How do you back up Jenkins?
73. How do you recover Jenkins after failure?
74. How do you implement zero-downtime deployment?
75. Blue-Green vs Canary deployment?
76. How do you implement rollback?

---

# 🔥 Scenario-Based Jenkins Questions

## 1. Jenkins build suddenly fails

```text
Git
 ↓
Jenkins ❌
```

### What will you check?

* Console output
* Git connection
* Maven/Gradle
* JDK
* Environment variables
* Dependency download
* Credentials
* Agent availability

---

## 2. Build works locally but fails in Jenkins

### Possible reasons

* Different Java version
* Different Maven version
* Missing environment variable
* Missing credentials
* Different OS
* Dependency/cache issue
* File permission
* Network access

### Question

**How would you troubleshoot it?**

---

# 3. Jenkins Cannot Clone Git Repository

Check:

```text id="n4q9qj"
Git URL
Credentials
SSH Key / Token
Network
Repository permissions
Branch
```

---

# 4. Docker Build Works Locally but Fails in Jenkins

Check:

* Docker installed on Agent
* Docker permissions
* Docker daemon
* Dockerfile
* Build context
* Disk space
* Registry authentication

---

# 5. Jenkins Cannot Push Docker Image to ECR

Check:

* AWS credentials
* IAM permissions
* ECR repository
* AWS Region
* Docker login
* Image tag

---

# 6. Production Deployment Failed

### Questions

* How do you stop the deployment?
* How do you rollback?
* How do you identify the failed version?
* How do you verify the previous version?
* How do you prevent the same issue next time?

---

# 7. Two Developers Trigger Production Deployment

### Question

How do you prevent concurrent production deployments?

Possible solutions:

* Disable concurrent builds
* Lockable Resources plugin
* Manual approval
* Deployment pipeline controls

---

# 8. Jenkins Agent Goes Down

### Questions

* What happens to running builds?
* How do you configure multiple agents?
* How do you make CI highly available?
* How do you route builds to available agents?

---

# 9. Pipeline Takes 40 Minutes

### How would you optimize?

Consider:

* Parallel testing
* Dependency caching
* Docker layer caching
* Smaller Docker images
* Incremental builds
* Faster agents
* Avoid unnecessary stages
* Parallel independent services

---

# 10. Secret Appears in Jenkins Logs

### Questions

* What is the security risk?
* How do you remove it?
* How should secrets be stored?
* How do Jenkins Credentials work?
* Could AWS Secrets Manager be used?

**Never hardcode:**

```groovy
AWS_SECRET = "xxxxxxxx"
```

Use Jenkins Credentials or an appropriate secret-management system.

---

# ⭐ Jenkins + Spring Boot Interview Project

For your profile, be ready to explain this pipeline:

```text id="n8g4px"
             Developer
                 |
                 ↓
          GitHub / GitLab
                 |
              Webhook
                 |
                 ↓
             Jenkins
                 |
       +---------+---------+
       |         |         |
    Compile    Test    SonarQube
       |         |         |
       +---------+---------+
                 |
              Maven
                 |
             Spring Boot
                 |
             Docker Build
                 |
                 ↓
                ECR
                 |
          ECS / Kubernetes
                 |
          Spring Boot App
                 |
       +---------+---------+
       |         |         |
      RDS      Redis     Kafka
```

---

# 📌 Jenkins 0–5 Year Roadmap

| Experience | Main Topics                                                             |
| ---------- | ----------------------------------------------------------------------- |
| **0–1**    | Jenkins, CI/CD, Jobs, Builds, Plugins, Controller/Agent                 |
| **1–2**    | Git, Maven, Webhooks, Artifacts, Parameters                             |
| **2–3**    | Jenkinsfile, Pipeline, Docker, ECR                                      |
| **3–4**    | Complete CI/CD, SonarQube, deployment, rollback                         |
| **4–5**    | Shared Libraries, Multibranch, HA, security, production troubleshooting |

## 🔥 For your 5+ year Java interview, master these first

**Jenkins → CI/CD → Git Webhook → Jenkinsfile → Declarative Pipeline → Maven/Gradle → Unit Testing → SonarQube → Docker → ECR → ECS/EKS → Credentials → Secrets → Deployment → Blue-Green/Canary → Rollback → Production Troubleshooting.**
