# Maven & POM Complete Guide

## Table of Contents

1. Introduction to Maven
2. Why Maven?
3. Maven Project Structure
4. Maven Build Lifecycle
5. What is pom.xml?
6. Parent POM
7. Child POM
8. Multi Module Project
9. Dependency Management
10. Dependencies
11. Properties
12. Build Plugins
13. WorkSphere Parent POM Explained
14. Common Library POM Explained
15. Employee Service POM Explained
16. Department Service POM Explained
17. Eureka Server POM Explained
18. API Gateway POM Explained
19. Best Practices
20. Interview Questions

---

# 1. What is Maven?

Apache Maven is a Build Automation and Dependency Management tool used for Java applications.

Instead of manually downloading JAR files and configuring the classpath, Maven automatically downloads, manages and updates all required libraries.

Maven also standardizes:

- Project Structure
- Build Process
- Dependency Management
- Packaging
- Testing
- Deployment

Official Website

https://maven.apache.org/

---

# 2. Why Maven?

Before Maven

Developer had to

✔ Download JAR files manually

✔ Copy JARs into project

✔ Configure Build Path

✔ Download transitive dependencies manually

Example

Spring Boot requires

- Spring Core
- Spring Context
- Spring Beans
- Spring MVC
- Jackson
- Tomcat
- Logging
- Validation

Without Maven

Developer downloads every JAR manually.

With Maven

Only one dependency is enough.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Maven downloads everything automatically.

---

# 3. Advantages of Maven

✔ Automatic Dependency Management

✔ Standard Directory Structure

✔ Build Automation

✔ Plugin Support

✔ Version Management

✔ Easy Integration with IDEs

✔ Continuous Integration Support

✔ Multi Module Project Support

✔ Central Repository

---

# 4. Maven Standard Project Structure

Every Maven project follows the same structure.

```
project
│
├── src
│
├── main
│   ├── java
│   └── resources
│
├── test
│   ├── java
│   └── resources
│
├── pom.xml
│
└── target
```

---

## src/main/java

Contains Java source code.

Example

```
EmployeeController
EmployeeService
EmployeeRepository
```

---

## src/main/resources

Contains

- application.yml
- application.properties
- SQL Scripts
- Static Files

---

## src/test/java

Contains

JUnit Tests

Integration Tests

Mockito Tests

---

## target

Generated during build.

Contains

Compiled classes

Executable JAR

Temporary build files

Never commit target folder to Git.

---

# 5. Maven Build Lifecycle

Whenever we execute

```
mvn clean install
```

Maven performs several phases.

```
validate

↓

compile

↓

test

↓

package

↓

verify

↓

install

↓

deploy
```

---

## validate

Checks project configuration.

Ensures pom.xml is valid.

---

## compile

Compiles Java source code.

```
.java

↓

.class
```

---

## test

Runs unit tests.

JUnit

Mockito

Integration Tests

---

## package

Creates

Jar

or

War

Example

```
employee-service.jar
```

---

## verify

Runs additional quality checks.

---

## install

Installs artifact into local Maven Repository.

```
~/.m2/repository
```

Now other projects can use it.

Example

Our Common Library.

---

## deploy

Uploads artifact to Remote Repository.

Example

Nexus

Artifactory

GitHub Packages

---

# 6. Common Maven Commands

Compile

```
mvn compile
```

Clean

```
mvn clean
```

Run Tests

```
mvn test
```

Package

```
mvn package
```

Install

```
mvn install
```

Run Spring Boot

```
mvn spring-boot:run
```

Clean + Install

```
mvn clean install
```

---

# 7. What is pom.xml?

POM stands for

Project Object Model

Every Maven project must contain

```
pom.xml
```

It contains

- Project Information
- Java Version
- Dependencies
- Plugins
- Modules
- Build Configuration

Think of it as the **blueprint of the project**.

Without pom.xml

Maven cannot build the application.

---

# 8. Basic Structure of pom.xml

```xml
<project>

    <modelVersion>4.0.0</modelVersion>

    <groupId></groupId>

    <artifactId></artifactId>

    <version></version>

    <dependencies>

    </dependencies>

    <build>

    </build>

</project>
```

Every Maven project starts with this structure.

---

# 9. modelVersion

```xml
<modelVersion>4.0.0</modelVersion>
```

This specifies the POM model version.

Current standard version is

```
4.0.0
```

Every modern Maven project uses this.

---

# 10. groupId

Example

```xml
<groupId>com.worksphere</groupId>
```

Represents the organization or company.

Usually written in reverse domain format.

Examples

```
com.amazon

com.microsoft

org.springframework

com.worksphere
```

Think of it as the company name.

---

# 11. artifactId

Example

```xml
<artifactId>employee-service</artifactId>
```

Represents the project/module name.

Examples

```
employee-service

department-service

common-library

api-gateway
```

Think of it as the application name.

---

# 12. version

Example

```xml
<version>1.0.0-SNAPSHOT</version>
```

Represents the current project version.

SNAPSHOT

Means

Project is under development.

Stable Release

```
1.0.0

2.0.0
```

Development

```
1.0.0-SNAPSHOT
```
---

# 13. Parent POM

In a Multi-Module Maven project, one project acts as the **Parent Project**.

It contains the common configuration shared by all child modules.

In WorkSphere, our Parent POM is:

```
worksphere-parent
```

Project Structure

```
worksphere-parent
│
├── common-library
├── employee-service
├── department-service
├── eureka-server
├── api-gateway
└── pom.xml
```

The Parent POM does **NOT** contain any business logic.

Its responsibilities are:

- Managing Java Version
- Managing Spring Boot Version
- Managing Spring Cloud Version
- Managing Common Dependencies
- Managing Maven Plugins
- Building all child modules together

Think of the Parent POM as the **Project Manager** of the entire application.

---

# 14. Why Parent POM?

Imagine we have five microservices.

Without Parent POM

Every project contains

```xml
<java.version>21</java.version>

<spring.boot.version>3.5.4</spring.boot.version>

<spring.cloud.version>2025.0.0</spring.cloud.version>
```

Five projects

↓

Same code repeated five times.

Now suppose Spring Boot changes from

```
3.5.4

↓

3.6.0
```

Without Parent POM

You update

Employee

Department

Gateway

Eureka

Common Library

One by one.

With Parent POM

Update only once.

All child modules inherit automatically.

This is one of the biggest advantages of Multi Module Projects.

---

# 15. WorkSphere Parent POM Explained

Our Parent POM

```xml
<groupId>com.worksphere</groupId>

<artifactId>worksphere-parent</artifactId>

<version>1.0.0-SNAPSHOT</version>

<packaging>pom</packaging>
```

---

## groupId

```xml
<groupId>com.worksphere</groupId>
```

Represents our organization.

Every child module automatically belongs to

```
com.worksphere
```

---

## artifactId

```xml
<artifactId>worksphere-parent</artifactId>
```

Represents the Parent Project.

This project manages the entire WorkSphere Platform.

---

## version

```xml
<version>1.0.0-SNAPSHOT</version>
```

Current development version.

Every child module automatically inherits this version.

---

## packaging

```xml
<packaging>pom</packaging>
```

Very important.

Normally Maven creates

```
jar

or

war
```

But Parent Project should not create

```
worksphere-parent.jar
```

It only manages child projects.

Therefore packaging must be

```xml
pom
```

---

# 16. Child POM

Every microservice contains

```xml
<parent>

    <groupId>com.worksphere</groupId>

    <artifactId>worksphere-parent</artifactId>

    <version>1.0.0-SNAPSHOT</version>

</parent>
```

Example

Employee Service

Department Service

Gateway

Eureka

Common Library

All inherit from Parent.

This is called **POM Inheritance**.

---

# 17. Benefits of Parent Inheritance

Child POM automatically receives

✔ Java Version

✔ Spring Boot Version

✔ Spring Cloud Version

✔ Dependency Management

✔ Plugin Management

✔ Encoding

✔ Compiler Settings

No duplication.

---

# 18. Multi Module Project

A Multi Module Project consists of multiple Maven projects managed by one Parent Project.

WorkSphere

```
worksphere-parent
│
├── common-library
├── employee-service
├── department-service
├── eureka-server
└── api-gateway
```

Every folder is an independent Maven Project.

Each can be

Built

Tested

Run

Independently.

But Parent can build everything together.

---

# 19. Why Multi Module?

Suppose we create everything inside one project.

```
Employee

Department

Gateway

Kafka

Redis

JWT

Notification

Inventory
```

Eventually

```
50,000+

Java Classes
```

Huge project.

Hard to maintain.

Instead

Each module has one responsibility.

Example

Employee Service

Only employee logic.

Department Service

Only department logic.

Gateway

Only routing.

Common Library

Only shared code.

This follows the **Single Responsibility Principle (SRP).**

---

# 20. modules

Our Parent POM contains

```xml
<modules>

    <module>common-library</module>

    <module>employee-service</module>

    <module>department-service</module>

    <module>eureka-server</module>

    <module>api-gateway</module>

</modules>
```

This tells Maven

These projects belong to one application.

Now

```
mvn clean install
```

Builds

```
Common Library

↓

Employee Service

↓

Department Service

↓

Eureka

↓

Gateway
```

Automatically.

Without modules

You must build each project manually.

---

# 21. What Happens During Build?

When we execute

```
mvn clean install
```

Maven first reads Parent POM.

↓

Finds all modules.

↓

Builds Common Library.

↓

Employee Service.

↓

Department Service.

↓

Gateway.

↓

Eureka.

Finally

Entire platform is built successfully.

---

# 22. Common Interview Question

### Why is Common Library built first?

Because

Employee Service

↓

Depends on

```
common-library
```

If Common Library is not built first

Employee Service compilation fails.

Maven automatically calculates this dependency graph.

This is called the **Maven Reactor Build Order**.

---

# 23. Maven Reactor

The Reactor is Maven's build engine for Multi Module Projects.

Instead of random order

Maven determines

Correct dependency order.

Example

```
Common Library

↓

Employee Service

↓

Gateway
```

instead of

```
Gateway

↓

Employee

↓

Common
```

which would fail.

---

# 24. Summary

Parent POM acts as the central manager of the entire WorkSphere project.

It stores:

- Common Versions
- Common Properties
- Dependency Management
- Module Information
- Build Configuration

Every microservice inherits from the Parent POM, reducing duplication and making version upgrades simple.

This architecture is widely used in enterprise applications because it improves maintainability, consistency, and scalability.


---

# 25. Properties

In Maven, the `<properties>` section is used to define reusable values.

Instead of writing the same value multiple times, we define it once and reuse it throughout the project.

Example

```xml
<properties>

    <java.version>21</java.version>

    <spring.boot.version>3.5.4</spring.boot.version>

    <spring.cloud.version>2025.0.0</spring.cloud.version>

    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

</properties>
```

---

## Why do we use Properties?

Imagine we don't use properties.

Our pom.xml may look like this.

```xml
<version>3.5.4</version>

<version>3.5.4</version>

<version>3.5.4</version>

<version>3.5.4</version>
```

Suppose Spring Boot releases

```
3.6.0
```

Now we have to change

```
3.5.4

↓

3.6.0
```

everywhere.

Very error prone.

Instead

```xml
<spring.boot.version>3.5.4</spring.boot.version>
```

Later

```xml
<spring.boot.version>3.6.0</spring.boot.version>
```

One change updates the entire project.

---

# 26. Java Version Property

Our Parent POM contains

```xml
<java.version>21</java.version>
```

Why?

All child modules automatically compile using Java 21.

Without this property

Every module would need

```xml
<maven.compiler.source>21</maven.compiler.source>

<maven.compiler.target>21</maven.compiler.target>
```

Properties eliminate duplication.

---

# 27. Spring Boot Version Property

```xml
<spring.boot.version>3.5.4</spring.boot.version>
```

This stores the Spring Boot version in one place.

Whenever we import

Spring Boot BOM

it uses this version.

Benefits

✔ Easy Upgrade

✔ Centralized Version Management

✔ No Duplicate Version Numbers

---

# 28. Spring Cloud Version Property

```xml
<spring.cloud.version>2025.0.0</spring.cloud.version>
```

Stores Spring Cloud version.

Used by

- Eureka

- Gateway

- OpenFeign

- LoadBalancer

- Config Server

Changing Spring Cloud version becomes a one-line change.

---

# 29. Source Encoding

```xml
<project.build.sourceEncoding>

UTF-8

</project.build.sourceEncoding>
```

Defines character encoding.

Without UTF-8

Special characters may appear incorrectly.

Always recommended.

---

# 30. Dependency Management

One of the most misunderstood Maven concepts.

Our Parent POM contains

```xml
<dependencyManagement>

...

</dependencyManagement>
```

Many beginners think

Dependency Management downloads dependencies.

It DOES NOT.

Its only job is

```
Manage Versions
```

Think of Dependency Management as

A Version Dictionary.

---

# 31. What does Dependency Management do?

Suppose Employee Service uses

Spring Web

Department Service uses

Spring Data JPA

Gateway uses

Spring Cloud Gateway

Instead of writing

```xml
<version>3.5.4</version>
```

inside every dependency,

Dependency Management stores the version once.

Child modules simply write

```xml
<dependency>

<artifactId>spring-boot-starter-web</artifactId>

</dependency>
```

Version is automatically inherited.

---

# 32. Spring Boot BOM

Inside Dependency Management

We imported

```xml
<dependency>

<groupId>org.springframework.boot</groupId>

<artifactId>spring-boot-dependencies</artifactId>

<version>${spring.boot.version}</version>

<type>pom</type>

<scope>import</scope>

</dependency>
```

This is called

Spring Boot BOM.

---

## What is BOM?

BOM stands for

```
Bill Of Materials
```

Just like a factory has

```
List of Parts
```

Spring Boot BOM contains

Compatible versions of

- Spring MVC

- Spring Core

- Jackson

- Hibernate

- Tomcat

- Validation

- Logging

- Security

- WebFlux

and hundreds more.

Instead of choosing versions manually,

Spring Boot already tested them.

---

# 33. Why use Spring Boot BOM?

Without BOM

```xml
spring-web

6.2.9

spring-context

6.2.9

spring-core

6.2.9

jackson

2.18

hibernate

6.x
```

Developer manages every version.

High chance of version conflicts.

With BOM

Just write

```xml
<dependency>

<artifactId>spring-boot-starter-web</artifactId>

</dependency>
```

Done.

---

# 34. Spring Cloud BOM

Similarly

```xml
<dependency>

<groupId>org.springframework.cloud</groupId>

<artifactId>spring-cloud-dependencies</artifactId>

<version>${spring.cloud.version}</version>

<type>pom</type>

<scope>import</scope>

</dependency>
```

This imports

Spring Cloud BOM.

It manages versions of

- Eureka

- Gateway

- OpenFeign

- LoadBalancer

- Config Server

- Circuit Breaker

- Bus

- Sleuth

Everything remains compatible.

---

# 35. Why do we need Spring Cloud BOM?

Suppose

Gateway

uses version

```
4.3
```

Feign

uses

```
4.1
```

Eureka

uses

```
4.5
```

These versions may not work together.

Spring Cloud BOM guarantees

Compatible versions.

---

# 36. Dependency Management vs Dependencies

This is a very common interview question.

| Dependency Management | Dependencies |
|----------------------|-------------|
| Manages versions | Downloads libraries |
| Doesn't add JARs | Adds JARs |
| Parent POM | Child POM |
| Version Control | Actual Usage |

Simple rule

Dependency Management

↓

Version Only

Dependencies

↓

Actual Library

---

# 37. Dependencies

Inside every module we have

```xml
<dependencies>

...

</dependencies>
```

Unlike Dependency Management,

this actually downloads the JARs.

Example

```xml
<dependency>

<groupId>org.springframework.boot</groupId>

<artifactId>spring-boot-starter-web</artifactId>

</dependency>
```

During build

Maven downloads

Spring MVC

Jackson

Tomcat

Logging

Validation

and all transitive dependencies.

---

# 38. What are Transitive Dependencies?

Suppose we add

```xml
spring-boot-starter-web
```

We never added

Tomcat.

We never added

Jackson.

We never added

Spring MVC.

Still they are available.

Why?

Because Maven downloads all required dependencies automatically.

This is called

```
Transitive Dependency Resolution
```

---

# 39. How Maven Resolves Dependencies

When we execute

```
mvn clean install
```

Maven follows these steps.

```
Read pom.xml

↓

Read Parent POM

↓

Read Dependency Management

↓

Resolve Versions

↓

Download Missing Libraries

↓

Store in Local Repository

↓

Compile Project
```

Downloaded libraries are stored in

```
~/.m2/repository
```

Future builds reuse them.

---

# 40. Summary

Properties

✔ Store reusable values.

Dependency Management

✔ Controls dependency versions.

Dependencies

✔ Actually download and use libraries.

Spring Boot BOM

✔ Provides tested versions of Spring Boot libraries.

Spring Cloud BOM

✔ Provides compatible versions of Spring Cloud libraries.

Together, these features make WorkSphere easy to maintain, easy to upgrade, and consistent across all microservices.

---

# 41. WorkSphere Module POM Explanation

Our WorkSphere project consists of five Maven modules.

```
worksphere-parent
│
├── common-library
├── employee-service
├── department-service
├── eureka-server
└── api-gateway
```

Every module has its own responsibility and therefore its own dependencies.

Let's understand each module one by one.

---

# 42. Common Library

Purpose

The Common Library contains reusable code shared across all microservices.

Instead of writing the same classes multiple times, we write them once and share them.

Current Classes

```
ApiResponse

ErrorResponse

ValidationErrorResponse

GlobalExceptionHandler

ResourceNotFoundException

DuplicateResourceException
```

---

## common-library/pom.xml

Current Dependencies

```xml
<dependency>

    <groupId>org.springframework.boot</groupId>

    <artifactId>spring-boot-starter-web</artifactId>

</dependency>

<dependency>

    <groupId>jakarta.servlet</groupId>

    <artifactId>jakarta.servlet-api</artifactId>

    <scope>provided</scope>

</dependency>
```

---

## spring-boot-starter-web

### Why did we add it?

Our Common Library contains

```
GlobalExceptionHandler

ResponseEntity

HttpStatus

@RestControllerAdvice
```

All these classes belong to Spring Web.

Without this dependency

The project cannot compile.

---

### What does it provide?

Internally it downloads

- Spring MVC

- Spring Core

- Spring Beans

- Jackson

- Validation

- Logging

- Embedded Tomcat (transitively)

Although Common Library doesn't expose REST APIs,

it still uses Spring Web annotations.

---

### What happens if we remove it?

Compilation fails.

Classes like

```
ResponseEntity

HttpStatus

@RestControllerAdvice
```

cannot be found.

---

## jakarta.servlet-api

### Why did we add it?

Inside

```
GlobalExceptionHandler
```

we use

```java
HttpServletRequest
```

to return

```
request.getRequestURI()
```

This class belongs to

```
jakarta.servlet
```

Without this dependency

HttpServletRequest cannot be resolved.

---

### Why is scope = provided?

```xml
<scope>provided</scope>
```

means

This dependency is required only during compilation.
It is already provided by the web server (Tomcat).
Therefore Maven does not package it inside our JAR.
This reduces application size.

---

# 43. Employee Service

Purpose
Employee Service manages all employee-related operations.
Responsibilities

✔ CRUD

✔ Validation

✔ Pagination

✔ Sorting

✔ Department Communication

✔ Swagger

✔ Database Operations

---

Current Dependencies

```xml
spring-boot-starter-web

spring-boot-starter-data-jpa

mysql-connector-j

spring-boot-starter-validation

springdoc-openapi

common-library

spring-cloud-starter-openfeign

spring-cloud-starter-netflix-eureka-client

spring-cloud-starter-loadbalancer

spring-boot-starter-actuator
```

Let's understand each.

---

## spring-boot-starter-web

Purpose
Provides REST API support.
Internally it provides

- DispatcherServlet

- Spring MVC

- Jackson

- Embedded Tomcat

- REST Controllers

- Request Mapping

- Exception Handling

Without it

```
@RestController

@GetMapping

@PostMapping

@RequestBody
```

do not work.

---

## spring-boot-starter-data-jpa

Purpose

Database Access.
Provides

Hibernate

JpaRepository

EntityManager

Transactions

JPQL

CRUD Repository

Without it

```
@Repository

JpaRepository

@Entity
```

will not work.

---

## mysql-connector-j

Purpose

Actual JDBC Driver.
Spring Boot knows
HOW
to use database.
But
it does NOT know
HOW to connect to MySQL. This driver solves that problem.
Without it

Application starts

↓

Database Connection fails.

---

## spring-boot-starter-validation

Purpose
Bean Validation.
Provides

```
@NotBlank

@NotNull

@Email

@Positive

@Size
```

Without it
Validation annotations are ignored.

---

## springdoc-openapi

Purpose
Automatically generates
Swagger UI.
Instead of manually documenting APIs,
Swagger reads annotations like

```
@Operation

@Schema
```

and generates documentation.
Without it

```
/swagger-ui.html
```

will not exist.

---

## common-library

Purpose
Reuse common code.
Instead of writing

```
ApiResponse

ErrorResponse

GlobalExceptionHandler
```

inside every service,
Employee Service imports them from Common Library.
Benefits

✔ Code Reuse

✔ Less Duplication

✔ Easier Maintenance

---

## spring-cloud-starter-openfeign

Purpose
Declarative REST Client.
Instead of writing

```java
RestClient

↓

uri()

↓

retrieve()

↓

body()
```

we simply write

```java
departmentFeignClient.getDepartment(id)
```

Internally
Spring creates a dynamic implementation of our interface.
We only define the contract.

---

## spring-cloud-starter-netflix-eureka-client

Purpose

Registers Employee Service inside Eureka.
Without this dependency
Employee Service will never appear in

```
http://localhost:8761
```

Gateway also won't discover Employee Service.

---

## spring-cloud-starter-loadbalancer

Purpose Client-side Load Balancing.

Earlier

Employee Service used

```
localhost:8082
```

Hardcoded. Now Employee Service calls

```
department-service
```

LoadBalancer asks Eureka where Department Service is currently running.
This removes hardcoded URLs.

---

## spring-boot-starter-actuator

Purpose Production Monitoring. Provides endpoints

```
/actuator/health

/actuator/info

/actuator/metrics
```

Useful for

Monitoring

Kubernetes

Prometheus

Grafana

Health Checks

---

# Summary

Employee Service is the most feature-rich module in WorkSphere.

It combines

✔ REST APIs

✔ Database Access

✔ Validation

✔ Documentation

✔ Service Discovery

✔ Service Communication

✔ Monitoring

to provide a complete enterprise-ready microservice.

---

# 44. Department Service

## Purpose

Department Service is responsible for managing all department-related operations.
Responsibilities

✔ Create Department

✔ Update Department

✔ Delete Department

✔ Get Department by Id

✔ Pagination

✔ Sorting

Unlike Employee Service, this service owns the Department database table.
Employee Service communicates with it whenever department information is required.

---

## Current Dependencies

```xml
spring-boot-starter-web

spring-boot-starter-data-jpa

mysql-connector-j

spring-boot-starter-validation

springdoc-openapi

common-library

spring-cloud-starter-netflix-eureka-client

spring-boot-starter-actuator
```

Notice that it does NOT require OpenFeign.

Why?

Because Department Service does not call any other service.
Employee Service calls Department Service.
Department Service is a provider.
Employee Service is the consumer.

---

## spring-boot-starter-web

Provides

- REST APIs

- JSON Conversion

- DispatcherServlet

- Embedded Tomcat

- REST Controllers

Used in

```
DepartmentController
```

Without it No REST APIs can be exposed.

---

## spring-boot-starter-data-jpa

Responsible for Database Communication Provides

- Hibernate

- JpaRepository

- EntityManager

- Transactions

Used by

```
DepartmentRepository
```

---

## mysql-connector-j

JDBC Driver for MySQL. Without it Spring Boot cannot connect to MySQL.

---

## spring-boot-starter-validation

Enables

```
@NotBlank

@NotNull

@Email

@Positive
```

Used inside DepartmentRequest

---

## springdoc-openapi

Automatically generates Swagger Documentation.

Provides

```
Swagger UI

OpenAPI Specification
```

Useful for API testing.

---

## common-library

Reuses

```
ApiResponse

GlobalExceptionHandler

Custom Exceptions
```

No duplicate code.

---

## spring-cloud-starter-netflix-eureka-client

Registers Department Service inside Eureka.
After startup
Department Service tells Eureka "I am running." Now
Employee Service Gateway Other Services can discover it.

---

## spring-boot-starter-actuator

Provides monitoring endpoints. Useful in production.

Examples

```
/actuator/health

/actuator/info

/actuator/metrics
```

---

# Summary

Department Service is intentionally lightweight. Its responsibility is only Department Management.

Following the Single Responsibility Principle keeps the service clean and maintainable.

---

# 45. Eureka Server

## Purpose

Eureka is the Service Registry.

Think of it as a phone directory for microservices.

Earlier



```
localhost:8082
```

Employee Service had hardcoded. Now Employee Service asks Eureka

"Where is Department Service running?"

Eureka replies

```
department-service

↓

localhost:8082
```

or

```
192.168.1.12:8082
```

or

```
Docker Container
```

Employee Service no longer needs to know the actual URL.

---

## Current Dependencies

```xml
spring-cloud-starter-netflix-eureka-server

spring-boot-starter-actuator
```

---

## spring-cloud-starter-netflix-eureka-server

Very important dependency.

Enables

```
@EnableEurekaServer
```

Provides

- Registry

- Dashboard

- Service Registration

- Service Discovery

- Heartbeat Monitoring

Without it

Eureka cannot function.

---

## spring-boot-starter-actuator

Provides health monitoring. Useful for Kubernetes and production environments.

---

# What happens internally?

When Employee Service starts

↓

Registers itself

↓

Eureka stores

```
employee-service

localhost:8081
```

When Department starts

↓

Registers itself

↓

Eureka stores

```
department-service

localhost:8082
```

Now every service knows where every other service is running.

---

# Summary

Eureka removes Hardcoded URLs.

This is one of the biggest improvements we made in WorkSphere.

---

# 46. API Gateway

## Purpose

Gateway is the single entry point for the entire application.

Instead of clients calling

```
Employee Service

Department Service

Notification Service

Inventory Service
```

directly, they call

```
API Gateway
```

Gateway forwards the request to the correct microservice.

---

## Current Dependencies

```xml
spring-cloud-starter-gateway

spring-cloud-starter-netflix-eureka-client

spring-cloud-starter-loadbalancer

spring-boot-starter-actuator
```

---

## spring-cloud-starter-gateway

Provides

Spring Cloud Gateway.

Responsible for

- Routing

- Request Forwarding

- Filters

- Authentication

- Logging

- Rate Limiting

It replaces Zuul in modern Spring Cloud.

---

## spring-cloud-starter-netflix-eureka-client

Registers Gateway inside Eureka. Also enables Gateway to discover other services.

---

## spring-cloud-starter-loadbalancer

Allows Gateway to route requests using service names.

Instead of

```
localhost:8081
```

Gateway forwards requests to

```
lb://employee-service
```

LoadBalancer consults Eureka to locate the correct instance.

---

## spring-boot-starter-actuator

Provides- 

* Health Monitoring

* Metrics

* Application Status

---

# Gateway Request Flow

Client

↓

API Gateway

↓

LoadBalancer

↓

Eureka

↓

Employee Service

↓

Database

Without Gateway  Client would need to know the URL of every service.
Gateway hides this complexity.

---

# Why is Gateway Important?

Advantages

✔ Single Entry Point

✔ Central Authentication

✔ Logging

✔ Routing

✔ Load Balancing

✔ Security

✔ Rate Limiting

✔ Request Filtering

---

# 47. Overall Dependency Flow

```
Client

↓

API Gateway

↓

LoadBalancer

↓

Eureka

↓

Employee Service

↓

OpenFeign / RestClient

↓

LoadBalancer

↓

Eureka

↓

Department Service

↓

MySQL
```

---

# 48. WorkSphere Architecture After Introducing Eureka

Before

```
Employee Service

↓

http://localhost:8082
```

Problems

❌ Hardcoded URL

❌ Difficult to deploy

❌ Difficult to scale

❌ Environment dependent

---

After

```
Employee Service

↓

department-service

↓

LoadBalancer

↓

Eureka

↓

Department Service
```

Advantages

✔ No hardcoded URLs

✔ Dynamic service discovery

✔ Multiple service instances supported

✔ Easier cloud deployment

✔ Better scalability

✔ Enterprise architecture

---

# 49. Interview Questions

### Why do we use Eureka?

To eliminate hardcoded service URLs and enable dynamic service discovery.

---

### Why do we need API Gateway?

To provide a single entry point for all client requests and centralize routing, security, logging, and rate limiting.

---

### Why does Employee Service need OpenFeign but Department Service doesn't?

Employee Service consumes another microservice (Department Service), so it needs a client.

Department Service does not consume any other service.

---

### Why is LoadBalancer required?

It resolves a service name like

```
department-service
```

into the actual running instance registered in Eureka.

---

### Why do we still use Common Library?

To avoid duplicating exception handling, response models, and reusable classes across multiple microservices.

---

# Summary

At this stage, WorkSphere has evolved from a simple CRUD application into a real microservices architecture.

Current architecture includes:

✔ Multi-module Maven Project

✔ Common Library

✔ Employee Service

✔ Department Service

✔ Eureka Server

✔ API Gateway

✔ OpenFeign

✔ RestClient

✔ Service Discovery

✔ Client-side Load Balancing

✔ Swagger

✔ Global Exception Handling

✔ Pagination & Sorting

✔ MySQL

✔ Spring Boot 3.5

✔ Java 21

This forms a solid enterprise-grade foundation on which future features like Config Server, Kafka, Redis, Security, Docker, Kubernetes, and Monitoring can be added.