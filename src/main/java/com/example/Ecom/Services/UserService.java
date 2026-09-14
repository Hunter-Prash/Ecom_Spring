package com.example.Ecom.Services;

import com.example.Ecom.DTOS.LoginRequest;
import com.example.Ecom.DTOS.LoginResponse;
import com.example.Ecom.DTOS.UserRegisterRequest;
import com.example.Ecom.DTOS.UserResponse;
import com.example.Ecom.Entities.User;
import com.example.Ecom.Repositories.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import lombok.*;

import java.time.Instant;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final JwtEncoder jwtEncoder;

    public UserService(PasswordEncoder passwordEncoder, UserRepo userRepo, JwtEncoder jwtEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.jwtEncoder = jwtEncoder;
    }

    public UserResponse createUser(UserRegisterRequest req) {
        User user = User.builder()
                .username(req.username())
                .email(req.email())
                .password(req.password())
                .role("USER")
                .build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public LoginResponse login(LoginRequest req) {
        User user = User.builder()
                .username(req.username())
                .password((req.password())).build();

        User exisitnguser = userRepo.findByUsername(user.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(user.getPassword(), exisitnguser.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        //jwt validation
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("ecom-backend")                    // Who created this token?
                .issuedAt(now)                             // When was it created?
                .expiresAt(now.plusSeconds(3600))          // Expires in 1 hour (3600 sec)
                .subject(exisitnguser.getUsername())       // The main identifying data (the user)
                .claim("userId", exisitnguser.getUserId())     // Custom data you want frontend to have
                .build();

        // 4. Stamp the Token
        // We hand the claims to the Nimbus machine we built, and it signs it with the SecretKey
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(token);
    }
}
