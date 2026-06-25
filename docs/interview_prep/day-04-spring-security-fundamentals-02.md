# Spring Security Fundamentals

## 1. Why Do We Need Spring Security?

Imagine a normal Spring Boot application.

```text
Request
   ↓
Controller
   ↓
Service
   ↓
Repository
```

Every controller would have to manually check:

* Is the user logged in?
* Is the password correct?
* Is the user allowed to access this API?

This would duplicate authentication and authorization logic across every controller.

Spring Security solves this by handling security **before** requests reach the controller.

---

# 2. What is a Filter?

A Filter is a component that intercepts an HTTP request before it reaches the controller.

A filter can:

* Read the request
* Modify the request
* Reject the request
* Continue the request

Example:

```text
Request
   ↓

Logging Filter
   ↓

Authentication Filter
   ↓

Controller
```

Instead of every controller checking authentication, the filter performs it once.

---

# 3. Is Spring Security a Filter?

No.

Spring Security is **a framework built using a chain of filters**.

Think of it like:

```text
Spring MVC
    ↓
Controllers

Spring Data JPA
    ↓
Repositories

Spring Security
    ↓
Filters
```

---

# 4. How Does Spring Security Work?

When a request arrives:

```text
Request
   ↓

Spring Security Filter Chain
   ↓

Controller
```

Every request passes through multiple filters before reaching the controller.

---

# 5. Who Creates These Filters?

We never manually create filters like:

* AuthorizationFilter
* AnonymousAuthenticationFilter
* ExceptionTranslationFilter

Spring creates them automatically.

Example:

```java
http
    .authorizeHttpRequests(...)
```

When Spring executes:

```java
http.build()
```

it internally creates a complete SecurityFilterChain.

---

# 6. What Does HttpSecurity Actually Do?

HttpSecurity is a builder.

Example:

```java
http
    .csrf(...)
    .authorizeHttpRequests(...)
    .sessionManagement(...)
```

We're not creating filters.

We're configuring Spring Security.

Finally,

```java
http.build()
```

constructs the filter chain.

---

# 7. Authentication vs Authorization

Authentication answers:

```text
Who are you?
```

Example:

* Username
* Password
* JWT

Authorization answers:

```text
What are you allowed to do?
```

Example:

* ADMIN
* PROVIDER
* ADJUSTER

Authentication happens before Authorization.

---

# 8. What Happens During Login?

```text
Username + Password
        ↓

AuthService
        ↓

Password Verified
        ↓

JWT Generated
        ↓

JWT Returned
```

The user is now authenticated.

---

# 9. Why Do We Need a JWT Filter?

Future requests don't contain the username/password anymore.

Instead:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

Spring must determine:

```text
Who sent this request?
```

The JwtAuthenticationFilter performs this task.

---

# 10. JwtAuthenticationFilter Flow

```text
Incoming Request
        ↓

Read Authorization Header
        ↓

Extract JWT
        ↓

Extract Username
        ↓

Load User
        ↓

Validate JWT
        ↓

Create Authentication Object
        ↓

Store Authentication
        ↓

Continue Request
```

---

# 11. Why Extend OncePerRequestFilter?

```java
public class JwtAuthenticationFilter
        extends OncePerRequestFilter
```

Guarantees the filter executes exactly once for every HTTP request.

Without it, filters may execute multiple times during forwards or error dispatches.

---

# 12. What is SecurityContextHolder?

This is the most important Spring Security concept.

Think of it as:

```text
Current Logged-in User
```

Spring stores the authenticated user here.

Example:

```java
SecurityContextHolder
    .getContext()
    .getAuthentication();
```

returns the current authenticated user.

It behaves like request-scoped storage.

---

# 13. What is UsernamePasswordAuthenticationToken?

Spring Security represents authenticated users using:

```java
UsernamePasswordAuthenticationToken
```

It contains:

* Principal (UserDetails)
* Credentials
* Authorities (Roles)

Once this object is placed inside SecurityContextHolder, Spring considers the request authenticated.

---

# 14. Why Register JwtAuthenticationFilter?

Creating a filter bean is not enough.

Spring must know where to place it.

We register it using:

```java
.addFilterBefore(
    jwtAuthenticationFilter,
    UsernamePasswordAuthenticationFilter.class)
```

This inserts our filter into Spring Security's filter chain.

---

# 15. Why Before UsernamePasswordAuthenticationFilter?

Spring Security originally authenticates users using username/password forms.

Our application authenticates users using JWT.

JWT authentication must happen before Spring performs authorization.

---

# 16. Stateless Authentication

Traditional applications use:

```text
HTTP Session
```

JWT applications use:

```text
SessionCreationPolicy.STATELESS
```

The server stores no authentication state.

Every request carries its own JWT.

---

# 17. Where Does Spring Security Get Filters From?

During startup:

```text
Application Starts
        ↓

SecurityConfig
        ↓

HttpSecurity Builder
        ↓

http.build()
        ↓

SecurityFilterChain Created
        ↓

Filters Registered
```

---

# 18. Does Spring Security Depend on Tomcat?

No.

Spring Security depends on the **Jakarta Servlet API**, not Tomcat.

It works with any servlet container implementing the Servlet specification.

Examples:

* Tomcat
* Jetty
* Undertow

This is why we import:

```java
jakarta.servlet.Filter
jakarta.servlet.FilterChain
jakarta.servlet.http.HttpServletRequest
```

instead of Tomcat-specific classes.

---

# 19. What Happens in WebFlux?

Spring MVC uses:

```java
SecurityFilterChain
OncePerRequestFilter
HttpServletRequest
```

Spring WebFlux uses:

```java
SecurityWebFilterChain
WebFilter
ServerHttpRequest
```

Same concepts.

Different APIs.

---

# 20. Typical Spring Security Request Flow

```text
Client
   │
   ▼

Authorization: Bearer <JWT>

   │
   ▼

JwtAuthenticationFilter

   │
   ▼

Extract Username

   │
   ▼

Load UserDetails

   │
   ▼

Validate JWT

   │
   ▼

Create Authentication

   │
   ▼

SecurityContextHolder

   │
   ▼

Authorization Filter

   │
   ▼

Controller

   │
   ▼

Service

   │
   ▼

Repository
```

---

# Interview Questions

### What is a Filter?

A servlet component that intercepts requests before they reach the controller.

---

### Why does Spring Security use Filters?

Authentication and authorization must occur before business logic executes.

---

### Is Spring Security itself a Filter?

No.

It is a framework that builds and manages a chain of servlet filters.

---

### What is SecurityContextHolder?

A thread-local store that holds the currently authenticated user for the lifetime of the request.

---

### What is OncePerRequestFilter?

A Spring-provided base class ensuring a filter executes exactly once per request.

---

### Why use addFilterBefore()?

To insert a custom filter at a specific point in Spring Security's filter chain.

---

### Why disable sessions when using JWT?

JWT provides stateless authentication. Every request carries its own authentication information.

---

### Does Spring Security depend on Tomcat?

No.

It depends on the Jakarta Servlet API, making it portable across Tomcat, Jetty, Undertow, and any servlet-compliant container.

---

### Authentication vs Authorization

Authentication:

```text
Who are you?
```

Authorization:

```text
What are you allowed to do?
```

Authentication always precedes authorization.
