# Task 11 - Custom Exceptions

## What We Built

Created a domain-specific exception:

```java
DuplicateUsernameException
```

Used when a registration request attempts to create a user with an existing username.

---

## Why Not Use RuntimeException Directly?

Bad:

```java
throw new RuntimeException(
        "Username already exists");
```

Problems:

* Generic
* Difficult to identify business failures
* Hard to handle globally
* Poor API design

---

## Why Create Custom Exceptions?

Benefits:

* Self-documenting code
* Better error handling
* Easier debugging
* Easier monitoring
* Supports standardized API responses

Example:

```java
DuplicateUsernameException
UserNotFoundException
ClaimNotFoundException
InvalidClaimStatusException
```

---

## Interview Questions

### Q: Why do custom exceptions improve maintainability?

They communicate business intent clearly and allow different failures to be handled differently.

---

### Q: Why extend RuntimeException?

Spring applications typically use unchecked exceptions for business failures.

Advantages:

* Cleaner method signatures
* Less boilerplate
* Automatic transaction rollback

---

### Q: What is the difference between Checked and Unchecked Exceptions?

Checked:

```java
IOException
SQLException
```

Must be declared or caught.

Unchecked:

```java
RuntimeException
```

No explicit handling required.

Spring applications generally prefer unchecked exceptions for business rules.

---

### Q: Why not return null when username already exists?

Returning null hides the actual failure.

Exceptions explicitly communicate that the operation cannot continue.

---

## Project Usage

Registration flow:

```text
POST /api/v1/auth/register
            │
            ▼
Check Username
            │
            ▼
DuplicateUsernameException
```

This exception will later be converted into a proper HTTP 409 Conflict response.


# Task 12 & 13 - Global Exception Handling

## What We Built

Created:

```java
ApiErrorResponse
```

and

```java
GlobalExceptionHandler
```

using:

```java
@RestControllerAdvice
```

Purpose:

* Centralized exception handling
* Consistent API responses
* Better client experience

---

## Why Not Let Exceptions Bubble Up?

Default Spring responses:

```json
{
  "timestamp":"...",
  "status":500,
  "error":"Internal Server Error"
}
```

Problems:

* Inconsistent
* Not business-friendly
* Hard for frontend teams

---

## What is @RestControllerAdvice?

Global exception handling mechanism.

Applies across all controllers.

Instead of:

```java
try {
}
catch(Exception e) {
}
```

inside every endpoint.

---

## What is @ExceptionHandler?

Maps an exception type to a response.

Example:

```java
@ExceptionHandler(
        DuplicateUsernameException.class)
```

Whenever that exception occurs:

```java
handleDuplicateUsernameException(...)
```

is invoked.

---

## Why Return ResponseEntity?

Allows control over:

* Status code
* Headers
* Response body

Example:

```java
ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(response);
```

---

## Why Use HTTP 409 Conflict?

Meaning:

```text
Request is valid
But conflicts with current resource state
```

Example:

```text
Username already exists
```

409 is more accurate than:

```text
400 Bad Request
500 Internal Server Error
```

---

## Common Exception → HTTP Mapping

```text
Validation Failure        -> 400
Authentication Failure    -> 401
Authorization Failure     -> 403
Resource Not Found        -> 404
Duplicate Resource        -> 409
Unexpected Failure        -> 500
```

---

## Interview Questions

### Q: Why centralize exception handling?

Benefits:

* Consistency
* Reduced duplication
* Easier maintenance
* Cleaner controllers

---

### Q: Difference between @ControllerAdvice and @RestControllerAdvice?

@ControllerAdvice

* Handles exceptions
* Typically used with MVC views

@RestControllerAdvice

* Includes @ResponseBody
* Returns JSON automatically

Ideal for REST APIs.

---

### Q: Why create ApiErrorResponse?

Provides a standard structure for all API failures.

Benefits:

* Predictable client behavior
* Easier frontend integration
* Easier monitoring and debugging

---

## Project Usage

```text
POST /api/v1/auth/register
            │
            ▼
Duplicate Username
            │
            ▼
DuplicateUsernameException
            │
            ▼
GlobalExceptionHandler
            │
            ▼
409 CONFLICT
```
