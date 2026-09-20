package com.example.Ecom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
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
        public JwtDecoder jwtDecoder() {

                SecretKey secretKey = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");

                return NimbusJwtDecoder.withSecretKey(secretKey).build();
        }

        @Bean
        public JwtAuthenticationConverter jwtAuthenticationConverter(){

                //Object that knows how to extract roles from a JWT. Creating it manually because you need to configure it before giving it to Spring.
                JwtGrantedAuthoritiesConverter authoritiesConverter=new JwtGrantedAuthoritiesConverter();

                //Hey, look at the role field inside the JWT to find the role"
                //Without this Spring looks for scope or scp by default — wrong field.
                authoritiesConverter.setAuthoritiesClaimName("role");

                //Spring Security internally expects roles to be prefixed with ROLE_.So "ADMIN" in your JWT → becomes "ROLE_ADMIN" inside Spring.This is just a Spring Security convention.
                authoritiesConverter.setAuthorityPrefix("ROLE_");


                //the main converter Spring uses when a JWT comes in.
                //You're plugging your configured authoritiesConverter into it.
                JwtAuthenticationConverter converter=new JwtAuthenticationConverter();
                converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

                return converter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http)
                        throws Exception {

                http
                                // 1. MUST Disable CSRF for POST requests to work without tokens
                                .csrf(csrf -> csrf.disable())

                                // 2. MUST tell Spring Security not to use Server Sessions (since we use JWT)
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                org.springframework.security.config.http.SessionCreationPolicy.STATELESS))

                                // Configure which requests are allowed
                                .authorizeHttpRequests(auth -> auth

                                                // Login and registration don't need a JWT
                                                .requestMatchers("/auth/login", "/auth/createUser")
                                                .permitAll()

                                                // Everything else requires authorization
                                                .anyRequest()
                                                .authenticated())

                                // Disable Spring's default HTML login page
                                .formLogin(form -> form.disable())

                                // Disable HTTP Basic authentication
                                .httpBasic(basic -> basic.disable())

                                // Tell Spring Security to process Bearer JWTs
                                .oauth2ResourceServer(oauth2 -> oauth2
                                        .jwt(jwt -> jwt
                                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                                        ));

                return http.build();
        }

}