package com.example.demo.model.teqplay;

public record TeqplayToken(
        String username,
        String refreshToken,
        String token,
        long expiresInSeconds,
        String createdAt
){
}
