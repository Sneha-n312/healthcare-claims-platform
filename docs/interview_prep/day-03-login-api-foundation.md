# Task 16 - Login API Foundation

## What We Built

Created:

```java
LoginRequest
LoginResponse
```

and added:

```java
login(...)
```

to AuthService.

Purpose:

* Prepare authentication flow
* Separate login from registration
* Establish API contract before implementing logic

---

## Why Create Separate Login DTOs?

Registration and Login have different responsibilities.

Registration:

```text
Create User
```

Login:

```text
Authenticate User
```

Different use cases require different DTOs.

---

## Why Not Return JWT Immediately?

Authentication should be implemented first.

Flow:

```text
Verify Credentials
        ↓
Generate JWT
```

JWT is a consequence of successful authentication.

---

## Interview Questions

### Q: What is Authentication?

Authentication verifies identity.

Example:

```text
Username + Password
```

Question answered:

```text
Who are you?
```

---

### Q: What is Authorization?

Authorization determines permissions.

Question answered:

```text
What are you allowed to do?
```

Example:

```text
ADMIN
PROVIDER
ADJUSTER
```

---

### Q: Difference Between Authentication and Authorization?

Authentication:

```text
Identity Verification
```

Authorization:

```text
Permission Verification
```

---

### Q: Why Separate Register and Login APIs?

Registration creates a user.

Login verifies a user.

These are distinct business operations.

---

## Project Flow

```text
POST /auth/login
        ↓
Find User
        ↓
Verify Password
        ↓
Return Success
```

JWT will be introduced after successful credential verification.

---

### Q: Why can't we compare BCrypt hashes using equals()?

Because BCrypt uses a random salt.

---