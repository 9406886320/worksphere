# Eureka Service Discovery

## Introduction

As a microservices application grows, managing communication between services becomes increasingly complex.

Initially, WorkSphere contained multiple independent services such as:

- Employee Service
- Department Service

These services communicated using fixed URLs.

Example

```
http://localhost:8082
```

Although this approach worked during development, it introduced several limitations as the application evolved.

For example:

- Service URLs had to be configured manually.
- Any port or host change required configuration updates.
- Multiple instances of the same service could not be discovered automatically.
- The architecture was difficult to scale.

To overcome these limitations, WorkSphere introduced **Netflix Eureka**.

Eureka acts as a **Service Registry**, allowing microservices to register themselves and discover each other dynamically.

Instead of remembering server addresses, services communicate using logical service names.

This removes hardcoded dependencies and makes the system more scalable, maintainable, and cloud-ready.

---

# Why WorkSphere Needed Eureka

Before Eureka was introduced, communication between services looked like this.

```
Employee Service

↓

http://localhost:8082

↓

Department Service
```

Employee Service always expected Department Service to be available at a fixed address.

This created several problems.

---

## Problem 1 - Hardcoded URLs

The service location was fixed.

Example

```
http://localhost:8082
```

If Department Service moved to another machine or another port, the application configuration had to be updated.

---

## Problem 2 - Environment Dependency

Different environments usually have different service addresses.

Example

Development

```
localhost:8082
```

Testing

```
10.10.20.5:8082
```

Production

```
department.company.com
```

Managing these configurations becomes increasingly difficult as the number of services grows.

---

## Problem 3 - Poor Scalability

Suppose multiple instances of Department Service are running.

```
Department Service

↓

Instance 1

Instance 2

Instance 3
```

A fixed URL points to only one instance.

The remaining instances cannot be utilized.

---

## Problem 4 - Cloud Deployment Challenges

Cloud platforms dynamically create and terminate service instances.

Since service addresses continuously change, fixed URLs are no longer practical.

Microservices require a dynamic discovery mechanism.

---

# Solution

WorkSphere introduced **Eureka Server**.

Instead of communicating through fixed URLs, services now communicate using logical service names.

Example

Instead of

```
http://localhost:8082
```

Employee Service now communicates with

```
department-service
```

Eureka automatically resolves the service name into the correct running service instance.

This removes the need for hardcoded URLs completely.

# What is Eureka?

## Introduction

Eureka is a **Service Discovery** solution developed by Netflix and now maintained as part of the Spring Cloud ecosystem.

It helps microservices locate and communicate with each other dynamically without requiring hardcoded network addresses.

Instead of remembering where a service is running, applications simply ask Eureka to locate the required service.

Eureka acts as a **central registry** that maintains information about all available microservices in the system.

---

# What is Service Discovery?

Service Discovery is a mechanism that allows services to find and communicate with each other automatically.

Instead of configuring the network location of every service manually, services register themselves with a central registry.

Other services query the registry whenever they need to communicate.

```
Employee Service

↓

Eureka Server

↓

Department Service
```

The Employee Service does not know where Department Service is running.

It only knows its logical service name.

---

# Eureka Components

Eureka consists of two primary components.

## 1. Eureka Server

The Eureka Server acts as the central registry.

Its responsibilities include:

- Maintaining a list of registered services.
- Receiving service registrations.
- Monitoring service health.
- Providing service locations to clients.
- Removing unavailable services.

Think of the Eureka Server as a **phone directory** for microservices.

Instead of searching for someone's phone number manually, you simply look it up in the directory.

Similarly, microservices ask Eureka for the location of another service.

---

## 2. Eureka Client

Every microservice that communicates through Eureka is called a Eureka Client.

Examples in WorkSphere include:

- Employee Service
- Department Service
- API Gateway

Each client performs two primary tasks:

- Registers itself with Eureka during startup.
- Queries Eureka whenever another service needs to be located.

---

# High-Level Architecture

The following diagram illustrates the relationship between the server and clients.

```
                Eureka Server
                     │
      ┌──────────────┼──────────────┐
      ▼              ▼              ▼
Employee Service  Department Service  API Gateway
```

Every service registers itself with Eureka.

Whenever communication is required, services consult Eureka instead of using hardcoded URLs.

---

# Service Registry

The Service Registry is the database maintained by Eureka.

It stores information about every registered service.

Typical information includes:

- Service Name
- Host Name
- IP Address
- Port Number
- Status
- Number of Instances

Example

```
employee-service

Host : localhost

Port : 8081

Status : UP
```

```
department-service

Host : localhost

Port : 8082

Status : UP
```

The registry is continuously updated as services start, stop, or become unavailable.

---

# Logical Service Names

One of the biggest advantages of Eureka is the use of logical service names.

Instead of calling

```
http://localhost:8082
```

WorkSphere communicates using

```
department-service
```

This logical name is configured in

```yaml
spring:
  application:
    name: department-service
```

The service name becomes the identity of the application inside Eureka.

---

# How Eureka Helps WorkSphere

When Employee Service requires department information, it performs the following steps.

```
Employee Service

↓

Ask Eureka

↓

Locate

department-service

↓

Return Service Address

↓

Communicate with Department Service
```

Employee Service never needs to know the actual IP address or port number.

Eureka provides this information automatically.

---

# Benefits of Eureka

Using Eureka provides several important benefits.

✔ Dynamic Service Discovery

✔ Automatic Service Registration

✔ No Hardcoded URLs

✔ Better Scalability

✔ Easier Deployment

✔ Cloud-Native Architecture

✔ Better Maintainability

✔ Support for Multiple Instances

---

# Real-Life Analogy

Imagine you want to call a friend.

Instead of memorizing every phone number, you search their name in your phone's contacts.

```
Friend Name

↓

Contacts

↓

Phone Number

↓

Call
```

Eureka works in the same way.

Instead of remembering service addresses, applications search for service names.

```
department-service

↓

Eureka

↓

IP Address + Port

↓

HTTP Request
```

This makes communication much simpler and more flexible.

---

# Interview Questions

### What is Eureka?

Eureka is a service registry that enables dynamic service discovery between microservices.

---

### What is the difference between Eureka Server and Eureka Client?

The Eureka Server maintains the service registry.

Eureka Clients register themselves with the server and query it to discover other services.

---

### What is a Service Registry?

A Service Registry is a centralized repository that stores information about all running services, including their names, addresses, ports, and health status.

---

### Why are logical service names used instead of URLs?

Logical service names remove the dependency on fixed network addresses, allowing services to move or scale without changing client configurations.

---

# Summary

Eureka is the central service registry used by WorkSphere to enable dynamic communication between microservices.

It maintains information about all available services and allows applications to communicate using logical service names rather than fixed URLs.

This provides greater flexibility, scalability, and maintainability while preparing the application for cloud-native deployment.

# Service Registration

## Introduction

One of the primary responsibilities of a Eureka Client is to register itself with the Eureka Server when it starts.

Registration allows the Eureka Server to maintain an up-to-date registry of all available microservices.

Once registered, other services can discover and communicate with it without knowing its physical location.

In WorkSphere, the following services register themselves with Eureka:

- Employee Service
- Department Service
- API Gateway

Each service becomes discoverable immediately after successful registration.

---

# Why is Registration Required?

Consider the following scenario.

Employee Service needs to communicate with Department Service.

Without registration,

Employee Service has no way of knowing:

- Is Department Service running?
- What IP address is it using?
- Which port is it running on?

Instead of storing this information manually, every service registers itself with Eureka.

This allows Eureka to maintain a central registry of all active services.

---

# Registration Process

When a microservice starts, the following sequence occurs.

```
Start Application

↓

Read application.yml

↓

Load Eureka Client Configuration

↓

Connect to Eureka Server

↓

Register Service

↓

Service Added to Registry

↓

Ready to Accept Requests
```

After registration, the service becomes available for discovery by other microservices.

---

# Registration Flow in WorkSphere

For example, when **Department Service** starts:

```
Department Service

↓

Reads Configuration

↓

Application Name

↓

department-service

↓

Connects to Eureka

↓

Registers Itself

↓

Registration Successful
```

Similarly, when **Employee Service** starts:

```
Employee Service

↓

Reads Configuration

↓

Application Name

↓

employee-service

↓

Registers with Eureka
```

The API Gateway follows the same process.

---

# Information Sent During Registration

When a service registers, it sends important metadata to the Eureka Server.

Typical information includes:

- Application Name
- Host Name
- IP Address
- Port Number
- Instance ID
- Service Status
- Health Information

Example

```
Application Name

department-service

Host

localhost

Port

8082

Status

UP
```

Eureka stores this information in its registry.

---

# Registration Architecture

The registration process in WorkSphere can be visualized as follows.

```
                Eureka Server
                     ▲
                     │
      ┌──────────────┼──────────────┐
      │              │              │
      │ Register     │ Register     │ Register
      │              │              │
      ▼              ▼              ▼
Employee Service  Department Service  API Gateway
```

Every service independently registers itself during startup.

---

# Configuration Used in WorkSphere

Each microservice contains the following configuration.

```yaml
spring:
  application:
    name: employee-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

For Department Service

```yaml
spring:
  application:
    name: department-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

Explanation

### spring.application.name

Defines the logical service name.

This is the name used by other services during discovery.

---

### defaultZone

Specifies the address of the Eureka Server.

Every client connects to this server during startup.

---

# What Happens After Registration?

Once registration completes successfully:

✔ The service appears in the Eureka Dashboard.

✔ Other services can discover it.

✔ OpenFeign can communicate using the service name.

Example

Instead of

```
http://localhost:8082
```

Employee Service now simply calls

```
department-service
```

Eureka resolves the actual address automatically.

---

# Benefits of Registration

Service registration provides several advantages.

✔ Automatic service discovery

✔ Dynamic infrastructure

✔ No manual configuration

✔ Easier deployment

✔ Better scalability

✔ Simplified communication

---

# Real-Life Example

Imagine joining a company.

On your first day,

you register your details with the HR department.

The HR database now contains:

- Your Name
- Employee ID
- Department
- Contact Information

Whenever another department needs to contact you,

they simply search the HR system.

They do not need to know where you are sitting.

Eureka works in exactly the same way.

Microservices register themselves,

and other services search the registry whenever communication is required.

---

# Interview Questions

### Why does a service register with Eureka?

To make itself discoverable by other microservices.

---

### What information is stored during registration?

Application name, host, IP address, port, instance ID, status, and other metadata.

---

### What is the purpose of `spring.application.name`?

It defines the logical identity of the service inside Eureka.

---

### What happens after successful registration?

The service becomes available for discovery, appears in the Eureka Dashboard, and can receive requests from other services.

---

### Can a service communicate before registration?

No.

Other services can discover it only after it has successfully registered with Eureka.

---

# Summary

Service registration is the first step in Eureka-based service discovery.

When a WorkSphere microservice starts, it reads its configuration, connects to the Eureka Server, and registers itself using its logical service name.

Once registered, it becomes discoverable by other services, eliminating the need for hardcoded URLs and enabling a scalable microservices architecture.


# Heartbeat Mechanism

## Introduction

Registering a service with Eureka is only the first step.

After successful registration, the Eureka Server must continuously verify whether the service is still running.

If a service crashes, shuts down, or loses network connectivity, Eureka should stop routing requests to it.

To achieve this, Eureka uses a **Heartbeat Mechanism**.

A heartbeat is a small message periodically sent by every Eureka Client to the Eureka Server.

As long as Eureka continues receiving heartbeats, it considers the service healthy and available.

---

# Why is Heartbeat Required?

Consider the following scenario.

Employee Service and Department Service are both registered with Eureka.

```
Employee Service

↓

Eureka

↓

Department Service
```

Now suppose Department Service crashes unexpectedly.

If Eureka is unaware of this failure, it will continue returning Department Service as an available instance.

As a result,

Employee Service will send requests to a service that is no longer running.

This leads to communication failures.

The heartbeat mechanism prevents this situation.

---

# How Heartbeat Works

After registration, every Eureka Client periodically sends a heartbeat to the Eureka Server.

```
Department Service

↓

Heartbeat

↓

Eureka Server

↓

Service Status = UP
```

Each successful heartbeat tells Eureka:

> "I am alive and ready to receive requests."

---

# Heartbeat Lifecycle

The heartbeat process follows this sequence.

```
Application Starts

↓

Registers with Eureka

↓

Heartbeat Sent Every Few Seconds

↓

Eureka Updates Lease

↓

Service Remains Available
```

This process continues throughout the lifetime of the application.

---

# What Happens if Heartbeats Stop?

Suppose Department Service suddenly crashes.

```
Department Service

↓

(No Heartbeat)

↓

Eureka Waits

↓

Lease Expires

↓

Service Removed from Registry
```

Once the lease expires,

Eureka marks the service as unavailable.

Future requests are no longer routed to that service.

---

# Lease Renewal

Every registered service has a **Lease**.

A lease is an agreement between the service and Eureka.

The service promises:

> "I will periodically send heartbeats."

Every heartbeat renews the lease.

```
Heartbeat

↓

Lease Renewed

↓

Service Status = UP
```

As long as the lease continues to renew,

the service remains discoverable.

---

# Lease Expiration

If Eureka does not receive heartbeats within the configured time,

the lease expires.

```
No Heartbeat

↓

Lease Timeout

↓

Service Removed

↓

Unavailable for Discovery
```

This prevents clients from communicating with failed services.

---

# Heartbeat Flow in WorkSphere

The following diagram illustrates the heartbeat process.

```
Department Service

↓

Registers

↓

Eureka Server

↓

Heartbeat

↓

Lease Renewed

↓

Heartbeat

↓

Lease Renewed

↓

Heartbeat

↓

Lease Renewed
```

The same process occurs for:

- Employee Service
- API Gateway

Every registered service continuously renews its lease.

---

# Default Timing

By default,

Eureka clients send heartbeats approximately every **30 seconds**.

If Eureka does not receive heartbeats for approximately **90 seconds**, it considers the service unavailable.

These values can be customized if required.

---

# Configuration in WorkSphere

Heartbeat behaviour can be configured using the following properties.

```yaml
eureka:
  instance:
    lease-renewal-interval-in-seconds: 30
    lease-expiration-duration-in-seconds: 90
```

### lease-renewal-interval-in-seconds

Defines how frequently the client sends heartbeats.

Default

```
30 Seconds
```

---

### lease-expiration-duration-in-seconds

Defines how long Eureka waits before removing a service that has stopped sending heartbeats.

Default

```
90 Seconds
```

---

# Benefits of Heartbeat Mechanism

The heartbeat mechanism provides several important benefits.

✔ Automatically detects failed services

✔ Keeps the registry up to date

✔ Prevents requests from reaching unavailable services

✔ Improves system reliability

✔ Supports dynamic cloud environments

✔ Enables automatic recovery after service restart

---

# Real-Life Example

Imagine an employee working remotely.

Every morning they mark their attendance.

```
Employee

↓

Attendance System

↓

Present
```

If they stop marking attendance for several days,

the system assumes they are unavailable.

Similarly,

a Eureka Client sends periodic heartbeats.

If the heartbeats stop,

Eureka assumes the service is no longer available.

---

# Interview Questions

### What is a heartbeat in Eureka?

A heartbeat is a periodic message sent by a Eureka Client to inform the Eureka Server that it is still running.

---

### Why is the heartbeat mechanism required?

It allows Eureka to detect failed services and keep the service registry updated.

---

### What is lease renewal?

Lease renewal occurs whenever a client sends a heartbeat, extending its registration in Eureka.

---

### What happens if a service stops sending heartbeats?

Its lease expires, and Eureka removes it from the service registry.

---

### Can a removed service register again?

Yes.

When the service restarts, it registers with Eureka again and becomes discoverable.

---

# Summary

The heartbeat mechanism ensures that the Eureka registry always reflects the current state of the system.

By continuously renewing its lease, each WorkSphere microservice informs Eureka that it is healthy and ready to receive requests.

If heartbeats stop, Eureka automatically removes the service, preventing communication with failed instances and improving the reliability of the overall microservices architecture.

# Service Discovery Process

## Introduction

After a microservice has successfully registered with Eureka and continues sending heartbeats, it becomes available for discovery by other services.

Service Discovery is the process of locating the correct service instance whenever one microservice needs to communicate with another.

In WorkSphere, Employee Service frequently requires department information.

Instead of communicating using a fixed URL, Employee Service simply requests the service by its logical name.

Eureka locates the appropriate service instance and returns its network address.

This entire process happens automatically without any manual intervention.

---

# Service Discovery in WorkSphere

Suppose a client requests employee details.

```
GET /api/v1/employees/1
```

The Employee Service needs department information.

Instead of calling

```
http://localhost:8082
```

it simply calls

```
department-service
```

OpenFeign forwards this request to Eureka.

Eureka returns the correct running instance.

---

# Complete Discovery Flow

The following diagram illustrates the complete discovery process.

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
            Eureka Server
                   │
     Locate department-service
                   │
                   ▼
        Department Service
                   │
                   ▼
      Department Database
                   │
                   ▼
        Department Service
                   │
                   ▼
              OpenFeign
                   │
                   ▼
          Employee Service
                   │
                   ▼
                Client
```

---

# Step-by-Step Discovery Process

## Step 1

The client sends a request.

```
GET /api/v1/employees/1
```

---

## Step 2

Employee Controller receives the request and forwards it to Employee Service.

---

## Step 3

Employee Service retrieves employee information from the Employee Database.

---

## Step 4

Employee Service requires department information.

It calls

```java
departmentFeignClient.getDepartment(id);
```

Notice that no URL is provided.

Only the service name is used.

---

## Step 5

OpenFeign creates the HTTP request.

Before sending the request,

it asks Eureka

```
Where is

department-service

running?
```

---

## Step 6

Eureka searches its registry.

Example

```
department-service

↓

Host

localhost

↓

Port

8082

↓

Status

UP
```

Eureka returns the available service instance.

---

## Step 7

OpenFeign now knows the actual network address.

It automatically sends the HTTP request.

```
http://localhost:8082/api/v1/departments/2
```

This happens internally.

The developer never writes this URL.

---

## Step 8

Department Service processes the request.

It retrieves department information from its database.

---

## Step 9

Department Service returns the response.

OpenFeign converts the JSON response into a Java object.

Employee Service receives the object.

---

## Step 10

Employee Service combines

- Employee Information
- Department Information

and returns the final response to the client.

---

# Discovery Architecture

The discovery architecture can be summarized as follows.

```
Employee Service

↓

DepartmentFeignClient

↓

OpenFeign

↓

Eureka

↓

Department Service
```

Each component performs a specific responsibility.

---

# Responsibilities

### Employee Service

- Executes business logic.
- Requests department information.

---

### DepartmentFeignClient

- Defines the REST API interface.
- Contains no implementation.

---

### OpenFeign

- Creates HTTP requests.
- Calls Eureka.
- Converts responses into Java objects.

---

### Eureka Server

- Maintains the service registry.
- Returns available service instances.

---

### Department Service

- Processes business logic.
- Returns department information.

---

# Discovery with Multiple Instances

Suppose multiple Department Service instances are running.

```
department-service

↓

Instance 1

Instance 2

Instance 3
```

Employee Service still uses

```
department-service
```

Eureka returns one of the available instances.

Spring Cloud LoadBalancer selects the instance automatically.

The application code remains exactly the same.

---

# Advantages of Service Discovery

Using Eureka for discovery provides several advantages.

✔ No Hardcoded URLs

✔ Automatic Service Location

✔ Better Scalability

✔ Easier Deployment

✔ Dynamic Infrastructure

✔ Better Fault Tolerance

✔ Cloud-Native Architecture

✔ Automatic Integration with OpenFeign

---

# Real-Life Example

Imagine ordering food using a delivery application.

You choose the restaurant by its name.

```
Pizza Hut
```

You do not know

- its IP address
- its server
- its network location

The delivery application locates the restaurant automatically.

Similarly,

Employee Service simply requests

```
department-service
```

Eureka finds the correct service instance.

---

# Interview Questions

### What is Service Discovery?

Service Discovery is the process of locating a running microservice dynamically through a service registry.

---

### How does Employee Service locate Department Service?

Employee Service calls DepartmentFeignClient.

OpenFeign asks Eureka for the location of `department-service`.

Eureka returns the running service instance.

---

### Does Employee Service know the actual URL?

No.

Employee Service only knows the logical service name.

The actual network address is resolved by Eureka.

---

### What happens if the Department Service changes its port?

No code changes are required.

Department Service registers its new address with Eureka, and future requests automatically use the updated location.

---

### What happens if multiple Department Service instances exist?

Eureka maintains all available instances.

Spring Cloud LoadBalancer automatically distributes requests among them.

---

# Summary

Service Discovery is one of the core capabilities provided by Eureka.

Instead of communicating using fixed URLs, WorkSphere services communicate using logical service names.

OpenFeign requests the service location from Eureka, Eureka returns an available instance, and communication proceeds automatically.

This approach greatly improves scalability, maintainability, and flexibility while eliminating hardcoded dependencies between microservices.

# Load Balancing

## Introduction

In a production environment, running only one instance of a microservice is rarely sufficient.

As the number of users increases, a single service instance may become overloaded.

To improve scalability and availability, multiple instances of the same microservice are deployed.

For example, WorkSphere may run multiple instances of the Department Service.

```
Department Service

↓

Instance 1

Instance 2

Instance 3
```

Instead of sending every request to a single instance, requests should be distributed across all available instances.

This process is known as **Load Balancing**.

---

# Why is Load Balancing Required?

Consider the following scenario.

Suppose only one Department Service instance is running.

```
Employee Service

↓

Department Service

↓

1000 Requests
```

As the number of requests increases, this single instance becomes overloaded.

Potential problems include:

- Increased response time
- High CPU usage
- High memory consumption
- Reduced availability
- Poor user experience

Running multiple instances solves this problem.

---

# Architecture Without Load Balancing

Without load balancing,

all requests are sent to a single service instance.

```
Employee Service

↓

Department Service (8082)

↓

All Requests
```

The remaining servers remain unused.

---

# Architecture With Load Balancing

With load balancing,

requests are distributed across multiple instances.

```
                 Employee Service
                        │
                        ▼
          Spring Cloud LoadBalancer
                        │
        ┌───────────────┼───────────────┐
        ▼               ▼               ▼
Department-1      Department-2     Department-3
   (8082)            (8083)           (8084)
```

This improves overall application performance and reliability.

---

# How Load Balancing Works in WorkSphere

The following sequence occurs when Employee Service requests department information.

```
Employee Service

↓

DepartmentFeignClient

↓

OpenFeign

↓

Eureka

↓

Available Instances

↓

Spring Cloud LoadBalancer

↓

Selected Instance

↓

Department Service
```

Notice that Employee Service never selects the instance.

The selection is performed automatically.

---

# Role of Eureka

Eureka is **not** responsible for load balancing.

Its responsibility is to maintain the list of available service instances.

Example

```
department-service

↓

localhost:8082

localhost:8083

localhost:8084
```

Eureka simply returns this list.

---

# Role of Spring Cloud LoadBalancer

Spring Cloud LoadBalancer receives the list of available instances from Eureka.

It selects one instance and forwards the request.

```
Available Instances

↓

LoadBalancer

↓

Selected Instance

↓

HTTP Request
```

This happens automatically.

The developer does not write any load balancing logic.

---

# Load Balancing Strategies

Different strategies can be used to distribute requests.

---

## Round Robin

Each request is sent to the next available instance.

Example

```
Request 1

↓

Instance 1

Request 2

↓

Instance 2

Request 3

↓

Instance 3

Request 4

↓

Instance 1
```

This is the most commonly used strategy.

---

## Random

A random instance is selected.

Example

```
Request

↓

Random Instance
```

---

## Weighted Load Balancing

Some instances receive more traffic based on their capacity.

Example

```
High Capacity Server

↓

More Requests

Low Capacity Server

↓

Fewer Requests
```

---

## Response-Time Based

Requests are routed to the fastest responding service instance.

This strategy is commonly used in advanced enterprise systems.

---

# Load Balancing in WorkSphere

Currently,

WorkSphere uses

```
OpenFeign

+

Spring Cloud LoadBalancer

+

Eureka
```

The communication flow is

```
Employee Service

↓

OpenFeign

↓

Eureka

↓

Available Instances

↓

LoadBalancer

↓

Department Service
```

This entire process is automatic.

---

# Advantages of Load Balancing

Load balancing provides several important benefits.

✔ Better Scalability

✔ Improved Performance

✔ Higher Availability

✔ Better Resource Utilization

✔ Fault Tolerance

✔ Reduced Response Time

✔ Better User Experience

---

# Real-Life Example

Imagine three billing counters at a supermarket.

```
Customer

↓

Counter 1

Counter 2

Counter 3
```

Instead of sending every customer to Counter 1,

customers are distributed across all available counters.

This reduces waiting time.

Load balancing works in exactly the same way.

---

# Interview Questions

### What is Load Balancing?

Load balancing is the process of distributing incoming requests across multiple service instances.

---

### Does Eureka perform Load Balancing?

No.

Eureka only maintains the registry of available service instances.

Spring Cloud LoadBalancer selects the instance.

---

### Why is Load Balancing required?

To improve scalability, availability, performance, and fault tolerance.

---

### What is the default load balancing strategy?

Spring Cloud LoadBalancer typically uses a Round Robin strategy by default.

---

### What happens if one service instance fails?

Eureka removes the failed instance from the registry.

Spring Cloud LoadBalancer routes future requests only to the remaining healthy instances.

---

# Summary

Load balancing ensures that requests are evenly distributed among multiple service instances.

In WorkSphere, Eureka provides the list of available instances, while Spring Cloud LoadBalancer selects the appropriate instance for each request.

This combination improves performance, scalability, availability, and fault tolerance without requiring any additional application code.

# Eureka Dashboard

## Introduction

One of the biggest advantages of Eureka is its built-in web dashboard.

The dashboard provides a real-time view of all registered microservices in the system.

It allows developers to verify:

- Which services are registered
- Service status
- Number of running instances
- Registration details
- Server information

During development and testing, the Eureka Dashboard is the primary tool used to verify that service registration is working correctly.

---

# Accessing the Dashboard

Once the Eureka Server is running, the dashboard can be accessed using:

```
http://localhost:8761
```

The browser displays the Eureka Dashboard.

---

# Dashboard Overview

A typical WorkSphere dashboard looks like:

```
=========================================

EUREKA

=========================================

Instances currently registered with Eureka

EMPLOYEE-SERVICE

(1) UP

DEPARTMENT-SERVICE

(1) UP

API-GATEWAY

(1) UP

=========================================
```

Each registered service appears automatically after startup.

---

# Understanding the Dashboard

The dashboard contains several important sections.

---

## Registered Applications

This section displays all services currently registered.

Example

```
EMPLOYEE-SERVICE

DEPARTMENT-SERVICE

API-GATEWAY
```

Each service is identified by its

```
spring.application.name
```

---

## Number of Instances

Example

```
EMPLOYEE-SERVICE

(1)
```

means

One instance of Employee Service is running.

If three instances are running

```
EMPLOYEE-SERVICE

(3)
```

then Eureka has discovered all three.

---

## Service Status

Example

```
UP
```

means

The service is healthy and actively sending heartbeats.

Possible statuses include

```
UP

DOWN

STARTING

UNKNOWN
```

Normally,

WorkSphere services should always appear as

```
UP
```

---

## Instance Details

Clicking a service displays additional information.

Typical details include

- Host Name
- IP Address
- Port
- Status
- Lease Information
- Metadata

This information helps during debugging.

---

# Dashboard in WorkSphere

When all WorkSphere services are running, the dashboard should display:

```
EMPLOYEE-SERVICE

↓

UP
```

```
DEPARTMENT-SERVICE

↓

UP
```

```
API-GATEWAY

↓

UP
```

If one service is missing,

it usually indicates a configuration or registration issue.

---

# Verifying Registration

The dashboard is the quickest way to confirm successful registration.

Checklist

✔ Eureka Server started

✔ Employee Service started

✔ Department Service started

✔ API Gateway started

✔ All services appear as UP

Once all services appear,

service discovery is functioning correctly.

---

# Common Dashboard Problems

## Service Not Appearing

Possible causes

- Eureka Server not running
- Incorrect `defaultZone`
- Missing Eureka dependency
- `register-with-eureka` disabled
- Network connectivity issue

---

## Status = DOWN

Possible causes

- Application startup failure
- Heartbeats not reaching Eureka
- Incorrect configuration
- Application crashed

---

## Duplicate Service Entries

Possible causes

- Multiple running instances
- Previous instance not removed yet
- Incorrect instance configuration

---

# Best Practice

Always verify the Eureka Dashboard before testing service-to-service communication.

If a service is not visible in the dashboard,

OpenFeign will not be able to discover it.

---

# Interview Questions

### Why is the Eureka Dashboard useful?

It provides a real-time view of all registered services and helps verify service discovery.

---

### How do you know if a service registered successfully?

The service appears in the Eureka Dashboard with the status **UP**.

---

### What does the number beside a service indicate?

It represents the number of running instances registered with Eureka.

---

### What should you check if a service is missing?

- Eureka Server
- `defaultZone`
- Eureka dependency
- Service startup logs
- Network connectivity

---

# Summary

The Eureka Dashboard provides a centralized view of all registered microservices.

In WorkSphere, it is used to verify service registration, monitor service health, inspect running instances, and troubleshoot service discovery issues.

# Eureka Configuration in WorkSphere

## Introduction

For a microservice to participate in service discovery, it must be configured as a Eureka Client.

Similarly, the Eureka Server itself requires configuration to act as the central service registry.

In WorkSphere, we configured Eureka using:

- Maven (`pom.xml`)
- Spring Boot Configuration (`application.yml`)

Each configuration property has a specific purpose.

Understanding these properties is essential for developing and troubleshooting enterprise microservice applications.

---

# Maven Configuration

## Eureka Server Dependency

The Eureka Server requires the following dependency.

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

### Why do we need this dependency?

This dependency converts a normal Spring Boot application into a Eureka Server.

It provides:

- Eureka Registry
- Dashboard
- Service Registration APIs
- Service Discovery APIs
- Heartbeat Management
- Lease Management

Without this dependency, the application cannot function as a Eureka Server.

---

## Eureka Client Dependency

Every microservice registers with Eureka using the following dependency.

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

In WorkSphere, this dependency is added to:

- Employee Service
- Department Service
- API Gateway

---

### Why is this dependency required?

It enables the application to:

- Register with Eureka
- Discover other services
- Send heartbeats
- Fetch the service registry
- Integrate with OpenFeign

Without this dependency, the service cannot communicate through Eureka.

---

# Eureka Server Configuration

The Eureka Server contains the following configuration.

```yaml
server:
  port: 8761

spring:
  application:
    name: eureka-server

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

---

## server.port

```yaml
server:
  port: 8761
```

### Purpose

Defines the port on which Eureka Server runs.

WorkSphere uses

```
8761
```

because it is the default port used by Eureka.

---

## spring.application.name

```yaml
spring:
  application:
    name: eureka-server
```

### Purpose

Defines the logical name of the Eureka Server.

Although clients usually don't communicate using this name,

it is still considered a best practice.

---

## register-with-eureka

```yaml
register-with-eureka: false
```

### Purpose

Determines whether the Eureka Server should register itself.

For the server,

the value is

```
false
```

because it is itself the registry.

It does not need to register.

---

## fetch-registry

```yaml
fetch-registry: false
```

### Purpose

Determines whether the Eureka Server should fetch another registry.

Since WorkSphere currently has a single Eureka Server,

this value remains

```
false
```

---

# Eureka Client Configuration

Employee Service and Department Service contain the following configuration.

```yaml
spring:
  application:
    name: employee-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

Department Service uses

```yaml
spring:
  application:
    name: department-service
```

The remaining configuration is identical.

---

## spring.application.name

This is one of the most important Eureka properties.

Example

```yaml
spring:
  application:
    name: employee-service
```

### Purpose

Defines the logical identity of the application.

This name is:

- Registered with Eureka
- Displayed in the Dashboard
- Used by OpenFeign
- Used during Service Discovery

For example,

OpenFeign communicates using

```
department-service
```

instead of

```
http://localhost:8082
```

---

## service-url.defaultZone

```yaml
defaultZone:
http://localhost:8761/eureka
```

### Purpose

Specifies the address of the Eureka Server.

Every Eureka Client connects to this URL during startup.

---

### Why does the URL end with `/eureka`?

The `/eureka` endpoint is the REST API exposed by the Eureka Server.

Clients register, renew leases, and discover services through this endpoint.

Without `/eureka`,

registration will fail.

---

# Optional Client Properties

WorkSphere currently uses Eureka's default values for many settings.

However, these can be customized.

Example

```yaml
eureka:
  instance:
    lease-renewal-interval-in-seconds: 30
    lease-expiration-duration-in-seconds: 90
```

---

## lease-renewal-interval-in-seconds

Specifies how frequently the client sends heartbeats.

Default

```
30 Seconds
```

---

## lease-expiration-duration-in-seconds

Specifies how long Eureka waits before removing a service that has stopped sending heartbeats.

Default

```
90 Seconds
```

---

# Do We Need @EnableDiscoveryClient?

In older versions of Spring Boot,

developers commonly used

```java
@EnableDiscoveryClient
```

or

```java
@EnableEurekaClient
```

to enable service discovery.

---

### WorkSphere

Since WorkSphere uses:

- Spring Boot 3.x
- Spring Cloud 2025.x

these annotations are **not required**.

Spring Boot automatically enables discovery whenever the Eureka Client dependency is present.

This keeps the configuration cleaner and reduces unnecessary annotations.

---

# Configuration Summary

| Property | Purpose |
|----------|----------|
| `server.port` | Runs Eureka Server on a specific port |
| `spring.application.name` | Logical service name |
| `defaultZone` | Eureka Server address |
| `register-with-eureka` | Register the application with Eureka |
| `fetch-registry` | Download registry from Eureka |
| `lease-renewal-interval-in-seconds` | Heartbeat interval |
| `lease-expiration-duration-in-seconds` | Service removal timeout |

---

# Best Practices

✔ Use meaningful application names.

✔ Never hardcode service URLs inside Java code.

✔ Keep Eureka configuration in `application.yml`.

✔ Use logical service names for communication.

✔ Verify registration using the Eureka Dashboard.

✔ Keep heartbeat values at their defaults unless tuning is required.

✔ Use the same Eureka Server for all services in the environment.

---

# Interview Questions

### Why is `spring.application.name` important?

It defines the logical identity of the service and is used by Eureka during registration and discovery.

---

### Why is `defaultZone` required?

It tells the Eureka Client where the Eureka Server is running.

---

### Why does the URL end with `/eureka`?

Because `/eureka` is the REST endpoint used by Eureka Clients to register, renew leases, and discover services.

---

### Why doesn't the Eureka Server register itself?

Because it is already the central registry.

There is no need for it to appear as a client.

---

### Do we still need `@EnableDiscoveryClient`?

No.

In Spring Boot 3.x and recent Spring Cloud versions, service discovery is automatically enabled when the Eureka Client dependency is present.

---

# Summary

The Eureka configuration in WorkSphere is intentionally simple yet powerful.

By combining the Eureka Client dependency with a few essential configuration properties, each microservice can automatically register with the Eureka Server, participate in service discovery, and communicate using logical service names.

This eliminates hardcoded URLs and provides the flexibility required for scalable enterprise microservices.

# How We Integrated Eureka into WorkSphere

## Introduction

As WorkSphere evolved into a microservices architecture, service-to-service communication became an important concern.

Initially, Employee Service communicated with Department Service using a fixed network address.

Although this approach worked during development, it introduced several limitations that would become problematic as the application scaled.

To solve these challenges, Eureka Service Discovery was introduced.

This section explains the complete journey from hardcoded URLs to dynamic service discovery.

---

# Architecture Before Eureka

Before Eureka, WorkSphere used direct communication between services.

```
Employee Service

↓

http://localhost:8082

↓

Department Service
```

Employee Service was configured with the exact address of Department Service.

Communication was performed using a fixed URL.

---

# Problems Before Eureka

## Problem 1 - Hardcoded URLs

Employee Service depended on a fixed address.

Example

```
http://localhost:8082
```

If Department Service changed its port,

Employee Service configuration had to be updated.

---

## Problem 2 - Environment Dependency

Different environments require different URLs.

Example

Development

```
localhost:8082
```

Testing

```
10.10.20.15:8082
```

Production

```
department.company.com
```

Managing these URLs becomes increasingly difficult.

---

## Problem 3 - Poor Scalability

Suppose multiple Department Service instances exist.

```
Department Service

↓

Instance 1

Instance 2

Instance 3
```

The hardcoded URL points to only one instance.

The remaining instances cannot be utilized.

---

## Problem 4 - Tight Coupling

Employee Service knew exactly where Department Service was running.

This created unnecessary dependency between services.

A microservice should know only the service name, not its physical location.

---

# Decision

To remove these limitations, Eureka Service Discovery was introduced.

The goal was:

✔ Remove hardcoded URLs

✔ Support multiple service instances

✔ Improve scalability

✔ Enable dynamic discovery

✔ Prepare WorkSphere for cloud deployment

---

# What We Removed

Before Eureka, service communication relied on fixed URLs.

Example

```yaml
department-service:
  url: http://localhost:8082
```

or

```java
http://localhost:8082/api/v1/departments
```

These configurations are no longer required.

---

# What We Added

## Eureka Server

A dedicated Eureka Server module was created.

Responsibilities:

- Maintain service registry
- Handle registrations
- Manage heartbeats
- Support service discovery

---

## Eureka Client

Eureka Client dependency was added to:

- Employee Service
- Department Service
- API Gateway

This allowed each service to:

- Register itself
- Discover other services
- Participate in service discovery

---

## Logical Service Names

Instead of using URLs,

services now communicate using application names.

Example

```yaml
spring:
  application:
    name: department-service
```

Communication changed from

```
http://localhost:8082
```

to

```
department-service
```

---

# Architecture After Eureka

The architecture became:

```
Employee Service

↓

Eureka Server

↓

Department Service
```

Employee Service no longer knows where Department Service is running.

It simply asks Eureka.

---

# Service Discovery Flow

```
Employee Service

↓

OpenFeign

↓

Eureka

↓

Locate

department-service

↓

Department Service
```

The actual network address is resolved automatically.

---

# Files Modified During Integration

## Eureka Server

Added

```xml
spring-cloud-starter-netflix-eureka-server
```

Added

```java
@EnableEurekaServer
```

Created

```yaml
application.yml
```

for Eureka configuration.

---

## Employee Service

Added

```xml
spring-cloud-starter-netflix-eureka-client
```

Added Eureka configuration.

Updated OpenFeign communication to use service names.

---

## Department Service

Added

```xml
spring-cloud-starter-netflix-eureka-client
```

Added Eureka configuration.

Enabled registration with Eureka.

---

## API Gateway

Added Eureka Client support.

Configured routing through service names.

---

# Benefits Achieved

After integrating Eureka, WorkSphere gained several advantages.

✔ Dynamic Service Discovery

✔ No Hardcoded URLs

✔ Better Scalability

✔ Easier Maintenance

✔ Improved Fault Tolerance

✔ Cloud Readiness

✔ Support for Multiple Instances

✔ Simplified Service Communication

---

# Enterprise Perspective

In enterprise environments, services are frequently:

- Deployed across multiple servers
- Scaled dynamically
- Restarted automatically
- Migrated between environments

Maintaining fixed URLs becomes impossible.

Eureka solves this problem by providing dynamic service discovery.

This is one of the reasons why service registries are a fundamental component of modern microservice architectures.

---

# What Would Happen Without Eureka?

Without Eureka:

❌ Hardcoded URLs

❌ Difficult scaling

❌ Manual configuration updates

❌ Environment-specific dependencies

❌ Higher maintenance effort

❌ Reduced flexibility

As the number of services increases, these problems become more severe.

---

# Interview Questions

### Why did you introduce Eureka into WorkSphere?

To eliminate hardcoded URLs and provide dynamic service discovery between microservices.

---

### What problem was Eureka solving?

Service location management, scalability, and environment dependency.

---

### What did you remove after introducing Eureka?

Hardcoded service URLs.

---

### What did you add after introducing Eureka?

Eureka Server, Eureka Clients, service registration, heartbeats, and service discovery.

---

### How did communication change?

Before:

```
Employee Service

↓

http://localhost:8082
```

After:

```
Employee Service

↓

department-service

↓

Eureka
```

---

# Summary

Eureka transformed WorkSphere from a collection of independently configured services into a true microservices ecosystem.

By introducing dynamic service discovery, WorkSphere eliminated hardcoded URLs, improved scalability, simplified service communication, and became better prepared for future enterprise features such as API Gateway, Config Server, Docker, and Kubernetes.

# Best Practices

Developing a microservices application involves more than simply configuring Eureka. Following best practices ensures that the system remains scalable, maintainable, and reliable.

---

## 1. Use Meaningful Service Names

Every microservice should have a unique and meaningful application name.

Good Example

```yaml
spring:
  application:
    name: employee-service
```

```yaml
spring:
  application:
    name: department-service
```

Avoid using generic names like:

```
service1

test-service

app
```

Meaningful names make the Eureka Dashboard easier to understand and simplify debugging.

---

## 2. Never Hardcode Service URLs

❌ Avoid

```
http://localhost:8082
```

✔ Use

```
department-service
```

Hardcoded URLs create tight coupling between services.

Using logical service names keeps services independent and easier to scale.

---

## 3. Always Verify Registration

Before testing service communication:

✔ Start Eureka Server

✔ Start all microservices

✔ Open the Eureka Dashboard

```
http://localhost:8761
```

Verify that every service appears with status:

```
UP
```

---

## 4. Keep Eureka Configuration Externalized

Configuration should always remain in

```
application.yml
```

Avoid placing server addresses inside Java code.

This makes environment changes much easier.

---

## 5. Use OpenFeign with Eureka

Instead of writing REST communication manually,

allow OpenFeign to communicate through Eureka.

Benefits

✔ Cleaner code

✔ Automatic Service Discovery

✔ Better maintainability

---

## 6. Keep Default Heartbeat Values

Unless there is a specific performance requirement,

keep the default heartbeat values.

```
Renewal Interval

30 Seconds
```

```
Lease Expiration

90 Seconds
```

These defaults work well for most applications.

---

## 7. Monitor the Eureka Dashboard

The dashboard should be checked whenever:

- A service fails to register.
- A service cannot be discovered.
- Communication fails.
- Multiple instances are deployed.

It is the first place to investigate service discovery issues.

---

## 8. Use Eureka Only for Internal Communication

Eureka should be used for communication between internal microservices.

External clients should communicate through an API Gateway rather than directly with Eureka.

---

## 9. Use API Gateway with Eureka

In enterprise applications,

clients should never call services directly.

Instead,

```
Client

↓

API Gateway

↓

Eureka

↓

Microservices
```

This provides a single entry point for the system.

---

## 10. Prepare for Cloud Deployment

One of the biggest advantages of Eureka is cloud readiness.

Services can:

- Scale automatically
- Restart automatically
- Change IP addresses
- Move between servers

without requiring code changes.

---

# Common Mistakes

❌ Hardcoding URLs

❌ Forgetting `spring.application.name`

❌ Incorrect `defaultZone`

❌ Starting services before Eureka Server

❌ Duplicate service names

❌ Missing Eureka Client dependency

❌ Assuming Eureka performs load balancing

❌ Ignoring the Eureka Dashboard during debugging

---

# Summary

Following these best practices ensures that WorkSphere remains scalable, maintainable, and aligned with enterprise microservices architecture.

# Interview Questions

## Basic Questions

### What is Eureka?

Eureka is a Service Registry that enables dynamic service discovery between microservices.

---

### Why is Eureka used?

To eliminate hardcoded URLs and enable dynamic service discovery.

---

### What is Service Discovery?

Service Discovery is the process of locating microservices dynamically through a central registry.

---

### What is the difference between Eureka Server and Eureka Client?

Eureka Server maintains the registry.

Eureka Clients register themselves and discover other services.

---

### What is a Service Registry?

A Service Registry stores information about all running microservices, including their names, IP addresses, ports, and status.

---

## Intermediate Questions

### What information is stored in Eureka?

- Service Name
- IP Address
- Port
- Host Name
- Instance ID
- Health Status
- Metadata

---

### What is `spring.application.name`?

It defines the logical identity of a microservice.

Other services discover it using this name.

---

### Why is `/eureka` added to `defaultZone`?

Because `/eureka` is the REST endpoint used by Eureka Clients for registration and discovery.

---

### What is Heartbeat?

A periodic message sent by Eureka Clients to indicate they are still alive.

---

### What is Lease Renewal?

Each heartbeat renews the service lease, keeping it active in the registry.

---

### What happens if heartbeats stop?

The lease expires.

Eureka removes the service from its registry.

---

### Does Eureka perform Load Balancing?

No.

Eureka maintains the registry.

Spring Cloud LoadBalancer performs load balancing.

---

### How does OpenFeign work with Eureka?

OpenFeign asks Eureka for the location of a service and automatically sends the request to the selected instance.

---

### What happens if the service port changes?

No code changes are required.

The service registers its new address with Eureka automatically.

---

### Can multiple instances have the same service name?

Yes.

Each instance registers separately under the same logical service name.

---

### What happens if one instance fails?

Its heartbeats stop.

Eureka removes it.

Future requests are routed only to healthy instances.

---

## WorkSphere-Specific Questions

### Why did you introduce Eureka into WorkSphere?

To replace hardcoded service URLs with dynamic service discovery.

---

### What problem did Eureka solve?

- Hardcoded URLs
- Environment dependency
- Poor scalability
- Tight coupling

---

### What changed after introducing Eureka?

Before

```
Employee Service

↓

http://localhost:8082
```

After

```
Employee Service

↓

department-service

↓

Eureka
```

---

### Which WorkSphere modules use Eureka Client?

- Employee Service
- Department Service
- API Gateway

---

### Which WorkSphere module acts as Eureka Server?

```
eureka-server
```

---

### How do you verify Eureka is working?

Open

```
http://localhost:8761
```

Verify all services appear as **UP**.

# Summary

Eureka Service Discovery is one of the core components of the WorkSphere microservices architecture.

Before Eureka, microservices communicated using hardcoded URLs, making the system tightly coupled, difficult to scale, and environment-dependent.

By introducing Eureka, WorkSphere achieved:

- Dynamic Service Discovery
- Automatic Service Registration
- Heartbeat Monitoring
- Load Balancing Support
- Better Scalability
- Improved Maintainability
- Cloud Readiness
- Elimination of Hardcoded URLs

The communication flow evolved from direct URL-based calls to service-name-based discovery using OpenFeign and Eureka.

Before

```
Employee Service

↓

http://localhost:8082

↓

Department Service
```

After

```
Employee Service

↓

OpenFeign

↓

Eureka

↓

Department Service
```

This architectural improvement transformed WorkSphere into a true enterprise-style microservices application.

Eureka now serves as the foundation for future enhancements such as:

- API Gateway
- Config Server
- Docker
- Kubernetes
- Monitoring
- Distributed Systems

Understanding Eureka is essential because it demonstrates how modern enterprise applications achieve dynamic communication, scalability, and resilience.