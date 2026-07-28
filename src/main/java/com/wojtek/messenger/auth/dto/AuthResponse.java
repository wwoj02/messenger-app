package com.wojtek.messenger.auth.dto;

public record AuthResponse(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email
) {
}
