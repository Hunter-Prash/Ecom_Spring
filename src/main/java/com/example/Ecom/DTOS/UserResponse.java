package com.example.Ecom.DTOS;

import java.time.ZonedDateTime;

/*
*
* {
    "userId": 1,
    "username": "rahul",
    "email": "rahul@gmail.com",
    "role": "USER",
    "createdAt": "2026-09-13T13:30:00+05:30"
}
*
* */
public record UserResponse(
        Long userId,
        String username,
        String email,
        String role,
        ZonedDateTime createdAt
) {}