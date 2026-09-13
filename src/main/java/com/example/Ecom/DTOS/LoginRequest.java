package com.example.Ecom.DTOS;

public record LoginRequest(
        String username,
        String password
) {}

/*
REQUEST JSON:

{
    "username": "rahul",
    "password": "hello123"
}
*/