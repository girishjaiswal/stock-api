package com.assignment.stockapi;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.Map;


@TestConfiguration
public class TestSecurityConfig {

    static final String OAUTH_TOKEN = "dummy token";
    static final String SUB = "user1@test.com";
    static final String AUTHORITY = "USER";

    @Bean
    public JwtDecoder jwtDecoder() {
        return new JwtDecoder() {
            @Override
            public Jwt decode(String token) {
                return jwt();
            }
        };
    }

    public Jwt jwt() {
        //adding claims used by controller
        Map<String, Object> claims = Map.of(
                "sub", SUB, "AUTHORITY", AUTHORITY
        );

        return new Jwt(
                OAUTH_TOKEN,
                Instant.now(),
                Instant.now().plusSeconds(30),
                Map.of("test", "test"),
                claims
        );
    }

}