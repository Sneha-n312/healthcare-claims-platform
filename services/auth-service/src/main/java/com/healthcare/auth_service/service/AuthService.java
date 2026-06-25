package com.healthcare.auth_service.service;

import com.healthcare.auth_service.dto.request.LoginRequest;
import com.healthcare.auth_service.dto.request.RegisterRequest;
import com.healthcare.auth_service.dto.response.LoginResponse;
import com.healthcare.auth_service.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);
}
