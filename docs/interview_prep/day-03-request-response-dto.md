# Task 3 & 4 - Request/Response DTOs

## What We Built

```java
RegisterRequest
RegisterResponse
```

Purpose:

* Define API contracts
* Separate API models from database entities

---

## Interview Questions

### Q: What is a DTO?

DTO stands for Data Transfer Object.

Purpose:

* Transfer data between layers
* Define API contracts
* Prevent exposing internal entities

---

### Q: Why not expose JPA entities directly?

Entities represent database structure.

Returning entities directly can expose:

* Passwords
* Internal fields
* Audit information
* Future implementation details

DTOs provide a stable API contract.

---

### Q: Why separate Request and Response DTOs?

Request DTO:

* Represents incoming data

Response DTO:

* Represents outgoing data

This separation allows API evolution without affecting clients.

---

### Q: Why use Java Records for DTOs?

Benefits:

* Immutable
* Less boilerplate
* Thread-safe
* Auto-generated constructor
* Auto-generated equals/hashCode

Example:

```java
public record RegisterRequest(
        String username,
        String password,
        Role role
) {}
```

---

### Q: Why use Validation Annotations?

Example:

```java
@NotBlank
@NotNull
```

Benefits:

* Fail fast
* Reduce service-layer validation code
* Produce cleaner APIs

---

## Project Usage

RegisterRequest:

```json
{
  "username": "provider1",
  "password": "password123",
  "role": "PROVIDER"
}
```

RegisterResponse:

```json
{
  "id": "...",
  "username": "provider1",
  "role": "PROVIDER"
}
```

---

# Task 5 - Service Interface

## What We Built

```java
public interface AuthService {

    RegisterResponse register(RegisterRequest request);

}
```

Purpose:

* Define business contract
* Separate implementation from consumers

---

## Interview Questions

### Q: Why create a Service Layer?

Responsibilities:

* Business logic
* Validation
* Transactions
* Orchestration

Controllers should remain thin.

---

### Q: Why use an Interface?

Benefits:

* Loose coupling
* Easier testing
* Multiple implementations possible
* Follows Dependency Inversion Principle

---

### Q: What should NOT be inside a Controller?

Avoid:

* Business logic
* Repository calls
* Transaction management

Controller responsibilities:

* Receive request
* Validate input
* Call service
* Return response

---

### Q: What is Dependency Inversion Principle?

High-level modules should depend on abstractions rather than concrete implementations.

Example:

```java
AuthService
```

instead of:

```java
AuthServiceImpl
```

---

## Project Usage

Current flow:

```text
Controller
    ↓
AuthService
    ↓
Repository
    ↓
Database
```

The service layer will become the central place for registration and login business logic.
