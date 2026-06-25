### Spring Security Flow - Very Important

### Ok so lets say /api/v1/test had a null authorization header,from where is the forbidden response coming from ?

THIS is the question I was hoping you'd ask. This is where Spring Security really "clicks."

The forbidden (or unauthorized) response does not come from your filter.

Let's trace the request exactly.

Request
GET /api/v1/test

No Authorization header.

### Step 1 - JwtAuthenticationFilter

Your filter executes.

String authHeader =
        request.getHeader("Authorization");

Result:

authHeader = null

Then:

if(authHeader == null
        || !authHeader.startsWith("Bearer ")) {

    filterChain.doFilter(request,response);

    return;
}

Notice something important.

Your filter doesn't reject the request.

It simply says:

"I can't authenticate this request. Someone else decide what to do."

Then it calls the next filter.

### Step 2 - AnonymousAuthenticationFilter

The next important filter is:

AnonymousAuthenticationFilter

This filter asks:

"Has anyone authenticated this request?"

It checks:

SecurityContextHolder
        .getContext()
        .getAuthentication()

What does it find?

Nothing.

Because your JWT filter never called:

SecurityContextHolder
        .getContext()
        .setAuthentication(...)

So Spring says:

"I'll treat this request as an anonymous user."

Internally it creates something like:

AnonymousAuthenticationToken

Now the SecurityContext contains:

Current User = anonymousUser

Not null anymore.

This surprises many developers.

### Step 3 - AuthorizationFilter

Now AuthorizationFilter runs.

It checks your configuration:

.anyRequest()
.authenticated()

This rule literally means:

"Every request must be authenticated."

So it asks:

Is anonymousUser authenticated?

Answer:

No.

Therefore authorization fails.

### Step 4 - ExceptionTranslationFilter

This is the clever part.

AuthorizationFilter doesn't directly write a response.

Instead, it throws an exception.

Something similar to:

AuthenticationException

or

AccessDeniedException

Now another filter catches it.

Remember one of the filters we discussed?

ExceptionTranslationFilter

Its job is:

"Convert Spring Security exceptions into HTTP responses."

### Step 5 - AuthenticationEntryPoint

Since authentication failed, ExceptionTranslationFilter calls:

authenticationEntryPoint.commence(...)

Your class:

JwtAuthenticationEntryPoint

executes:

response.setStatus(401);

objectMapper.writeValue(...);

This generates:

{
    "status":401,
    "message":"Authentication required"
}
Entire Flow
Client

    │

    ▼

JwtAuthenticationFilter

    │

No JWT Found

    │

Continue

    ▼

AnonymousAuthenticationFilter

    │

Creates anonymousUser

    ▼

AuthorizationFilter

    │

Checks:

authenticated() ?

    │

False

    ▼

Throws AuthenticationException

    │

ExceptionTranslationFilter

    │

Calls AuthenticationEntryPoint

    │

Writes 401 Response

    ▼

Client