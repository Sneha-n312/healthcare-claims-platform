# Task 25 - JwtAuthenticationFilter

## What We Built

Created a custom JWT authentication filter using:

```java
OncePerRequestFilter
```

Purpose:

* Read JWT from Authorization header
* Validate token
* Authenticate user
* Populate SecurityContextHolder

---

## Request Flow

```text
Request
   ↓
JwtAuthenticationFilter
   ↓
Extract JWT
   ↓
Extract Username
   ↓
Load User
   ↓
Validate JWT
   ↓
SecurityContextHolder
   ↓
Controller
```

---

## Why Extend OncePerRequestFilter?

Guarantees execution exactly once per HTTP request.

Prevents duplicate authentication logic.

---

## What Is SecurityContextHolder?

Stores the authenticated user for the current request.

Accessible throughout:

* Controllers
* Services
* Security Rules

---

## What Is UsernamePasswordAuthenticationToken?

Spring Security's implementation of an authenticated user.

Contains:

* Principal (UserDetails)
* Credentials
* Authorities/Roles

---

## Why Use Authorization Header?

Standard HTTP mechanism:

```http
Authorization: Bearer <jwt>
```

---

## Interview Questions

### Q: What is the responsibility of JwtAuthenticationFilter?

Authenticate incoming requests using JWT.

---

### Q: Why populate SecurityContextHolder?

Allows Spring Security and application code to know who the current user is.

---

### Q: What happens if SecurityContextHolder is not populated?

Request remains anonymous.

Authorization checks will fail.

---

### Q: Why use OncePerRequestFilter?

Ensures authentication logic executes once per request.

---

### Q: What does filterChain.doFilter() do?

Passes control to the next filter in the chain.


# Task 26 - Register JWT Filter

## What We Built

Registered JwtAuthenticationFilter in Spring Security.

```java
.addFilterBefore(
        jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class)
```

---

## Why Register The Filter?

Creating a filter bean is not enough.

Spring Security must be told where it belongs in the filter chain.

---

## Why Use addFilterBefore()?

JWT authentication must occur before authorization decisions.

The user must be authenticated before Spring checks permissions.

---

## Why Stateless Sessions?

JWT stores authentication state inside the token.

No server-side session storage is required.

```java
SessionCreationPolicy.STATELESS
```

---

## What Happens Now?

Every request passes through:

```text
JwtAuthenticationFilter
        ↓
Authentication
        ↓
Authorization
        ↓
Controller
```

---

## Interview Questions

### Q: Why disable sessions when using JWT?

JWT provides stateless authentication.

Server-side sessions become unnecessary.

---

### Q: What does addFilterBefore() do?

Inserts a custom filter before another filter in the Spring Security filter chain.

---

### Q: Why place JWT filter before UsernamePasswordAuthenticationFilter?

JWT authentication must be completed before Spring Security performs authorization.

---

### Q: Is creating a filter bean enough?

No.

The filter must be registered in SecurityFilterChain.

###Q: Does Spring Security depend on Tomcat?

Answer:

No.

Spring Security depends on the Jakarta Servlet API.

Any servlet container that implements the Servlet specification (such as Tomcat, Jetty, or Undertow) can host a Spring MVC application using Spring Security.

If an interviewer asks:

###Q: Why do we return after filterChain.doFilter()?

A good answer is:

doFilter() forwards the request to the next filter in the chain, but it does not terminate execution of the current filter. Without return, the remaining code in the current filter would continue executing after the downstream filters complete, which can lead to exceptions or duplicate processing.

# Task 30 - Role-Based Authorization (RBAC)

## What We Built

- Enabled method security.
- Protected endpoints using `@PreAuthorize`.
- Enforced role-based access.

---

## Authentication vs Authorization

Authentication:

Who are you?

Authorization:

What are you allowed to do?

---

## Why EnableMethodSecurity?

Allows Spring Security annotations like:

- `@PreAuthorize`
- `@PostAuthorize`
- `@Secured`

---

## Why Does hasRole("ADMIN") Work?

`UserDetailsService` uses:

```java
.roles("ADMIN")
```

Spring automatically prefixes it as:

```
ROLE_ADMIN
```

Internally.

---

## hasRole vs hasAuthority

`hasRole("ADMIN")`

↓

Checks:

```
ROLE_ADMIN
```

---

`hasAuthority("ROLE_ADMIN")`

↓

Checks exactly that authority.

---

## Why Load Roles From DB Instead of JWT?

Pros:

- Role changes take effect immediately.
- Disabled users lose access immediately.
- More secure.

Cons:

- One database lookup per request.

---

## Interview Questions

### Q: Difference between hasRole and hasAuthority?

`hasRole("ADMIN")` automatically adds the `ROLE_` prefix.

`hasAuthority()` checks the exact authority string.

---

### Q: Why enable method security?

To apply authorization rules directly on methods using annotations.

---

### Q: Why return 403 instead of 401?

401 means authentication failed.

403 means authentication succeeded, but the user lacks permission.