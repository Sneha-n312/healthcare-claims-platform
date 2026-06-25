# Task 6-9 - AuthServiceImpl, Persistence and Controller

## What We Built

Implemented the registration flow:

1. Validate username uniqueness
2. Hash password using BCrypt
3. Create User entity
4. Save user to database
5. Return RegisterResponse
6. Expose POST /api/v1/auth/register

---

## Interview Questions

### Q: Why create a Service Layer?

Responsibilities:

* Business logic
* Validation
* Transactions
* Coordination between repositories

Controllers should remain thin.

---

### Q: Why use Constructor Injection?

Benefits:

* Immutable dependencies
* Easier testing
* Explicit dependencies
* Recommended by Spring

Example:

```java id="5hk9vx"
@RequiredArgsConstructor
public class AuthServiceImpl {
    private final UserRepository userRepository;
}
```

---

### Q: Why is Constructor Injection preferred over Field Injection?

Field Injection:

```java id="7j6hrd"
@Autowired
private UserRepository repository;
```

Problems:

* Harder testing
* Mutable dependencies
* Hidden dependencies

Constructor Injection solves these issues.

---

### Q: What does @Service do?

Marks a class as a Spring-managed service bean.

Allows Spring to discover and inject it automatically.

---

### Q: Why should business logic not be placed in Controllers?

Controllers should:

* Receive requests
* Validate input
* Delegate work
* Return responses

Business rules belong in the service layer.

---

### Q: What does save() do?

JpaRepository.save() can perform:

* INSERT
* UPDATE

depending on entity state.

Internally delegates to JPA EntityManager.

---

### Q: What determines INSERT vs UPDATE?

New Entity:

```java id="mvn0sj"
new User()
```

results in INSERT.

Existing managed entity:

```java id="6y6uoa"
existingUser.setUsername(...)
```

results in UPDATE.

---

### Q: Why validate duplicate usernames before saving?

Business requirement:

```text id="8hnj7j"
Username must be unique
```

Validation prevents database constraint violations and provides a better user experience.

---

### Q: Why return RegisterResponse instead of User?

Prevents exposing:

* Password hash
* Internal fields
* Audit information

Provides a stable API contract.

---

### Q: What does @RestController do?

Combination of:

```java id="7jjktw"
@Controller
@ResponseBody
```

Returns JSON directly from controller methods.

---

### Q: What does @RequestBody do?

Maps incoming JSON into Java objects.

Example:

```json id="oowj5s"
{
  "username": "provider1"
}
```

becomes:

```java id="lkh7e2"
RegisterRequest
```

---

### Q: What does @Valid do?

Triggers Jakarta Bean Validation.

Annotations such as:

```java id="h15qit"
@NotBlank
@NotNull
```

are automatically enforced before entering service logic.

---

## Project Flow

```text id="e9owso"
POST /api/v1/auth/register
            │
            ▼
      AuthController
            │
            ▼
       AuthService
            │
            ▼
      UserRepository
            │
            ▼
        PostgreSQL
```
