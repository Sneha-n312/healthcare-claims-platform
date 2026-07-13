package com.healthcare.auth_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth.keystore")
public class KeyStoreProperties {

    private String path;

    private String password;

    private String alias;
}
