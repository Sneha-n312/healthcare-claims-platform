package com.healthcare.auth_service;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcTest {

    public static void main(String[] args) throws Exception {

        System.setProperty("user.timezone", "Asia/Kolkata");
        String url = "jdbc:postgresql://127.0.0.1:5432/healthcare_claims";

        System.out.println("Connecting to " + url);

        Connection connection =
                DriverManager.getConnection(
                        url,
                        "healthcare_user",
                        "healthcare_password");

        System.out.println("CONNECTED SUCCESSFULLY");

        connection.close();
    }
}