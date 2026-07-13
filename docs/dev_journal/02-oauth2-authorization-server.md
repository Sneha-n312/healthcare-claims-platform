# OAuth2 Authorization Server

## Responsibilities

-   Authenticate users
-   Authenticate OAuth clients
-   Issue access & refresh tokens
-   Publish JWKS

## Important Beans

-   AuthorizationServerConfig
-   RegisteredClientRepository
-   JWKSource
-   JwtDecoder
-   AuthorizationServerSettings

## RSA

Private key signs tokens. Public key verifies tokens.

## OAuth Actors

-   Resource Owner
-   Client
-   Authorization Server
-   Resource Server
