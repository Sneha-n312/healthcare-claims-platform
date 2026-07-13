package com.healthcare.auth_service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserProvider {

    public AuthenticatedUser getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new IllegalStateException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AuthenticatedUser user) {
            return user;
        }

        throw new IllegalStateException("Expected AuthenticatedUser but found: " +
                                                    principal.getClass().getSimpleName());
    }
}
