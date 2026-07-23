# API Gateway

## Introduction

As WorkSphere evolved into a microservices architecture, multiple independent services were created.

Examples include:

- Employee Service
- Department Service

Initially, these services could communicate with each other using Eureka Service Discovery and OpenFeign.

However, external clients still needed to know the exact URL of every microservice.

Example:

```
Employee Service

http://localhost:8081
```

```
Department Service

http://localhost:8082
```

As the number of services increased, managing these URLs became difficult.

Clients had to know:

- Which service provides which functionality
- The port number of every service
- The endpoint of every service

This approach does not scale well in enterprise applications.

To solve this problem, WorkSphere introduced an **API Gateway**.

The API Gateway acts as a **single entry point** for all client requests.

Instead of communicating directly with individual microservices, clients communicate only with the API Gateway.

The gateway then routes each request to the appropriate microservice.

This provides:

- Centralized request routing
- Better security
- Simplified client communication
- Improved scalability
- Easier maintenance

The API Gateway becomes the front door of the entire WorkSphere system.

---

# Why WorkSphere Needed an API Gateway

Before introducing the API Gateway, clients communicated directly with each microservice.

The architecture looked like this.

```
                Client
                /    \
               /      \
              ▼        ▼
     Employee Service  Department Service
```

Each microservice exposed its own REST endpoints.

For example:

Employee APIs

```
http://localhost:8081/api/v1/employees
```

Department APIs

```
http://localhost:8082/api/v1/departments
```

Although this worked during development, several problems became apparent as the application grew.

---

## Problem 1 – Multiple Endpoints

The client had to maintain multiple URLs.

Example

```
Employee

http://localhost:8081
```

```
Department

http://localhost:8082
```

If more services were added, the number of URLs would continue increasing.

---

## Problem 2 – Tight Coupling

The client knew exactly where every microservice was running.

This created unnecessary dependency between the client and backend services.

If a service changed its port or hostname, every client had to update its configuration.

---

## Problem 3 – No Centralized Security

Every microservice would need to implement:

- Authentication
- Authorization
- Request validation
- CORS configuration

This would result in duplicated code across services.

---

## Problem 4 – Difficult Maintenance

Managing:

- Logging
- Monitoring
- Rate limiting
- Request filtering

inside every microservice becomes difficult.

A centralized solution is preferred.

---

## Problem 5 – Client Complexity

The client needed knowledge of the internal architecture.

For example:

- Which service provides employee data?
- Which service provides department data?
- Which port should be used?
- Which URL should be called?

Ideally, the client should not know these implementation details.

---

# Solution

WorkSphere introduced an **API Gateway**.

Instead of exposing every microservice directly, only the gateway is exposed.

The architecture became:

```
                 Client
                    │
                    ▼
              API Gateway
              /          \
             ▼            ▼
 Employee Service   Department Service
```

Now the client communicates with only one application.

The API Gateway receives every request and forwards it to the correct microservice.

This greatly simplifies communication and prepares WorkSphere for enterprise-scale deployment.

---

# Benefits of Introducing API Gateway

After introducing the API Gateway, WorkSphere gained several advantages.

✔ Single Entry Point

✔ Centralized Routing

✔ Better Security

✔ Easier Client Integration

✔ Simplified URL Management

✔ Better Scalability

✔ Centralized Logging

✔ Centralized Monitoring

✔ Future Support for Authentication and Rate Limiting

---

# Enterprise Perspective

Almost every modern microservices application places an API Gateway between clients and backend services.

Instead of exposing dozens of microservices, organizations expose only one endpoint.

The gateway becomes responsible for:

- Routing
- Authentication
- Authorization
- Logging
- Monitoring
- Request filtering
- Rate limiting
- Load balancing

This keeps individual microservices focused only on business logic.

---

# Interview Questions

### What is an API Gateway?

An API Gateway is a server that acts as a single entry point for all client requests in a microservices architecture.

---

### Why did WorkSphere introduce an API Gateway?

To provide a single entry point, simplify client communication, centralize routing, and prepare the system for enterprise features such as security and monitoring.

---

### What problem does an API Gateway solve?

It eliminates direct client communication with multiple microservices and centralizes request routing.

---

### Can clients call microservices directly?

Technically yes, but it is not recommended because it increases coupling, complicates security, and exposes internal service details.

---

# Summary

As WorkSphere evolved, direct communication between clients and multiple microservices became difficult to manage.

Introducing an API Gateway provided a centralized entry point for all requests, simplified client communication, improved maintainability, and established the foundation for future enterprise capabilities such as authentication, monitoring, logging, and rate limiting.

# What is an API Gateway?

## Introduction

An API Gateway is a server that acts as the single entry point for all client requests in a microservices architecture.

Instead of clients communicating directly with multiple microservices, they send every request to the API Gateway.

The gateway receives the request, determines which microservice should handle it, forwards the request, receives the response, and finally returns it to the client.

From the client's perspective, there is only one backend application.

Internally, the API Gateway communicates with multiple microservices.

---

# Definition

An API Gateway is a reverse proxy that sits between clients and backend microservices.

It performs several responsibilities, including:

- Request Routing
- Service Discovery
- Authentication
- Authorization
- Logging
- Monitoring
- Rate Limiting
- Request Filtering
- Response Filtering

Rather than implementing these features inside every microservice, they are centralized in the gateway.

---

# Position of API Gateway

The API Gateway sits between clients and backend services.

```
                  Client
                     │
                     ▼
               API Gateway
          ┌──────────┼──────────┐
          ▼          ▼          ▼
   Employee      Department    Future Services
    Service        Service
```

Clients never communicate directly with backend services.

Every request first reaches the gateway.

---

# Request Lifecycle

A request passes through several stages.

```
Client

↓

API Gateway

↓

Identify Route

↓

Locate Service

↓

Forward Request

↓

Receive Response

↓

Return Response

↓

Client
```

The gateway performs all routing automatically.

---

# API Gateway Responsibilities

The gateway performs several important tasks.

## Request Routing

The gateway determines which microservice should receive the request.

Example

```
GET

/api/employees
```

↓

Employee Service

Example

```
GET

/api/departments
```

↓

Department Service

---

## Service Discovery

The gateway does not store fixed service URLs.

Instead, it asks Eureka to locate the required service.

```
Gateway

↓

Eureka

↓

department-service

↓

localhost:8082
```

This enables dynamic communication.

---

## Request Filtering

The gateway can inspect every incoming request.

Examples include:

- Validate headers
- Validate tokens
- Add custom headers
- Reject invalid requests
- Log request information

---

## Response Filtering

Before returning the response to the client,

the gateway can:

- Add headers
- Remove sensitive data
- Compress responses
- Modify responses
- Log response information

---

## Authentication

The gateway can verify user identity before forwarding requests.

Example

```
Client

↓

JWT Token

↓

Gateway

↓

Validate Token

↓

Forward Request
```

This prevents unauthorized users from accessing backend services.

---

## Authorization

After authentication,

the gateway determines whether the user has permission to access the requested resource.

Example

```
Admin

↓

Allowed

User

↓

Denied
```

---

## Logging

The gateway provides a centralized location for request logging.

Instead of every service implementing logging,

all requests can be recorded in one place.

---

## Monitoring

The gateway allows centralized monitoring of:

- Request count
- Response time
- Error rate
- Active connections

This information helps monitor overall system health.

---

# API Gateway vs Direct Communication

Without Gateway

```
Client

↓

Employee Service

Client

↓

Department Service

Client

↓

Project Service
```

The client manages multiple URLs.

---

With Gateway

```
Client

↓

API Gateway

↓

Employee Service

↓

Department Service

↓

Project Service
```

The client manages only one URL.

---

# API Gateway vs Eureka

Many developers confuse Eureka and API Gateway.

Their responsibilities are completely different.

| API Gateway | Eureka |
|-------------|---------|
| Single entry point for clients | Service registry |
| Routes client requests | Stores service locations |
| Applies filters | Maintains service registry |
| Can authenticate users | Registers services |
| Can log requests | Sends heartbeats |
| Can rate-limit requests | Enables service discovery |

Think of it this way:

- **Eureka** answers: *"Where is the service?"*
- **API Gateway** answers: *"How should the client's request reach the service?"*

They work together but solve different problems.

---

# API Gateway vs Load Balancer

Another common confusion is between an API Gateway and a Load Balancer.

API Gateway

- Receives client requests
- Routes requests
- Applies filters
- Performs authentication
- Can transform requests and responses

Load Balancer

- Receives a list of available service instances
- Selects one instance
- Distributes requests among instances

In WorkSphere:

```
Client

↓

API Gateway

↓

Eureka

↓

Spring Cloud LoadBalancer

↓

Department Service
```

Each component has a different responsibility.

---

# Real-Life Example

Imagine visiting a large hospital.

Instead of walking directly into every department,

you first visit the reception desk.

```
Patient

↓

Reception

↓

Find Correct Department

↓

Doctor
```

The receptionist:

- Identifies your requirement
- Sends you to the correct department
- Provides guidance

The receptionist is similar to an API Gateway.

The doctors represent the microservices.

Patients never need to know where every department is located.

---

# Benefits of API Gateway

Using an API Gateway provides several advantages.

✔ Single Entry Point

✔ Simplified Client Communication

✔ Centralized Security

✔ Centralized Logging

✔ Easier Monitoring

✔ Request Filtering

✔ Response Filtering

✔ Better Scalability

✔ Cloud Readiness

✔ Better Maintainability

---

# Interview Questions

### What is an API Gateway?

An API Gateway is a server that acts as the single entry point for all client requests in a microservices architecture.

---

### What are the primary responsibilities of an API Gateway?

- Request Routing
- Authentication
- Authorization
- Logging
- Monitoring
- Request Filtering
- Response Filtering
- Integration with Service Discovery

---

### What is the difference between Eureka and API Gateway?

Eureka manages service discovery.

API Gateway manages client communication and request routing.

---

### Does API Gateway replace Eureka?

No.

They solve different problems and work together.

---

### Can an API Gateway communicate without Eureka?

Yes, by using fixed URLs.

However, combining API Gateway with Eureka enables dynamic service discovery and removes hardcoded service addresses.

---

# Summary

An API Gateway is the central entry point for all client communication in WorkSphere.

It simplifies client interaction by exposing a single endpoint while handling routing, security, monitoring, logging, and service discovery behind the scenes.

Combined with Eureka, it forms the foundation of a scalable and maintainable microservices architecture.

# Architecture Before and After API Gateway

## Introduction

As WorkSphere evolved, the communication between clients and microservices also evolved.

Initially, clients communicated directly with individual microservices.

After introducing Eureka, services could discover each other dynamically, but external clients still needed to know the address of every microservice.

To simplify client communication and prepare the application for enterprise deployment, WorkSphere introduced an API Gateway.

This section explains how the architecture changed and why the new design is better.

---

# Architecture Before API Gateway

Initially, every client communicated directly with individual microservices.

```
                 Client
                /      \
               /        \
              ▼          ▼
Employee Service    Department Service
```

Example URLs

Employee APIs

```
http://localhost:8081/api/v1/employees
```

Department APIs

```
http://localhost:8082/api/v1/departments
```

The client had to know the location of every service.

---

# Problems Before API Gateway

## Multiple URLs

The client maintained different URLs for every service.

Example

```
Employee Service

http://localhost:8081
```

```
Department Service

http://localhost:8082
```

As more services were added, managing these URLs became increasingly difficult.

---

## Tight Coupling

The client was tightly coupled to backend services.

If any service changed:

- Host
- Port
- URL

the client also needed to be updated.

---

## No Centralized Security

Each microservice had to implement its own:

- Authentication
- Authorization
- CORS Configuration
- Logging

This resulted in duplicate code across services.

---

## Difficult Monitoring

Monitoring requests across multiple services became difficult.

There was no single point where requests could be tracked.

---

## Client Complexity

The client needed to know:

- Which service to call
- Which port to use
- Which endpoint belonged to which service

This exposed the internal architecture to external users.

---

# Why This Architecture Was Not Suitable

Although direct communication works for small applications, it becomes difficult to manage in enterprise environments where:

- Dozens of services exist.
- Services scale dynamically.
- Security is centralized.
- APIs change frequently.

A better approach was required.

---

# Architecture After API Gateway

WorkSphere introduced an API Gateway as the single entry point.

The architecture became:

```
                    Client
                       │
                       ▼
                 API Gateway
                       │
          ┌────────────┴────────────┐
          ▼                         ▼
 Employee Service         Department Service
```

Now the client communicates with only one application.

The gateway determines which microservice should handle each request.

---

# Communication Flow

The communication process is now:

```
Client

↓

API Gateway

↓

Identify Route

↓

Locate Service using Eureka

↓

Forward Request

↓

Microservice

↓

Response

↓

API Gateway

↓

Client
```

The client never communicates directly with backend services.

---

# Architecture Evolution

WorkSphere evolved in multiple stages.

### Stage 1

Direct Communication

```
Client

↓

Employee Service
```

Only one service existed.

---

### Stage 2

Multiple Microservices

```
Client

↓

Employee Service

Client

↓

Department Service
```

Each service exposed its own endpoint.

---

### Stage 3

Eureka Introduced

```
Employee Service

↓

Eureka

↓

Department Service
```

Backend services discovered each other dynamically.

Clients still communicated directly.

---

### Stage 4

API Gateway Introduced

```
Client

↓

API Gateway

↓

Employee Service

↓

Department Service
```

Clients now use only one endpoint.

---

# Benefits of the New Architecture

After introducing API Gateway, WorkSphere achieved:

✔ Single Entry Point

✔ Simplified Client Communication

✔ Dynamic Service Discovery

✔ Better Security

✔ Centralized Routing

✔ Easier Monitoring

✔ Easier Logging

✔ Better Scalability

✔ Better Maintainability

✔ Cloud-Ready Architecture

---

# Before vs After

| Before API Gateway | After API Gateway |
|--------------------|-------------------|
| Multiple URLs | Single URL |
| Client knows every service | Client knows only Gateway |
| Distributed security | Centralized security |
| Difficult monitoring | Centralized monitoring |
| Difficult routing | Centralized routing |
| Tight coupling | Loose coupling |
| Harder to scale | Easier to scale |

---

# Real-Life Example

Imagine visiting a shopping mall.

### Without Information Desk

You must know:

- Which floor has electronics
- Which floor has clothing
- Which floor has food

You search everything yourself.

---

### With Information Desk

```
Customer

↓

Information Desk

↓

Correct Shop
```

You simply ask the information desk.

It directs you to the correct location.

The Information Desk acts like an API Gateway.

The shops represent individual microservices.

---

# Interview Questions

### Why was API Gateway introduced after Eureka?

Eureka enables service discovery between microservices.

API Gateway provides a single entry point for external clients.

They solve different problems and complement each other.

---

### Does API Gateway replace direct communication?

Yes.

Clients communicate only with the gateway.

The gateway communicates with backend services.

---

### What are the major architectural improvements?

- Single entry point
- Centralized routing
- Better security
- Simplified client communication
- Easier scalability

---

### Why shouldn't clients call microservices directly?

It increases coupling, complicates security, exposes internal architecture, and makes maintenance difficult.

---

# Summary

Introducing the API Gateway significantly improved the WorkSphere architecture.

The client no longer communicates with multiple backend services.

Instead, all requests pass through a centralized gateway that handles routing, integrates with Eureka for service discovery, and prepares the application for enterprise features such as authentication, logging, monitoring, and rate limiting.

This architecture is widely used in modern cloud-native microservices applications because it provides flexibility, scalability, and easier system management.

# Request Flow Through API Gateway

## Introduction

The API Gateway acts as the central entry point for all incoming client requests.

Whenever a client sends a request, it does not communicate directly with any microservice.

Instead, every request first reaches the API Gateway.

The gateway is responsible for:

- Receiving the request
- Identifying the correct route
- Discovering the target service
- Forwarding the request
- Receiving the response
- Returning the response to the client

This entire process happens transparently, allowing clients to interact with WorkSphere using a single endpoint.

---

# High-Level Request Flow

The complete request flow in WorkSphere is shown below.

```
                Client
                   │
                   ▼
             API Gateway
                   │
                   ▼
             Eureka Server
                   │
                   ▼
        Locate Target Service
                   │
                   ▼
        Employee Service
                   │
                   ▼
        Employee Database
                   │
                   ▼
        Employee Service
                   │
                   ▼
             API Gateway
                   │
                   ▼
                Client
```

The client never communicates directly with Employee Service.

---

# Example Request

Suppose the client wants employee information.

The client sends:

```
GET

/api/employees/1
```

Notice that the client does **not** know

- Employee Service URL
- Employee Service Port
- Employee Service Host

The client only communicates with the API Gateway.

---

# Step-by-Step Request Flow

## Step 1

Client sends a request.

```
Client

↓

GET /api/employees/1
```

The request reaches the API Gateway.

---

## Step 2

API Gateway receives the request.

The gateway examines:

- URL
- HTTP Method
- Headers
- Route Configuration

Example

```
/api/employees/**
```

The gateway identifies that this request belongs to Employee Service.

---

## Step 3

Gateway asks Eureka

```
Where is

employee-service?
```

Eureka searches its registry.

Example

```
employee-service

↓

localhost:8081

Status

UP
```

The service location is returned.

---

## Step 4

Gateway forwards the request.

```
Gateway

↓

Employee Service
```

The client never sees the internal URL.

---

## Step 5

Employee Service processes the request.

Business logic is executed.

Database is queried.

Employee data is retrieved.

---

## Step 6

Suppose Employee Service also requires department information.

It calls

```
DepartmentFeignClient
```

OpenFeign performs service discovery through Eureka.

```
Employee Service

↓

OpenFeign

↓

Eureka

↓

Department Service
```

Department information is retrieved.

---

## Step 7

Employee Service prepares the final response.

Example

```
Employee

+

Department

↓

EmployeeResponseDTO
```

---

## Step 8

Employee Service returns the response to the API Gateway.

```
Employee Service

↓

Gateway
```

---

## Step 9

Gateway sends the response back to the client.

```
Gateway

↓

Client
```

The request lifecycle is complete.

---

# Complete Runtime Flow

The complete WorkSphere request lifecycle is shown below.

```
Client

↓

API Gateway

↓

Route Matching

↓

Eureka

↓

Employee Service

↓

Employee Database

↓

OpenFeign

↓

Eureka

↓

Department Service

↓

Department Database

↓

Department Service

↓

Employee Service

↓

API Gateway

↓

Client
```

This entire sequence occurs automatically.

---

# Internal Processing Inside API Gateway

The gateway performs multiple operations before forwarding the request.

```
Incoming Request

↓

Validate Route

↓

Apply Filters

↓

Locate Service

↓

Forward Request

↓

Receive Response

↓

Apply Response Filters

↓

Return Response
```

These steps are transparent to the client.

---

# Why is this Process Better?

Without Gateway

```
Client

↓

Employee Service

Client

↓

Department Service

Client

↓

Project Service
```

The client manages multiple URLs.

---

With Gateway

```
Client

↓

API Gateway

↓

All Microservices
```

The client uses only one endpoint.

This reduces complexity significantly.

---

# Advantages

The request flow provides several advantages.

✔ Single Entry Point

✔ Centralized Routing

✔ Dynamic Service Discovery

✔ Better Security

✔ Easier Monitoring

✔ Centralized Logging

✔ Better Scalability

✔ Cleaner Architecture

---

# Real-Life Example

Imagine visiting an airport.

You first reach the main entrance.

```
Passenger

↓

Security Check

↓

Terminal

↓

Gate

↓

Flight
```

You don't directly enter the aircraft.

Similarly,

Every client request first enters through the API Gateway before reaching the appropriate microservice.

---

# Interview Questions

### What is the first component that receives a client request?

The API Gateway.

---

### How does the Gateway identify the destination service?

It matches the incoming URL with the configured routes.

---

### How does the Gateway locate the service?

It queries Eureka using the service name.

---

### Does the client know the actual microservice URL?

No.

The client only communicates with the API Gateway.

---

### Can the Gateway communicate without Eureka?

Yes, by using fixed URLs.

However, using Eureka enables dynamic service discovery and removes hardcoded service addresses.

---

# Summary

In WorkSphere, every client request passes through the API Gateway before reaching any backend service.

The gateway identifies the correct route, discovers the target service using Eureka, forwards the request, and returns the response to the client.

This architecture simplifies client communication, centralizes routing, and enables scalable, enterprise-grade microservices.

# Gateway Routing

## Introduction

Routing is one of the primary responsibilities of an API Gateway.

When a client sends a request to the API Gateway, the gateway must determine which microservice should process that request.

This decision is called **Routing**.

Instead of hardcoding URLs inside the application, Spring Cloud Gateway uses route definitions to automatically forward requests to the correct microservice.

In WorkSphere, routing is configured in the `application.yml` file.

Each route maps a URL pattern to a target microservice.

---

# What is Routing?

Routing is the process of forwarding an incoming client request to the appropriate backend service.

Example

```
Incoming Request

↓

/api/employees/**

↓

Employee Service
```

Another example

```
Incoming Request

↓

/api/departments/**

↓

Department Service
```

The client never needs to know where these services are running.

---

# How Routing Works

The routing process follows these steps.

```
Client Request

↓

API Gateway

↓

Read Route Configuration

↓

Match URL Pattern

↓

Locate Service using Eureka

↓

Forward Request

↓

Return Response
```

Every request follows this sequence.

---

# Routing in WorkSphere

Suppose a client sends the following request.

```
GET

/api/employees/101
```

The Gateway compares the request URL with all configured routes.

Example

```
Route 1

/api/employees/**
```

```
Route 2

/api/departments/**
```

The first route matches.

The request is forwarded to Employee Service.

---

# Route Configuration

A typical route configuration looks like this.

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: employee-service
          uri: lb://employee-service
          predicates:
            - Path=/api/employees/**
```

Each property has a specific purpose.

---

# Route ID

```yaml
id: employee-service
```

### Purpose

Every route must have a unique identifier.

The ID is mainly used internally by Spring Cloud Gateway.

Example

```
employee-service
```

```
department-service
```

The route ID does **not** affect client requests.

---

# URI

```yaml
uri: lb://employee-service
```

This is one of the most important Gateway properties.

### What does URI mean?

The URI tells the Gateway where the request should be forwarded.

Unlike traditional applications, we do **not** write:

```
http://localhost:8081
```

Instead, we use

```
lb://employee-service
```

---

# Why use `lb://`?

`lb` stands for

```
Load Balancer
```

This tells Spring Cloud Gateway:

1. Ask Eureka for available instances.
2. Use Spring Cloud LoadBalancer.
3. Select one healthy instance.
4. Forward the request.

This enables dynamic routing.

---

# Why not use localhost?

Using

```
http://localhost:8081
```

creates several problems.

- Hardcoded URL
- Environment dependency
- No scalability
- No load balancing

Using

```
lb://employee-service
```

solves all these issues.

---

# Predicates

Predicates define the conditions under which a route should be selected.

Example

```yaml
predicates:
  - Path=/api/employees/**
```

Meaning

If the incoming request starts with

```
/api/employees/
```

forward it to Employee Service.

---

Another example

```yaml
predicates:
  - Path=/api/departments/**
```

Now all department requests go to Department Service.

---

# Route Matching

Suppose the Gateway has two routes.

```
Route A

/api/employees/**
```

```
Route B

/api/departments/**
```

Incoming request

```
/api/departments/5
```

The Gateway compares the request against each route.

```
Employee Route

×

Department Route

✓
```

The request is forwarded to Department Service.

---

# Complete Routing Flow

```
Client

↓

GET /api/employees/10

↓

Gateway

↓

Check Routes

↓

Match Employee Route

↓

Ask Eureka

↓

employee-service

↓

LoadBalancer

↓

Employee Service

↓

Response

↓

Gateway

↓

Client
```

This process happens automatically.

---

# Multiple Routes in WorkSphere

As WorkSphere grows, additional routes can be added.

Example

```yaml
routes:

- employee-service

- department-service

- project-service

- payroll-service

- notification-service
```

The Gateway can manage dozens of services through a single configuration.

---

# Advantages of Gateway Routing

Routing provides many benefits.

✔ Centralized configuration

✔ Easy maintenance

✔ Dynamic service discovery

✔ No hardcoded URLs

✔ Better scalability

✔ Easier addition of new services

✔ Integration with Eureka

✔ Integration with LoadBalancer

---

# Real-Life Example

Imagine a courier company.

A parcel arrives at the central sorting center.

```
Parcel

↓

Sorting Center

↓

Identify Destination

↓

Send to Correct City
```

The sorting center decides where every parcel should go.

The API Gateway performs the same function for HTTP requests.

---

# Best Practices

✔ Use meaningful route IDs.

✔ Always use `lb://` with Eureka.

✔ Keep routing configuration in `application.yml`.

✔ Use clear and consistent URL patterns.

✔ Avoid hardcoded service URLs.

✔ Group related APIs under common paths.

---

# Interview Questions

### What is routing in API Gateway?

Routing is the process of forwarding client requests to the correct backend service.

---

### What is the purpose of `uri`?

It specifies the destination service.

When using Eureka, it usually starts with:

```
lb://
```

---

### What does `lb://` mean?

It tells the Gateway to use Spring Cloud LoadBalancer together with Eureka Service Discovery.

---

### What are Predicates?

Predicates are conditions used to determine whether a route matches an incoming request.

---

### Why use `lb://employee-service` instead of `http://localhost:8081`?

Because it supports:

- Dynamic service discovery
- Load balancing
- Better scalability
- Environment independence

---

# Summary

Gateway Routing is responsible for directing incoming client requests to the appropriate microservice.

In WorkSphere, routing is configured using Spring Cloud Gateway routes.

The Gateway matches the incoming request with a route, uses Eureka to locate the service, applies load balancing, and forwards the request automatically.

This eliminates hardcoded URLs and enables scalable, maintainable microservice communication.

# Gateway Predicates

## Introduction

When a client sends a request to the API Gateway, the gateway must determine which route should handle that request.

This decision is made using **Predicates**.

A predicate is simply a condition.

If the incoming request satisfies the condition, the corresponding route is selected.

If the condition is not satisfied, the gateway continues checking the remaining routes.

Predicates are therefore the decision-making mechanism of Spring Cloud Gateway.

---

# What is a Predicate?

A Predicate is a rule that determines whether an incoming request matches a particular route.

Think of it as an **IF condition**.

```
IF

Request matches condition

↓

Forward request

ELSE

Check next route
```

Without predicates, the gateway would not know which microservice should receive the request.

---

# Why are Predicates Required?

Suppose WorkSphere contains multiple microservices.

```
Employee Service

Department Service

Payroll Service

Notification Service
```

A client sends

```
GET

/api/departments/5
```

The Gateway must determine

> Which service should receive this request?

Predicates answer this question.

---

# Predicate Flow

The routing decision follows this process.

```
Incoming Request

↓

Read Route

↓

Evaluate Predicate

↓

Match Found?

↓

Yes

↓

Forward Request

↓

No

↓

Check Next Route
```

---

# Predicates Used in WorkSphere

Currently, WorkSphere uses the **Path Predicate**.

Example

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: employee-service
          uri: lb://employee-service
          predicates:
            - Path=/api/employees/**
```

This means

Every request beginning with

```
/api/employees/
```

is forwarded to Employee Service.

---

Another route

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: department-service
          uri: lb://department-service
          predicates:
            - Path=/api/departments/**
```

Now all department requests are forwarded to Department Service.

---

# Path Predicate

The Path Predicate is the most commonly used predicate.

Example

```yaml
Path=/api/employees/**
```

Meaning

Match every URL beginning with

```
/api/employees/
```

Examples

Matches

```
/api/employees

/api/employees/1

/api/employees/100

/api/employees/search
```

Does NOT Match

```
/api/departments

/api/payroll

/api/projects
```

---

# How Path Matching Works

Suppose the Gateway has three routes.

```
Employee

/api/employees/**
```

```
Department

/api/departments/**
```

```
Payroll

/api/payroll/**
```

Incoming request

```
/api/payroll/salary
```

Gateway checks

Employee

❌

Department

❌

Payroll

✅

The request is forwarded to Payroll Service.

---

# Other Types of Predicates

Spring Cloud Gateway supports many predicate types.

WorkSphere currently uses only **Path Predicate**, but future versions may use additional predicates.

---

## Method Predicate

Matches based on the HTTP method.

Example

```yaml
Method=GET
```

Matches

```
GET

/api/employees
```

Does not match

```
POST

/api/employees
```

Useful when different HTTP methods require different routing behaviour.

---

## Header Predicate

Matches requests containing a specific HTTP header.

Example

```yaml
Header=X-Request-Source, Mobile
```

Only requests with

```
X-Request-Source: Mobile
```

will match.

---

## Host Predicate

Matches based on the hostname.

Example

```yaml
Host=api.worksphere.com
```

Useful when multiple domains point to the same gateway.

---

## Query Predicate

Matches based on query parameters.

Example

```yaml
Query=role,admin
```

Matches

```
/employees?role=admin
```

---

## Cookie Predicate

Matches requests containing specific cookies.

Example

```yaml
Cookie=sessionId,.*
```

Useful for session-based applications.

---

## Remote Address Predicate

Matches requests coming from specific IP addresses.

Example

```yaml
RemoteAddr=192.168.1.0/24
```

Commonly used for internal APIs.

---

# Why WorkSphere Uses Path Predicate

At the current stage,

WorkSphere primarily routes requests based on REST endpoints.

Examples

```
/api/employees/**
```

```
/api/departments/**
```

Path Predicate is therefore the simplest and most appropriate choice.

As WorkSphere evolves,

additional predicates may be introduced for:

- Security
- Multi-tenancy
- Versioning
- Internal APIs
- Mobile applications

---

# Enterprise Example

Suppose an organization has two applications.

```
Mobile App

↓

/mobile/**
```

```
Web Portal

↓

/web/**
```

Gateway configuration

```yaml
Path=/mobile/**
```

↓

Mobile Service

```yaml
Path=/web/**
```

↓

Web Service

The gateway automatically routes requests based on the URL.

---

# Advantages of Predicates

Predicates provide several benefits.

✔ Flexible Routing

✔ Easy Configuration

✔ Cleaner Architecture

✔ No Java Code Required

✔ Supports Multiple Conditions

✔ Easy to Extend

✔ Enterprise Ready

---

# Real-Life Example

Imagine entering a large office building.

At the reception desk, the receptionist asks

```
Which department would you like to visit?
```

If you say

```
Human Resources
```

you are directed to HR.

If you say

```
Finance
```

you are directed to Finance.

The receptionist is using conditions to decide where you should go.

Predicates perform the same role inside the API Gateway.

---

# Best Practices

✔ Use Path Predicates for REST APIs.

✔ Keep URL patterns consistent.

✔ Avoid overlapping routes.

✔ Use meaningful route IDs.

✔ Organize related APIs under common prefixes.

✔ Introduce advanced predicates only when required.

---

# Interview Questions

### What is a Predicate in Spring Cloud Gateway?

A Predicate is a condition used to determine whether a route matches an incoming request.

---

### Which Predicate does WorkSphere currently use?

Path Predicate.

---

### Why is the Path Predicate commonly used?

Because REST APIs are naturally organized using URL paths.

---

### Can multiple predicates be used together?

Yes.

A route can contain multiple predicates.

The request must satisfy all configured predicates for the route to match.

---

### Name some commonly used predicates.

- Path
- Method
- Header
- Host
- Query
- Cookie
- Remote Address

---

# Summary

Predicates are the decision-making component of Spring Cloud Gateway.

They determine which route should process an incoming request.

In WorkSphere, Path Predicates are used to route requests to the appropriate microservice based on the request URL.

As the application evolves, additional predicate types can be introduced to support more advanced routing requirements while keeping the gateway configuration simple, scalable, and maintainable.

# Gateway Filters

## Introduction

Routing determines **where** a request should go.

Filters determine **what should happen** before the request reaches the destination service and after the response returns.

In Spring Cloud Gateway, Filters provide a mechanism to intercept and process HTTP requests and responses.

Filters allow developers to implement common functionality in one central location instead of duplicating the same code across multiple microservices.

This keeps the architecture clean, maintainable, and scalable.

---

# What is a Gateway Filter?

A Gateway Filter is a component that executes before or after a request is processed.

It can:

- Inspect requests
- Modify requests
- Validate requests
- Add or remove headers
- Authenticate users
- Log requests
- Modify responses
- Handle errors

Filters work transparently.

Clients and backend services are unaware that filters are executing.

---

# Why are Filters Required?

Suppose WorkSphere contains the following services.

```
Employee Service

Department Service

Payroll Service

Notification Service
```

Without filters,

every service would need to implement:

- Authentication
- Logging
- Header validation
- Request tracking
- Error handling

This results in duplicated code.

Instead,

the API Gateway performs these operations once before forwarding requests.

---

# Request Lifecycle with Filters

```
                Client
                   │
                   ▼
          Pre Filters Execute
                   │
                   ▼
             Route Matching
                   │
                   ▼
          Forward Request
                   │
                   ▼
            Microservice
                   │
                   ▼
         Receive Response
                   │
                   ▼
         Post Filters Execute
                   │
                   ▼
                Client
```

---

# Types of Filters

Spring Cloud Gateway provides two main types of filters.

```
Gateway Filters

↓

Pre Filters

↓

Post Filters
```

---

# Pre Filters

Pre Filters execute before the request reaches the destination service.

```
Client

↓

Pre Filter

↓

Employee Service
```

Typical responsibilities include:

- Authentication
- Authorization
- Request Validation
- Header Validation
- Logging
- Rate Limiting

---

# Example

Incoming Request

```
GET

/api/employees/1
```

Pre Filter checks

✔ JWT Token

✔ Request Headers

✔ User Permissions

If validation succeeds,

the request is forwarded.

Otherwise,

the Gateway immediately returns an error.

---

# Post Filters

Post Filters execute after the microservice returns a response.

```
Employee Service

↓

Post Filter

↓

Client
```

Typical responsibilities include:

- Response Logging
- Adding Headers
- Compressing Responses
- Response Transformation
- Error Handling

---

# Filter Execution Flow

```
Incoming Request

↓

Pre Filter

↓

Route Matching

↓

Microservice

↓

Response

↓

Post Filter

↓

Client
```

This sequence occurs automatically for every request.

---

# Route Filters

A Route Filter applies only to a specific route.

Example

Employee Service

↓

Authentication Filter

Department Service

↓

No Authentication

Only Employee Service executes the filter.

---

# Global Filters

A Global Filter executes for every request.

```
Client

↓

Global Filter

↓

Employee Service
```

```
Client

↓

Global Filter

↓

Department Service
```

```
Client

↓

Global Filter

↓

Payroll Service
```

Every request passes through the same filter.

---

# Common Uses of Global Filters

Global Filters are commonly used for:

✔ Authentication

✔ Logging

✔ Request Tracking

✔ Correlation IDs

✔ Monitoring

✔ Performance Measurement

---

# Common Uses of Route Filters

Route Filters are useful when only one service requires additional processing.

Examples

Employee APIs

↓

JWT Authentication

Public APIs

↓

No Authentication

---

# Request Header Manipulation

Filters can modify request headers.

Example

Incoming Request

```
Authorization

Bearer xxxx
```

Gateway may add

```
X-Request-ID

123456
```

before forwarding the request.

---

# Response Header Manipulation

Filters can also modify responses.

Example

Gateway adds

```
X-Gateway

WorkSphere Gateway
```

before sending the response to the client.

---

# Authentication Example

Future versions of WorkSphere will use JWT authentication.

The request flow will become:

```
Client

↓

JWT Token

↓

Gateway Filter

↓

Validate Token

↓

Forward Request

↓

Employee Service
```

If the token is invalid,

the request never reaches the microservice.

---

# Logging Example

Every request can be logged.

Example

```
Time

Client IP

HTTP Method

Endpoint

Response Time

Status Code
```

Instead of logging inside every microservice,

logging is performed once inside the Gateway.

---

# Error Handling

Filters can intercept errors before returning them to the client.

Example

Microservice returns

```
500 Internal Server Error
```

Gateway Filter can convert it into a standard error format.

This ensures all services return consistent error responses.

---

# Benefits of Gateway Filters

Using Filters provides many advantages.

✔ Centralized Authentication

✔ Centralized Logging

✔ Better Security

✔ Cleaner Microservices

✔ Response Transformation

✔ Request Validation

✔ Consistent Error Handling

✔ Easier Maintenance

✔ Enterprise Scalability

---

# WorkSphere Perspective

At the current stage,

WorkSphere primarily uses the API Gateway for routing.

As the project evolves,

Filters will be introduced for:

- JWT Authentication
- Request Logging
- Correlation IDs
- Rate Limiting
- Performance Monitoring
- Custom Headers
- Exception Handling

This allows business logic to remain inside microservices while common infrastructure concerns remain inside the Gateway.

---

# Real-Life Example

Imagine entering a corporate office.

Before reaching the meeting room,

security performs several checks.

```
Visitor

↓

Security Desk

↓

Identity Verification

↓

Visitor Pass

↓

Meeting Room
```

When leaving,

security records the exit time.

```
Meeting Room

↓

Security Desk

↓

Exit Recorded

↓

Leave Building
```

The Security Desk acts like a Gateway Filter.

It performs processing before and after entry.

---

# Best Practices

✔ Keep filters lightweight.

✔ Do not implement business logic inside filters.

✔ Use Global Filters only for common functionality.

✔ Use Route Filters for service-specific processing.

✔ Centralize authentication in the Gateway.

✔ Log requests responsibly without exposing sensitive data.

✔ Keep filter execution fast to avoid increasing response time.

---

# Interview Questions

### What is a Gateway Filter?

A Gateway Filter intercepts requests and responses to perform additional processing before or after routing.

---

### What is the difference between Pre and Post Filters?

Pre Filters execute before forwarding the request.

Post Filters execute after receiving the response.

---

### What is the difference between Global Filters and Route Filters?

Global Filters execute for every request.

Route Filters execute only for specific routes.

---

### Why are Filters useful?

They centralize cross-cutting concerns such as authentication, logging, monitoring, validation, and header manipulation.

---

### Should business logic be written inside Filters?

No.

Filters should only perform infrastructure-related tasks.

Business logic should remain inside microservices.

---

# Summary

Gateway Filters are one of the most powerful features of Spring Cloud Gateway.

They allow WorkSphere to centralize authentication, logging, monitoring, validation, and response processing while keeping individual microservices focused solely on business logic.

As WorkSphere evolves, Filters will become the foundation for implementing enterprise-grade security and operational features without modifying individual services.

# API Gateway Configuration in WorkSphere

## Introduction

After understanding the purpose of an API Gateway, the next step is configuring it.

In WorkSphere, the API Gateway is implemented using **Spring Cloud Gateway** and integrated with **Eureka Service Discovery**.

The configuration is intentionally simple while remaining scalable enough for enterprise applications.

The Gateway configuration consists of two parts:

- Maven Dependencies (`pom.xml`)
- Spring Boot Configuration (`application.yml`)

---

# Maven Configuration

The API Gateway is created using the following dependency.

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

---

## Why do we need this dependency?

This dependency converts a normal Spring Boot application into an API Gateway.

It provides:

- Request Routing
- Route Predicates
- Gateway Filters
- Load Balancer Integration
- Reactive HTTP Server
- Integration with Eureka

Without this dependency, the application behaves like a normal Spring Boot application and cannot route requests.

---

# Eureka Client Dependency

The Gateway is also a Eureka Client.

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

---

## Why does Gateway need Eureka?

The Gateway should not know the physical location of microservices.

Instead, it asks Eureka for the available service instances.

Example

```
Gateway

↓

Eureka

↓

employee-service

↓

localhost:8081
```

This removes hardcoded URLs.

---

# Application Configuration

The API Gateway configuration is stored inside

```
application.yml
```

A simplified version is shown below.

```yaml
server:
  port: 8080

spring:
  application:
    name: api-gateway

  cloud:
    gateway:
      routes:
        - id: employee-service
          uri: lb://employee-service
          predicates:
            - Path=/api/employees/**

        - id: department-service
          uri: lb://department-service
          predicates:
            - Path=/api/departments/**

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

---

# server.port

```yaml
server:
  port: 8080
```

## Purpose

Defines the port on which the API Gateway runs.

Clients communicate only with this port.

Example

```
http://localhost:8080
```

The client never accesses Employee Service or Department Service directly.

---

# spring.application.name

```yaml
spring:
  application:
    name: api-gateway
```

## Purpose

Defines the logical name of the Gateway.

This name is registered with Eureka.

The Gateway itself also appears in the Eureka Dashboard.

---

# Gateway Routes

```yaml
spring:
  cloud:
    gateway:
      routes:
```

This section contains all routing rules.

Each route tells the Gateway:

- Which requests should be matched
- Which microservice should receive them

---

# Route ID

```yaml
id: employee-service
```

## Purpose

Provides a unique identifier for the route.

It is mainly used internally by Spring Cloud Gateway.

---

# URI

```yaml
uri: lb://employee-service
```

## Purpose

Specifies the destination service.

Notice that the URI does not contain:

```
localhost

IP Address

Port Number
```

Instead,

the Gateway asks Eureka to locate

```
employee-service
```

This supports dynamic service discovery.

---

# Why use lb:// ?

The prefix

```
lb://
```

means

```
Load Balancer
```

When the Gateway sees

```
lb://employee-service
```

it performs the following steps.

```
Gateway

↓

Eureka

↓

Available Instances

↓

Spring Cloud LoadBalancer

↓

Selected Instance

↓

Forward Request
```

This entire process happens automatically.

---

# Predicates

```yaml
predicates:
  - Path=/api/employees/**
```

## Purpose

Defines the condition for matching a request.

Example

Incoming Request

```
/api/employees/101
```

↓

Matches

```
/api/employees/**
```

↓

Forward to

Employee Service

---

# Eureka Configuration

```yaml
eureka:
  client:
    service-url:
      defaultZone:
        http://localhost:8761/eureka
```

## Purpose

Specifies the location of the Eureka Server.

The Gateway uses this address to:

- Register itself
- Discover services
- Fetch the service registry

---

# How Everything Works Together

The following sequence occurs for every request.

```
Client

↓

API Gateway

↓

Match Route

↓

Ask Eureka

↓

Locate Service

↓

LoadBalancer

↓

Forward Request

↓

Microservice

↓

Gateway

↓

Client
```

---

# WorkSphere Routing Table

| Client URL | Gateway Route | Target Service |
|------------|---------------|----------------|
| `/api/employees/**` | Employee Route | Employee Service |
| `/api/departments/**` | Department Route | Department Service |

As additional services are introduced, new routes can be added without changing existing ones.

---

# Common Configuration Mistakes

### Incorrect Service Name

Incorrect

```yaml
uri: lb://employees
```

Correct

```yaml
uri: lb://employee-service
```

The service name must exactly match

```yaml
spring.application.name
```

---

### Missing Eureka Client

Without the Eureka Client dependency,

the Gateway cannot discover services.

---

### Wrong defaultZone

Incorrect

```
http://localhost:8761
```

Correct

```
http://localhost:8761/eureka
```

The `/eureka` endpoint is required.

---

### Hardcoded URLs

Avoid

```yaml
uri: http://localhost:8081
```

Always prefer

```yaml
uri: lb://employee-service
```

---

# Best Practices

✔ Keep all routes inside `application.yml`.

✔ Use logical service names.

✔ Avoid hardcoded URLs.

✔ Register the Gateway with Eureka.

✔ Use meaningful route IDs.

✔ Keep URL patterns consistent.

✔ Verify route registration using the Eureka Dashboard.

---

# Interview Questions

### Why does the API Gateway need the Eureka Client dependency?

Because it discovers service locations dynamically instead of using fixed URLs.

---

### What is the purpose of `lb://`?

It tells the Gateway to use Spring Cloud LoadBalancer together with Eureka Service Discovery.

---

### Why is `spring.application.name` important?

It defines the Gateway's logical identity and registers it with Eureka.

---

### Why is the API Gateway registered with Eureka?

So other services can discover it if needed, and so it participates in the overall service registry.

---

### Where are routes configured?

Routes are configured inside the `application.yml` file under:

```yaml
spring:
  cloud:
    gateway:
      routes:
```

---

# Summary

The API Gateway configuration in WorkSphere combines Spring Cloud Gateway with Eureka Service Discovery.

The Gateway receives all client requests, matches them against configured routes, discovers backend services dynamically using Eureka, applies load balancing, and forwards requests without exposing the internal network structure.

This approach provides a clean, scalable, and maintainable architecture suitable for enterprise microservices.

# How We Integrated API Gateway into WorkSphere

## Introduction

After successfully implementing Eureka Service Discovery, WorkSphere microservices were able to discover each other dynamically.

Employee Service no longer depended on hardcoded URLs to communicate with Department Service.

However, one important problem still remained.

External clients were still communicating directly with individual microservices.

As the number of services increased, this architecture became difficult to maintain.

To solve this problem, WorkSphere introduced an API Gateway.

The API Gateway became the single entry point for all client requests while Eureka continued to manage service discovery between microservices.

---

# Architecture Before API Gateway

Before introducing the API Gateway, every client communicated directly with the backend services.

```
                    Client
                   /      \
                  /        \
                 ▼          ▼
        Employee Service   Department Service
```

Example

```
GET http://localhost:8081/api/employees
```

```
GET http://localhost:8082/api/departments
```

Each service exposed its own REST endpoints.

---

# Problems Before API Gateway

## Problem 1 – Multiple URLs

The client had to maintain different URLs for every service.

Example

Employee Service

```
http://localhost:8081
```

Department Service

```
http://localhost:8082
```

As more services were added, the number of URLs continued to increase.

---

## Problem 2 – Tight Coupling

Clients knew:

- Service Names
- Port Numbers
- API Locations

If a service changed its port,

every client had to update its configuration.

---

## Problem 3 – Security

Every microservice would eventually need to implement:

- Authentication
- Authorization
- Request Validation
- Logging

The same code would be repeated in every service.

---

## Problem 4 – No Centralized Routing

Each client decided which service to call.

There was no centralized component responsible for request routing.

---

## Problem 5 – Difficult Maintenance

Features like

- Logging
- Monitoring
- Rate Limiting
- Header Validation

would need to be implemented repeatedly inside every microservice.

---

# Engineering Decision

To eliminate these limitations,

WorkSphere introduced Spring Cloud Gateway.

Instead of exposing every microservice,

only the Gateway is exposed to external clients.

---

# What We Added

A new microservice

```
api-gateway
```

was introduced.

Its responsibilities include:

- Request Routing
- Service Discovery Integration
- Gateway Filters
- Centralized Entry Point

---

# Dependencies Added

The following Maven dependencies were added.

Spring Cloud Gateway

```xml
spring-cloud-starter-gateway
```

Eureka Client

```xml
spring-cloud-starter-netflix-eureka-client
```

These dependencies enable the Gateway to route requests dynamically using Eureka.

---

# Configuration Added

The following configuration was introduced.

```yaml
spring:
  cloud:
    gateway:
      routes:
```

Each route maps a client URL to a backend microservice.

Example

```
/api/employees/**

↓

employee-service
```

```
/api/departments/**

↓

department-service
```

---

# Architecture After API Gateway

After integration,

the architecture became

```
                    Client
                       │
                       ▼
                 API Gateway
                       │
          ┌────────────┴────────────┐
          ▼                         ▼
 Employee Service          Department Service
```

Clients no longer communicate directly with backend services.

---

# Communication Flow

A request now follows this sequence.

```
Client

↓

API Gateway

↓

Route Matching

↓

Eureka

↓

Employee Service

↓

Database

↓

Employee Service

↓

Gateway

↓

Client
```

If Employee Service requires department information,

it continues using OpenFeign.

```
Employee Service

↓

OpenFeign

↓

Eureka

↓

Department Service
```

Notice that the Gateway is only responsible for client-to-service communication.

Service-to-service communication continues through Eureka and OpenFeign.

---

# Files Modified During Integration

## API Gateway Module

Created

```
api-gateway
```

---

## pom.xml

Added

```
spring-cloud-starter-gateway
```

Added

```
spring-cloud-starter-netflix-eureka-client
```

---

## application.yml

Configured

- Gateway Port
- Eureka Server
- Routes
- Service Names

---

## Eureka Server

No major changes were required.

The Gateway simply registered itself as another Eureka Client.

---

## Employee Service

No routing changes were required.

Employee Service continues exposing REST APIs.

The Gateway now forwards client requests automatically.

---

## Department Service

No routing changes were required.

Department Service continues exposing REST APIs.

---

# Benefits Achieved

After introducing API Gateway,

WorkSphere gained

✔ Single Entry Point

✔ Dynamic Routing

✔ Cleaner Client Communication

✔ Better Security

✔ Easier Monitoring

✔ Easier Logging

✔ Better Scalability

✔ Cloud Readiness

✔ Better Maintainability

✔ Future Support for JWT Authentication

---

# How API Gateway and Eureka Work Together

One common misunderstanding is that Gateway replaces Eureka.

It does not.

Both components have different responsibilities.

```
Client

↓

API Gateway

↓

Eureka

↓

Employee Service
```

Gateway

↓

Handles Client Requests

Eureka

↓

Finds Service Locations

Spring Cloud LoadBalancer

↓

Selects Service Instance

Each component has a clearly defined responsibility.

---

# What Would Happen Without API Gateway?

Without Gateway

❌ Clients manage multiple URLs

❌ Duplicate authentication logic

❌ Difficult monitoring

❌ No centralized routing

❌ Increased maintenance

❌ Tight coupling

As the application grows,

these issues become increasingly difficult to manage.

---

# Interview Questions

### Why did WorkSphere introduce an API Gateway?

To provide a single entry point, simplify client communication, centralize routing, and prepare the application for enterprise features such as authentication and monitoring.

---

### What changed after introducing the Gateway?

Clients stopped communicating directly with microservices.

Instead,

all requests now pass through the API Gateway.

---

### Does API Gateway replace Eureka?

No.

Gateway routes client requests.

Eureka performs service discovery.

---

### What services communicate through the Gateway?

External clients communicate through the Gateway.

Internal microservices communicate using OpenFeign and Eureka.

---

### What are the biggest benefits of introducing Gateway?

- Single Entry Point
- Dynamic Routing
- Better Security
- Cleaner Architecture
- Easier Maintenance
- Better Scalability

---

# Summary

Introducing the API Gateway transformed WorkSphere into a more structured and enterprise-ready microservices platform.

Instead of exposing multiple backend services directly to clients, all external requests now pass through a centralized Gateway.

The Gateway works together with Eureka Service Discovery to locate backend services dynamically while providing a foundation for future capabilities such as authentication, authorization, logging, monitoring, and rate limiting.

This architectural improvement makes WorkSphere more scalable, maintainable, and aligned with modern cloud-native application design.

# Best Practices

Developing an API Gateway is more than simply configuring routes.

A well-designed Gateway should remain lightweight, secure, scalable, and easy to maintain.

The following best practices are followed in WorkSphere.

---

## 1. Keep the Gateway Lightweight

The API Gateway should only handle infrastructure-related responsibilities such as:

- Routing
- Authentication
- Authorization
- Logging
- Monitoring
- Request Filtering

Avoid placing business logic inside the Gateway.

Business logic belongs inside microservices.

---

## 2. Never Hardcode Service URLs

❌ Avoid

```
http://localhost:8081
```

✔ Use

```
lb://employee-service
```

Using service names enables dynamic service discovery through Eureka.

---

## 3. Register the Gateway with Eureka

The Gateway should also act as a Eureka Client.

Benefits include:

- Dynamic service discovery
- No hardcoded URLs
- Automatic instance updates

---

## 4. Keep Routing Configuration Centralized

All routes should be maintained inside

```
application.yml
```

This makes adding new microservices simple.

Example

```
Employee Route

Department Route

Payroll Route
```

No Java code changes are required.

---

## 5. Use Meaningful Route IDs

Good

```
employee-service

department-service
```

Avoid

```
route1

test

abc
```

Meaningful names simplify debugging.

---

## 6. Keep URL Patterns Consistent

Good

```
/api/employees/**

/api/departments/**
```

Avoid

```
/emp

/departmentAPI

/getEmployees
```

Consistent URL patterns improve readability.

---

## 7. Centralize Authentication

Authentication should occur in the Gateway.

Instead of validating JWT tokens inside every service,

validate them once.

```
Client

↓

Gateway

↓

JWT Validation

↓

Microservice
```

---

## 8. Log Requests Centrally

Request logging should happen inside the Gateway.

Typical information includes:

- Timestamp
- HTTP Method
- URL
- Client IP
- Response Time
- Status Code

This eliminates duplicate logging code.

---

## 9. Keep Filters Small

Filters should perform only infrastructure tasks.

Examples

✔ Authentication

✔ Logging

✔ Header Validation

Avoid

❌ Business Calculations

❌ Database Operations

❌ Complex Business Rules

---

## 10. Monitor Gateway Performance

Monitor

- Request Count
- Error Rate
- Response Time
- Active Connections

The Gateway is the entry point of the system.

Performance issues here affect every service.

---

## 11. Prepare for Future Growth

Design routes so that new services can be added easily.

Example

```
Employee

Department

Payroll

Notification

Project

Attendance
```

Adding new routes should require minimal changes.

---

## Common Mistakes

❌ Hardcoded URLs

❌ Business logic inside Gateway

❌ Duplicate authentication

❌ Incorrect Route IDs

❌ Missing Eureka Client

❌ Ignoring Gateway Logs

❌ Exposing backend services directly

---

# Summary

Following these best practices keeps WorkSphere scalable, secure, maintainable, and aligned with enterprise microservices architecture.

# Interview Questions

## Basic Questions

### What is an API Gateway?

An API Gateway is a server that acts as the single entry point for all client requests in a microservices architecture.

---

### Why is an API Gateway used?

To centralize routing, security, logging, monitoring, and client communication.

---

### What problems does an API Gateway solve?

- Multiple service URLs
- Tight coupling
- Centralized security
- Easier routing
- Better scalability

---

### What is Routing?

Routing is the process of forwarding a client request to the appropriate backend service.

---

### What are Predicates?

Predicates are conditions that determine whether a request matches a route.

---

### What are Filters?

Filters execute before or after request routing to perform tasks such as authentication, logging, and validation.

---

## Intermediate Questions

### Difference between Gateway and Eureka?

Gateway

- Entry point
- Routes requests
- Applies filters

Eureka

- Service Registry
- Stores service locations
- Enables service discovery

---

### What does `lb://` mean?

It tells Spring Cloud Gateway to use Spring Cloud LoadBalancer with Eureka Service Discovery.

---

### Why doesn't Gateway use localhost?

Because service locations should be dynamic.

Using `lb://service-name` supports:

- Scalability
- Environment independence
- Load balancing

---

### Difference between Route Filter and Global Filter?

Route Filter

Applies only to one route.

Global Filter

Applies to every request.

---

### Difference between Pre Filter and Post Filter?

Pre Filter

Executes before forwarding the request.

Post Filter

Executes after receiving the response.

---

### Does Gateway perform Load Balancing?

Gateway itself does not.

It delegates instance selection to Spring Cloud LoadBalancer.

---

### Can Gateway work without Eureka?

Yes.

Using fixed URLs.

However,

dynamic service discovery requires Eureka.

---

## WorkSphere Questions

### Why did WorkSphere introduce Gateway?

To provide a single entry point and simplify client communication.

---

### Why introduce Gateway after Eureka?

Eureka solves service discovery between microservices.

Gateway solves client-to-service communication.

---

### What responsibilities belong to Gateway?

- Routing
- Authentication
- Authorization
- Logging
- Monitoring
- Header Manipulation
- Request Validation

---

### Should business logic be written in Gateway?

No.

Business logic belongs inside microservices.

---

### Which WorkSphere module acts as Gateway?

```
api-gateway
```

---

### Which services currently communicate through Gateway?

External Clients

↓

Gateway

↓

Employee Service

↓

Department Service

---

### What future features will Gateway support?

- JWT Authentication
- Rate Limiting
- Request Logging
- Correlation IDs
- Performance Monitoring
- Header Validation


# Summary

The API Gateway is a fundamental component of the WorkSphere microservices architecture.

Before introducing the Gateway, clients communicated directly with individual microservices, resulting in multiple service URLs, tight coupling, and increased maintenance.

By introducing Spring Cloud Gateway, WorkSphere established a single entry point for all client communication.

The Gateway works closely with Eureka Service Discovery to dynamically locate backend services and route requests without exposing internal service locations.

Throughout this document, we explored:

- The purpose of an API Gateway
- Why WorkSphere needed it
- Architecture before and after Gateway
- Request flow
- Gateway Routing
- Route Predicates
- Gateway Filters
- Configuration in WorkSphere
- Integration with Eureka
- Best Practices
- Common Interview Questions

The API Gateway prepares WorkSphere for future enterprise features such as:

- JWT Authentication
- Authorization
- Rate Limiting
- Distributed Logging
- Monitoring
- API Versioning
- Docker Deployment
- Kubernetes
- Cloud-native Scaling

Combined with Eureka, the API Gateway transforms WorkSphere into a scalable, maintainable, and enterprise-ready microservices platform that follows modern cloud-native architecture principles.