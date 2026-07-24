# 15. Bean Validation

## Table of Contents

1. Introduction
2. Why Bean Validation?
3. Why Validate Request DTOs Instead of Entity Classes?
4. Validation Architecture in WorkSphere
5. Validation Flow
6. EmployeeRequest Validation
7. DepartmentRequest Validation
8. Controller Validation using `@Valid`
9. Global Exception Handling
10. Sample API Requests
11. Sample Validation Responses
12. Best Practices
13. Common Mistakes
14. Interview Questions
15. Summary

---

# 1. Introduction

Data validation is one of the most important aspects of any enterprise application. Every request received by a REST API should be validated before it reaches the business logic. Without proper validation, invalid or incomplete data can be stored in the database, leading to data inconsistency and unexpected application behavior.

Spring Boot provides built-in support for **Jakarta Bean Validation**, allowing developers to validate incoming requests declaratively using annotations instead of writing repetitive validation logic manually.

In the WorkSphere project, Bean Validation is implemented using:

- Request Records (DTOs) for accepting client requests.
- Jakarta Validation annotations to define validation rules.
- `@Valid` annotation in controller methods to trigger validation automatically.
- Global Exception Handler to return consistent and meaningful validation error responses.

This approach ensures that only valid data reaches the service layer while keeping the code clean, maintainable, and aligned with enterprise development practices.

---

# 2. Why Bean Validation?

Before Bean Validation, developers often performed validation manually inside service methods.

Example:

```java
if (request.getFirstName() == null || request.getFirstName().isBlank()) {
    throw new IllegalArgumentException("First name is required");
}

if (request.getSalary() <= 0) {
    throw new IllegalArgumentException("Salary must be greater than zero");
}
```

As the application grows, this approach leads to several problems:

- Duplicate validation logic.
- Difficult maintenance.
- Poor readability.
- Inconsistent error handling.
- Increased boilerplate code.

Jakarta Bean Validation solves this problem by allowing validation rules to be declared directly on the request model.

Instead of writing manual validation logic, developers simply define constraints using annotations.

Example:

```java
@NotBlank(message = "First name is required")
String firstName;

@Email(message = "Please provide a valid email")
String email;

@Positive(message = "Salary must be greater than zero")
Double salary;
```

Spring Boot automatically validates the request before the controller method is executed.

If any validation rule fails:

- The controller method is not executed.
- The service layer is not called.
- A validation exception is generated automatically.
- The Global Exception Handler returns a structured error response to the client.

This significantly reduces boilerplate code while improving application reliability.

---

# 3. Why Validate Request DTOs Instead of Entity Classes?

WorkSphere follows the industry-standard approach of validating **Request DTOs (Records)** instead of JPA Entity classes.

The application architecture is designed as follows:

```text
Client
   │
   ▼
EmployeeRequest / DepartmentRequest
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Mapper
   │
   ▼
Entity
   │
   ▼
Database
```

The **Request Record** represents the incoming API request and is responsible for validating client input.

The **Entity** represents the database model and is responsible only for persistence.

Keeping these responsibilities separate provides several advantages:

- Clear separation between API models and database models.
- Validation rules remain independent of persistence logic.
- Different validation rules can be applied for Create and Update operations in the future.
- Entities remain focused on database mapping.
- API changes do not directly impact database entities.

For these reasons, WorkSphere performs validation on:

- `EmployeeRequest`
- `DepartmentRequest`

instead of:

- `Employee`
- `Department`

This approach follows widely accepted enterprise Spring Boot development practices.

---

# 4. Validation Architecture in WorkSphere

WorkSphere follows a layered architecture where validation is performed at the API boundary before the request reaches the business logic.

```text
                  Client
                     │
                     ▼
        EmployeeRequest / DepartmentRequest
                     │
                     ▼
               @Valid Annotation
                     │
                     ▼
      Jakarta Bean Validation Engine
                     │
         ┌───────────┴───────────┐
         │                       │
         ▼                       ▼
   Validation Passed      Validation Failed
         │                       │
         ▼                       ▼
     Controller          MethodArgumentNotValidException
         │                       │
         ▼                       ▼
      Service          GlobalExceptionHandler
         │                       │
         ▼                       ▼
      Repository     ValidationErrorResponse
         │
         ▼
      Database
```

### Workflow

1. Client sends a request to the REST API.
2. Spring converts the JSON request into a Request Record.
3. The `@Valid` annotation triggers Jakarta Bean Validation.
4. If validation succeeds, the request reaches the Service layer.
5. If validation fails, Spring throws `MethodArgumentNotValidException`.
6. `GlobalExceptionHandler` catches the exception and returns a structured validation response.

This approach ensures that invalid data never reaches the business layer.

---

# 5. Validation Flow

The following sequence illustrates how request validation works in WorkSphere.

```text
Client
   │
   │ POST /api/v1/employees
   ▼
EmployeeController
   │
   │ @Valid EmployeeRequest
   ▼
Bean Validation
   │
   ├───────────────┐
   │               │
Valid          Invalid
   │               │
   ▼               ▼
EmployeeService   MethodArgumentNotValidException
   │               │
   ▼               ▼
Repository    GlobalExceptionHandler
   │               │
   ▼               ▼
Database      ValidationErrorResponse
```

Validation occurs before the controller method executes.

This means:

- Invalid requests never reach the Service layer.
- Invalid data is never saved to the database.
- Business logic remains clean because validation is handled automatically.

---

# 6. EmployeeRequest Validation

The `EmployeeRequest` record represents the data required to create or update an employee.

WorkSphere validates every important field before processing the request.

## EmployeeRequest

```java
public record EmployeeRequest(

    @NotBlank(message = "First name is required")
    String firstName,

    @NotBlank(message = "Last name is required")
    String lastName,

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    String email,

    @Positive(message = "Salary must be greater than zero")
    Double salary,

    @NotNull(message = "Department Id is required")
    Long departmentId

) {}
```

### Validation Rules

| Field | Validation | Description |
|--------|------------|-------------|
| firstName | `@NotBlank` | Employee first name cannot be empty. |
| lastName | `@NotBlank` | Employee last name cannot be empty. |
| email | `@NotBlank` + `@Email` | Email must not be blank and must follow a valid email format. |
| salary | `@Positive` | Salary must be greater than zero. |
| departmentId | `@NotNull` | Every employee must belong to a department. |

These validation rules guarantee that only valid employee information reaches the Service layer.

---

# 7. DepartmentRequest Validation

The `DepartmentRequest` record validates department information before creating or updating a department.

## DepartmentRequest

```java
public record DepartmentRequest(

    @NotBlank(message = "Department name is required")
    String departmentName,

    @NotBlank(message = "Department code is required")
    String departmentCode,

    @NotBlank(message = "Department head is required")
    String departmentHead,

    @NotBlank(message = "Department location is required")
    String location

) {}
```

### Validation Rules

| Field | Validation | Description |
|--------|------------|-------------|
| departmentName | `@NotBlank` | Department name is mandatory. |
| departmentCode | `@NotBlank` | Every department must have a unique code. |
| departmentHead | `@NotBlank` | Department head cannot be empty. |
| location | `@NotBlank` | Department location is required. |

This validation prevents incomplete department information from entering the system.


---

# 8. Controller Validation using `@Valid`

Spring Boot performs validation automatically when the request object is annotated with the `@Valid` annotation.

In WorkSphere, both Employee and Department controllers use `@Valid` to validate incoming requests before they reach the Service layer.

## Employee Controller

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public EmployeeResponse createEmployee(
        @Valid @RequestBody EmployeeRequest request) {

    return employeeService.createEmployee(request);
}
```

## Department Controller

```java
@PostMapping
public DepartmentResponse createDepartment(
        @Valid @RequestBody DepartmentRequest request) {

    return departmentService.createDepartment(request);
}
```

### How `@Valid` Works

1. Spring receives the HTTP request.
2. The JSON payload is converted into a Request Record.
3. Bean Validation validates all annotated fields.
4. If validation succeeds, the controller method executes.
5. If validation fails, Spring throws `MethodArgumentNotValidException`.

This automatic validation eliminates the need for manual validation logic inside the controller or service layer.

---

# 9. Global Exception Handling

WorkSphere uses a centralized `GlobalExceptionHandler` to handle validation failures.

When validation fails, Spring throws a `MethodArgumentNotValidException`.

The Global Exception Handler catches this exception and returns a structured error response.

Example:

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ValidationErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex,
        HttpServletRequest request) {

    List<String> messages = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .toList();

    ValidationErrorResponse response = new ValidationErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            messages,
            request.getRequestURI()
    );

    return ResponseEntity.badRequest().body(response);
}
```

This approach ensures that all validation errors are handled consistently across the application.

---

# 10. Sample API Requests

## Valid Employee Request

```http
POST /api/v1/employees
```

```json
{
  "firstName": "Sakshi",
  "lastName": "Agrawal",
  "email": "sakshi@gmail.com",
  "salary": 65000,
  "departmentId": 1
}
```

Response:

```http
201 Created
```

---

## Invalid Employee Request

```json
{
  "firstName": "",
  "lastName": "",
  "email": "abc",
  "salary": -5000,
  "departmentId": null
}
```

Response:

```http
400 Bad Request
```

---

# 11. Sample Validation Response

WorkSphere returns a structured validation response when validation fails.

Example:

```json
{
  "timestamp": "2026-07-24T18:45:00",
  "status": 400,
  "message": "Validation Failed",
  "errors": [
    "First name is required",
    "Last name is required",
    "Please provide a valid email",
    "Salary must be greater than zero",
    "Department Id is required"
  ],
  "path": "/api/v1/employees"
}
```

This response provides meaningful error messages that help clients identify and correct invalid input.

---

# 12. Best Practices

WorkSphere follows these Bean Validation best practices:

- Validate Request DTOs instead of Entity classes.
- Keep validation rules close to the API contract.
- Use meaningful validation messages.
- Use `@Valid` in controller methods.
- Handle validation failures using a centralized exception handler.
- Return consistent error responses for all validation failures.
- Keep business logic free from validation code.

Following these practices improves code readability, maintainability, and overall application quality.

---

# 13. Common Mistakes

The following are common mistakes developers should avoid when implementing Bean Validation:

- Performing manual validation inside service methods.
- Validating JPA Entity classes instead of Request DTOs.
- Forgetting to use the `@Valid` annotation.
- Returning raw Spring validation errors directly to clients.
- Using generic validation messages that do not help API consumers.
- Mixing validation logic with business logic.

Avoiding these mistakes results in cleaner and more maintainable code.

---

# 14. Interview Questions

### 1. What is Bean Validation?

Bean Validation is a specification that allows developers to validate Java objects using annotations.

---

### 2. Which implementation is used by Spring Boot?

Spring Boot uses **Hibernate Validator**, the reference implementation of Jakarta Bean Validation.

---

### 3. What is the purpose of `@Valid`?

The `@Valid` annotation tells Spring Boot to validate the request object before invoking the controller method.

---

### 4. Which exception is thrown when validation fails?

`MethodArgumentNotValidException`

---

### 5. Why should validation be performed on Request DTOs instead of Entities?

Because Request DTOs represent client input, while Entities represent the database model. Keeping them separate improves maintainability and follows the Single Responsibility Principle.

---

### 6. Where should validation exceptions be handled?

Validation exceptions should be handled in a Global Exception Handler using `@RestControllerAdvice`.

---

# 15. Summary

Bean Validation is an essential part of the WorkSphere project. It ensures that only valid requests reach the business layer by validating incoming data before processing.

WorkSphere implements Bean Validation using:

- Request Records (`EmployeeRequest` and `DepartmentRequest`)
- Jakarta Bean Validation annotations
- `@Valid` in controller methods
- Global Exception Handling
- Custom `ValidationErrorResponse`

This approach provides:

- Cleaner code
- Reduced boilerplate
- Better maintainability
- Consistent error handling
- Improved API reliability

By validating data at the API boundary, WorkSphere follows enterprise-grade Spring Boot development practices and ensures high-quality, reliable REST APIs.