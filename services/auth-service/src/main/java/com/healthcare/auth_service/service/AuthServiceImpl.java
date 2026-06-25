package com.healthcare.auth_service.service;

import com.healthcare.auth_service.dto.request.LoginRequest;
import com.healthcare.auth_service.dto.request.RegisterRequest;
import com.healthcare.auth_service.dto.response.LoginResponse;
import com.healthcare.auth_service.dto.response.RegisterResponse;
import com.healthcare.auth_service.entity.User;
import com.healthcare.auth_service.exception.DuplicateUsernameException;
import com.healthcare.auth_service.exception.InvalidCredentialsException;
import com.healthcare.auth_service.exception.UserNotFoundException;
import com.healthcare.auth_service.repository.UserRepository;
import com.healthcare.auth_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService  jwtService;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
       if(userRepository.existsByUsername(registerRequest.username()))
           throw new DuplicateUsernameException(registerRequest.username());

       User user = new User();
       user.setId(UUID.randomUUID());
       user.setUsername(registerRequest.username());
       user.setPassword(passwordEncoder.encode(registerRequest.password()));
       user.setRole(registerRequest.role());

        User savedUser = userRepository.save(user);

        return new RegisterResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getRole());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new UserNotFoundException(loginRequest.username()));

        boolean validatePassword = passwordEncoder.matches(loginRequest.password(), user.getPassword());
        if(!validatePassword)
            throw new InvalidCredentialsException("Invalid username or password");

        String accessToken = jwtService.generateToken(user);

        return new LoginResponse(accessToken);


    }
}
