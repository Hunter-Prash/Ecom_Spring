package com.example.Ecom.Services;

import com.example.Ecom.DTOS.UserRegisterRequest;
import com.example.Ecom.DTOS.UserResponse;
import com.example.Ecom.Entities.User;
import com.example.Ecom.Repositories.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.*;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public UserService(PasswordEncoder passwordEncoder,UserRepo userRepo){
        this.passwordEncoder=passwordEncoder;
        this.userRepo=userRepo;
    }

    public UserResponse createUser(UserRegisterRequest req){
        User user=User.builder()
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
}
