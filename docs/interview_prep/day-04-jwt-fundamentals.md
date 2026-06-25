# Task 18-20 - JWT Fundamentals

## What We Built

Added JWT support using JJWT.

Created:

- JwtService
- Secret Key Configuration
- Token Generation

---

## What is JWT?

JSON Web Token.

A compact signed token used to represent authenticated users.

Example:

```text
eyJhbGciOiJIUzI1NiJ9...
```

---

## JWT Structure

Header

```json
{
  "alg": "HS256"
}
```

Payload

```json
{
  "sub": "provider1",
  "role": "PROVIDER"
}
```

Signature

```text
HMACSHA256(...)
```

---

## Why Use JWT?

Benefits:

- Stateless Authentication
- No server-side session storage
- Works well with microservices
- Scales horizontally

---

## What Claims Are We Storing?

```text
Username
Role
Issued At
Expiration
```

---

## Why Store Role In JWT?

Avoids database lookup on every request.

Authorization decisions can be made directly from the token.

---

## Interview Questions

### Q: Difference between Session Authentication and JWT?

Session:

```text
Server stores state
```

JWT:

```text
Client stores token
Server remains stateless
```

---

### Q: What is the "sub" claim?

Subject.

Typically represents the authenticated user.

Example:

```text
provider1
```

---

### Q: Why Sign JWT?

To prevent tampering.

If payload changes:

```text
Signature becomes invalid
```

---

### Q: Why Not Store Password In JWT?

JWT payload is Base64 encoded.

Anyone can decode it.

Sensitive data should never be stored inside JWT.

---

### Q: What Happens When Token Expires?

Authentication fails.

Client must login again or refresh token.

---
### Q:Why do we put:

.claim("role", user.getRole().name())

inside JWT?

Answer:

Allows authorization without a database lookup.

The API can determine whether the user is
ADMIN / PROVIDER / ADJUSTER directly from
the token.

# Task 22-24 - JWT Authentication Foundation

## What We Built

- Login now returns JWT
- JwtService can parse JWT
- Added CustomUserDetailsService

---

## Why Do We Need CustomUserDetailsService?

Spring Security works with:

```java
UserDetails
```

Our database entity:

```java
User
```

must be converted into:

```java
UserDetails
```

for Spring Security.

---

## What Does loadUserByUsername() Do?

Responsible for loading a user during authentication.

Flow:

```text
JWT
   ↓
Username
   ↓
loadUserByUsername()
   ↓
UserDetails
```

---

## What Is UserDetails?

Spring Security abstraction representing an authenticated user.

Contains:

- Username
- Password
- Authorities/Roles

---

## Why Parse JWT?

To identify the user making the request.

Example:

```json
{
  "sub":"provider1",
  "role":"PROVIDER"
}
```

The `sub` claim becomes the authenticated username.

---

## Interview Questions

### Q: Why not query database using JWT directly?

JWT contains identity information.

We first extract username, then load the user.

---

### Q: What is the purpose of UserDetailsService?

Acts as a bridge between application users and Spring Security.

---

### Q: What is the "sub" claim?

JWT Subject.

Typically stores username or user identifier.

---

### Q: What happens if a JWT is tampered with?

Signature validation fails.

Token is rejected.

Q: What is a Filter?

A component that intercepts requests before they reach controllers.

---

Q: Why does Spring Security use Filters?

Authentication and authorization must happen before business logic executes.

---

Q: What is SecurityContextHolder?

A thread-local store containing the currently authenticated user.

---

Q: What is OncePerRequestFilter?

A Spring filter guaranteed to execute once per request.

---

Q: What is the role of JwtAuthenticationFilter?

Extract JWT, validate it, authenticate user, and populate SecurityContextHolder.

---

Q: Authentication vs Authorization?

Authentication = Who are you?

Authorization = What are you allowed to do?

Q: Why use a filter instead of an interceptor?

Short answer:

Filters operate at the servlet container level.

Interceptors operate at the Spring MVC level.

Security is fundamental enough that it should happen before MVC processing, so Spring Security uses filters.