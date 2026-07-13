package com.healthcare.auth_service.security;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
@RequiredArgsConstructor
public class RsaKeyProperties {

    public RSAKey toRSAKey() {

        try {

            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            try (InputStream inputStream =
                         new ClassPathResource("auth-server.p12").getInputStream()) {

                keyStore.load(inputStream, "changeit".toCharArray());
            }

            RSAPrivateKey privateKey =
                    (RSAPrivateKey) keyStore.getKey(
                            "auth-server",
                            "changeit".toCharArray());

            RSAPublicKey publicKey =
                    (RSAPublicKey) keyStore
                            .getCertificate("auth-server")
                            .getPublicKey();

            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID("healthcare-auth-key")
                    .build();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}