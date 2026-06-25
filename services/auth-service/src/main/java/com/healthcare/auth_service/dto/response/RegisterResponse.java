package com.healthcare.auth_service.dto.response;

import com.healthcare.auth_service.entity.Role;

import java.util.UUID;

public record RegisterResponse (
        UUID id,
        String username,
        Role role
){
}
