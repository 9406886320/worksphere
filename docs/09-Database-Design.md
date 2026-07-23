# Database Design

# Learning Objectives

After reading this document, you will be able to:

- Understand the importance of database design in software development.
- Learn the difference between monolithic and microservices database architectures.
- Understand the Database per Service Pattern.
- Explain why WorkSphere uses separate databases for each microservice.
- Understand how Employee Service and Department Service manage their own data.
- Learn why microservices should avoid sharing databases.
- Understand how services communicate without database-level relationships.
- Answer common interview questions related to microservice database architecture.

---

# Introduction

A database is the backbone of any application. It is responsible for storing, organizing, retrieving, and managing application data efficiently.

In modern enterprise applications, a well-designed database is essential for achieving scalability, maintainability, performance, and reliability.

In traditional monolithic applications, all modules usually share a single database. Although this approach is simple during the initial stages of development, it becomes increasingly difficult to maintain as the application grows.

Microservices architecture follows a different approach.

Instead of sharing one database, each microservice owns its own database. Every service becomes responsible for managing its own data without allowing other services to access its database directly.

WorkSphere follows this architecture by implementing the **Database per Service Pattern**.

Current database structure:

```
MySQL

├── worksphere_employee
│
│   └── employees
│
└── worksphere_department
    │
    └── departments
```

Each database belongs to a single microservice and is managed independently.

---

# Why Database Design Matters

Database design directly affects the quality of an application.

A properly designed database provides:

- Faster query execution
- Reduced data redundancy
- Better maintainability
- Improved scalability
- Better data consistency
- Easier future enhancements
- Clear ownership of business data

Poor database design can introduce several problems.

Examples include:

- Duplicate records
- Slow database queries
- Difficult schema changes
- Tight coupling between modules
- Data inconsistency
- Complex maintenance

For enterprise applications, database design is considered one of the most important architectural decisions.

---

# Database Architecture Approaches

There are two common approaches for designing databases.

## Shared Database (Monolithic Architecture)

In a monolithic application, all modules share the same database.

```
                Monolithic Application

        Employee Module
        Department Module
        Payroll Module

                │
                ▼

        ┌─────────────────────────┐
        │      worksphere_db      │
        │                         │
        │ employees               │
        │ departments             │
        │ payroll                 │
        └─────────────────────────┘
```

Every module accesses the same database.

### Advantages

- Simple architecture
- Easy SQL joins
- Centralized data storage

### Disadvantages

- High coupling
- Difficult independent deployments
- Difficult scaling
- Schema changes affect every module
- Poor fault isolation

As applications become larger, these disadvantages become significant.

---

## Database per Service (Microservices Architecture)

Microservices follow a different design.

Each service owns its own database.

```
Employee Service

        │

        ▼

worksphere_employee

        │

   employees
```

```
Department Service

        │

        ▼

worksphere_department

        │

   departments
```

Every service is responsible for:

- Creating its own tables
- Managing its own schema
- Performing CRUD operations
- Exposing APIs for other services

No service directly accesses another service's database.

Instead, services communicate through REST APIs.

---

> 💡 Engineering Insight
>
> One of the core principles of microservices is:
>
> **A service owns its data.**
>
> Other services communicate through APIs—not through direct database access.

---

# Why WorkSphere Uses Database per Service Pattern

WorkSphere is built as a microservices application.

Each microservice represents a separate business capability.

For example:

- Employee Service manages employee information.
- Department Service manages department information.

Since each service has its own responsibility, each service also owns its own database.

Current database architecture:

```
MySQL

├── worksphere_employee
│
│   └── employees
│
└── worksphere_department
    │
    └── departments
```

This design provides several important advantages.

## Independent Development

Each service can evolve independently without affecting another service's database.

---

## Independent Deployment

Employee Service can be deployed without modifying Department Service.

---

## Better Scalability

If Employee Service receives heavy traffic, only the Employee database needs additional resources.

Department Service remains unaffected.

---

## Better Fault Isolation

If one database becomes unavailable, only the corresponding microservice is impacted.

Other services continue functioning normally.

---

## Better Security

Each service only has permission to access its own database.

Cross-service database access is avoided.

---

## Loose Coupling

Since services do not share databases, changes made by one team do not directly affect another team's data model.

This improves maintainability and reduces dependencies.

---

# Engineering Decision

## Problem

A decision had to be made regarding how application data would be stored.

Should all services share one database, or should every service own its own database?

---

## Options Considered

### Option 1

Shared Database

Advantages

- Easy joins
- Simple implementation

Disadvantages

- Tight coupling
- Difficult scaling
- Difficult deployments

---

### Option 2

Database per Service

Advantages

- Independent ownership
- Better scalability
- Better fault isolation
- Loose coupling

Disadvantages

- Service communication is required.
- SQL joins across services are not possible.

---

## Decision

WorkSphere uses the **Database per Service Pattern**.

Each microservice owns its own database.

```
Employee Service

↓

worksphere_employee
```

```
Department Service

↓

worksphere_department
```

Services communicate using REST APIs through OpenFeign and Eureka rather than sharing a common database.

---

## Why This Decision?

This architecture aligns with modern enterprise microservices principles.

It allows every service to:

- Scale independently
- Deploy independently
- Manage its own schema
- Own its own data

---

## Trade-offs

Although SQL joins across services are no longer possible, the benefits of loose coupling and independent deployment significantly outweigh this limitation.

---

## Production Verdict

This is the recommended approach for enterprise microservices and is widely adopted in cloud-native applications.

# WorkSphere Database Architecture

WorkSphere follows the **Database per Service Pattern**, where every microservice owns and manages its own database.

Unlike a monolithic application, services do not share a common database. Each service is responsible for storing and maintaining only its own business data.

The current database architecture is shown below.

```
                    MySQL Server
                          │
        ┌─────────────────┴─────────────────┐
        │                                   │
        ▼                                   ▼
worksphere_employee              worksphere_department
        │                                   │
        ▼                                   ▼
   employees Table                 departments Table
        │                                   │
        ▼                                   ▼
 Employee Service                Department Service
```

This architecture ensures:

- Independent data ownership
- Loose coupling
- Better scalability
- Independent deployments
- Fault isolation

Each database belongs to exactly one microservice.

No database is shared between services.

---

# Employee Database

Employee Service manages all employee-related information.

Its database is:

```
worksphere_employee
```

Inside this database, WorkSphere currently contains one table.

```
worksphere_employee

└── employees
```

The Employee Service performs all CRUD operations on this table.

No other service accesses this database directly.

---

# Employees Table

The **employees** table stores employee-related information.

Current table structure:

| Column | Data Type | Description |
|----------|-----------|-------------|
| id | BIGINT | Primary Key |
| first_name | VARCHAR | Employee First Name |
| last_name | VARCHAR | Employee Last Name |
| email | VARCHAR | Employee Email Address |
| salary | DOUBLE | Employee Salary |
| department_id | BIGINT | Department Identifier |

Example data

| id | first_name | last_name | email | salary | department_id |
|----|------------|-----------|-------|---------|---------------|
| 1 | Sakshi | Agrawal | sakshi@gmail.com | 65000 | 1 |

---

# Employee Entity Mapping

The Employee entity is mapped using JPA.

```java
@Entity
@Table(name = "employees")
public class Employee
```

## @Entity

```java
@Entity
```

Marks the class as a JPA entity.

Spring Boot treats this class as a database table.

---

## @Table

```java
@Table(name = "employees")
```

Maps the entity to the

```
employees
```

table.

Without this annotation, Hibernate would use the class name as the table name.

---

# Primary Key

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

The **id** column uniquely identifies every employee.

Example

```
1

2

3

4
```

The value is generated automatically by MySQL.

Using

```java
GenerationType.IDENTITY
```

allows MySQL's AUTO_INCREMENT feature to generate unique IDs.

---

# Employee Attributes

## First Name

```java
private String firstName;
```

Stores the employee's first name.

Database column

```
first_name
```

---

## Last Name

```java
private String lastName;
```

Stores the employee's last name.

Database column

```
last_name
```

---

## Email

```java
private String email;
```

Stores the employee's email address.

Every employee should ideally have a unique email.

> 💡 Engineering Recommendation
>
> In future versions, this field should be annotated with:
>
> ```java
> @Column(nullable = false, unique = true)
> ```
>
> This prevents duplicate email addresses.

---

## Salary

```java
private Double salary;
```

Stores the employee's salary.

Current implementation uses

```
Double
```

> 💡 Production Recommendation
>
> Enterprise applications generally use
>
> ```java
> BigDecimal
> ```
>
> for financial values because it avoids floating-point precision issues.

---

## Department ID

```java
@Column(nullable = false)
private Long departmentId;
```

This field stores the department identifier.

Example

```
department_id = 1
```

Notice that it stores only the department's ID.

It does **not** store the Department object.

---

# Why Only departmentId?

Many beginners expect something like:

```java
@ManyToOne
@JoinColumn(name = "department_id")
private Department department;
```

WorkSphere intentionally avoids this design.

Instead, it stores only:

```java
private Long departmentId;
```

This keeps Employee Service independent.

Whenever department information is required,

Employee Service calls Department Service using OpenFeign.

```
Employee Service

↓

departmentId

↓

OpenFeign

↓

Department Service

↓

Department Database
```

This follows one of the most important principles of microservices.

> 📝 Engineering Decision
>
> Services communicate through APIs.
>
> They do **not** communicate through database relationships.

# Department Database

Department Service is responsible for managing all department-related information within WorkSphere.

Its dedicated database is:

```
worksphere_department
```

Inside this database, WorkSphere currently contains one table.

```
worksphere_department

└── departments
```

The Department Service performs all CRUD operations on this table.

No other microservice directly accesses this database.

---

# Departments Table

The **departments** table stores department-related information.

Current table structure:

| Column | Data Type | Description |
|----------|-----------|-------------|
| id | BIGINT | Primary Key |
| department_name | VARCHAR | Department Name |
| department_code | VARCHAR | Unique Department Code |
| department_head | VARCHAR | Department Head |
| location | VARCHAR | Department Location |

Example data

| id | department_code | department_head | department_name | location |
|----|-----------------|-----------------|-----------------|----------|
| 1 | ENG001 | Sakshi Agrawal | Engineering | Indore |
| 2 | CVG001 | Sandesh Agrawal | Civil Service | Indore |

---

# Department Entity Mapping

The Department entity is mapped using JPA.

```java
@Entity
@Table(name = "departments")
public class Department
```

---

# @Entity

```java
@Entity
```

Marks the class as a JPA entity.

Spring Boot automatically maps this entity to a database table.

---

# @Table

```java
@Table(name = "departments")
```

Specifies that this entity is mapped to the

```
departments
```

table.

This improves readability and removes any dependency on default naming conventions.

---

# Primary Key

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

The **id** uniquely identifies each department.

Example

```
1

2

3
```

The value is automatically generated by MySQL using the **AUTO_INCREMENT** feature.

---

# Department Attributes

## Department Name

```java
@Column(nullable = false)
private String departmentName;
```

Stores the department name.

Examples

```
Engineering

Civil Service

Human Resources
```

This field is mandatory because every department must have a name.

---

## Department Code

```java
@Column(nullable = false, unique = true)
private String departmentCode;
```

Stores a unique code for each department.

Examples

```
ENG001

CVG001

HR001
```

The `unique = true` constraint ensures that no two departments can have the same code.

This makes the department code a reliable business identifier.

---

## Department Head

```java
@Column(nullable = false)
private String departmentHead;
```

Stores the name of the department head.

Examples

```
Sakshi Agrawal

Sandesh Agrawal
```

Every department should have a responsible owner, so this field is mandatory.

---

## Location

```java
@Column(nullable = false)
private String location;
```

Stores the physical or operational location of the department.

Examples

```
Indore

Bangalore

Pune
```

This field helps identify where the department operates.

---

# Why Use Constraints?

Most fields in the Department entity are annotated with:

```java
@Column(nullable = false)
```

These constraints help maintain data integrity.

Benefits include:

- Prevents incomplete records.
- Ensures mandatory business information is always available.
- Improves database consistency.
- Reduces validation errors.

---

# Why Is departmentCode Unique?

Although the **id** is the primary key, it is mainly used internally by the database.

The **departmentCode** serves as a business identifier.

For example:

```
ENG001
```

is much easier to reference in reports or integrations than:

```
1
```

Using a unique business code also prevents duplicate department records.

---

> 💡 Engineering Insight
>
> In enterprise systems, it is common to have:
>
> - A technical identifier (Primary Key)
> - A business identifier (Unique Code)
>
> WorkSphere follows this approach by using:
>
> - `id` → Technical Identifier
> - `departmentCode` → Business Identifier

---

# Engineering Decision

## Problem

Every department needs a stable identifier that users and external systems can easily recognize.

---

## Options Considered

### Option 1

Use only the database-generated `id`.

Advantages

- Simple implementation.

Disadvantages

- IDs have no business meaning.
- Difficult to reference outside the system.

---

### Option 2

Introduce a unique department code.

Advantages

- Human-readable.
- Easy to search.
- Easy to integrate with external systems.
- Prevents duplicate departments.

Disadvantages

- Requires validation to ensure uniqueness.

---

## Decision

WorkSphere stores both:

- Database-generated Primary Key (`id`)
- Business Identifier (`departmentCode`)

This follows a common enterprise design pattern and provides flexibility for future integrations.

# Why We Don't Use Foreign Keys Between Microservices

One of the biggest differences between monolithic and microservices architectures is how relationships between entities are managed.

In a traditional monolithic application, it is common to create database relationships using foreign keys.

For example:

```
employees

-------------------------
id
first_name
last_name
department_id  ----+
                   |
                   |
departments <------+
-------------------------
id
department_name
department_code
```

In this design, the database itself maintains the relationship between employees and departments.

Although this approach works well in a monolithic application, it is **not recommended** in a microservices architecture.

---

# Why Foreign Keys Are Avoided

WorkSphere follows the **Database per Service Pattern**.

Each microservice owns its own database.

Current architecture:

```
                MySQL

      ┌──────────────────────────┐
      │ worksphere_employee      │
      │                          │
      │ employees                │
      └──────────────────────────┘

                ▲

                │ REST API

                ▼

      ┌──────────────────────────┐
      │ worksphere_department    │
      │                          │
      │ departments              │
      └──────────────────────────┘
```

Notice that the databases are completely independent.

There is **no database-level relationship** between them.

---

# How Employee Service Stores Department Information

Instead of storing the complete Department object, Employee Service stores only the department identifier.

```java
@Column(nullable = false)
private Long departmentId;
```

Example

```
Employee

ID : 1

Name : Sakshi Agrawal

Department ID : 1
```

The employee database only knows the department's identifier.

It does not know:

- Department Name
- Department Code
- Department Head
- Location

That information belongs exclusively to Department Service.

---

# How Employee Service Gets Department Information

Whenever Employee Service needs department details, it does not query the Department database.

Instead, it calls Department Service.

The communication flow is:

```
Employee Service

        │

        ▼

OpenFeign Client

        │

        ▼

Eureka Server

        │

        ▼

Department Service

        │

        ▼

worksphere_department

        │

        ▼

departments Table
```

Department Service retrieves the data from its own database and returns the response.

Employee Service never directly accesses another service's database.

---

# Why Is This Better?

This architecture provides several important benefits.

## Loose Coupling

Each service owns its own data.

Changes made in one database do not directly affect another service.

---

## Independent Deployment

Department Service can be deployed independently without changing Employee Service.

---

## Better Scalability

Each database can be scaled independently based on its workload.

---

## Better Security

Each service has access only to its own database.

Direct database access from other services is avoided.

---

## Better Maintainability

Each team manages its own database schema.

This reduces dependencies between development teams.

---

# Why Not Use @ManyToOne?

Many developers coming from monolithic applications expect something like:

```java
@ManyToOne
@JoinColumn(name = "department_id")
private Department department;
```

This is appropriate when both entities exist in the same database.

However, in WorkSphere:

- Employee belongs to Employee Service.
- Department belongs to Department Service.
- Each service has its own database.

Because of this separation, JPA cannot maintain a relationship across two different databases owned by different services.

Instead, WorkSphere stores only the department ID and retrieves department details through a REST API.

This keeps the services independent and follows microservices best practices.

---

> 💡 Engineering Insight
>
> In a monolithic application, relationships are managed by the database.
>
> In a microservices architecture, relationships are managed by services through APIs.

---

# Engineering Decision

## Problem

Employee records need to reference a department.

How should this relationship be maintained?

---

## Options Considered

### Option 1

Use a foreign key relationship.

Advantages

- Simple SQL joins.
- Automatic referential integrity.

Disadvantages

- Tight coupling.
- Shared database dependency.
- Violates microservices principles.

---

### Option 2

Store only the department ID.

Advantages

- Loose coupling.
- Independent databases.
- Better scalability.
- Independent deployments.

Disadvantages

- Requires an API call to retrieve department details.

---

## Decision

WorkSphere stores only:

```
departmentId
```

Department information is retrieved through:

- OpenFeign
- Eureka Service Discovery
- REST APIs

This design follows enterprise microservices architecture.

---

## Trade-offs

Using service communication instead of SQL joins introduces additional network calls.

However, the benefits of loose coupling, scalability, and independent ownership far outweigh this cost.

---

## Production Verdict

This is the recommended approach for microservices and is widely adopted in enterprise systems built with Spring Boot and Spring Cloud.

---

# Common Mistakes

Avoid the following mistakes when designing microservices databases.

❌ Sharing one database across multiple services.

❌ Creating foreign keys between different microservice databases.

❌ Using `@ManyToOne` across service boundaries.

❌ Allowing one service to directly query another service's database.

❌ Duplicating data without a clear synchronization strategy.

Following these practices keeps services independent and easier to maintain.

# Database Naming Conventions

A well-defined naming convention improves readability, consistency, and maintainability.

WorkSphere follows simple and meaningful naming conventions for databases, tables, columns, and entities.

---

## Database Names

Database names are written in lowercase and prefixed with the project name.

Examples

```
worksphere_employee

worksphere_department
```

Benefits

- Easy to identify
- Avoids naming conflicts
- Clearly indicates ownership

---

## Table Names

Table names use plural nouns.

Examples

```
employees

departments
```

Plural names indicate that the table stores multiple records.

---

## Column Names

Database columns follow the snake_case naming convention.

Examples

```
first_name

last_name

department_name

department_code

department_head
```

Snake case is widely used in relational databases because it improves readability.

---

## Java Entity Fields

Java follows the camelCase naming convention.

Examples

```java
private String firstName;

private String lastName;

private String departmentName;

private String departmentCode;
```

Hibernate automatically maps Java camelCase fields to snake_case database columns.

---

## Primary Keys

Every table contains a single primary key.

Example

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

Using a generated primary key simplifies record management and ensures uniqueness.

---

# Database Best Practices

WorkSphere follows several database design best practices.

## 1. One Database Per Service

Each microservice owns its own database.

Benefits

- Loose coupling
- Independent deployment
- Better scalability

---

## 2. Avoid Cross-Database Access

Services never directly query another service's database.

Instead, they communicate through REST APIs.

---

## 3. Use Meaningful Table Names

Good

```
employees

departments
```

Avoid

```
emp

dept

table1
```

Meaningful names improve readability.

---

## 4. Use Primary Keys

Every table should have a unique primary key.

This allows efficient record retrieval and updates.

---

## 5. Apply Database Constraints

Use constraints whenever possible.

Examples

```java
@Column(nullable = false)
```

```java
@Column(unique = true)
```

Constraints help maintain data integrity.

---

## 6. Keep Services Independent

Each service should manage only its own business data.

Business logic should never depend on another service's database schema.

---

## 7. Use Business Identifiers

Along with technical identifiers (`id`), use meaningful business identifiers where appropriate.

Example

```
departmentCode
```

This improves integration with external systems.

---

## 8. Design for Scalability

The database design should support future growth.

For example, WorkSphere can easily introduce additional services such as:

```
Payroll Service

Project Service

Attendance Service

Notification Service
```

Each service would own its own database.

---

# Knowledge Check

After completing this chapter, you should be able to answer the following questions.

✔ What is the Database per Service Pattern?

✔ Why does WorkSphere use separate databases?

✔ Why doesn't Employee Service directly access the Department database?

✔ Why is `departmentId` stored instead of a Department object?

✔ Why are foreign keys avoided between microservices?

✔ How does OpenFeign replace SQL joins?

✔ What are the advantages of this architecture?

✔ What are the trade-offs of using independent databases?

---

# Interview Questions

## Beginner Level

### What is database design?

### Why is database design important?

### What is a primary key?

### What is the purpose of `@Entity`?

### What is the purpose of `@Table`?

### Why is `GenerationType.IDENTITY` used?

---

## Intermediate Level

### What is the Database per Service Pattern?

### Why does WorkSphere use multiple databases?

### What are the benefits of separate databases?

### Why does Employee Service store only `departmentId`?

### What is the purpose of `departmentCode`?

### Why are database constraints important?

---

## Advanced Level

### Why are foreign keys avoided between microservices?

### Why isn't `@ManyToOne` used in WorkSphere?

### How does Employee Service retrieve department information?

### What happens if Department Service is unavailable?

### What are the trade-offs of service-to-service communication?

### How would you maintain consistency between multiple microservices?

### How would you scale the database architecture if WorkSphere had 50 microservices?

### What are the advantages and disadvantages of Database per Service compared to a Shared Database?

---

# Summary

In this document, we explored the database architecture of WorkSphere and the reasoning behind its design.

We learned that WorkSphere follows the **Database per Service Pattern**, where each microservice owns and manages its own database independently.

The project currently contains:

```
worksphere_employee
    └── employees

worksphere_department
    └── departments
```

Employee Service manages employee data, while Department Service manages department data.

Instead of sharing databases or using foreign key relationships, services communicate through REST APIs using OpenFeign and Eureka Service Discovery.

This architecture provides:

- Loose coupling
- Independent deployment
- Better scalability
- Improved maintainability
- Better fault isolation
- Clear ownership of business data

Throughout this chapter, we covered:

- Database Design Fundamentals
- Database per Service Pattern
- WorkSphere Database Architecture
- Employee Database
- Department Database
- Entity Mapping
- Primary Keys
- Business Identifiers
- Engineering Decisions
- Best Practices
- Common Mistakes
- Interview Questions

By following these principles, WorkSphere aligns with modern enterprise microservices architecture and is well-prepared for future enhancements and scaling.
