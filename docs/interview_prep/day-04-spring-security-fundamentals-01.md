Part 1 - What is a Filter?

Think about a normal request.

Browser/Postman
        │
        ▼
Controller
        ▼
Service
        ▼
Repository

But what if every request needs some common processing?

Examples:

Logging
Authentication
Authorization
Rate Limiting
Tracing

You don't want this in every controller.

So Java Web Applications introduced:

Filter

A filter sits before the controller.

Visual
Request
   │
   ▼

[Filter 1]
   │
   ▼

[Filter 2]
   │
   ▼

[Filter 3]
   │
   ▼

Controller

Each filter can:

Inspect Request
Modify Request
Reject Request
Continue Request
Example

Suppose:

GET /claims

A logging filter:

System.out.println(request.getURI());

prints:

/claims

before controller executes.

Part 2 - What is Spring Security Doing?

Spring Security is basically:

A very sophisticated chain of filters

That's all.

Most people think:

Spring Security = annotations

No.

Internally:

Spring Security = Filters
Request Flow
Request
   │
   ▼

Security Filter Chain
   │
   ▼

Controller
What Happens Today?

Suppose:

GET /claims

with no JWT.

Spring Security receives request first.

Not controller.

Spring checks:

Is user authenticated?

Answer:

No

Spring returns:

401 Unauthorized

Controller never executes.

SecurityFilterChain

This class:

@Bean
public SecurityFilterChain securityFilterChain(...)

creates the filter chain.

Current config:

http
    .csrf(...)
    .authorizeHttpRequests(...)

is actually building filters.

What is Authentication?

Question:

Who are you?

Example:

Sneha

verified by:

Username + Password

or

JWT
What is Authorization?

Question:

What are you allowed to do?

Example:

ADMIN

can:

Approve Claims

while:

PROVIDER

cannot.

Authentication vs Authorization

Interviewers LOVE this.

Authentication:

Identity

Authorization:

Permission
Part 3 - What Happens During Login?

Current flow:

POST /auth/login

We do:

userRepository.findByUsername(...)

Then:

passwordEncoder.matches(...)

Then:

jwtService.generateToken(...)

Response:

{
  "accessToken":"eyJ..."
}

At this point:

User is authenticated
Part 4 - Then Why Do We Need JWT Filter?

Because every future request must prove identity.

Example:

POST /claims

with:

Authorization: Bearer eyJ...

How does Spring know:

This is provider1

?

Answer:

JwtAuthenticationFilter


Part 5: SecurityContext

This is THE most important Spring Security concept.

Think:

Current Logged-in User

needs to be available anywhere.

Example:

ClaimController

needs:

Who submitted this claim?

Spring stores authenticated user in:

SecurityContextHolder

Think of it as:

Request Scoped Storage

for logged-in user.

Visual:

Request Arrives
      │
      ▼

JWT Filter
      │
      ▼

SecurityContextHolder
 stores user
      │
      ▼

Controller
      │
      ▼

Service

Anywhere:

SecurityContextHolder
    .getContext()
    .getAuthentication()

returns current user.

Part 6 - What Will Our JWT Filter Do?

Request:

GET /claims
Authorization: Bearer eyJ...

Filter:

Step 1

Extract token

eyJ...
Step 2

Extract username

provider1

from JWT.

Step 3

Load user

CustomUserDetailsService
Step 4

Validate token

jwtService.isTokenValid(...)
Step 5

Create Authentication object

UsernamePasswordAuthenticationToken
Step 6

Store in SecurityContext

SecurityContextHolder
Step 7

Continue request

filterChain.doFilter(...)

Controller now sees:

Authenticated User




What is OncePerRequestFilter?

Spring provides:

OncePerRequestFilter

Meaning:

Execute exactly once
for each HTTP request

Without it:

A filter can execute multiple times during forwards/errors.

Every JWT filter you've ever seen:

extends OncePerRequestFilter

for this reason.

The Entire Flow
LOGIN

username/password
        │
        ▼

Auth Service
        │
        ▼

JWT Generated
        │
        ▼

Client Stores JWT



FUTURE REQUEST

Authorization: Bearer xxx
        │
        ▼

JwtAuthenticationFilter
        │
        ▼

Extract Username
        │
        ▼

Load User
        │
        ▼

Validate JWT
        │
        ▼

SecurityContextHolder
        │
        ▼

Controller
        │
        ▼

Service