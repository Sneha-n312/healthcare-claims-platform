# Spring Security Notes

## Security Filter Chains

-   Authorization Server chain handles `/oauth2/**`.
-   Application chain handles business APIs.
-   Spring chooses the first matching chain.

## Authentication Flow (Legacy)

User -\> UserDetailsService -\> JwtService -\> JwtAuthenticationFilter
-\> SecurityContextHolder

## CurrentUser Pattern

Wrap `SecurityContextHolder` access in a provider component.

## Key Interview Topics

-   `@Controller` vs `@RestController`
-   `@ControllerAdvice` vs `@RestControllerAdvice`
-   Multiple `SecurityFilterChain`s
-   Servlet filter integration
