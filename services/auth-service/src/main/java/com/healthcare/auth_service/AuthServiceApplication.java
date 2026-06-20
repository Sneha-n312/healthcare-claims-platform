package com.healthcare.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {

        System.out.println("TIMEZONE=" + java.util.TimeZone.getDefault().getID());
        SpringApplication.run(AuthServiceApplication.class, args);
	}

}
