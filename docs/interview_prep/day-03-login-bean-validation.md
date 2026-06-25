# Task 14 - Bean Validation

## What We Built

Added validation annotations to RegisterRequest.

Example:

```java id="x3f7m2"
@NotBlank
String username;

@NotBlank
String password;

@NotNull
Role role;
```

Purpose:

* Reject invalid requests early
* Prevent bad data reaching service layer
* Improve API quality

---

## What Does @Valid Do?

```java id="j5k8n1"
@Valid @RequestBody RegisterRequest request
```

Triggers Jakarta Bean Validation before entering controller logic.

If validation fails:

```text id="f4n7w2"
MethodArgumentNotValidException
```

is thrown automatically.

---

## Why Validate at API Boundary?

Benefits:

* Fail fast
* Cleaner service layer
* Consistent validation
* Better API contracts

---

## @NotNull vs @NotBlank

### @NotNull

Rejects:

```text id="v2d6r9"
null
```

Allows:

```text id="s8q4k3"
""
"   "
```

---

### @NotBlank

Rejects:

```text id="m7h2p8"
null
""
"   "
```

Best choice for text fields.

---

## Common Validation Annotations

### String

```java id="r5n8t2"
@NotBlank
@Size
@Pattern
@Email
```

---

### Numbers

```java id="w1p4m7"
@Min
@Max
@Positive
@PositiveOrZero
```

---

### Collections

```java id="t9k3q6"
@NotEmpty
@Size
```

---

## Interview Questions

### Q: Why use Bean Validation?

Provides declarative validation close to the model and reduces manual validation code.

---

### Q: What happens when validation fails?

Spring throws:

```java id="c4v7n1"
MethodArgumentNotValidException
```

before the controller method executes.

---

### Q: Why should validation not be done in the service layer?

Validation belongs at the application boundary.

The service layer should assume incoming requests are already valid.

---

### Q: What is Jakarta Bean Validation?

A standard validation specification used by Spring Boot.

Hibernate Validator is the most common implementation.

---

## Project Flow

```text id="k8m2r5"
Request
   ↓
@Valid
   ↓
Validation
   ↓
Controller
   ↓
Service
```

Only valid requests reach business logic.

# Task 15 - Validation Error Handling

## What We Built

Created a centralized validation error response using:

```java
MethodArgumentNotValidException
```

and returned a structured response.

---

## Why Not Use Spring's Default Validation Response?

Problems:

- Inconsistent between versions
- Difficult for frontend teams
- Contains unnecessary information

Custom responses provide a stable API contract.

---

## What is MethodArgumentNotValidException?

Thrown by Spring when:

```java
@Valid
```

fails during request validation.

Occurs before controller business logic executes.

---

## Why Return Field-Level Errors?

Example:

```json
{
  "errors": {
    "username": "Username is required"
  }
}
```

Benefits:

- Better user experience
- Frontend can highlight specific fields
- Easier debugging

---

## Interview Questions

### Q: When is MethodArgumentNotValidException thrown?

When validation annotations fail on an object annotated with:

```java
@Valid
```

---

### Q: Why centralize validation error handling?

Benefits:

- Consistent responses
- Cleaner controllers
- Easier maintenance

---

### Q: Why return field names in validation responses?

Allows clients to map errors directly to form fields.

---

### Q: What is BindingResult?

Spring object that contains validation errors generated during request validation.

Used to inspect:

- Field errors
- Global errors
- Validation messages

---

## Project Usage

```text
Request
    ↓
@Valid
    ↓
Validation Failure
    ↓
MethodArgumentNotValidException
    ↓
GlobalExceptionHandler
    ↓
400 Bad Request
```