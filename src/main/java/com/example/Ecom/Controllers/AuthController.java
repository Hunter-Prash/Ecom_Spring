package com.example.Ecom.Controllers;

import com.example.Ecom.DTOS.LoginRequest;
import com.example.Ecom.DTOS.LoginResponse;
import com.example.Ecom.DTOS.UserRegisterRequest;
import com.example.Ecom.DTOS.UserResponse;
import com.example.Ecom.Services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Public endpoint — no JWT required
    @PostMapping("/createUser")
    public UserResponse createUser(@RequestBody UserRegisterRequest req) {
        return userService.createUser(req);
    }

    // Public endpoint — no JWT required
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        return userService.login(req);
    }
}