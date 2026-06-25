package com.healthcare.auth_service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.auth_service.dto.response.SecurityErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        SecurityErrorResponse errorResponse =   new SecurityErrorResponse(
                OffsetDateTime.now(),
                401,
                "Unauthorized",
                "Authentication required",
                request.getRequestURI()
        );

        objectMapper.writeValue(
                response.getOutputStream(),
                errorResponse);
    }
}