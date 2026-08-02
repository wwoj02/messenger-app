package com.wojtek.messenger.auth.dto;

public record RegisterResponse(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email
) {
}
