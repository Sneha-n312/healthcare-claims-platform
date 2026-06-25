package com.healthcare.auth_service.security;

import com.healthcare.auth_service.config.JwtProperties;
import com.healthcare.auth_service.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    /**
     * Generate JWT for authenticated user
     */
    public String generateToken(User user) {

        Date now = new Date();
        System.out.println("jwt expiration: " + jwtProperties.getExpiration());
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(
                        new Date(
                                now.getTime()+ jwtProperties.getExpiration()
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Create signing key
     */
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                jwtProperties
                        .getSecret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }


    /**
     * Validate JWT belongs to user and isn't expired
     */
    public boolean isTokenValid(
            String token,
            String username) {

        String tokenUsername =
                extractUsername(token);

        return tokenUsername.equals(username)
                && !isTokenExpired(token);
    }

    /**
     * Check expiration
     */
    private boolean isTokenExpired(
            String token) {

        return extractExpiration(token)
                .before(new Date());
    }

    /**
     * Extract expiration
     */
    private Date extractExpiration(
            String token) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }

    /**
     * Extract username from JWT
     */
    public String extractUsername(String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }

    /**
     * Generic claim extractor
     */
    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }


    /**
     * Parse token
     */
    private Claims extractAllClaims(
            String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}