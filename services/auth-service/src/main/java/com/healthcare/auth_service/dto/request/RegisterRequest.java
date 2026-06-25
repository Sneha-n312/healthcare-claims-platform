package com.healthcare.auth_service.dto.request;

import com.healthcare.auth_service.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest (

        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "password is required")
        String password,

        @NotNull(message = "Role is required")
        Role role
){
}
