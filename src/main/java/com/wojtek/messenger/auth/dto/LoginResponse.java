package com.wojtek.messenger.auth.dto;

public record LoginResponse (
        String token,
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email
) {
}
