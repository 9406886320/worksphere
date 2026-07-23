# Service Communication

## Introduction

In a microservices architecture, every service is designed to perform a specific business function and owns its own database.

Unlike a monolithic application, where different modules communicate through direct method calls within the same application, microservices run as independent applications on different ports or even different servers.

Because of this separation, one microservice cannot directly call another service's methods or access another service's database.

Instead, services communicate through well-defined APIs over the network.

WorkSphere follows this architecture by separating employee-related operations and department-related operations into different microservices.

- Employee Service manages employee information.
- Department Service manages department information.

When Employee Service needs department details, it sends an HTTP request to Department Service instead of accessing its database directly.

This communication is known as **Service Communication**.

---

# Service Communication in WorkSphere

Our current architecture is shown below.

```
                Client
                   │
                   ▼
             API Gateway
                   │
         ┌─────────┴─────────┐
         ▼                   ▼
Employee Service     Department Service
         │                   │
         ▼                   ▼
 Employee DB          Department DB
```

Each service is independently developed, deployed, and maintained.

Communication between these services happens through REST APIs using OpenFeign and Eureka Service Discovery.

---

# Why is Service Communication Important?

Consider the following example.

Suppose the client requests

```
GET /api/v1/employees/1
```

Employee Service can retrieve

- Employee ID
- First Name
- Last Name
- Email
- Salary

from its own database.

However,

the department information is stored in Department Service.

Employee Service cannot access the Department database directly.

Instead, it sends a request to Department Service.

```
Employee Service

        │
        ▼

Department Service

        │
        ▼

Department Database
```

Department Service returns the required information.

Employee Service combines both responses into a single object and returns it to the client.

Without service communication, this would not be possible.

---

# Characteristics of Service Communication

Service communication in a microservices architecture should have the following characteristics.

- Loose Coupling
- Independent Deployment
- Platform Independence
- Scalability
- Fault Isolation
- Standard Communication Protocols
- Service Discovery
- Security

These characteristics make microservices more flexible and maintainable than traditional monolithic applications.

---

# Objectives of Service Communication

The primary objectives are

- Allow services to exchange information.
- Keep services independent.
- Avoid direct database access between services.
- Support scalability.
- Enable cloud-native deployment.
- Improve maintainability.
- Build reusable business services.

WorkSphere follows all of these principles while designing communication between its services.

# Types of Service Communication

Microservices can communicate with each other using different communication patterns.

The choice of communication depends on the business requirements, response time expectations, scalability, and reliability of the system.

Broadly, service communication is classified into two types:

1. Synchronous Communication
2. Asynchronous Communication

WorkSphere currently uses **Synchronous Communication**, while **Asynchronous Communication** will be introduced later using Kafka.

---

# 1. Synchronous Communication

## What is Synchronous Communication?

In synchronous communication, one service sends a request to another service and waits for the response before continuing its execution.

The caller cannot proceed until it receives a response or the request times out.

```
Employee Service

        │
        ▼

Department Service

        │
        ▼

Response

        │
        ▼

Employee Service continues
```

This is similar to making a phone call.

You ask a question and wait until the other person answers before continuing the conversation.

---

# Synchronous Communication in WorkSphere

Currently, Employee Service communicates with Department Service using synchronous REST APIs.

Example

```
Client

↓

Employee Service

↓

Department Service

↓

Department Database

↓

Department Service

↓

Employee Service

↓

Client
```

Employee Service waits until Department Service returns the department details.

Only then is the final response returned to the client.

---

# Advantages of Synchronous Communication

✔ Easy to understand

✔ Simple implementation

✔ Immediate response

✔ Suitable for CRUD operations

✔ Easy debugging

✔ Simple request-response model

---

# Disadvantages of Synchronous Communication

❌ Calling service must wait for the response.

❌ Increased response time.

❌ If one service is unavailable, the calling service may also fail.

❌ High dependency between services.

❌ Network latency directly affects user response time.

---

# Examples of Synchronous Communication

Examples include:

- REST APIs
- OpenFeign
- RestClient
- WebClient (when used synchronously)
- gRPC (Request-Response)

WorkSphere currently uses:

- OpenFeign
- REST APIs
- Eureka Service Discovery

---

# 2. Asynchronous Communication

## What is Asynchronous Communication?

In asynchronous communication, the calling service sends a message and immediately continues its work.

It does not wait for the receiving service to process the request.

Instead of direct communication, a message broker is used.

```
Producer

↓

Message Broker

↓

Consumer
```

The producer and consumer operate independently.

---

# Real-Life Example

Think of sending an email.

You send the email and continue your work immediately.

You do not wait for the recipient to read it.

The recipient reads it whenever they are available.

Asynchronous communication works in a similar way.

---

# Asynchronous Communication Architecture

```
Employee Service

↓

Kafka

↓

Notification Service
```

Employee Service publishes an event.

Kafka stores the event.

Notification Service processes it later.

Employee Service does not wait.

---

# Advantages of Asynchronous Communication

✔ Loose coupling

✔ Better scalability

✔ High throughput

✔ Improved fault tolerance

✔ Faster user response

✔ Better performance under heavy load

---

# Disadvantages of Asynchronous Communication

❌ More complex architecture

❌ Eventual consistency

❌ More difficult debugging

❌ Requires a message broker

❌ Event ordering may require additional handling

---

# Examples of Asynchronous Communication

Popular technologies include:

- Apache Kafka
- RabbitMQ
- ActiveMQ
- Amazon SQS
- Google Pub/Sub

WorkSphere will introduce **Apache Kafka** in a later phase of the project.

---

# Why Does WorkSphere Currently Use Synchronous Communication?

At the current stage of the project, users expect an immediate response.

For example:

```
Get Employee

↓

Get Department

↓

Return Combined Response
```

The client cannot receive the employee details until the department information is available.

Therefore, synchronous communication is the appropriate choice.

---

# Where Will WorkSphere Use Asynchronous Communication?

In future versions, WorkSphere will introduce Kafka for operations that do not require an immediate response.

Examples include:

- Sending email notifications
- Audit logging
- Activity tracking
- Event publishing
- Report generation
- Analytics
- Notification services

These operations can execute independently without delaying the user's request.

---

# Synchronous vs Asynchronous Communication

| Synchronous | Asynchronous |
|-------------|--------------|
| Waits for response | Does not wait |
| Request-Response | Event Driven |
| Simpler | More Complex |
| Immediate result | Eventual processing |
| REST APIs | Kafka, RabbitMQ |
| Higher coupling | Lower coupling |
| Used for CRUD | Used for background processing |

---

# Which Approach Does WorkSphere Use?

Current Implementation

```
Employee Service

↓

OpenFeign

↓

Department Service
```

Communication Type

✔ Synchronous

Future Implementation

```
Employee Service

↓

Kafka

↓

Notification Service
```

Communication Type

✔ Asynchronous

This combination is commonly used in enterprise microservice applications.

---

# Interview Questions

### What is synchronous communication?

Synchronous communication is a request-response model where the calling service waits for the response before continuing execution.

---

### What is asynchronous communication?

Asynchronous communication allows a service to send a message and continue processing without waiting for the receiver.

---

### Which communication model does WorkSphere currently use?

WorkSphere currently uses synchronous communication through REST APIs, OpenFeign, and Eureka Service Discovery.

---

### Why didn't we use Kafka initially?

The current business operations require immediate responses. Kafka is more suitable for background processing and event-driven workflows, which will be introduced in future phases.

---

### When should synchronous communication be used?

When the client requires an immediate response, such as CRUD operations, authentication, or retrieving related business data.

---

### When should asynchronous communication be used?

When tasks can execute independently without blocking the user's request, such as notifications, logging, analytics, or report generation.

---

# Summary

Microservices communicate using either synchronous or asynchronous communication.

WorkSphere currently relies on synchronous REST communication because the application requires immediate responses between Employee Service and Department Service.

As the project evolves, asynchronous communication using Kafka will be introduced for background processing, making the architecture more scalable and event-driven.

# RestClient Implementation in WorkSphere

## Introduction

When WorkSphere was initially developed, the Employee Service needed to retrieve department information from the Department Service.

At that time, we had not yet introduced Spring Cloud OpenFeign or Eureka Service Discovery.

To enable communication between the two services, we used **Spring RestClient**, which is Spring Framework's modern synchronous HTTP client.

RestClient allowed Employee Service to send HTTP requests to Department Service and receive department details in response.

---

# Why Did We Choose RestClient Initially?

Before introducing advanced Spring Cloud components, it was important to understand how inter-service communication works at a lower level.

RestClient helped us understand:

- How HTTP requests are created.
- How REST APIs are invoked.
- How JSON data is exchanged.
- How Java objects are converted to JSON and vice versa.
- How service communication works before introducing abstraction layers.

By implementing RestClient first, we built a strong understanding of the communication process before moving to OpenFeign.

---

# Initial Architecture

The communication architecture initially looked like this.

```
Client

↓

Employee Service

↓

DepartmentRestClient

↓

RestClient

↓

HTTP Request

↓

Department Service

↓

Department Database
```

Employee Service was responsible for explicitly making an HTTP request whenever department information was required.

---

# DepartmentRestClient

To keep the business logic clean, we created a dedicated component.

```
DepartmentRestClient
```

Its responsibility was to communicate with Department Service.

EmployeeService simply called

```java
departmentRestClient.getDepartment(id);
```

Instead of handling HTTP communication directly.

This separation followed the **Single Responsibility Principle (SRP)**.

---

# URL Configuration

Initially, RestClient required the base URL of Department Service.

Instead of hardcoding it inside Java code, we externalized it using `application.yml`.

Example

```yaml
department-service:
  base-url: http://localhost:8082
```

This improved maintainability because the URL could be changed without modifying the source code.

---

# Request Flow

When the client requested employee details with department information, the following sequence occurred.

```
Client

↓

Employee Controller

↓

Employee Service

↓

DepartmentRestClient

↓

RestClient

↓

Department Service

↓

Department Database

↓

Department Service

↓

Employee Service

↓

Client
```

Employee Service waited until Department Service returned the required department details before sending the final response.

---

# Advantages of RestClient

RestClient provided several advantages during the initial development phase.

✔ Modern Spring HTTP client

✔ Fluent API

✔ Simple request-response communication

✔ Automatic JSON conversion

✔ Easy integration with Spring Boot

✔ Good for understanding REST communication

---

# Limitations of RestClient

Although RestClient worked well, we identified several limitations as the project evolved.

### Fixed Service Location

Even though the URL was stored in `application.yml`, it still referred to a specific service instance.

```
http://localhost:8082
```

If the Department Service moved to another server or port, the configuration had to be updated.

---

### Manual Request Construction

Every REST call required explicit code for:

- HTTP method
- URI
- Request execution
- Response handling

As the number of service calls increased, this resulted in repetitive boilerplate code.

---

### No Service Discovery

RestClient had no knowledge of available service instances.

It only communicated with the configured URL.

---

### No Automatic Load Balancing

If multiple instances of Department Service were running, RestClient always communicated with the configured instance.

It could not distribute requests automatically.

---

# Why We Replaced RestClient

As WorkSphere evolved into a true microservices architecture, the communication requirements also evolved.

We needed:

- Dynamic service discovery
- Cleaner service communication
- Less boilerplate code
- Better scalability
- Integration with Spring Cloud

These requirements led us to adopt **OpenFeign** together with **Eureka Service Discovery**.

---

# What We Learned

Implementing RestClient first helped us understand the fundamentals of REST communication.

We learned:

- How HTTP requests are created.
- How JSON is exchanged.
- How Spring handles serialization and deserialization.
- How services communicate over the network.

This knowledge made it much easier to understand OpenFeign later because we already knew what happened behind the scenes.

---

# Interview Questions

### Why did WorkSphere use RestClient initially?

RestClient was used to understand and implement basic synchronous REST communication before introducing higher-level abstractions like OpenFeign.

---

### Why was the URL moved to `application.yml`?

To externalize configuration, making the application easier to maintain across different environments without changing Java code.

---

### What were the limitations of RestClient?

- Fixed service URLs
- Manual request construction
- No service discovery
- No automatic load balancing

---

### Why was RestClient replaced?

It was replaced with OpenFeign and Eureka to simplify communication, remove boilerplate code, and enable dynamic service discovery.

---

# Summary

RestClient served as the first communication mechanism between Employee Service and Department Service in WorkSphere.

It provided a clear understanding of synchronous REST communication and formed the foundation for later adopting OpenFeign and Eureka Service Discovery.

Although it was eventually replaced, implementing RestClient first was an important learning step in the evolution of the project's architecture.

# OpenFeign Implementation in WorkSphere

## Introduction

As WorkSphere evolved into a microservices-based application, the number of service-to-service API calls increased.

Although RestClient successfully enabled communication between Employee Service and Department Service, it required developers to manually construct every HTTP request.

As the application grew, this approach became repetitive and difficult to maintain.

To simplify inter-service communication, WorkSphere adopted **Spring Cloud OpenFeign**.

OpenFeign provides a declarative approach to REST communication by allowing developers to define interfaces instead of writing HTTP client code.

---

# Why Did We Move from RestClient to OpenFeign?

RestClient required explicit HTTP request construction.

Example responsibilities included:

- Building URLs
- Selecting HTTP methods
- Sending requests
- Receiving responses
- Handling JSON conversion

As more services were added, this resulted in repetitive boilerplate code.

To improve maintainability and reduce manual coding, we migrated to OpenFeign.

---

# Architecture Before OpenFeign

```
Employee Service

↓

DepartmentRestClient

↓

RestClient

↓

Department Service
```

Every communication request required manual code.

---

# Architecture After OpenFeign

```
Employee Service

↓

DepartmentFeignClient

↓

OpenFeign

↓

Department Service
```

Instead of constructing HTTP requests manually, Employee Service simply invokes methods on an interface.

---

# DepartmentFeignClient

WorkSphere introduced the following interface.

```java
@FeignClient(name = "department-service")
public interface DepartmentFeignClient {

    @GetMapping("/api/v1/departments/{id}")

    DepartmentResponse getDepartment(
            @PathVariable Long id);

}
```

Notice that this interface contains **no implementation class**.

Spring automatically creates the implementation during application startup.

Employee Service simply injects the interface and calls its methods.

---

# Communication Flow

When the client requests employee details, the following sequence occurs.

```
Client

↓

Employee Controller

↓

Employee Service

↓

DepartmentFeignClient

↓

OpenFeign

↓

Department Service

↓

Department Database

↓

Department Service

↓

Employee Service

↓

Client
```

Unlike RestClient, Employee Service does not manually create HTTP requests.

OpenFeign performs this work automatically.

---

# Integration with Spring IoC

During application startup,

Spring scans the project.

↓

Finds

```
@FeignClient
```

↓

Creates an implementation.

↓

Registers it as a Spring Bean.

↓

Injects it wherever required.

Employee Service therefore uses constructor injection exactly like any other Spring component.

---

# Integration with Eureka

One of the biggest advantages of OpenFeign is its seamless integration with Eureka.

Instead of calling

```
http://localhost:8082
```

Employee Service simply references

```
department-service
```

When a request is made,

```
DepartmentFeignClient

↓

OpenFeign

↓

Eureka

↓

Department Service Instance

↓

HTTP Request
```

The actual service location is determined dynamically.

---

# Improvements Over RestClient

Compared to RestClient, OpenFeign provides several improvements.

✔ Less Boilerplate Code

✔ Declarative Programming

✔ Cleaner Business Logic

✔ Automatic Service Discovery

✔ Easier Maintenance

✔ Better Readability

✔ Better Integration with Spring Cloud

---

# Benefits for WorkSphere

Using OpenFeign improved the overall architecture of WorkSphere.

The communication layer became:

- Easier to maintain
- Easier to extend
- Easier to test
- More scalable
- Better aligned with enterprise microservice development

Business logic remained focused on business operations instead of HTTP communication.

---

# Why OpenFeign Fits Microservices

Microservices often communicate with multiple services.

Without OpenFeign,

developers must repeatedly write HTTP client code.

With OpenFeign,

communication becomes as simple as calling a Java method.

Example

```java
departmentFeignClient.getDepartment(id);
```

This makes the code easier to understand and maintain.

---

# Evolution of Communication

### Phase 1

```
Employee Service

↓

RestClient

↓

Department Service
```

---

### Phase 2

```
Employee Service

↓

OpenFeign

↓

Department Service
```

---

### Phase 3 (Current)

```
Employee Service

↓

OpenFeign

↓

Eureka

↓

Department Service
```

Each phase reduced complexity while improving scalability and maintainability.

---

# Interview Questions

### Why did WorkSphere migrate from RestClient to OpenFeign?

To reduce boilerplate code, simplify service communication, integrate with Eureka, and improve maintainability.

---

### Does DepartmentFeignClient have an implementation class?

No.

Spring automatically generates the implementation during application startup.

---

### Why is OpenFeign considered declarative?

Because developers define communication through interfaces and annotations instead of manually constructing HTTP requests.

---

### How does Employee Service communicate with Department Service now?

Employee Service calls methods on `DepartmentFeignClient`.

OpenFeign creates the HTTP request, Eureka resolves the service location, and Department Service processes the request.

---

### Is OpenFeign only useful with Eureka?

No.

OpenFeign can communicate using configured URLs.

However, when combined with Eureka, it provides dynamic service discovery and automatic integration with Spring Cloud.

---

# Summary

WorkSphere replaced RestClient with OpenFeign to simplify inter-service communication.

By defining service interfaces instead of writing HTTP client code, the application became cleaner, easier to maintain, and better prepared for enterprise-scale microservice architectures.

Combined with Eureka, OpenFeign enables dynamic service discovery while keeping business logic independent of service locations.

# Complete Request Flow in WorkSphere

After introducing REST communication and OpenFeign, the communication between services in WorkSphere became much cleaner and easier to maintain.

The following diagram illustrates the complete request flow.

```
                Client
                   │
                   ▼
          Employee Controller
                   │
                   ▼
           Employee Service
                   │
                   ▼
       DepartmentFeignClient
                   │
                   ▼
             OpenFeign
                   │
                   ▼
        Department Controller
                   │
                   ▼
         Department Service
                   │
                   ▼
      Department Repository
                   │
                   ▼
        Department Database
                   │
                   ▼
      Department Repository
                   │
                   ▼
         Department Service
                   │
                   ▼
        Department Controller
                   │
                   ▼
             OpenFeign
                   │
                   ▼
          Employee Service
                   │
                   ▼
          Employee Controller
                   │
                   ▼
                Client
```

---

# Step-by-Step Flow

### Step 1

The client requests employee details.

```
GET /api/v1/employees/{id}
```

---

### Step 2

Employee Controller receives the request and delegates it to Employee Service.

---

### Step 3

Employee Service retrieves employee information from the Employee Database.

---

### Step 4

Employee Service requires department information.

Instead of accessing another database directly, it calls

```
DepartmentFeignClient
```

---

### Step 5

OpenFeign creates the HTTP request automatically.

Employee Service does not write any HTTP communication code.

---

### Step 6

Department Service receives the request and retrieves department information from its database.

---

### Step 7

Department Service returns the department details.

---

### Step 8

Employee Service combines

- Employee information
- Department information

into a single response object.

---

### Step 9

The final response is returned to the client.

---

# Why This Architecture?

This approach follows the core principles of microservices.

✔ Independent services

✔ Independent databases

✔ Loose coupling

✔ Separation of responsibilities

✔ Easy maintenance

✔ Better scalability

# Communication Evolution in WorkSphere

The communication architecture of WorkSphere evolved gradually as the project became more enterprise-oriented.

---

# Phase 1

Initially, Employee Service only managed employee data.

```
Client

↓

Employee Service

↓

Employee Database
```

There was no communication with other services.

---

# Phase 2

Department Service was introduced.

Employee Service needed department information.

Communication was implemented using RestClient.

```
Employee Service

↓

RestClient

↓

Department Service
```

This worked correctly but required manual HTTP request construction.

---

# Phase 3

The service URL was externalized.

Instead of hardcoding

```
http://localhost:8082
```

the URL was moved to

```
application.yml
```

This improved maintainability.

---

# Phase 4

OpenFeign replaced RestClient.

```
Employee Service

↓

DepartmentFeignClient

↓

OpenFeign

↓

Department Service
```

Communication became much cleaner.

---

# Current Architecture

```
Client

↓

Employee Service

↓

OpenFeign

↓

Department Service

↓

Department Database
```

---

# Future Architecture

In the next phases of WorkSphere, the architecture will continue evolving.

Upcoming improvements include

- Eureka Service Discovery
- API Gateway
- Config Server
- Kafka
- Circuit Breaker
- Docker
- Kubernetes

Each improvement removes limitations from the previous architecture while making the application more scalable and cloud-ready.

# Best Practices

While implementing service communication in WorkSphere, several best practices were followed.

---

## Never Access Another Service's Database

Every microservice owns its own database.

Communication should always happen through APIs.

---

## Keep Business Logic Inside Services

Controllers should only receive requests and return responses.

Business logic belongs inside the service layer.

---

## Use DTOs

Services should exchange DTOs instead of entities.

This keeps the internal data model isolated.

---

## Use Declarative Clients

OpenFeign reduces boilerplate code and improves readability.

---

## Externalize Configuration

Configuration values should never be hardcoded.

Store them in configuration files.

---

## Keep Services Loosely Coupled

Each service should know only what it needs.

Avoid unnecessary dependencies between services.

---

## Design for Scalability

Always build communication layers that can support future enhancements such as

- Eureka
- API Gateway
- Kafka
- Docker
- Kubernetes

without requiring major code changes.

# Interview Questions

### What is service communication?

Service communication is the process by which independent microservices exchange information using network protocols such as HTTP.

---

### Why do microservices communicate through APIs?

Each microservice owns its own database and business logic.

APIs provide a controlled and loosely coupled communication mechanism.

---

### Which communication model does WorkSphere currently use?

WorkSphere currently uses synchronous REST communication.

---

### Why did WorkSphere first implement RestClient?

To understand low-level REST communication before introducing higher-level abstractions.

---

### Why was OpenFeign introduced?

To simplify HTTP communication and reduce boilerplate code.

---

### What are the advantages of OpenFeign?

- Declarative programming
- Cleaner code
- Easy maintenance
- Better Spring Cloud integration

---

### Why shouldn't one service access another service's database?

Doing so creates tight coupling and violates microservice principles.

Communication should always occur through APIs.

# Summary

Service communication is one of the core concepts of microservices.

In WorkSphere, Employee Service and Department Service communicate using REST APIs.

The project evolved from manual HTTP communication with RestClient to declarative communication using OpenFeign.

Throughout this evolution, the architecture became cleaner, more maintainable, and better aligned with enterprise development practices.

This communication model provides the foundation for future enhancements such as Eureka Service Discovery, API Gateway, Kafka, Docker, and Kubernetes, which will be introduced in the upcoming phases of the project.