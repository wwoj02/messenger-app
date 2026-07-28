package com.wojtek.messenger.user.dto;

public record UpdateProfileRequest(
        String username,
        String firstName,
        String lastName
) {
}
