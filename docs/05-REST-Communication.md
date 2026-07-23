# 05 - REST Communication

---

# Table of Contents

1. Introduction
2. What is REST?
3. Why REST in Microservices?
4. HTTP Protocol
5. HTTP Methods
6. HTTP Status Codes
7. Request & Response Structure
8. JSON Communication
9. REST API Best Practices
10. RestClient
11. OpenFeign
12. RestClient vs OpenFeign
13. Error Handling
14. Best Practices
15. Interview Questions

---

# Introduction

Modern applications are no longer built as a single large application (Monolith). Instead, they are divided into multiple independent services called **Microservices**.

Each microservice is responsible for a specific business capability and runs independently.

For example, in WorkSphere:

- Employee Service manages employee-related operations.
- Department Service manages department-related operations.
- Eureka Server handles service discovery.
- API Gateway acts as the entry point for all client requests.

Since each service runs independently, they cannot directly call each other's methods.

Instead, they communicate over the network using **REST APIs**.

This document explains the REST communication mechanism used throughout the WorkSphere project and the technologies adopted to implement it.

---

# Learning Objectives

After reading this document, you will understand:

- What REST is
- Why REST is used in Microservices
- HTTP communication fundamentals
- Request and Response lifecycle
- REST API design principles
- Spring RestClient
- Spring OpenFeign
- Differences between RestClient and OpenFeign
- Best practices for REST communication
- Enterprise implementation strategies

---

# REST Communication in WorkSphere

The following services communicate using REST APIs.

```

Client

↓

API Gateway

↓

Employee Service

↓

Department Service

```

Example

When the client requests employee details,

Employee Service fetches employee information from its own database.

If department information is also required,

Employee Service sends a REST request to Department Service.

Department Service returns the department information.

Employee Service combines both responses and sends a single response back to the client.

This communication happens entirely through REST APIs.

---

# Why REST?

REST has become the standard communication protocol for modern distributed applications because it is:

- Lightweight
- Platform independent
- Language independent
- Easy to understand
- Easy to integrate
- Stateless
- Scalable

Most enterprise microservices today communicate using REST.

---

# REST Communication Flow

```

Client

↓

HTTP Request

↓

Employee Service

↓

HTTP Request

↓

Department Service

↓

HTTP Response

↓

Employee Service

↓

HTTP Response

↓

Client

```

Every communication between services follows this request-response model.

---

# Technologies Used in WorkSphere

Throughout this project, REST communication is implemented using:

| Technology | Purpose |
|------------|---------|
| Spring MVC | Build REST APIs |
| Spring Boot | Microservice Framework |
| RestClient | Synchronous HTTP Client |
| OpenFeign | Declarative REST Client |
| Eureka | Service Discovery |
| API Gateway | Centralized Routing |
| Jackson | JSON Serialization & Deserialization |

As the project evolves, each technology improves the communication architecture by reducing coupling and increasing scalability.

---

# Summary

REST is the foundation of communication between the WorkSphere microservices.

Instead of invoking methods directly, services communicate using HTTP requests and JSON responses.

This approach enables:

- Independent deployment
- Better scalability
- Loose coupling
- Cloud readiness
- Technology independence

The following sections explain REST communication in detail, starting with the fundamentals of REST architecture.

# What is REST?

## Definition

REST (Representational State Transfer) is an architectural style used for designing distributed systems and web services.

It was introduced by **Roy Fielding** in his Ph.D. dissertation in the year **2000**.

Unlike SOAP, REST is **not a protocol**. Instead, it is a set of architectural principles and constraints that guide how web services should communicate.

REST allows two independent applications to exchange information over the HTTP protocol in a lightweight, scalable, and platform-independent manner.

---

## Why was REST introduced?

Before REST became popular, many enterprise applications used SOAP (Simple Object Access Protocol) for communication.

Although SOAP was feature-rich, it had several drawbacks:

- XML-based communication (large payloads)
- Complex message structure
- Difficult to understand
- High processing overhead
- Slower performance

REST was introduced to provide a simpler, faster, and more scalable way for applications to communicate.

Today, REST has become the de facto standard for communication in modern web applications and microservices.

---

# REST in WorkSphere

WorkSphere is built using a microservices architecture.

Each service is deployed independently.

Examples:

- Employee Service
- Department Service
- Eureka Server
- API Gateway

Since these services are independent applications, they cannot directly invoke each other's methods.

Instead, they communicate through REST APIs.

Example

Client requests employee details.

```
GET /api/v1/employees/1/rest
```

Employee Service fetches employee information from its own database.

To obtain department details, Employee Service sends another REST request.

```
GET /api/v1/departments/2
```

Department Service returns the department information as JSON.

Employee Service combines both responses and returns a single response to the client.

This entire interaction follows REST principles.

---

# Characteristics of REST

REST has several important characteristics.

## 1. Client-Server Architecture

REST separates the client from the server.

The client is responsible for sending requests.

The server is responsible for processing requests and returning responses.

Example

```
Browser

↓

Employee Service
```

The browser never accesses the database directly.

Everything happens through REST APIs.

Benefits

- Better separation of concerns
- Independent development
- Easier maintenance
- Better scalability

---

## 2. Stateless Communication

REST is stateless.

This means the server does **not** remember previous requests.

Every request must contain all the information required to process it.

Example

Request 1

```
GET /employees/1
```

Request 2

```
GET /employees/2
```

The second request is completely independent of the first.

The server does not maintain any session between requests.

Benefits

- Easy horizontal scaling
- Better performance
- Fault tolerance
- Simpler architecture

---

## 3. Resource-Based Architecture

REST focuses on **resources** rather than actions.

A resource is any business entity exposed through an API.

Examples in WorkSphere

Employee

Department

Project (Future)

Attendance (Future)

Resources are identified using URLs.

Good Example

```
/employees

/departments
```

Poor Example

```
/getEmployee

/createDepartment
```

REST recommends using nouns instead of verbs.

---

## 4. Uniform Interface

REST APIs follow consistent conventions.

For example

Retrieve employee

```
GET /employees/1
```

Create employee

```
POST /employees
```

Update employee

```
PUT /employees/1
```

Delete employee

```
DELETE /employees/1
```

A consistent API design makes applications easier to learn and maintain.

---

## 5. Representation

REST does not return database objects directly.

Instead, resources are represented in a transferable format.

Common formats

- JSON
- XML

WorkSphere uses JSON because it is lightweight and widely supported.

Example Response

```json
{
    "id": 1,
    "firstName": "Sakshi",
    "departmentId": 2
}
```

---

## 6. Layered Architecture

A client does not need to know whether it is communicating directly with the service or through intermediate components.

Example in WorkSphere

```
Client

↓

API Gateway

↓

Employee Service

↓

Department Service
```

The client only communicates with the API Gateway.

It is unaware of the internal architecture.

Benefits

- Better security
- Centralized routing
- Easier monitoring
- Better scalability

---

# Why REST is Perfect for Microservices

Microservices require communication between independent applications.

REST provides:

- Standard HTTP communication
- Platform independence
- Language independence
- Stateless architecture
- Easy integration
- High scalability

For this reason, REST is the preferred communication mechanism in most enterprise microservice applications.

---

# REST vs Method Calls

In a monolithic application

```
EmployeeService

↓

DepartmentService.getDepartment()
```

The services exist within the same application and can directly invoke methods.

In a microservices architecture

```
Employee Service

↓

HTTP Request

↓

Department Service
```

The services run in separate processes.

Direct method calls are not possible.

Communication must occur over HTTP.

---

# Advantages of REST

REST provides several advantages.

- Lightweight communication
- Faster than SOAP
- Human-readable JSON
- Platform independent
- Language independent
- Stateless
- Easy integration
- Supports caching
- Scalable
- Cloud friendly

---

# Limitations of REST

Although REST is widely used, it has some limitations.

- Multiple API calls may be required to fetch related data.
- No built-in service discovery.
- No automatic retries.
- No guaranteed message delivery.
- Synchronous communication may increase latency.

Many of these limitations are addressed by additional technologies such as:

- OpenFeign
- Eureka
- API Gateway
- Kafka
- Resilience4j

These technologies are introduced later in the WorkSphere project.

---

# Real-Life Example

Consider an online shopping application.

Customer places an order.

The Order Service needs to communicate with:

- Inventory Service
- Payment Service
- Notification Service

Each communication occurs using REST APIs.

```
Order Service

↓

Inventory Service

↓

Payment Service

↓

Notification Service
```

The same communication pattern is followed in WorkSphere between Employee Service and Department Service.

---

# Interview Questions

### What is REST?

REST (Representational State Transfer) is an architectural style for designing distributed systems that communicate over HTTP.

---

### Is REST a protocol?

No.

REST is an architectural style, not a protocol.

---

### Who introduced REST?

Roy Fielding in the year 2000.

---

### Why is REST popular?

Because it is lightweight, stateless, scalable, easy to understand, and widely supported.

---

### Why is REST suitable for Microservices?

Microservices are independent applications that need a standard communication mechanism.

REST provides a simple, platform-independent, and scalable solution.

---

### What format does REST commonly use?

JSON is the most commonly used data format.

---

### What is a resource in REST?

A resource represents a business entity exposed through an API.

Examples:

- Employee
- Department
- Project

---

# Summary

REST is the foundation of communication in the WorkSphere project.

It enables independent microservices to exchange information using standard HTTP requests and JSON responses.

Throughout the remainder of this document, we will explore how REST communication is implemented using Spring Boot technologies such as RestClient and OpenFeign, and how these integrate with Eureka and the API Gateway to build an enterprise-grade microservices architecture.

# HTTP Protocol

## What is HTTP?

HTTP (HyperText Transfer Protocol) is an application-layer protocol used for communication between a client and a server over the internet or a private network.

It defines a standard way for applications to send requests and receive responses.

In simple words,

> **HTTP is the language through which two applications communicate.**

In WorkSphere, all communication between microservices happens using HTTP.

Example:

```
Employee Service
        │
        │ HTTP Request
        ▼
Department Service
```

Similarly,

```
Browser
      │
      │ HTTP Request
      ▼
API Gateway
      │
      ▼
Employee Service
```

Without HTTP, our services would not be able to exchange information.

---

# Why HTTP?

HTTP provides several advantages:

- Standard protocol
- Platform independent
- Language independent
- Lightweight
- Easy to debug
- Supported by every programming language
- Supported by browsers
- Firewall friendly

For these reasons, almost every REST API today uses HTTP.

---

# HTTP Communication Flow

Whenever a client requests data, the following steps occur.

```
Client
   │
   │ 1. HTTP Request
   ▼
Employee Service
   │
   │ 2. Process Request
   │
   │ 3. Database Query
   ▼
Database

Employee Service
   │
   │ 4. HTTP Response
   ▼
Client
```

If another service is involved,

```
Client

↓

Employee Service

↓

HTTP Request

↓

Department Service

↓

HTTP Response

↓

Employee Service

↓

HTTP Response

↓

Client
```

This is exactly what happens in WorkSphere when fetching employee details along with department information.

---

# Components of an HTTP Request

Every HTTP request contains several parts.

```
GET /api/v1/employees/1 HTTP/1.1

Host: localhost:8081

Accept: application/json

Authorization: Bearer Token
```

An HTTP request consists of:

- HTTP Method
- URL
- Headers
- Query Parameters (optional)
- Path Variables (optional)
- Request Body (optional)

---

# HTTP Method

The HTTP method tells the server what action should be performed.

Examples:

```
GET

POST

PUT

DELETE

PATCH
```

We will study each method in detail in the next chapter.

---

# URL

The URL identifies the resource.

Example

```
http://localhost:8081/api/v1/employees/1
```

Let's break it down.

```
http
```

Protocol

```
localhost
```

Host

```
8081
```

Port

```
/api/v1
```

API Version

```
employees
```

Resource

```
1
```

Resource Identifier

---

# Path Variable

A Path Variable identifies a specific resource.

Example

```
GET /employees/1
```

Here,

```
1
```

is the employee ID.

Spring Boot

```java
@GetMapping("/{id}")
public EmployeeResponse getEmployee(
        @PathVariable Long id) {

}
```

If the request is

```
GET /employees/25
```

Spring automatically assigns

```
id = 25
```

---

# Query Parameters

Query parameters provide additional information to the server.

Example

```
GET /employees?page=0&size=10
```

Here,

```
page=0

size=10
```

are query parameters.

Spring Boot

```java
@GetMapping
public EmployeePageResponse getEmployees(

@RequestParam int page,

@RequestParam int size

)
```

---

# HTTP Headers

Headers provide metadata about the request.

Examples

```
Content-Type

Accept

Authorization

Host

User-Agent
```

Example Request

```
GET /employees

Accept: application/json

Authorization: Bearer Token
```

Headers do not contain business data.

Instead, they describe the request.

---

# Request Body

Some requests send additional data.

Example

POST

```json
{
    "firstName":"Sakshi",
    "lastName":"Agrawal",
    "email":"sakshi@gmail.com"
}
```

Spring Boot

```java
@PostMapping

public EmployeeResponse createEmployee(

@RequestBody EmployeeRequest request

)
```

Spring converts JSON into a Java object automatically.

---

# Response Body

The server sends data back to the client.

Example

```json
{
    "id":1,
    "firstName":"Sakshi",
    "departmentId":2
}
```

Spring automatically converts Java objects into JSON.

This process is called **Serialization**.

The opposite process (JSON → Java Object) is called **Deserialization**.

Spring Boot performs both automatically using Jackson.

---

# Request Lifecycle in WorkSphere

Suppose the client requests

```
GET /employees/1/rest
```

Step 1

Client sends request.

↓

Step 2

EmployeeController receives it.

↓

Step 3

EmployeeService executes business logic.

↓

Step 4

DepartmentRestClient sends another HTTP request.

↓

Step 5

DepartmentController processes the request.

↓

Step 6

Department Service returns JSON.

↓

Step 7

Employee Service combines both responses.

↓

Step 8

Spring converts the Java object into JSON.

↓

Step 9

Client receives the response.

---

# HTTP Communication Between Services

When Employee Service calls Department Service,

RestClient internally creates an HTTP request.

```
GET

http://department-service/api/v1/departments/2
```

Department Service processes it.

Returns

```json
{
    "id":2,
    "departmentName":"Engineering"
}
```

RestClient converts the JSON into

```java
DepartmentResponse
```

No manual JSON parsing is required.

---

# Why is HTTP Stateless?

HTTP is a stateless protocol.

This means every request is independent.

Example

```
GET /employees/1
```

followed by

```
GET /employees/2
```

The server does not remember the previous request.

Every request contains everything needed for processing.

Benefits

- Easier scaling
- Better reliability
- Simpler architecture
- Better performance

---

# Enterprise Benefits

HTTP is the foundation of modern distributed systems.

Every enterprise system uses HTTP for:

- REST APIs
- API Gateway
- OAuth Authentication
- JWT Tokens
- OpenFeign
- RestClient
- Kubernetes Ingress
- Cloud APIs

Understanding HTTP is essential for building scalable microservices.

---

# Interview Questions

### What is HTTP?

HTTP (HyperText Transfer Protocol) is an application-layer protocol used for communication between clients and servers.

---

### Why do REST APIs use HTTP?

Because HTTP is lightweight, platform-independent, widely supported, and follows the request-response model.

---

### What are the main parts of an HTTP request?

- HTTP Method
- URL
- Headers
- Path Variables
- Query Parameters
- Request Body

---

### What is the difference between Path Variable and Query Parameter?

Path Variable identifies a specific resource.

Example

```
/employees/10
```

Query Parameters provide additional filtering or options.

Example

```
/employees?page=0&size=10
```

---

### What is Serialization?

Converting a Java object into JSON.

---

### What is Deserialization?

Converting JSON into a Java object.

---

### Why is HTTP called stateless?

Because the server does not remember previous requests. Every request is processed independently.

---

# Summary

HTTP is the communication protocol used by all REST APIs in WorkSphere.

Whenever the client or one microservice communicates with another, an HTTP request is created, processed, and an HTTP response is returned.

Understanding HTTP is the foundation for understanding RestClient, OpenFeign, Eureka, API Gateway, and all enterprise microservice communication.

# HTTP Methods

## What are HTTP Methods?

HTTP methods (also called HTTP verbs) define the action that the client wants the server to perform on a particular resource.

Think of them as commands sent to the server.

Example

```
Client

↓

GET /employees/1

↓

Employee Service
```

Here, **GET** tells the server that the client wants to retrieve employee information.

Similarly,

```
POST /employees
```

means the client wants to create a new employee.

Each HTTP method has a specific purpose and should be used according to REST principles.

---

# HTTP Methods Used in WorkSphere

WorkSphere primarily uses the following HTTP methods:

| Method | Purpose |
|---------|----------|
| GET | Retrieve Data |
| POST | Create Data |
| PUT | Update Entire Resource |
| PATCH | Partially Update Resource (Future) |
| DELETE | Delete Resource |

---

# GET Method

## Purpose

The GET method is used to retrieve data from the server.

It should never modify data.

### Example

Retrieve Employee

```
GET /api/v1/employees/1
```

Retrieve Department

```
GET /api/v1/departments/2
```

Retrieve Employees with Pagination

```
GET /api/v1/employees?page=0&size=10
```

Spring Boot Example

```java
@GetMapping("/{id}")
public EmployeeResponse getEmployeeById(
        @PathVariable Long id) {

    return employeeService.getEmployeeById(id);
}
```

Flow

```
Client

↓

GET /employees/1

↓

Employee Controller

↓

Employee Service

↓

Database

↓

Employee Response

↓

Client
```

### Characteristics

- Reads data only
- No modification
- Safe
- Idempotent
- Most frequently used HTTP method

---

# POST Method

## Purpose

POST is used to create a new resource.

Unlike GET, POST changes the server state.

### Example

Create Employee

```
POST /api/v1/employees
```

Request Body

```json
{
  "firstName":"Sakshi",
  "lastName":"Agrawal",
  "email":"sakshi@gmail.com",
  "salary":65000,
  "departmentId":2
}
```

Spring Boot Example

```java
@PostMapping
public EmployeeResponse createEmployee(
        @RequestBody EmployeeRequest request) {

    return employeeService.createEmployee(request);
}
```

Flow

```
Client

↓

POST Request

↓

Employee Controller

↓

Employee Service

↓

Database Insert

↓

Response

↓

Client
```

### Characteristics

- Creates new resource
- Changes server state
- Not idempotent

Calling the same POST request multiple times creates multiple records.

Example

```
POST Employee

↓

Employee #1 Created

↓

POST Employee

↓

Employee #2 Created
```

---

# PUT Method

## Purpose

PUT updates an existing resource completely.

The client sends the complete updated object.

### Example

```
PUT /api/v1/employees/1
```

Request

```json
{
  "firstName":"Sakshi",
  "lastName":"Agrawal",
  "email":"sakshi@gmail.com",
  "salary":70000,
  "departmentId":3
}
```

Spring Boot Example

```java
@PutMapping("/{id}")
public EmployeeResponse updateEmployee(
        @PathVariable Long id,
        @RequestBody EmployeeRequest request) {

    return employeeService.updateEmployee(id, request);
}
```

### Characteristics

- Updates entire object
- Idempotent

Calling PUT multiple times with the same data results in the same final state.

---

# PATCH Method

## Purpose

PATCH updates only selected fields.

Example

Employee

Before

```json
{
    "salary":65000
}
```

PATCH Request

```json
{
    "salary":70000
}
```

Only the salary changes.

The remaining fields remain unchanged.

### Current Status in WorkSphere

PATCH is not implemented yet.

It will be introduced in a future version.

---

# DELETE Method

## Purpose

DELETE removes a resource.

Example

```
DELETE /api/v1/employees/1
```

Spring Boot Example

```java
@DeleteMapping("/{id}")
public void deleteEmployee(
        @PathVariable Long id) {

    employeeService.deleteEmployee(id);
}
```

Flow

```
Client

↓

DELETE

↓

Employee Service

↓

Database Delete

↓

204 No Content

↓

Client
```

### Characteristics

- Removes resource
- Idempotent

Calling DELETE multiple times results in the same final state.

The resource remains deleted.

---

# Safe Methods

A safe method does not modify server data.

Safe Methods

```
GET
```

Unsafe Methods

```
POST

PUT

PATCH

DELETE
```

GET only reads information.

The others modify server data.

---

# Idempotent Methods

A method is idempotent if performing the same request multiple times produces the same final result.

Example

PUT

```
PUT Employee Salary = 70000

↓

Salary = 70000

↓

PUT Employee Salary = 70000

↓

Still 70000
```

Result does not change.

Therefore PUT is idempotent.

DELETE

```
DELETE Employee

↓

Employee Deleted

↓

DELETE Employee Again

↓

Still Deleted
```

DELETE is also idempotent.

POST

```
POST Employee

↓

Employee Created

↓

POST Again

↓

Another Employee Created
```

POST is **not** idempotent.

---

# HTTP Methods in WorkSphere

| API | HTTP Method |
|------|-------------|
| Create Employee | POST |
| Get Employee | GET |
| Get All Employees | GET |
| Update Employee | PUT |
| Delete Employee | DELETE |
| Get Department | GET |
| Create Department | POST |
| Update Department | PUT |
| Delete Department | DELETE |

---

# Best Practices

✔ Use GET for reading data.

✔ Use POST for creating new resources.

✔ Use PUT for updating complete resources.

✔ Use PATCH for partial updates.

✔ Use DELETE only for deletion.

Avoid using POST for updates.

Avoid using GET to modify data.

Follow REST conventions consistently.

---

# Interview Questions

### What is the difference between GET and POST?

GET retrieves data and should not modify server state.

POST creates new resources and changes server state.

---

### Why is GET called a safe method?

Because it only retrieves information without modifying any data.

---

### What is the difference between PUT and PATCH?

PUT updates the complete resource.

PATCH updates only specific fields.

---

### Which HTTP methods are idempotent?

- GET
- PUT
- DELETE

---

### Why is POST not idempotent?

Because each POST request creates a new resource.

---

### Can GET have a request body?

According to HTTP standards, GET requests generally should not have a request body. Data should be passed using path variables or query parameters.

---

### Which HTTP method is used to update only one field?

PATCH.

---

### Which HTTP methods have you implemented in WorkSphere?

Currently, WorkSphere implements:

- GET
- POST
- PUT
- DELETE

PATCH will be added in a future version.

---

# Summary

HTTP methods define the action that the client wants the server to perform.

WorkSphere follows standard REST conventions by using:

- GET for retrieval
- POST for creation
- PUT for complete updates
- DELETE for deletion

Using the correct HTTP methods makes APIs more intuitive, maintainable, and compliant with REST principles.

# HTTP Status Codes

## What are HTTP Status Codes?

Whenever a client sends an HTTP request to a server, the server processes the request and returns an HTTP response.

Along with the response body, the server also returns a **Status Code**.

An HTTP Status Code is a three-digit number that indicates whether the request was successfully processed or if an error occurred.

Example

```
Client

↓

GET /api/v1/employees/1

↓

Employee Service

↓

200 OK
```

The status code is the first thing a client checks before processing the response body.

---

# Why are Status Codes Important?

Status codes help the client understand the result of the request without reading the response body.

For example,

```
200
```

means the request was successful.

```
404
```

means the requested resource was not found.

```
500
```

means something went wrong inside the server.

Using proper status codes makes APIs easier to understand and integrate.

---

# Categories of HTTP Status Codes

HTTP status codes are divided into five categories.

| Range | Category | Meaning |
|--------|----------|---------|
| 1xx | Informational | Request received |
| 2xx | Success | Request completed successfully |
| 3xx | Redirection | Further action required |
| 4xx | Client Error | Problem in client's request |
| 5xx | Server Error | Problem inside the server |

In REST APIs, we mainly use **2xx**, **4xx**, and **5xx** status codes.

---

# 2xx – Success Responses

A 2xx status code means the server successfully processed the request.

---

## 200 OK

### Purpose

Returned when the request is processed successfully.

### Examples in WorkSphere

Get Employee

```
GET /api/v1/employees/1
```

Response

```
200 OK
```

Get Department

```
GET /api/v1/departments/2
```

Response

```
200 OK
```

---

## 201 Created

### Purpose

Returned after successfully creating a new resource.

Example

```
POST /api/v1/employees
```

Response

```
201 Created
```

The response usually contains the newly created resource.

Current Note

At present, WorkSphere returns **200 OK** for create operations because we haven't explicitly set the response status.

In future, we can improve this by using

```java
@ResponseStatus(HttpStatus.CREATED)
```

or

```java
ResponseEntity.status(HttpStatus.CREATED)
```

---

## 204 No Content

### Purpose

Returned when the request is successful but no response body is required.

Example

```
DELETE /api/v1/employees/1
```

Response

```
204 No Content
```

Current Note

Currently, WorkSphere returns **200 OK** for delete operations.

Later we can improve this by returning **204 No Content**.

---

# 4xx – Client Errors

A 4xx status code means the client sent an invalid request.

---

## 400 Bad Request

### Purpose

Returned when the request is invalid.

Examples

- Missing required field
- Invalid JSON
- Validation failure
- Invalid data type

Example

```json
{
    "firstName":"",
    "email":"invalid-email"
}
```

Spring Validation detects the problem.

Response

```
400 Bad Request
```

In WorkSphere,

Bean Validation produces this response automatically.

---

## 401 Unauthorized

### Purpose

Returned when authentication is required.

Example

```
Authorization header missing
```

Response

```
401 Unauthorized
```

Current Status

Security is not implemented yet.

This status code will be used after adding Spring Security and JWT Authentication.

---

## 403 Forbidden

### Purpose

Returned when the user is authenticated but does not have permission.

Example

Employee tries to access an Admin API.

Response

```
403 Forbidden
```

Future Usage

Will be introduced after implementing Role-Based Access Control.

---

## 404 Not Found

### Purpose

Returned when the requested resource does not exist.

Example

```
GET /employees/100
```

Suppose Employee 100 does not exist.

Our application throws

```java
ResourceNotFoundException
```

The Global Exception Handler catches it.

Response

```json
{
    "status":404,
    "message":"Employee not found"
}
```

This is one of the most commonly used error responses in WorkSphere.

---

## 405 Method Not Allowed

### Purpose

Returned when the endpoint exists but the HTTP method is incorrect.

Example

```
POST /employees/1
```

Suppose only GET is allowed.

Spring returns

```
405 Method Not Allowed
```

---

# 5xx – Server Errors

A 5xx status code indicates that the problem occurred inside the server.

---

## 500 Internal Server Error

### Purpose

Returned when an unexpected exception occurs.

Example

Database connection failure

NullPointerException

Programming error

Invalid server configuration

Example from WorkSphere

When Employee Service tried to call Department Service using an incorrect URL,

Spring returned

```
500 Internal Server Error
```

Another example

Department Service throws an exception.

Employee Service receives

```
500 Internal Server Error
```

---

## 503 Service Unavailable

### Purpose

Returned when the requested service is temporarily unavailable.

Example

Department Service is down.

Employee Service tries to call it.

Possible Response

```
503 Service Unavailable
```

Future Usage

After introducing Resilience4j or Circuit Breaker, this status code may be returned when downstream services become unavailable.

---

# Status Codes Used in WorkSphere

| Operation | Expected Status |
|------------|-----------------|
| Get Employee | 200 OK |
| Get Department | 200 OK |
| Create Employee | 201 Created (Future Improvement) |
| Update Employee | 200 OK |
| Delete Employee | 204 No Content (Future Improvement) |
| Validation Failure | 400 Bad Request |
| Employee Not Found | 404 Not Found |
| Department Not Found | 404 Not Found |
| Unexpected Exception | 500 Internal Server Error |

---

# Best Practices

✔ Return meaningful status codes.

✔ Do not return 200 OK for every request.

✔ Use 404 when resources do not exist.

✔ Use 400 for validation failures.

✔ Use 500 only for unexpected server errors.

✔ Keep status codes consistent across all services.

---

# Interview Questions

### What is an HTTP Status Code?

An HTTP Status Code is a three-digit number returned by the server indicating the outcome of an HTTP request.

---

### What is the difference between 400 and 500?

400 indicates the client sent an invalid request.

500 indicates an unexpected error occurred inside the server.

---

### When should 404 be returned?

When the requested resource does not exist.

---

### What is the difference between 401 and 403?

401 means the user is not authenticated.

403 means the user is authenticated but does not have permission.

---

### Why is 201 preferred after POST?

Because a new resource has been successfully created.

---

### Why is 204 preferred after DELETE?

Because the operation succeeded and no response body is required.

---

# Summary

HTTP Status Codes provide a standardized way to communicate the outcome of an HTTP request.

Using appropriate status codes makes REST APIs more predictable, easier to integrate, and compliant with REST best practices.

WorkSphere currently uses standard success and error responses and will be further enhanced as additional features such as Spring Security, Circuit Breakers, and advanced exception handling are introduced.

# Request & Response Structure

## Introduction

REST APIs communicate using a **Request-Response** model.

The client sends an HTTP request to the server.

The server processes the request and returns an HTTP response.

Every REST API in WorkSphere follows this architecture.

```
Client
      │
      │ HTTP Request
      ▼
Employee Service
      │
      │ Process Request
      ▼
Database / Another Microservice
      │
      │ HTTP Response
      ▼
Client
```

Without this request-response cycle, REST communication cannot happen.

---

# HTTP Request

An HTTP Request is sent by the client to perform an operation on a resource.

A request contains several components.

```
HTTP Method

+

URL

+

Headers

+

Path Variables

+

Query Parameters

+

Request Body (Optional)
```

Example

```
POST /api/v1/employees

Content-Type: application/json

{
    "firstName":"Sakshi",
    "lastName":"Agrawal",
    "email":"sakshi@gmail.com",
    "salary":65000,
    "departmentId":2
}
```

---

# Components of an HTTP Request

## 1. HTTP Method

Specifies the action.

Examples

```
GET

POST

PUT

DELETE

PATCH
```

Example

```
GET /employees/1
```

means

Retrieve Employee.

---

## 2. URL

Identifies the resource.

Example

```
http://localhost:8081/api/v1/employees/1
```

Breakdown

```
http
```

Protocol

```
localhost
```

Host

```
8081
```

Port

```
/api/v1
```

API Version

```
employees
```

Resource

```
1
```

Resource Identifier

---

## 3. Headers

Headers provide additional information about the request.

Example

```
Content-Type: application/json

Accept: application/json

Authorization: Bearer Token
```

Common Headers

| Header | Purpose |
|----------|----------|
| Content-Type | Type of request body |
| Accept | Expected response format |
| Authorization | Authentication token |
| User-Agent | Client information |
| Host | Server information |

---

## 4. Path Variables

Used to identify a specific resource.

Example

```
GET /employees/15
```

Spring Boot

```java
@GetMapping("/{id}")
public EmployeeResponse getEmployee(
        @PathVariable Long id)
```

Spring automatically maps

```
15
```

to

```
id
```

---

## 5. Query Parameters

Used to filter or customize the request.

Example

```
GET /employees?page=0&size=5&sortBy=salary
```

Spring Boot

```java
@GetMapping
public EmployeePageResponse getEmployees(

@RequestParam int page,

@RequestParam int size,

@RequestParam String sortBy

)
```

Query parameters are commonly used for

- Pagination
- Filtering
- Searching
- Sorting

---

## 6. Request Body

Used when sending data to the server.

Usually used with

```
POST

PUT

PATCH
```

Example

```json
{
    "firstName":"Sakshi",
    "lastName":"Agrawal",
    "email":"sakshi@gmail.com",
    "salary":65000,
    "departmentId":2
}
```

Spring Boot automatically converts this JSON into

```java
EmployeeRequest
```

using Jackson.

---

# HTTP Response

After processing the request, the server sends an HTTP Response.

A response contains

```
Status Code

+

Headers

+

Response Body
```

Example

```json
{
    "id":1,
    "firstName":"Sakshi",
    "lastName":"Agrawal",
    "email":"sakshi@gmail.com",
    "salary":65000,
    "departmentId":2
}
```

---

# Components of an HTTP Response

## 1. Status Code

Indicates whether the request succeeded.

Examples

```
200 OK

201 Created

400 Bad Request

404 Not Found

500 Internal Server Error
```

We discussed these in the previous section.

---

## 2. Response Headers

Provide metadata.

Examples

```
Content-Type

Content-Length

Date

Server
```

These help the client interpret the response correctly.

---

## 3. Response Body

Contains the actual data.

Example

```json
{
    "id":1,
    "firstName":"Sakshi",
    "departmentId":2
}
```

Spring Boot converts Java objects into JSON automatically.

---

# Request-Response Lifecycle in WorkSphere

Let's understand what happens when the client requests

```
GET /api/v1/employees/1/rest
```

### Step 1

The client sends the request.

↓

### Step 2

EmployeeController receives it.

↓

### Step 3

EmployeeService retrieves employee details from MySQL.

↓

### Step 4

EmployeeService calls DepartmentRestClient.

↓

### Step 5

DepartmentRestClient sends an HTTP request.

```
GET /api/v1/departments/2
```

↓

### Step 6

DepartmentController processes the request.

↓

### Step 7

DepartmentService retrieves department details.

↓

### Step 8

Department Service returns JSON.

↓

### Step 9

Employee Service combines

Employee

+

Department

↓

### Step 10

Spring converts the Java object into JSON.

↓

### Step 11

Client receives the response.

---

# Serialization and Deserialization

Spring Boot automatically converts data between Java Objects and JSON.

## Serialization

Java Object

↓

JSON

Example

```java
EmployeeResponse
```

becomes

```json
{
    "id":1,
    "firstName":"Sakshi"
}
```

---

## Deserialization

JSON

↓

Java Object

Example

```json
{
    "firstName":"Sakshi"
}
```

becomes

```java
EmployeeRequest
```

Jackson performs both operations automatically.

---

# WorkSphere Example

Request

```
POST /api/v1/employees
```

Request Body

```json
{
    "firstName":"Sakshi",
    "lastName":"Agrawal",
    "email":"sakshi@gmail.com",
    "salary":65000,
    "departmentId":2
}
```

↓

Spring converts JSON

↓

EmployeeRequest

↓

Employee Entity

↓

Database

↓

EmployeeResponse

↓

JSON

↓

Client

Notice that at no point do we manually parse JSON.

Spring Boot handles it automatically.

---

# Best Practices

✔ Keep request bodies simple.

✔ Use DTOs instead of Entities.

✔ Return meaningful responses.

✔ Use proper HTTP status codes.

✔ Validate request data.

✔ Avoid exposing database entities directly.

✔ Keep request and response formats consistent.

---

# Interview Questions

### What is an HTTP Request?

An HTTP Request is a message sent by the client to perform an operation on a server resource.

---

### What are the main components of an HTTP Request?

- HTTP Method
- URL
- Headers
- Path Variables
- Query Parameters
- Request Body

---

### What is an HTTP Response?

An HTTP Response is the server's reply containing a status code, headers, and an optional response body.

---

### What is the difference between Request Body and Query Parameters?

Query Parameters are used for filtering or searching.

Request Body is used to send complete object data.

---

### What is Serialization?

Converting a Java object into JSON.

---

### What is Deserialization?

Converting JSON into a Java object.

---

### Which library performs JSON conversion in Spring Boot?

Jackson.

---

# Summary

Every REST API follows a request-response model.

The client sends an HTTP request containing methods, URLs, headers, and optional data.

The server processes the request, performs business logic, and returns a structured HTTP response.

Spring Boot automatically handles JSON serialization and deserialization, making REST API development much simpler.

# JSON Communication

## Introduction

JSON (JavaScript Object Notation) is the standard data exchange format used by REST APIs.

Whenever two microservices communicate in WorkSphere, they exchange information in JSON format.

Although our application uses Java objects internally, these objects cannot be sent directly over the network.

Instead, Spring Boot automatically converts Java objects into JSON before sending them and converts JSON back into Java objects after receiving them.

This automatic conversion makes REST communication simple and efficient.

---

# What is JSON?

JSON stands for **JavaScript Object Notation**.

It is a lightweight, text-based format used to exchange data between applications.

Example

```json
{
    "id": 1,
    "firstName": "Sakshi",
    "lastName": "Agrawal",
    "email": "sakshi@gmail.com",
    "salary": 65000,
    "departmentId": 2
}
```

Although JSON originated from JavaScript, today it is supported by almost every programming language, including Java, Python, C#, Go, Node.js, and many others.

---

# Why JSON?

Modern applications prefer JSON because it is:

- Lightweight
- Human-readable
- Easy to parse
- Language independent
- Platform independent
- Faster than XML
- Supported by all major frameworks

For these reasons, WorkSphere uses JSON for communication between all microservices.

---

# JSON in WorkSphere

Whenever the client calls an API,

```
Client

↓

JSON Request

↓

Employee Service

↓

Java Object

↓

Business Logic

↓

Java Object

↓

JSON Response

↓

Client
```

The same process happens between Employee Service and Department Service.

```
Employee Service

↓

JSON Request

↓

Department Service

↓

JSON Response

↓

Employee Service
```

Notice that **all communication happens using JSON**.

---

# JSON Request Example

Suppose the client creates a new employee.

HTTP Request

```
POST /api/v1/employees
```

Request Body

```json
{
    "firstName": "Sakshi",
    "lastName": "Agrawal",
    "email": "sakshi@gmail.com",
    "salary": 65000,
    "departmentId": 2
}
```

Spring automatically converts this JSON into

```java
EmployeeRequest
```

using Jackson.

---

# JSON Response Example

Suppose the employee is created successfully.

Spring returns

```java
EmployeeResponse
```

Jackson converts it into

```json
{
    "id": 1,
    "firstName": "Sakshi",
    "lastName": "Agrawal",
    "email": "sakshi@gmail.com",
    "salary": 65000,
    "departmentId": 2
}
```

The client receives JSON instead of Java objects.

---

# Serialization

Serialization is the process of converting a Java Object into JSON.

```
Java Object

↓

Jackson

↓

JSON
```

Example

```java
EmployeeResponse response =
new EmployeeResponse(
1L,
"Sakshi",
"Agrawal",
"sakshi@gmail.com",
65000,
2L
);
```

becomes

```json
{
    "id":1,
    "firstName":"Sakshi",
    "lastName":"Agrawal",
    "email":"sakshi@gmail.com",
    "salary":65000,
    "departmentId":2
}
```

This process happens automatically when returning data from a controller.

---

# Deserialization

Deserialization is the reverse process.

```
JSON

↓

Jackson

↓

Java Object
```

Example

```json
{
    "firstName":"Sakshi",
    "salary":65000
}
```

becomes

```java
EmployeeRequest request;
```

Spring automatically performs this conversion before invoking the controller method.

---

# Jackson in Spring Boot

Spring Boot uses the **Jackson** library for JSON processing.

Jackson is responsible for

- Serialization
- Deserialization
- Field Mapping
- Date Formatting
- Null Handling

Developers usually don't need to invoke Jackson manually because Spring Boot configures it automatically.

---

# How Spring Converts JSON Automatically

Suppose the controller contains

```java
@PostMapping
public EmployeeResponse createEmployee(
        @RequestBody EmployeeRequest request) {

    return employeeService.createEmployee(request);
}
```

Step 1

Client sends JSON.

↓

Step 2

Spring receives the request.

↓

Step 3

Jackson converts JSON into

```
EmployeeRequest
```

↓

Step 4

Business logic executes.

↓

Step 5

Controller returns

```
EmployeeResponse
```

↓

Step 6

Jackson converts it into JSON.

↓

Step 7

Client receives JSON.

No manual conversion is required.

---

# JSON Communication Between Microservices

Suppose Employee Service calls Department Service.

Employee Service

↓

RestClient

↓

HTTP Request

↓

Department Service

↓

DepartmentResponse

↓

JSON

↓

Employee Service

↓

DepartmentResponse Object

Again, Jackson performs all conversions automatically.

---

# Why We Use DTOs Instead of Entities

WorkSphere uses DTOs for REST communication.

Examples

```
EmployeeRequest

EmployeeResponse

DepartmentResponse

EmployeeWithDepartmentResponse
```

Instead of

```
Employee Entity

Department Entity
```

Advantages

- Better Security
- Loose Coupling
- Hide Database Structure
- Flexible API Design
- Easier Versioning
- Better Validation

This is considered an enterprise best practice.

---

# JSON vs XML

| JSON | XML |
|------|-----|
| Lightweight | Heavy |
| Less Data | More Data |
| Easy to Read | Verbose |
| Faster | Slower |
| Preferred for REST | Common in SOAP |

Example

JSON

```json
{
    "id":1,
    "name":"Engineering"
}
```

XML

```xml
<department>
    <id>1</id>
    <name>Engineering</name>
</department>
```

Modern REST APIs prefer JSON.

---

# Best Practices

✔ Use DTOs for JSON communication.

✔ Keep JSON structures simple.

✔ Use meaningful field names.

✔ Avoid exposing database entities.

✔ Validate incoming JSON.

✔ Return consistent response formats.

---

# Interview Questions

### What is JSON?

JSON (JavaScript Object Notation) is a lightweight text-based data format used to exchange information between applications.

---

### Why do REST APIs use JSON?

Because it is lightweight, easy to read, easy to parse, platform independent, and faster than XML.

---

### Which library does Spring Boot use for JSON conversion?

Jackson.

---

### What is Serialization?

Serialization is converting a Java object into JSON.

---

### What is Deserialization?

Deserialization is converting JSON into a Java object.

---

### Why do we use DTOs instead of Entities?

DTOs provide better security, loose coupling, API flexibility, and prevent exposing internal database structures.

---

### Does RestClient return JSON?

The HTTP response contains JSON. Spring and Jackson automatically convert that JSON into the required Java object (such as `DepartmentResponse`).

---

### Does OpenFeign require manual JSON conversion?

No.

OpenFeign also relies on Spring's HTTP message converters and Jackson to automatically serialize and deserialize JSON.

---

# Summary

JSON is the standard communication format used throughout WorkSphere.

All client requests and responses, as well as communication between microservices, use JSON.

Spring Boot, together with Jackson, automatically converts Java objects to JSON and JSON back to Java objects, allowing developers to focus on business logic instead of data conversion.

# RestClient

## Introduction

Microservices are independent applications.

Unlike a monolithic application, one microservice cannot directly invoke another service's methods.

Instead, services communicate by sending HTTP requests over the network.

Spring Boot provides several HTTP clients for making these requests.

The evolution is:

```

RestTemplate

↓

WebClient

↓

RestClient

```

In WorkSphere, we use **RestClient** for synchronous communication between Employee Service and Department Service.

---

# Why Do We Need RestClient?

Suppose Employee Service receives the following request.

```
GET /api/v1/employees/1/rest
```

Employee information exists in the Employee database.

However,

Department Name

Department Code

Department Head

are stored in Department Service.

Employee Service cannot directly access another service's database.

Instead, it must call Department Service through an HTTP request.

```
Employee Service

↓

HTTP Request

↓

Department Service

↓

HTTP Response

↓

Employee Service

```

RestClient performs this communication.

---

# Evolution of Spring HTTP Clients

Spring has introduced multiple HTTP clients over time.

## RestTemplate

Introduced in Spring Framework 3.

Advantages

- Simple
- Easy to use

Limitations

- Older API
- Verbose
- No fluent syntax

RestTemplate is now in maintenance mode.

No new features are being added.

---

## WebClient

Introduced with Spring WebFlux.

Designed for

- Reactive Programming
- Non-blocking communication
- High concurrency

Suitable for reactive applications.

However,

WorkSphere is currently a traditional synchronous Spring MVC application.

Using WebClient here would add unnecessary complexity.

---

## RestClient

Introduced in Spring Framework 6.1.

Spring Boot 3.2+

RestClient combines

✔ Simple API

✔ Modern fluent syntax

✔ Synchronous programming

✔ Easy integration

It is now the recommended synchronous HTTP client.

---

# Why Did We Choose RestClient?

WorkSphere is built using

- Spring Boot
- Spring MVC
- Synchronous APIs

Our requirements were

- Simple REST communication
- Easy to understand
- Modern API
- Spring Boot recommendation

RestClient satisfies all these requirements.

---

# Request Flow in WorkSphere

Suppose the client requests

```
GET /api/v1/employees/1/rest
```

The complete flow is

```
Client

↓

EmployeeController

↓

EmployeeService

↓

DepartmentRestClient

↓

RestClient

↓

HTTP Request

↓

Department Service

↓

DepartmentResponse

↓

Employee Service

↓

EmployeeWithDepartmentResponse

↓

Client
```

RestClient is responsible only for the highlighted communication.

```
DepartmentRestClient

↓

RestClient

↓

Department Service
```

---

# Our Implementation

We created a dedicated component.

```java
DepartmentRestClient
```

Responsibilities

- Build HTTP request
- Send request
- Receive response
- Convert JSON
- Return Java Object

EmployeeService does not know

- HTTP
- URLs
- JSON

It simply calls

```java
departmentRestClient.getDepartment(id);
```

This keeps business logic clean.

---

# RestClient Bean

We created a configuration class.

```java
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {

        return RestClient.builder()

                .baseUrl(departmentServiceUrl)

                .build();
    }
}
```

Why?

Instead of creating a new RestClient object every time,

Spring creates it once.

Stores it inside the IoC Container.

Injects it wherever required.

Benefits

- Better Performance

- Reusable

- Singleton Bean

- Easy Configuration

---

# Builder Pattern

Notice

```java
RestClient.builder()
```

This follows the **Builder Design Pattern**.

Instead of

```java
new RestClient(...)
```

Spring allows step-by-step configuration.

Example

```java
RestClient.builder()

.baseUrl(...)

.defaultHeader(...)

.build();
```

Advantages

- Readable

- Flexible

- Easy Configuration

- Immutable Object

Many Spring classes use Builder Pattern.

Examples

- RestClient

- WebClient

- UriComponentsBuilder

- HttpSecurity

---

# Our DepartmentRestClient

Our implementation

```java
public DepartmentResponse getDepartment(
        Long departmentId) {

    return restClient.get()

            .uri("/api/v1/departments/{id}",
                    departmentId)

            .retrieve()

            .body(DepartmentResponse.class);
}
```

Let's understand every step.

---

## Step 1

```
restClient.get()
```

Creates an HTTP GET request.

---

## Step 2

```
.uri(...)
```

Builds the complete URL.

Example

```
http://localhost:8082

+

/api/v1/departments/2
```

↓

```
http://localhost:8082/api/v1/departments/2
```

---

## Step 3

```
.retrieve()
```

Sends the HTTP request.

Waits for the response.

---

## Step 4

```
.body(DepartmentResponse.class)
```

Spring receives JSON.

Jackson converts JSON

↓

DepartmentResponse

No manual parsing required.

---

# Internal Working of RestClient

When we call

```java
departmentRestClient.getDepartment(2L);
```

Internally

```
DepartmentRestClient

↓

RestClient

↓

Create HTTP Request

↓

Open TCP Connection

↓

Send HTTP Request

↓

Department Service

↓

JSON Response

↓

Jackson

↓

DepartmentResponse Object

↓

Return Object
```

Everything happens automatically.

---

# How Spring Converts JSON?

Department Service returns

```json
{
    "id":2,
    "departmentName":"Engineering"
}
```

Spring receives JSON.

↓

Jackson

↓

Creates

```java
DepartmentResponse
```

Developers never write parsing logic.

---

# Why We Moved URL to application.yml

Initially

```
http://localhost:8082
```

was written directly inside Java code.

Problems

- Hardcoded

- Difficult to change

- Environment dependent

Then we moved it to

application.yml

```yaml
department-service:

base-url: http://localhost:8082
```

Benefits

- Externalized Configuration

- Better Maintainability

- Environment specific

- Cleaner Code

---

# Limitations of RestClient

Although RestClient solved many problems,

some issues remained.

## Fixed Service Location

Still dependent on

```
localhost:8082
```

If Department Service moves,

configuration must change.

---

## Manual Request Construction

Developers still write

```java
restClient.get()

.uri(...)

.retrieve()
```

for every API.

---

## No Service Discovery

RestClient cannot automatically locate services.

It only knows the configured URL.

---

## No Automatic Load Balancing

Suppose

Department Service

runs on

```
8082

8083

8084
```

RestClient still calls only

```
8082
```

---

# Enterprise Benefits

RestClient provides

- Modern API

- Fluent Syntax

- Better Readability

- Easy Testing

- Spring Integration

- Jackson Integration

- Configuration Support

It is recommended for synchronous communication in Spring Boot applications.

---

# Interview Questions

### What is RestClient?

RestClient is Spring Framework's modern synchronous HTTP client introduced in Spring Framework 6.1 for making REST calls.

---

### Why did Spring introduce RestClient?

To replace the older RestTemplate with a cleaner, fluent, and more modern API while keeping synchronous programming.

---

### Why did you use RestClient in WorkSphere?

Because WorkSphere is a synchronous Spring MVC application that required simple and modern REST communication between microservices.

---

### Does RestClient support asynchronous programming?

No.

RestClient is synchronous.

For reactive or asynchronous communication, Spring provides WebClient.

---

### Why did you move the URL to application.yml?

To externalize configuration, making it easier to manage different environments without changing Java code.

---

### What are the limitations of RestClient?

- Fixed service URLs
- Manual request creation
- No service discovery
- No automatic load balancing

These limitations were later addressed using OpenFeign and Eureka.

---

# Summary

RestClient is Spring Boot's recommended synchronous HTTP client.

It simplifies REST communication by providing a fluent API for sending HTTP requests and automatically converting JSON responses into Java objects.

In WorkSphere, RestClient enabled clean communication between Employee Service and Department Service while separating HTTP communication from business logic.

Although it improved maintainability and readability, it still relied on fixed service URLs, which motivated the later introduction of OpenFeign and Eureka for dynamic service discovery.

# OpenFeign

## Introduction

OpenFeign is a declarative HTTP client provided by Spring Cloud.

Unlike RestClient, developers do not manually construct HTTP requests.

Instead, they simply define a Java interface, and Spring automatically generates the implementation at runtime.

This greatly reduces boilerplate code and makes inter-service communication cleaner and easier to maintain.

In WorkSphere, OpenFeign is used for communication between Employee Service and Department Service.

---

# Why Do We Need OpenFeign?

With RestClient, every request had to be built manually.

Example

```java
restClient.get()

.uri("/api/v1/departments/{id}", id)

.retrieve()

.body(DepartmentResponse.class);
```

Although this is much better than RestTemplate, developers still need to write several lines of code for every API call.

OpenFeign removes this boilerplate.

Instead of writing request-building code, we simply write

```java
departmentFeignClient.getDepartment(id);
```

OpenFeign handles everything else automatically.

---

# Before OpenFeign

```
Employee Service

↓

DepartmentRestClient

↓

RestClient

↓

HTTP Request

↓

Department Service
```

Developer responsibilities

- Build URL
- Specify HTTP Method
- Send Request
- Receive Response
- Convert JSON

---

# After OpenFeign

```
Employee Service

↓

DepartmentFeignClient

↓

Generated Proxy Object

↓

HTTP Request

↓

Department Service
```

Developer responsibility

Only define the interface.

Everything else is handled by Spring.

---

# Our Implementation

We created the following interface.

```java
@FeignClient(name = "department-service")
public interface DepartmentFeignClient {

    @GetMapping("/api/v1/departments/{id}")

    DepartmentResponse getDepartment(

            @PathVariable Long id);

}
```

Notice something interesting.

There is

- no class
- no implementation
- no new keyword
- no RestClient

Yet,

the code works.

Why?

The answer is Dynamic Proxy.

---

# What Happens Internally?

During application startup,

Spring scans the project.

↓

Finds

```java
@FeignClient
```

↓

Registers a Bean

↓

Creates a Dynamic Proxy

↓

Stores it inside Spring IoC Container

↓

Injects it wherever required.

From the developer's perspective,

it behaves exactly like a normal Java class.

---

# Dynamic Proxy

Dynamic Proxy is a Java feature that creates an object at runtime for an interface.

Normally,

Suppose we have

```java
public interface Calculator {

    int add(int a, int b);

}
```

Normally,

we must create

```java
public class CalculatorImpl
implements Calculator {

}
```

But with Dynamic Proxy,

Java creates the implementation automatically while the application is running.

OpenFeign uses this feature internally.

---

# Why Reflection is Required?

To generate the implementation,

Spring must inspect the interface.

It reads

- Interface Name
- Method Names
- Return Types
- Parameters
- Annotations

This inspection is performed using **Reflection**.

Reflection allows Java to inspect classes and interfaces during runtime.

Without Reflection,

OpenFeign could not determine

- which HTTP method to call
- which URL to invoke
- what parameters to send
- what object should be returned

---

# Reflection in WorkSphere

Our interface

```java
@FeignClient(name = "department-service")
```

Spring reads

```
Service Name

↓

department-service
```

Then

```java
@GetMapping("/api/v1/departments/{id}")
```

Spring discovers

- HTTP Method = GET
- URL = /api/v1/departments/{id}

Then

```java
@PathVariable Long id
```

Spring understands

```
Replace

{id}

with

actual id.
```

Everything is discovered using Reflection.

---

# Request Lifecycle

Suppose Employee Service executes

```java
departmentFeignClient.getDepartment(2L);
```

Internally,

```
Employee Service

↓

Feign Proxy

↓

Reflection

↓

Build HTTP Request

↓

Service Discovery (Eureka)

↓

Department Service

↓

JSON Response

↓

Jackson

↓

DepartmentResponse

↓

Employee Service
```

Notice

Developers never create

- URL
- Request
- JSON Parser

Everything happens automatically.

---

# How Does OpenFeign Know Which Service to Call?

Earlier,

RestClient used

```
http://localhost:8082
```

Now,

Feign only knows

```java
department-service
```

Where does the actual address come from?

Eureka.

```
department-service

↓

Eureka

↓

localhost:8082
```

If tomorrow another instance starts

```
8083
```

Feign automatically discovers it.

No Java code changes.

---

# Integration with Jackson

Department Service returns

```json
{
    "id":2,
    "departmentName":"Engineering"
}
```

Feign receives JSON.

↓

Spring Message Converter

↓

Jackson

↓

DepartmentResponse

Developers never write parsing logic.

---

# Integration with Spring IoC

OpenFeign automatically registers

```
DepartmentFeignClient
```

as a Spring Bean.

Therefore,

we simply use

```java
private final DepartmentFeignClient
departmentFeignClient;
```

Constructor Injection works exactly like any other Spring Bean.

---

# Benefits of OpenFeign

✔ Very little code

✔ Declarative Programming

✔ Better Readability

✔ Automatic JSON Conversion

✔ Eureka Integration

✔ Spring Integration

✔ Easy Testing

✔ Cleaner Business Logic

✔ Less Boilerplate

---

# Comparison with RestClient

| RestClient | OpenFeign |
|------------|------------|
| Manual HTTP request creation | Automatic |
| Manual URL building | Automatic |
| More code | Less code |
| Better control | Better simplicity |
| Works without Spring Cloud | Requires Spring Cloud |
| Service discovery needs configuration | Works naturally with Eureka |

---

# Why Did We Replace RestClient?

RestClient was useful for understanding HTTP communication.

However,

it still required

- manual URL creation
- manual request building
- explicit HTTP calls

OpenFeign removes these responsibilities.

This results in cleaner and more maintainable code.

---

# Enterprise Benefits

OpenFeign is widely used in enterprise microservice applications because it

- integrates with Eureka
- supports Load Balancing
- reduces boilerplate
- improves readability
- simplifies service communication

---

# Interview Questions

### What is OpenFeign?

OpenFeign is a declarative HTTP client provided by Spring Cloud that generates REST client implementations automatically at runtime.

---

### Why is there no implementation class for FeignClient?

Because Spring creates a Dynamic Proxy implementation during application startup.

---

### How does OpenFeign know which URL to call?

It uses the service name provided in `@FeignClient` and asks Eureka for the current instance of that service.

---

### Why does OpenFeign use Reflection?

Reflection allows Spring to inspect the interface, its methods, parameters, and annotations at runtime so it can build the appropriate HTTP requests.

---

### Does OpenFeign use Jackson?

Yes.

Spring automatically uses Jackson to convert JSON responses into Java objects and Java objects into JSON request bodies.

---

### Is DepartmentFeignClient a Spring Bean?

Yes.

Spring automatically registers it as a Bean, so it can be injected using constructor injection.

---

### Why did you migrate from RestClient to OpenFeign in WorkSphere?

To eliminate manual HTTP request construction, reduce boilerplate code, integrate with Eureka for service discovery, and make inter-service communication cleaner and easier to maintain.

---

# Summary

OpenFeign simplifies REST communication by allowing developers to define Java interfaces instead of writing HTTP client code.

Spring uses Reflection to inspect the interface and creates a Dynamic Proxy implementation at runtime.

Combined with Eureka and Jackson, OpenFeign automatically discovers services, sends HTTP requests, converts JSON responses into Java objects, and integrates seamlessly with Spring's IoC container.

This makes microservice communication more maintainable, scalable, and aligned with enterprise development practices.


# RestClient vs OpenFeign

## Introduction

WorkSphere uses both RestClient and OpenFeign to communicate between microservices.

Initially, we implemented service communication using **RestClient** to understand how HTTP requests are constructed and processed.

Later, we migrated to **OpenFeign** to reduce boilerplate code and integrate with Eureka for dynamic service discovery.

This section explains the differences between these two approaches and why OpenFeign became our preferred solution.

---

# Evolution in WorkSphere

## Phase 1

Employee Service communicated using RestClient.

```
Employee Service

↓

DepartmentRestClient

↓

RestClient

↓

HTTP Request

↓

Department Service
```

The URL was configured in `application.yml`.

```
department-service:
  base-url: http://localhost:8082
```

Although the URL was externalized, it still represented a fixed service location.

---

## Phase 2

After introducing Eureka and OpenFeign

```
Employee Service

↓

DepartmentFeignClient

↓

Feign Proxy

↓

Eureka

↓

Department Service
```

The application now communicates using the registered service name instead of a fixed URL.

```
department-service
```

This makes the application much more flexible and scalable.

---

# Code Comparison

## RestClient

```java
return restClient.get()

        .uri("/api/v1/departments/{id}", id)

        .retrieve()

        .body(DepartmentResponse.class);
```

The developer is responsible for

- Selecting the HTTP method
- Building the URL
- Sending the request
- Receiving the response
- Converting the response

---

## OpenFeign

```java
@FeignClient(name = "department-service")

public interface DepartmentFeignClient {

    @GetMapping("/api/v1/departments/{id}")

    DepartmentResponse getDepartment(
            @PathVariable Long id);

}
```

The developer only defines the interface.

Spring generates the implementation automatically.

---

# Feature Comparison

| Feature | RestClient | OpenFeign |
|---------|------------|------------|
| Programming Style | Imperative | Declarative |
| Boilerplate Code | More | Very Less |
| URL Construction | Manual | Automatic |
| Request Building | Manual | Automatic |
| JSON Conversion | Automatic | Automatic |
| Spring Integration | Yes | Yes |
| Eureka Integration | Manual configuration | Native support |
| Load Balancing | Requires additional setup | Works with Eureka |
| Readability | Good | Excellent |
| Maintainability | Moderate | High |

---

# Service Discovery

## RestClient

RestClient communicates using a configured URL.

```
http://localhost:8082
```

Even though this URL is stored in `application.yml`, it still points to a specific service instance.

If the Department Service moves to another host or port, the configuration must be updated.

---

## OpenFeign

OpenFeign communicates using the service name.

```java
@FeignClient(name = "department-service")
```

When a request is made,

```
department-service

↓

Eureka

↓

Available Instance

↓

Department Service
```

The actual address is resolved automatically.

No code or configuration changes are required when service instances change.

---

# Load Balancing

Suppose Department Service has three running instances.

```
8082

8083

8084
```

## RestClient

If configured with

```
http://localhost:8082
```

every request goes only to port 8082.

---

## OpenFeign

OpenFeign asks Eureka for available instances.

```
department-service

↓

Eureka

↓

8082

8083

8084
```

Spring Cloud LoadBalancer selects one instance automatically.

This distributes requests across multiple instances.

---

# Error Handling

## RestClient

The developer usually writes additional logic to handle HTTP errors.

Example

- 404 Not Found
- 500 Internal Server Error

---

## OpenFeign

Feign provides built-in support for

- Custom Error Decoders
- Retry mechanisms
- Fallbacks (with Resilience4j)
- Circuit Breakers

These features make Feign more suitable for enterprise microservices.

---

# Development Experience

## RestClient

Advantages

- Simple
- Explicit
- Full control over requests

Disadvantages

- More boilerplate
- Manual request construction
- Repetitive code

---

## OpenFeign

Advantages

- Cleaner code
- Less boilerplate
- Declarative approach
- Easy integration with Spring Cloud

Disadvantages

- Requires Spring Cloud dependencies
- Less suitable for highly customized HTTP requests

---

# Why Did We Learn RestClient First?

Although OpenFeign is more powerful, we intentionally started with RestClient.

This helped us understand

- How HTTP requests are created
- How URLs are built
- How JSON is exchanged
- How Spring converts responses
- What happens internally during REST communication

Once these concepts were clear, migrating to OpenFeign became much easier.

This learning path mirrors how many real-world applications evolve.

---

# Why Did We Migrate to OpenFeign?

As WorkSphere evolved into a microservices architecture, we required

- Dynamic service discovery
- Cleaner service communication
- Reduced boilerplate code
- Better maintainability
- Integration with Eureka

OpenFeign addressed all these requirements.

---

# Enterprise Perspective

Many organizations begin with manual REST clients.

As systems grow and microservices increase, maintaining explicit HTTP request code becomes difficult.

Declarative clients such as OpenFeign improve readability, reduce maintenance effort, and integrate naturally with service discovery and load balancing.

This is why OpenFeign is commonly used in enterprise Spring Cloud applications.

---

# Interview Questions

### What is the main difference between RestClient and OpenFeign?

RestClient requires developers to manually construct HTTP requests, whereas OpenFeign allows developers to define interfaces and automatically generates the implementation.

---

### Which one is more suitable for microservices?

OpenFeign, because it integrates with Eureka, supports service discovery, reduces boilerplate code, and works well with Spring Cloud.

---

### Why did WorkSphere implement RestClient before OpenFeign?

To understand the fundamentals of REST communication before introducing higher-level abstractions such as OpenFeign.

---

### Does OpenFeign use RestClient internally?

No.

OpenFeign has its own infrastructure for creating and executing HTTP requests. It integrates with Spring's HTTP message converters and uses Jackson for JSON serialization and deserialization.

---

### When would you still choose RestClient?

When an application does not use Spring Cloud, requires highly customized HTTP requests, or needs explicit control over request construction.

---

# Summary

RestClient and OpenFeign both enable communication between microservices.

RestClient offers explicit control over HTTP communication, making it valuable for learning and for applications with simple requirements.

OpenFeign builds upon these concepts by providing a declarative programming model, automatic service discovery, and seamless Spring Cloud integration.

WorkSphere initially used RestClient to understand REST communication and later migrated to OpenFeign to create a cleaner, more scalable, and enterprise-ready architecture.

# REST Communication Best Practices

Building REST APIs is not only about making endpoints work.

Enterprise applications follow a set of best practices to ensure APIs are consistent, maintainable, scalable, secure, and easy to consume.

WorkSphere follows these practices wherever applicable and will continue to adopt more as the project evolves.

---

# 1. Use Meaningful Resource Names

REST APIs should represent resources using nouns instead of verbs.

✔ Good

```
/employees

/departments
```

❌ Avoid

```
/getEmployees

/createEmployee

/deleteDepartment
```

Resource names should clearly represent business entities.

---

# 2. Use Appropriate HTTP Methods

Always use the correct HTTP method for the intended operation.

| Method | Purpose |
|---------|---------|
| GET | Read Data |
| POST | Create Data |
| PUT | Update Entire Resource |
| PATCH | Partial Update |
| DELETE | Delete Resource |

Using proper HTTP methods makes APIs intuitive and REST compliant.

---

# 3. Version Your APIs

APIs evolve over time.

Instead of changing existing endpoints, create a new version.

Example

```
/api/v1/employees

/api/v2/employees
```

WorkSphere currently uses

```
/api/v1
```

This allows future enhancements without breaking existing clients.

---

# 4. Use DTOs Instead of Entities

Controllers should never expose JPA entities directly.

Instead, use dedicated DTOs.

Example

```
EmployeeRequest

EmployeeResponse

DepartmentResponse

EmployeeWithDepartmentResponse
```

Benefits

- Better Security

- Loose Coupling

- Flexible API Design

- Hide Database Structure

---

# 5. Validate Incoming Requests

Always validate client input before processing.

Example

```java
@NotBlank

@Email

@NotNull
```

Controller

```java
@PostMapping

public EmployeeResponse createEmployee(

@Valid

@RequestBody EmployeeRequest request)
```

Benefits

- Prevent Invalid Data

- Better Error Messages

- Improved Data Integrity

---

# 6. Return Proper HTTP Status Codes

Do not always return

```
200 OK
```

Instead use

| Scenario | Status |
|-----------|---------|
| Success | 200 |
| Resource Created | 201 |
| No Content | 204 |
| Invalid Request | 400 |
| Resource Not Found | 404 |
| Internal Error | 500 |

This improves API usability.

---

# 7. Use Global Exception Handling

Instead of handling exceptions inside every controller,

use

```
@RestControllerAdvice
```

Benefits

- Centralized Error Handling

- Consistent Error Response

- Cleaner Controllers

WorkSphere already follows this approach.

---

# 8. Externalize Configuration

Never hardcode

- URLs

- Credentials

- Ports

Store them inside

```
application.yml
```

or

```
Config Server
```

Benefits

- Easier Environment Management

- Better Maintainability

- Cleaner Code

---

# 9. Use Service Discovery

Instead of

```
localhost:8082
```

use

```
department-service
```

through Eureka.

Benefits

- Dynamic Discovery

- Load Balancing

- Cloud Ready

- No Hardcoded URLs

---

# 10. Keep Controllers Thin

Controllers should only

- Receive Request

- Validate Input

- Delegate to Service

Business logic belongs inside the Service layer.

Example

```
Controller

↓

Service

↓

Repository
```

---

# 11. Keep Business Logic in Services

Avoid writing business logic inside Controllers.

Incorrect

```java
@PostMapping

// Business Logic Here
```

Correct

```java
@PostMapping

↓

EmployeeService

↓

Repository
```

This improves maintainability and testability.

---

# 12. Use Pagination

Avoid returning thousands of records.

Instead use

```
?page=0

&size=10
```

Benefits

- Better Performance

- Lower Memory Usage

- Faster APIs

WorkSphere supports pagination for listing employees and departments.

---

# 13. Keep APIs Consistent

Maintain consistent

- URL naming

- Request structure

- Response structure

- Error responses

Consistency makes APIs easier to learn and use.

---

# 14. Avoid Returning Sensitive Information

Never expose

- Passwords

- Internal IDs

- Secret Keys

Use DTOs to control what is returned.

---

# 15. Document APIs

Good APIs should include documentation.

WorkSphere uses

Swagger / OpenAPI

Benefits

- Interactive Testing

- Better Developer Experience

- Automatic Documentation

---

# REST Best Practices Used in WorkSphere

✔ RESTful URLs

✔ API Versioning

✔ DTO Pattern

✔ Validation

✔ Pagination

✔ Swagger

✔ Eureka

✔ OpenFeign

✔ Externalized Configuration

✔ Layered Architecture

✔ Global Exception Handling

---

# Interview Questions

### Why should entities not be exposed directly?

Because entities represent the database model, while DTOs expose only the data required by clients.

---

### Why is API versioning important?

It allows APIs to evolve without breaking existing consumers.

---

### Why should Controllers remain thin?

Controllers should only handle HTTP concerns, while business logic belongs in the Service layer.

---

### Why use pagination?

To improve performance and avoid returning excessively large datasets.

---

### Why externalize configuration?

To support different environments without modifying Java code.

---

# Summary

Following REST best practices results in APIs that are easier to maintain, easier to test, more scalable, and more suitable for enterprise applications.

WorkSphere follows these principles to ensure the project reflects real-world backend development standards.

# Conclusion

Throughout this document, we explored how REST communication is implemented in WorkSphere.

We began with the fundamentals of REST and HTTP before understanding how Spring Boot processes requests and responses.

As the project evolved, we improved our service communication by moving through different stages of implementation.

---

# Evolution of REST Communication in WorkSphere

## Stage 1

Basic REST APIs

```
Client

↓

Employee Service

↓

Database
```

---

## Stage 2

Inter-Service Communication using RestClient

```
Employee Service

↓

RestClient

↓

Department Service
```

Benefits

- Modern HTTP Client

- Better than RestTemplate

- Fluent API

---

## Stage 3

Externalized Configuration

Instead of hardcoding

```
localhost:8082
```

we moved configuration to

```
application.yml
```

Benefits

- Cleaner Code

- Environment Independence

---

## Stage 4

Service Discovery with Eureka

Instead of

```
localhost:8082
```

we used

```
department-service
```

Benefits

- Dynamic Discovery

- Cloud Ready

- No Hardcoded URLs

---

## Stage 5

OpenFeign

Instead of manually building requests,

we defined interfaces.

Spring automatically generated the implementation.

Benefits

- Less Boilerplate

- Cleaner Code

- Better Maintainability

---

# Technologies Covered

During this chapter we learned

- REST

- HTTP

- JSON

- Jackson

- DTO

- Request / Response

- Serialization

- Deserialization

- RestClient

- OpenFeign

- Eureka Integration

- Reflection

- Dynamic Proxy

- Service Discovery

---

# Key Design Decisions

Instead of hardcoded URLs

↓

Externalized Configuration

↓

Eureka Service Discovery

↓

OpenFeign

Each step improved the architecture and made the application more scalable.

---

# What We Achieved

By completing this chapter, WorkSphere now demonstrates

✔ Modern REST APIs

✔ Microservice Communication

✔ Enterprise Architecture

✔ Dynamic Service Discovery

✔ Clean Code Principles

✔ Spring Cloud Integration

---

# Complete Interview Revision

Before an interview, revise the following topics:

- What is REST?
- REST Principles
- HTTP Methods
- HTTP Status Codes
- Request vs Response
- JSON
- Serialization
- Deserialization
- Jackson
- DTO
- RestClient
- OpenFeign
- Reflection
- Dynamic Proxy
- Eureka
- Service Discovery
- Load Balancing
- REST Best Practices

If you can confidently explain these topics using WorkSphere as an example, you'll be well prepared for most Spring Boot and Microservices interviews.

---

# Final Summary

REST communication is the foundation of modern microservice architectures.

WorkSphere started with simple REST APIs and gradually evolved into an enterprise-ready system by introducing externalized configuration, service discovery through Eureka, and declarative communication using OpenFeign.

This progression not only improved the application's architecture but also demonstrates the reasoning behind each design decision, making WorkSphere much more than a CRUD project.