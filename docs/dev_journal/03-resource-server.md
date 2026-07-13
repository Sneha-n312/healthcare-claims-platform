# Resource Server

## Configuration

``` yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8081
```

## Runtime Flow

Bearer Token -\> BearerTokenAuthenticationFilter -\> JwtDecoder -\>
Authentication -\> Controller

No custom JWT filter or JwtService required.
