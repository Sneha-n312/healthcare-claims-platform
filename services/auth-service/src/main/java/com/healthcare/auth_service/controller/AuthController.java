package com.healthcare.auth_service.controller;

import com.healthcare.auth_service.dto.request.LoginRequest;
import com.healthcare.auth_service.dto.request.RegisterRequest;
import com.healthcare.auth_service.dto.response.LoginResponse;
import com.healthcare.auth_service.dto.response.RegisterResponse;
import com.healthcare.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponse register( @Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
