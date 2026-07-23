# Introduction

Every Spring Boot application requires configuration.

Instead of hardcoding values like:

- Database URL
- Database Username
- Server Port
- Logging Level
- External Service URLs

Spring Boot stores them inside **application.yml** (or application.properties).

This makes the application configurable without changing the source code.

Example:

Without application.yml

```java
String url = "jdbc:mysql://localhost:3306/worksphere";
```
With application.yml

```
spring:
datasource:
url: jdbc:mysql://localhost:3306/worksphere
```

Spring injects the value automatically.

Benefits:

Environment independent
Easier deployment
Better maintainability
Better security
Cloud ready

---

# 2. What is application.yml?

application.yml is Spring Boot's central configuration file.

It stores all configurable properties required by the application.

Examples include:

- Database configuration
- Server configuration
- Logging configuration
- Cache configuration
- Security configuration
- Kafka configuration
- Eureka configuration
- API Gateway configuration

Instead of changing Java code, only configuration changes.

This follows the **Externalized Configuration** principle.


# 3. Why YAML instead of application.properties?


Spring Boot supports two formats.

application.properties

Example

```java
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3306/db
spring.datasource.username=root

application.yml

server:
  port: 8081

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db
    username: root

```
# YAML is preferred because:

- More readable
- Hierarchical
- Less repetitive
- Easier for large applications

Enterprise projects usually use YAML.




# 4. How Spring Boot Reads application.yml


When the application starts:

1. SpringApplication.run()
             
   ↓

2. Spring Boot loads application.yml

   ↓

3. Parses YAML

   ↓

4. Creates Environment Object

   ↓

5. Makes properties available to the entire application

   ↓

6. Beans consume values using:

- @Value
- @ConfigurationProperties
- Auto Configuration

Example:

```java
@Value("${server.port}")
private int port;

Spring injects the configured value automatically.

Spring Boot loads configuration before creating most beans, so those beans can use configuration values during initialization.
```

# 5. Configuration Priority

Spring Boot can receive configuration from multiple places.

Highest Priority

1. Command Line Arguments

   ↓

2. Environment Variables

   ↓

3. application.yml

   ↓

4. application.properties

   ↓

5. Default Values

Example

```bash
java -jar app.jar --server.port=9090

This overrides

server:
  port: 8081
```
Understanding this becomes especially important when we introduce Docker, Config Server, and Kubernetes, where environment-specific configuration is common.

# Employee Service - application.yml

Current Configuration

```yaml
spring:
  application:
    name: employee-service

  datasource:
    url: jdbc:mysql://localhost:3306/worksphere_employee
    username: root
    password: root

  jpa:
    hibernate:
      ddl-auto: update

    show-sql: true

    properties:
      hibernate:
        format_sql: true

server:
  port: 8081

department-service:
  base-url: http://localhost:8082

logging:
  level:
    root: INFO
    com.worksphere: DEBUG

  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

# 1. spring.application.name

```yaml
spring:
  application:
    name: employee-service
```

## Purpose

Defines the unique name of the Spring Boot application.

---

## Why do we need it?

Spring Boot itself can run without an application name.

However, in a microservices architecture, every service must have a unique identity.

Examples:

- employee-service
- department-service
- api-gateway
- config-server
- eureka-server

---

## How Spring Boot uses it

During startup Spring creates an Environment object.

The application name becomes available everywhere in the application.

Example:

```java
@Value("${spring.application.name}")
private String appName;
```

Spring automatically injects:

```
employee-service
```

---

## Why it became more important after Eureka

Before Eureka:

```
Employee Service

↓

http://localhost:8082
```

After Eureka:

```
Employee Service

↓

department-service

↓

Eureka

↓

Actual Server
```

Instead of calling

```
localhost:8082
```

we call

```
department-service
```

Eureka finds the correct instance.

---

## If removed

The application still starts.

But:

- Eureka registration becomes incorrect.
- Config Server cannot identify the application.
- Distributed tracing becomes difficult.
- Logging becomes harder.
- Monitoring tools lose the service identity.

---

## Enterprise Best Practice

Always choose meaningful names.

Examples

```
employee-service
```

```
payment-service
```

```
inventory-service
```

Avoid

```
app1
```

```
demo
```

```
test
```

---

## Interview Question

**Why is `spring.application.name` important in a microservices architecture?**

Answer:

It provides a unique identity for the service. Components like Eureka, Config Server, API Gateway, distributed tracing, and monitoring tools rely on this name to identify and communicate with the service.

# 2. spring.datasource

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/worksphere_employee
    username: root
    password: root
```

## Purpose

Defines how Spring Boot connects to the database.

Without these properties, Spring Boot does not know:

- Which database to use.
- Where the database is running.
- Which credentials should be used.

---

## datasource.url

```yaml
url: jdbc:mysql://localhost:3306/worksphere_employee
```

### Breakdown

```
jdbc
```

Java Database Connectivity protocol.

↓

```
mysql
```

Database type.

↓

```
localhost
```

Database server.

↓

```
3306
```

MySQL default port.

↓

```
worksphere_employee
```

Database name.

---

## datasource.username

Database username.

Example

```
root
```

Spring passes this to MySQL while opening a connection.

---

## datasource.password

Database password.

Spring uses this together with the username.

---

## How Spring Boot uses it

During startup:

```
application.yml

↓

DataSource AutoConfiguration

↓

HikariCP Connection Pool

↓

Database Connection

↓

Ready
```

Notice that **Spring Boot doesn't connect directly to MySQL**.

It creates a **connection pool** (HikariCP by default) and your application borrows connections from the pool.

---

## Why Connection Pooling?

Imagine 100 users access the application.

Without pooling:

```
100 Requests

↓

100 Database Connections

↓

Slow
```

With HikariCP:

```
100 Requests

↓

10 Connections Reused

↓

Fast
```

This is why Spring Boot uses HikariCP by default.

---

## If removed

If no embedded database (H2, HSQL, Derby) is available, the application fails during startup because Spring cannot configure a DataSource.

---

## Enterprise Best Practice

Never hardcode production credentials.

Instead use:

- Environment Variables
- Config Server
- Vault
- Kubernetes Secrets

We'll implement better approaches later in WorkSphere.

---

## Interview Question

**Why does Spring Boot use HikariCP by default?**

Answer:

HikariCP is a high-performance JDBC connection pool. It reduces the cost of opening database connections by reusing them, resulting in better performance and scalability.

# 3. spring.jpa

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update

    show-sql: true

    properties:
      hibernate:
        format_sql: true
```
# 3.1 hibernate.ddl-auto

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

## Purpose

Controls how Hibernate manages the database schema.

Whenever the application starts, Hibernate checks your Entity classes and compares them with the database.

---

## How it works

Application Starts

↓

Hibernate Reads Entities

↓

Reads Existing Database Tables

↓

Compares Both

↓

Executes Required SQL

---

Example

Suppose Employee Entity has:

```java
private String email;
```

Database

```
employee
--------
id
first_name
last_name
salary
```

Application Starts

↓

Hibernate detects

```
email
```

is missing

↓

Automatically executes

```sql
ALTER TABLE employee
ADD email VARCHAR(255);
```

No manual SQL required.

---

## Available Options

### none

```
Does nothing.
```

Hibernate ignores schema changes.

---

### validate

Checks whether tables match Entities.

If anything is missing

↓

Application fails.

Used mostly in Production.

---

### update

Current option.

Creates missing tables.

Creates missing columns.

Never deletes existing tables.

Safest option during development.

---

### create

Deletes existing tables.

Creates new tables.

Every restart starts with an empty database.

---

### create-drop

Same as create

+

Drops all tables when application stops.

Mostly used in testing.

---

## Why we use update

During development we frequently change Entities.

Instead of writing SQL every time

Hibernate updates the schema automatically.

---

## Enterprise Best Practice

Development

```
update
```

Testing

```
create-drop
```

Production

```
validate
```

Database changes are handled using migration tools like:

- Flyway
- Liquibase

instead of Hibernate.

---

## If removed

Hibernate defaults to

```
none
```

New tables won't be created automatically.

---

## Interview Question

Why shouldn't we use ddl-auto=update in Production?

Answer:

Because Hibernate may make unintended schema changes. Production databases should be managed using controlled migration tools like Flyway or Liquibase.

# 3.2 show-sql

```yaml
show-sql: true
```

## Purpose

Prints every SQL statement executed by Hibernate.

---

Without show-sql

```
Application Running...
```

Nothing is visible.

---

With show-sql

```
select
    e.id,
    e.first_name,
    e.salary
from employee e
where e.id=?
```

Now developers know exactly what Hibernate executes.

---

## Why useful?

Useful for

- Debugging
- Learning Hibernate
- Performance Analysis

---

## If removed

Queries still execute.

They simply won't be printed.

---

## Production Recommendation

Usually

```
false
```

because printing every query affects performance and generates huge log files.

# 3.3 format_sql

```yaml
hibernate:
    format_sql: true
```

## Purpose

Formats SQL into a readable structure.

---

Without

```
select id,first_name,last_name,email,salary from employee where id=1
```

---

With

```
select
    id,
    first_name,
    last_name,
    email,
    salary
from
    employee
where
    id=1
```

Much easier to read.

---

## Does it affect execution?

No.

Only formatting changes.

Database receives exactly the same SQL.

---

## Production Recommendation

Usually disabled because SQL logging itself is disabled.

# 4. server.port

```yaml
server:
  port: 8081
```

## Purpose

Defines the port on which the embedded Tomcat server listens.

---

How it works

Application Starts

↓

Embedded Tomcat Starts

↓

Listens on Port 8081

↓

Receives HTTP Requests

---

Without

```
8081
```

Spring Boot defaults to

```
8080
```

---

Why are we using 8081?

Because

Employee Service

↓

8081

Department Service

↓

8082

Eureka

↓

8761

Gateway

↓

8080

Each microservice must run on a different port when running locally.

---

Production

Ports are often assigned by Docker, Kubernetes, or cloud platforms rather than being fixed in the application.

# 5. department-service.base-url

```yaml
department-service:
    base-url: http://localhost:8082
```

## Purpose

Stores the Department Service endpoint used by RestClient.

---

Before

```java
RestClient.builder()
.baseUrl("http://localhost:8082")
```

Hardcoded.

---

Now

```yaml
department-service:
    base-url: http://localhost:8082
```

↓

```java
@Value("${department-service.base-url}")
```

↓

Injected automatically.

---

Why is this better?

No Java code changes.

Only configuration changes.

---

Different Environments

Development

```
localhost:8082
```

Testing

```
test.company.com
```

Production

```
department.company.com
```

Only YAML changes.

---

Current Project

RestClient still uses this property.

---

Future

After introducing Eureka completely

```
department-service
```

will replace

```
localhost:8082
```

Eventually this property will no longer be needed because service discovery will resolve the service dynamically.

# 6. logging.level

```yaml
logging:
  level:
      root: INFO
      com.worksphere: DEBUG
```

## Purpose

Controls how much logging is generated.

---

Available Levels

TRACE

↓

DEBUG

↓

INFO

↓

WARN

↓

ERROR

Higher levels produce fewer log messages.

---

root

Applies to every package.

```
INFO
```

means

INFO

WARN

ERROR

will be printed.

---

com.worksphere

Only our application package.

```
DEBUG
```

means

DEBUG

INFO

WARN

ERROR

will be printed.

---

Example

```java
log.debug("Employee Found");
```

This prints only because

```
com.worksphere=DEBUG
```

---

Why useful?

Third-party libraries remain quiet while our application logs contain more details for debugging.

# 7. logging.pattern.console

```yaml
console:
"%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

## Purpose

Defines the format of every log printed to the console.

---

Example Output

```
2026-07-21 14:32:51

[main]

INFO

EmployeeServiceImpl

Employee Created Successfully
```

---

Pattern Breakdown

%d

Current Date & Time

---

[%thread]

Current Thread

Example

```
main
```

---

%-5level

Log Level

```
INFO
```

```
DEBUG
```

```
ERROR
```

---

%logger{36}

Class Name

Example

```
EmployeeServiceImpl
```

---

%msg

Actual Log Message

---

%n

New Line

---

Why customize logs?

Enterprise systems generate millions of log entries.

A structured format makes it easier to:

- Search
- Debug
- Monitor
- Integrate with tools like ELK, Splunk, or Grafana.

# Department Service application.yml

The Department Service shares most of its configuration with the Employee Service. Therefore, only the properties that differ are explained below.

Current Configuration

```yaml
spring:
  application:
    name: department-service

  datasource:
    url: jdbc:mysql://localhost:3306/worksphere_department

server:
  port: 8082
```
spring.application.name

Unlike Employee Service, this service registers itself as:

department-service

This unique name is used by:

Eureka Server
API Gateway
Service Discovery
datasource.url

Employee Service

worksphere_employee

Department Service

worksphere_department

Each microservice owns its own database.

This follows the Database per Service pattern.

Benefits:

Loose coupling
Independent deployment
Independent scaling
Better fault isolation
server.port

Employee Service

8081

Department Service

8082

Both services run simultaneously on different ports during local development.


---
# Eureka Server application.yml

The Eureka Server is the central registry of our microservices architecture.

Unlike Employee Service and Department Service, the Eureka Server does not contain any business logic or connect to a database.

Its primary responsibility is to maintain a registry of all running microservices so that they can discover and communicate with each other dynamically.

Current Configuration

```yaml
spring:
  application:
    name: eureka-server

server:
  port: 8761

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```
## spring.application.name

```yaml
spring:
  application:
    name: eureka-server
```

### Purpose

Provides the identity of the Eureka Server.

Unlike other services, this application acts as the Service Registry instead of a business microservice.

Application Name

```
eureka-server
```

This name helps identify the application during startup, logging, monitoring, and future cloud deployments.

---

### Why is it required?

Every Spring Boot application should have a unique application name.

Although the Eureka Server does not register itself, assigning an application name keeps the project consistent and simplifies future monitoring and configuration.


## server.port

```yaml
server:
  port: 8761
```

### Purpose

Defines the port on which the Eureka Dashboard runs.

Current Local Ports

```
API Gateway         → 8080

Employee Service    → 8081

Department Service  → 8082

Eureka Server       → 8761
```

Once the application starts, the dashboard becomes available at

```
http://localhost:8761
```

This dashboard displays every registered microservice along with its current status.

---

### Why 8761?

8761 is the commonly used default port for Netflix Eureka.

Although any available port can be used, most enterprise projects retain 8761 for consistency.

## eureka.client.register-with-eureka

```yaml
register-with-eureka: false
```

### Purpose

Controls whether this application should register itself with another Eureka Server.

Current Value

```
false
```

---

### Why?

The Eureka Server itself is the registry.

It does not need to register as a client.

Architecture

```
Employee Service
        │
        ▼
Department Service
        │
        ▼
   Eureka Server
```

If this property were set to

```
true
```

the server would attempt to register itself with another Eureka Server, which is unnecessary in our single-server setup.

---

### Enterprise Usage

Single Eureka Server

```
register-with-eureka=false
```

Multiple Eureka Servers (High Availability)

Each Eureka Server may register with the others for replication.

## eureka.client.fetch-registry

```yaml
fetch-registry: false
```

### Purpose

Determines whether this application should download the registry of available services.

Current Value

```
false
```

---

### Why?

The Eureka Server already owns the registry.

It does not need to download it from another server.

Only client applications such as Employee Service, Department Service, and API Gateway fetch the registry.

---

### Enterprise Usage

Single Eureka Server

```
fetch-registry=false
```

Clustered Eureka Servers

This may be enabled to synchronize registry information between servers.

# Summary

The Eureka Server acts as the central directory of all running microservices.

Unlike business services, it:

- Does not expose business APIs
- Does not connect to a database
- Does not store application data
- Maintains only the service registry

All client applications register themselves with the Eureka Server during startup.

This removes the need for hardcoded service URLs and enables dynamic service discovery.

# Interview Questions

### Why do we use Eureka Server?

To enable dynamic service discovery between microservices.

---

### Why is register-with-eureka set to false?

Because the Eureka Server is itself the registry and does not need to register with another Eureka Server.

---

### Why is fetch-registry set to false?

Because the Eureka Server already owns the registry and therefore does not need to fetch it.

---

### What problem does Eureka solve?

It eliminates hardcoded service URLs and allows microservices to discover each other dynamically.

# API Gateway application.yml

The API Gateway acts as the single entry point for all client requests.

Instead of clients directly communicating with individual microservices, every request first reaches the API Gateway. The gateway then forwards the request to the appropriate microservice.

Current Configuration

```yaml
spring:
  application:
    name: api-gateway

  cloud:
    gateway:
      server:
        webmvc:
          discovery:
            locator:
              enabled: true
              lower-case-service-id: true

server:
  port: 8080

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

## spring.application.name

```yaml
spring:
  application:
    name: api-gateway
```

### Purpose

Provides the identity of the API Gateway.

Application Name

```
api-gateway
```

Like every microservice, the gateway registers itself with Eureka using this name.

This allows:

- Service monitoring
- Service discovery
- Future cloud deployment
- Centralized configuration

---

### Why is it required?

Every Spring Boot application participating in the microservices ecosystem should have a unique application name.

## server.port

```yaml
server:
  port: 8080
```

### Purpose

Defines the port on which the API Gateway listens for incoming client requests.

Current Local Ports

```
API Gateway        → 8080

Employee Service   → 8081

Department Service → 8082

Eureka Server      → 8761
```

Instead of calling

```
http://localhost:8081
```

or

```
http://localhost:8082
```

clients communicate only with

```
http://localhost:8080
```

The API Gateway routes the request internally to the correct service.

## spring.cloud.gateway.server.webmvc.discovery.locator.enabled

```yaml
enabled: true
```

### Purpose

Enables automatic route generation using Eureka.

Without this property, every route would need to be configured manually.

Instead of

```
Employee Service

↓

localhost:8081
```

the gateway simply asks Eureka:

```
Where is employee-service running?
```

Eureka returns the current service instance.

---

### Benefits

- Automatic routing
- No hardcoded URLs
- Supports multiple service instances
- Easier maintenance

## lower-case-service-id

```yaml
lower-case-service-id: true
```

### Purpose

Converts all Eureka service names to lowercase when generating routes.

Example

Registered Service

```
EMPLOYEE-SERVICE
```

Gateway Route

```
/employee-service/**
```

instead of

```
/EMPLOYEE-SERVICE/**
```

---

### Why?

Lowercase URLs are easier to read and are the common REST API convention.


## eureka.client.service-url.defaultZone

```yaml
defaultZone: http://localhost:8761/eureka/
```

### Purpose

Specifies the location of the Eureka Server.

During startup, the API Gateway:

1. Connects to Eureka.
2. Registers itself.
3. Downloads the list of available microservices.
4. Uses that list to route incoming requests.

Without this property, the gateway would not know where the service registry is located.

# Request Flow

Without API Gateway

```
Client
   │
   ├──► Employee Service
   │
   └──► Department Service
```

Client needs to know every service URL.

---

With API Gateway

```
                Client
                   │
                   ▼
             API Gateway
                   │
        ┌──────────┴──────────┐
        ▼                     ▼
Employee Service      Department Service
```

The client only knows one URL.

The gateway decides where to forward the request.

# Why API Gateway?

Without API Gateway

Problems

- Client must know every service URL.
- Multiple endpoints to manage.
- Difficult authentication.
- Difficult logging.
- Difficult monitoring.
- Hard to scale.

---

With API Gateway

Benefits

- Single Entry Point
- Centralized Routing
- Authentication
- Authorization
- Logging
- Rate Limiting
- Load Balancing
- Security
- Monitoring

# Enterprise Benefits

In large organizations, an API Gateway becomes the front door of the system.

Every request passes through the gateway before reaching any backend service.

This allows:

- JWT Authentication
- OAuth2 Integration
- Request Validation
- Response Transformation
- API Versioning
- Circuit Breakers
- Rate Limiting
- Distributed Tracing
- Metrics Collection

Almost every enterprise microservices architecture includes an API Gateway.

# Interview Questions

### Why do we use an API Gateway?

To provide a single entry point for all client requests and centralize routing, security, logging, and monitoring.

---

### What happens if there is no API Gateway?

Clients must communicate directly with every microservice and maintain the URLs of all services.

---

### Why do we enable discovery.locator?

It automatically creates routes for all services registered with Eureka, eliminating the need for manual route configuration.

---

### What is the advantage of lower-case-service-id?

It creates clean, consistent, and REST-friendly URLs.

---

### Does the API Gateway contain business logic?

No.

The gateway only forwards requests and applies cross-cutting concerns such as authentication, logging, routing, and monitoring.

