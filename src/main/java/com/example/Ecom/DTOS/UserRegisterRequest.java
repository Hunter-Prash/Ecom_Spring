package com.example.Ecom.DTOS;

/*
{
    "username": "rahul",
    "email": "rahul@gmail.com",
    "password": "hello123"
}
 */
public record UserRegisterRequest(
        String username,
        String email,
        String password
) {}