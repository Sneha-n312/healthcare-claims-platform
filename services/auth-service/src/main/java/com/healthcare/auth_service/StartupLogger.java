package com.healthcare.auth_service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @PostConstruct
    public void log() {
        System.out.println("URL=" + url);
        System.out.println("USERNAME=" + username);
    }
}
