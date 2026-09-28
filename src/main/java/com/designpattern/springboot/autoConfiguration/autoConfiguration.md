# Spring Boot Auto-Configuration

**Auto-Configuration** is one of the main features of Spring Boot. It automatically configures Spring application components based on the **dependencies present in the classpath** and the application's configuration.

### Simple Example

Suppose you add:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Spring Boot detects the web-related dependencies and automatically configures things such as:

```text
spring-boot-starter-web
        ↓
Spring MVC detected
        ↓
Auto-Configuration
        ↓
DispatcherServlet
Embedded Tomcat
Jackson
MVC configuration
```

You don't need to manually configure all of these components.

---

## How Auto-Configuration Works

The main annotation is:

```java
@SpringBootApplication
```

It includes:

```java
@Configuration
@EnableAutoConfiguration
@ComponentScan
```

The important one for auto-configuration is:

```java
@EnableAutoConfiguration
```

Flow:

```text
Spring Boot Application
        ↓
@SpringBootApplication
        ↓
@EnableAutoConfiguration
        ↓
Find Auto-Configuration Classes
        ↓
Check Conditions
        ↓
Create Required Beans
```

### Example

If Spring Boot finds JPA and a database driver:

```text
JPA dependency
     +
MySQL driver
     +
Database configuration
        ↓
Hibernate/JPA Auto Configuration
        ↓
EntityManagerFactory
DataSource
TransactionManager
```

---

## Conditional Configuration

Auto-configuration doesn't blindly create everything.

Spring Boot uses conditions such as:

```java
@ConditionalOnClass
@ConditionalOnMissingBean
@ConditionalOnProperty
@ConditionalOnBean
```

For example:

```java
@Configuration
@ConditionalOnClass(DataSource.class)
public class MyAutoConfiguration {
    
}
```

Meaning:

> Configure this class only if `DataSource` is available on the classpath.

Another important condition:

```java
@ConditionalOnMissingBean
```

means:

> Create the default bean only if the user hasn't already defined one.

This allows you to **override Spring Boot's default configuration**.

---

## Example: DataSource

Suppose `application.properties` contains:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/test
spring.datasource.username=root
spring.datasource.password=password
```

And you have:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

Spring Boot can automatically configure:

```text
DataSource
    ↓
EntityManagerFactory
    ↓
Hibernate
    ↓
Transaction Manager
```

You don't need to manually create:

```java
@Bean
public DataSource dataSource() {
    // manual configuration
}
```

unless you need custom behavior.

---

## Can We Disable Auto-Configuration?

Yes.

For a specific configuration:

```java
@SpringBootApplication(
    exclude = {DataSourceAutoConfiguration.class}
)
public class Application {
}
```

Or:

```properties
spring.autoconfigure.exclude=\
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

---

## Auto-Configuration vs Component Scanning

These are different.

### Component Scanning

Finds **your application's beans**:

```java
@Service
@Repository
@Controller
@Component
```

### Auto-Configuration

Configures **Spring Boot's infrastructure** based on conditions:

```text
Dependencies
     ↓
Classpath detection
     ↓
Conditional configuration
     ↓
Spring beans
```

---

## Interview Example

**Interviewer:** How does Spring Boot know to configure Tomcat when you add `spring-boot-starter-web`?

**Answer:**

> `spring-boot-starter-web` brings Spring MVC and embedded Tomcat dependencies onto the classpath. Spring Boot's auto-configuration detects these classes and applies the relevant auto-configuration classes. Conditional annotations determine which beans should be created, so the embedded server and MVC infrastructure are configured automatically.

---

## ⭐ 30-Second Interview Answer

> **“Spring Boot Auto-Configuration automatically configures application components based on the dependencies available in the classpath and the application's properties. It is enabled through `@EnableAutoConfiguration`, which is included in `@SpringBootApplication`. Spring Boot uses conditional annotations such as `@ConditionalOnClass`, `@ConditionalOnMissingBean`, and `@ConditionalOnProperty` to decide which configurations should be applied. If we provide our own bean, Spring Boot can usually back off from its default configuration.”**

### ⭐ Important 5+ Year Follow-ups

1. How does `@EnableAutoConfiguration` work internally?
2. What is `@SpringBootApplication`?
3. What are conditional annotations?
4. What is `@ConditionalOnClass`?
5. What is `@ConditionalOnMissingBean`?
6. How can you exclude auto-configuration?
7. How do you see which auto-configurations were applied?
8. What is the difference between auto-configuration and component scanning?
9. How do you create custom auto-configuration?
10. What is the role of `spring-boot-autoconfigure`?
11. How does Spring Boot decide which auto-configuration to apply?
12. How would you debug an unexpected auto-configured bean?


# Spring Boot Auto-Configuration — Interview Answers

These are **important 3–7 year interview questions**. For a 5+ year interview, focus especially on the internal flow, conditional annotations, custom auto-configuration, and debugging.

---

## 1. How does `@EnableAutoConfiguration` work internally?

`@EnableAutoConfiguration` tells Spring Boot to automatically configure the application based on the dependencies available on the classpath.

```java
@EnableAutoConfiguration
```

Internally, Spring Boot:

```text
@EnableAutoConfiguration
        ↓
Auto-configuration import mechanism
        ↓
Find auto-configuration classes
        ↓
Evaluate conditions
        ↓
Apply matching configurations
        ↓
Create required Beans
```

For example, if you add:

```xml
spring-boot-starter-data-jpa
```

Spring Boot detects JPA/Hibernate and database-related classes and can configure:

```text
DataSource
EntityManagerFactory
TransactionManager
```

Modern Spring Boot versions use the auto-configuration imports mechanism, with entries in:

```text
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

### Interview answer

> `@EnableAutoConfiguration` enables Spring Boot's auto-configuration mechanism. Spring Boot discovers candidate auto-configuration classes, evaluates their conditions, and registers the configurations whose conditions match the application's classpath and properties.

---

# 2. What is `@SpringBootApplication`?

`@SpringBootApplication` is a convenience annotation that combines three important annotations:

```java
@SpringBootApplication
```

Equivalent conceptually to:

```java
@Configuration
@EnableAutoConfiguration
@ComponentScan
```

### Meaning

| Annotation                 | Purpose                         |
| -------------------------- | ------------------------------- |
| `@Configuration`           | Defines configuration/beans     |
| `@EnableAutoConfiguration` | Enables Boot auto-configuration |
| `@ComponentScan`           | Finds application components    |

Example:

```java
@SpringBootApplication
public class PaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
}
```

### Interview answer

> `@SpringBootApplication` is a convenience annotation that combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`. It allows Spring Boot to configure the application, discover application beans, and apply suitable auto-configurations.

---

# 3. What are Conditional Annotations?

Conditional annotations tell Spring:

> **Create this configuration or bean only when a particular condition is satisfied.**

Examples:

```java
@ConditionalOnClass
@ConditionalOnMissingBean
@ConditionalOnBean
@ConditionalOnProperty
@ConditionalOnWebApplication
@ConditionalOnMissingClass
```

Example:

```java
@Configuration
@ConditionalOnClass(PaymentClient.class)
public class PaymentAutoConfiguration {
}
```

This configuration is applied only when `PaymentClient` exists on the classpath.

### Why are they important?

They allow Spring Boot to provide **smart defaults** instead of blindly creating every bean.

---

# 4. What is `@ConditionalOnClass`?

`@ConditionalOnClass` activates configuration when a particular class is available on the classpath.

Example:

```java
@Configuration
@ConditionalOnClass(RedisTemplate.class)
public class RedisConfiguration {

}
```

If `RedisTemplate` exists:

```text
Redis dependency
      ↓
RedisTemplate found
      ↓
Condition = TRUE
      ↓
Configuration can be applied
```

If the Redis class isn't available:

```text
RedisTemplate not found
      ↓
Condition = FALSE
      ↓
Configuration not applied
```

### Interview answer

> `@ConditionalOnClass` is used to activate configuration only when the specified class is available on the application's classpath. It is commonly used by auto-configuration to detect whether a particular library or technology is available.

---

# 5. What is `@ConditionalOnMissingBean`?

It tells Spring Boot:

> **Create the default bean only if the application hasn't already defined one.**

Example:

```java
@Bean
@ConditionalOnMissingBean
public PaymentClient paymentClient() {
    return new DefaultPaymentClient();
}
```

If the user hasn't defined a `PaymentClient`:

```text
No PaymentClient bean
        ↓
Condition TRUE
        ↓
Default PaymentClient created
```

If the user defines:

```java
@Bean
public PaymentClient paymentClient() {
    return new CustomPaymentClient();
}
```

Then:

```text
PaymentClient already exists
        ↓
Condition FALSE
        ↓
Default bean not created
```

### Why is this important?

This is one of the mechanisms that allows Spring Boot's **default configuration to be overridden**.

### Interview answer

> `@ConditionalOnMissingBean` allows Spring Boot to create a default bean only when an application-defined bean of that type is not already present.

---

# 6. How can you exclude auto-configuration?

There are several ways.

### Method 1 — `exclude`

```java
@SpringBootApplication(
    exclude = {
        DataSourceAutoConfiguration.class
    }
)
public class Application {
}
```

### Method 2 — `excludeName`

```java
@SpringBootApplication(
    excludeName = {
        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
    }
)
```

### Method 3 — application properties

```properties
spring.autoconfigure.exclude=\
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

### Why would you exclude one?

For example, your application contains JPA dependencies but doesn't actually need a database.

Without proper configuration, Boot might attempt to configure a `DataSource`.

You can exclude:

```java
DataSourceAutoConfiguration.class
```

### Interview answer

> Auto-configuration can be excluded using the `exclude` attribute of `@SpringBootApplication` or `@EnableAutoConfiguration`, or using the `spring.autoconfigure.exclude` property.

---

# 7. How do you see which auto-configurations were applied?

The easiest way is to enable **debug logging**.

```properties
debug=true
```

Then start the application.

Spring Boot prints an **auto-configuration report** showing:

```text
Positive matches
Negative matches
Unconditional classes
```

For example:

```text
DataSourceAutoConfiguration
      ↓
Positive match
```

or:

```text
SomeAutoConfiguration
      ↓
Did not match
      ↓
@ConditionalOnClass missing
```

You can also use **Spring Boot Actuator** for runtime inspection and configuration-related endpoints where appropriate.

### Interview answer

> I normally enable `debug=true` to see Spring Boot's condition evaluation report. It shows which auto-configurations matched and which didn't, along with the condition that caused the decision.

---

# 8. Difference between Auto-Configuration and Component Scanning

This is a very common interview question.

### Component Scanning

Component scanning finds **your application's components**.

For example:

```java
@Service
@Repository
@Component
@Controller
@RestController
```

Flow:

```text
@ComponentScan
      ↓
Find application classes
      ↓
Register Beans
```

### Auto-Configuration

Auto-configuration provides **framework/library configuration** based on conditions.

```text
Dependencies
      ↓
Auto-configuration candidates
      ↓
Conditions
      ↓
Configuration/Beans
```

### Comparison

| Component Scanning             | Auto-Configuration                    |
| ------------------------------ | ------------------------------------- |
| Finds application components   | Configures framework features         |
| `@Service`                     | Database/web/security configuration   |
| `@Repository`                  | DataSource, MVC infrastructure, etc.  |
| `@Controller`                  | Usually based on classpath/properties |
| Controlled by `@ComponentScan` | Enabled by `@EnableAutoConfiguration` |

### Interview answer

> Component scanning discovers application-defined components such as services, repositories and controllers, while auto-configuration configures framework infrastructure based on classpath dependencies, properties and conditional rules.

---

# 9. How do you create custom Auto-Configuration?

For a reusable library, you can create your own auto-configuration.

Example:

```java
@AutoConfiguration
@ConditionalOnClass(PaymentClient.class)
public class PaymentAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PaymentClient paymentClient() {
        return new DefaultPaymentClient();
    }
}
```

Then register it in:

```text
src/main/resources/
META-INF/spring/
org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

File content:

```text
com.example.PaymentAutoConfiguration
```

Then another application can add your library as a dependency.

Spring Boot discovers the auto-configuration and applies it when the conditions match.

### Typical structure

```text
my-payment-starter
│
├── src/main/java
│   └── PaymentAutoConfiguration.java
│
└── src/main/resources
    └── META-INF
        └── spring
            └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

### Interview answer

> To create custom auto-configuration, I create an `@AutoConfiguration` class, use conditional annotations such as `@ConditionalOnClass` and `@ConditionalOnMissingBean`, and register the configuration in `AutoConfiguration.imports`. Then applications can activate it simply by adding the library dependency.

---

# 10. What is the role of `spring-boot-autoconfigure`?

`spring-boot-autoconfigure` is a core Spring Boot module containing many of Boot's **auto-configuration implementations**.

Conceptually:

```text
spring-boot
     +
spring-boot-autoconfigure
     ↓
Spring Boot automatic configuration
```

It contains auto-configuration for areas such as:

```text
Web
DataSource
JPA
MongoDB
Redis
Security
Messaging
Caching
Actuator-related infrastructure
```

The exact set depends on the Spring Boot version.

### Important distinction

`spring-boot-autoconfigure` contains the **mechanism and configurations**.

Starters such as:

```text
spring-boot-starter-web
spring-boot-starter-data-jpa
```

primarily make it convenient to bring in the dependencies needed for a particular capability.

### Interview answer

> `spring-boot-autoconfigure` provides Spring Boot's auto-configuration classes and supporting infrastructure. These configurations use conditional rules to configure framework features based on the application's dependencies and settings.

---

# 11. How does Spring Boot decide which auto-configuration to apply?

Spring Boot considers several things.

### 1. Dependencies on the classpath

Example:

```text
JPA + Hibernate
       ↓
JPA-related auto-configuration becomes a candidate
```

### 2. Conditions

For example:

```java
@ConditionalOnClass
@ConditionalOnMissingBean
@ConditionalOnProperty
```

### 3. Application properties

Example:

```properties
spring.datasource.url=...
```

### 4. Existing application beans

If you define your own bean, an auto-configuration using:

```java
@ConditionalOnMissingBean
```

may back off.

### Overall flow

```text
Application starts
       ↓
Find auto-configuration candidates
       ↓
Check classpath
       ↓
Check application properties
       ↓
Check existing beans
       ↓
Evaluate conditions
       ↓
Matching configurations applied
       ↓
Beans registered
```

### Interview answer

> Spring Boot selects auto-configurations based primarily on the application's classpath, configuration properties, existing beans, and conditional annotations. Only configurations whose conditions match are applied.

---

# 12. How would you debug an unexpected auto-configured bean?

This is a **very important production/interview scenario**.

Suppose you don't understand why a `DataSource` bean was created.

### Step 1 — Enable debug

```properties
debug=true
```

Restart the application.

Look at the **Condition Evaluation Report**.

You may see:

```text
DataSourceAutoConfiguration matched because:
- DataSource class found
- JDBC classes found
```

### Step 2 — Check dependencies

Look at:

```bash
mvn dependency:tree
```

or:

```bash
./mvnw dependency:tree
```

For Gradle:

```bash
./gradlew dependencies
```

You may discover that a transitive dependency brought in JDBC/JPA.

### Step 3 — Check application properties

Look for:

```properties
spring.datasource.*
spring.jpa.*
spring.autoconfigure.exclude
```

### Step 4 — Check existing beans

Search your project for:

```java
@Bean
@Component
@Configuration
```

and check whether another configuration is contributing the bean.

### Step 5 — Check auto-configuration conditions

Find the relevant auto-configuration and determine which condition matched.

### Production debugging flow

```text
Unexpected Bean
      ↓
debug=true
      ↓
Condition Evaluation Report
      ↓
Check dependency tree
      ↓
Check application.properties/yaml
      ↓
Check existing @Bean definitions
      ↓
Identify matching condition
      ↓
Override or exclude if required
```

### Interview answer

> I would first enable `debug=true` and inspect the condition evaluation report to determine why the auto-configuration matched. Then I would check the Maven or Gradle dependency tree, application properties, and existing bean definitions. Once I identify the matching condition, I can override the bean, change the configuration, or exclude the auto-configuration if appropriate.

---

# ⭐ Senior-Level Summary

Remember this flow for interviews:

```text
@SpringBootApplication
        |
        +-------------------+
        |                   |
        v                   v
@ComponentScan       @EnableAutoConfiguration
        |                   |
        v                   v
Application Beans    Auto-Configuration Candidates
                            |
                            v
                     Conditional Checks
                            |
                +-----------+-----------+
                |           |           |
             Classpath   Properties   Existing Beans
                |           |           |
                +-----------+-----------+
                            |
                            v
                   Matching Configuration
                            |
                            v
                       Spring Beans
```

### The 5 annotations/classes you should remember

```text
@SpringBootApplication
        ↓
@EnableAutoConfiguration
        ↓
@AutoConfiguration
        ↓
@ConditionalOnClass
@ConditionalOnMissingBean
```

**Most important 5+ year interview question:**

> **“If Spring Boot provides a default bean through auto-configuration, and I define my own bean of the same type, what happens?”**

Typical answer: **the auto-configuration can back off when its configuration uses `@ConditionalOnMissingBean`; your application-defined bean is then used instead.**
