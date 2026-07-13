# Authentication Flow

User ↓ Authorization Server ↓ UserDetailsService ↓ RSA Signed JWT ↓
Claims Service ↓ JWKS ↓ JwtDecoder ↓ Controller
