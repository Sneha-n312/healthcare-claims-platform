# Spring Security Handbook

## Healthcare Claims Platform

---

# 1. Why Do We Need Spring Security?

Imagine a Spring Boot application without Spring Security.

```text
Request
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

Every controller would have to perform authentication manually.

Example:

```java
if(!loggedIn){
    throw new UnauthorizedException();
}
```

Every controller would also need to verify:

* Is the user logged in?
* Is the JWT valid?
* Does the user have permission?
* Is the account active?

This results in duplicated code across the application.

Spring Security moves all authentication and authorization logic **before** the request reaches the controller.

---

# 2. What is a Filter?

A Filter is a Servlet component that intercepts every HTTP request before it reaches the controller.

A filter can:

* Read the request
* Modify the request
* Reject the request
* Continue processing

Example:

```text
Request
   │
   ▼

Logging Filter
   │
   ▼

Authentication Filter
   │
   ▼

Controller
```

Instead of putting authentication logic inside every controller, one filter performs it once.

---

# 3. Is Spring Security a Filter?

No.

Spring Security is a framework built using a **chain of servlet filters**.

Think of it like:

```text
Spring MVC
        │
Controllers

Spring Data JPA
        │
Repositories

Spring Security
        │
Filters
```

---

# 4. Servlet Filters

Spring Security does **not** invent Filters.

It uses the Servlet API provided by Jakarta EE.

Examples:

```java
jakarta.servlet.Filter
jakarta.servlet.FilterChain
jakarta.servlet.http.HttpServletRequest
jakarta.servlet.http.HttpServletResponse
```

These are interfaces.

Tomcat, Jetty and Undertow provide their implementations.

Therefore Spring Security does **not** depend on Tomcat.

It depends only on the Servlet specification.

---

# 5. Request Lifecycle

Without Security

```text
Client
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

With Spring Security

```text
Client
    │
    ▼

Security Filter Chain
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

Every request passes through the filter chain before reaching business logic.

---

# 6. SecurityFilterChain

We created:

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http)
```

Many developers think this method creates filters.

It does not.

Instead it configures a builder.

Example:

```java
http
    .csrf(...)
    .authorizeHttpRequests(...)
    .sessionManagement(...)
```

Finally,

```java
http.build()
```

creates the SecurityFilterChain internally.

---

# 7. Spring Security Filter Chain

Spring automatically creates several filters.

Examples:

* SecurityContextHolderFilter
* AnonymousAuthenticationFilter
* AuthorizationFilter
* ExceptionTranslationFilter
* UsernamePasswordAuthenticationFilter

We inserted our own filter using:

```java
.addFilterBefore(
    jwtAuthenticationFilter,
    UsernamePasswordAuthenticationFilter.class
)
```

Spring then inserts our filter into the correct position.

---

# 8. Authentication vs Authorization

Authentication answers:

> Who are you?

Examples:

* Username + Password
* JWT
* OAuth

Authorization answers:

> What are you allowed to do?

Examples:

* ADMIN
* PROVIDER
* ADJUSTER

Authentication always happens before Authorization.

---

# 9. Login Flow

```text
Username + Password
        │
        ▼

UserRepository

        │
        ▼

BCrypt Password Match

        │
        ▼

Generate JWT

        │
        ▼

Return Access Token
```

At this point the user is authenticated.

---

# 10. Why JWT?

Without JWT

Server stores:

```text
HTTP Session
```

With JWT

Client stores:

```text
Access Token
```

Every request carries authentication information.

This is called Stateless Authentication.

---

# 11. JWT Structure

JWT consists of three parts.

```text
Header
.
Payload
.
Signature
```

Example

```text
xxxxx.yyyyy.zzzzz
```

---

Payload Example

```json
{
    "sub":"provider1",
    "role":"PROVIDER",
    "iat":1782388001,
    "exp":1782391601
}
```

Important claims

sub

Current user

role

Application role

iat

Issued At

exp

Expiration Time

---

# 12. JwtService Responsibilities

JwtService is responsible only for:

* Generating JWT
* Parsing JWT
* Validating JWT

It should never build HTTP responses.

That responsibility belongs elsewhere.

---

# 13. JwtAuthenticationFilter

Purpose:

Authenticate every incoming request.

Flow:

```text
Incoming Request
        │
        ▼

Read Authorization Header

        │
        ▼

Extract JWT

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

Create Authentication Object

        │
        ▼

SecurityContextHolder

        │
        ▼

Continue Filter Chain
```

Notice:

The filter never calls the controller.

It only authenticates the request.

---

# 14. Why OncePerRequestFilter?

Our filter extends

```java
OncePerRequestFilter
```

Guarantees execution exactly once per HTTP request.

Without it a filter may execute multiple times during forwards or error dispatches.

---

# 15. SecurityContextHolder

This is one of the most important Spring Security classes.

Think of it as:

```text
Current Logged-in User
```

After successful authentication we execute:

```java
SecurityContextHolder
        .getContext()
        .setAuthentication(authentication);
```

Every controller and service can now access the authenticated user.

---

# 16. UsernamePasswordAuthenticationToken

Spring Security represents authenticated users using:

```java
UsernamePasswordAuthenticationToken
```

Contains:

* UserDetails
* Credentials
* Authorities (Roles)

Once placed inside SecurityContextHolder, Spring considers the request authenticated.

---

# 17. UserDetailsService

Spring Security does not understand our User entity.

It understands:

```java
UserDetails
```

CustomUserDetailsService converts:

```text
Database User
```

into

```text
UserDetails
```

---

# 18. AuthenticationEntryPoint

Question:

Who returns the HTTP 401 response?

Not JwtService.

Not JwtAuthenticationFilter.

Spring Security delegates authentication failures to:

```java
AuthenticationEntryPoint
```

This allows all authentication failures to return a consistent JSON response.

---

# 19. Why @RestControllerAdvice Doesn't Handle Security

Request Flow

```text
Request
        │
        ▼

Security Filter Chain

        │
        ▼

Controller
```

Authentication failures occur before the controller.

Therefore

```java
@RestControllerAdvice
```

never executes.

Spring Security provides

```java
AuthenticationEntryPoint
```

for this purpose.

---

# 20. Stateless Authentication

Configuration

```java
.sessionManagement(session ->
    session.sessionCreationPolicy(
        SessionCreationPolicy.STATELESS))
```

No HTTP session.

Every request contains its own JWT.

---

# 21. Method Security

Enabled using

```java
@EnableMethodSecurity
```

Allows annotations:

```java
@PreAuthorize
@PostAuthorize
@Secured
```

Example

```java
@PreAuthorize("hasRole('ADMIN')")
```

---

# 22. hasRole vs hasAuthority

We used

```java
.roles("ADMIN")
```

Spring automatically creates

```text
ROLE_ADMIN
```

Therefore

```java
hasRole("ADMIN")
```

works.

If we instead use

```java
.authorities(...)
```

then we must use

```java
hasAuthority(...)
```

---

# 23. Authorization Flow

```text
Client
      │
      ▼

JWT Filter

      │
      ▼

Authenticated User

      │
      ▼

Authorization Filter

      │
      ▼

@PreAuthorize

      │
      ▼

Controller
```

---

# 24. Production Decisions We Made

### Constructor Injection

Used constructor injection with final fields.

Avoids null dependencies.

---

### JWT Configuration

Used

```java
@ConfigurationProperties
```

instead of multiple @Value annotations.

Cleaner and scalable.

---

### Roles Loaded From Database

Even though the JWT stores the role, we load the latest user from the database.

Advantages:

* Role changes take effect immediately.
* Disabled users lose access immediately.

Trade-off:

One database lookup per request.

---

### Stateless Authentication

No server-side sessions.

Microservice friendly.

Horizontally scalable.

---

# 25. Common Bugs We Encountered

## Bug 1

Missing

```java
return;
```

after

```java
filterChain.doFilter(...)
```

Result:

NullPointerException.

Lesson:

Calling doFilter() does **not** stop execution.

---

## Bug 2

JwtService was null.

Cause:

Dependency injection issue.

Lesson:

Always use constructor injection.

---

## Bug 3

JWT expired immediately.

Root cause:

Configuration property

```java
expiration
```

was not bound.

Result:

```text
iat == exp
```

Lesson:

Verify ConfigurationProperties binding.

---

## Bug 4

JWT configuration inside

```yaml
spring:
```

instead of

```yaml
jwt:
```

Lesson:

Configuration prefix must match

```java
@ConfigurationProperties(prefix="jwt")
```

---

## Bug 5

Swagger requests succeeded without manually adding JWT.

Reason:

Swagger Authorize button automatically attaches the stored JWT.

Lesson:

Always verify security using Postman or curl when debugging authentication.

---

# 26. Interview Questions

### What is a Filter?

A Servlet component that intercepts HTTP requests before controllers.

---

### Why does Spring Security use Filters?

Authentication and authorization must happen before business logic executes.

---

### What is SecurityFilterChain?

A chain of security filters executed for every request.

---

### What is OncePerRequestFilter?

Guarantees a filter executes exactly once per HTTP request.

---

### What is SecurityContextHolder?

Stores the authenticated user for the current request.

---

### Authentication vs Authorization?

Authentication

Who are you?

Authorization

What are you allowed to do?

---

### Why use JWT?

Stateless authentication.

No server-side session.

Suitable for distributed systems.

---

### Why AuthenticationEntryPoint?

Authentication failures happen before controllers.

@RestControllerAdvice cannot handle them.

---

### hasRole vs hasAuthority?

hasRole automatically prefixes ROLE_.

hasAuthority checks the exact authority string.

---

### Why use @ConfigurationProperties?

Groups related configuration into a strongly typed class.

Cleaner than multiple @Value annotations.

---

### Does Spring Security depend on Tomcat?

No.

It depends on the Jakarta Servlet API.

Therefore it works with Tomcat, Jetty, Undertow and any Servlet-compliant container.
---

### If I were interviewing you, I might ask:

### Q Does filterChain.doFilter() terminate the execution of the current filter?

The correct answer is:

No. filterChain.doFilter() invokes the next filter in the chain. Once the downstream filters and ultimately the controller finish processing the request, execution returns to the current filter. That's why we often use return immediately after doFilter() in early-exit scenarios to prevent the remaining code in the current filter from executing.

---

# Key Takeaways

By implementing Spring Security from scratch, we learned:

* How the Servlet filter chain works.
* How Spring Security authenticates requests.
* How JWT-based stateless authentication is implemented.
* How authorization is enforced using roles.
* How Spring stores the authenticated user.
* How to debug security-related issues in production.
* Why Spring Security is designed around separation of responsibilities instead of placing all logic inside one filter.
