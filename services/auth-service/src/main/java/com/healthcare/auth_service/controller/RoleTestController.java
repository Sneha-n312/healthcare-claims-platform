package com.healthcare.auth_service.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleTestController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "Welcome Admin";
    }

    @GetMapping("/provider")
    @PreAuthorize("hasRole('PROVIDER')")
    public String provider() {
        return "Welcome Provider";
    }

    @GetMapping("/adjuster")
    @PreAuthorize("hasRole('ADJUSTER')")
    public String adjuster() {
        return "Welcome Adjuster";
    }
}