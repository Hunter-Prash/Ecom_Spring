package com.example.Ecom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

// BEAN-FACTORY
@Configuration
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtEncoder jwtEncoder() {

        SecretKey secretKey = new SecretKeySpec(
                jwtSecret.getBytes(),
                "HmacSHA256");

        return NimbusJwtEncoder
                .withSecretKey(secretKey)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // Define which HTTP requests are allowed or blocked
                .authorizeHttpRequests(auth -> auth

                        // These two endpoints are PUBLIC.
                        // User does NOT need a JWT/token to access them.
                        // We need this because a new user has no token yet.
                        .requestMatchers("/auth/login", "/auth/createUser")
                        .permitAll()

                        // Every other endpoint requires the user to be authenticated.
                        // Example:
                        // /products  → 🔒
                        // /orders    → 🔒
                        // /inventory → 🔒
                        .anyRequest()
                        .authenticated()
                )

                // Disable Spring Security's default HTML login page.
                // We are going to create our own /auth/login endpoint.
                .formLogin(form -> form.disable())

                // Disable HTTP Basic Authentication.
                // We are using JWT instead.
                .httpBasic(basic -> basic.disable());

        // Build and return the security configuration.
        // Spring uses this SecurityFilterChain for incoming requests.
        return http.build();
    }
}