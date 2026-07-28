package com.wojtek.messenger.user.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Integer id,
        String username,
        String email,
        String firstName,
        String lastName,
        LocalDateTime joinedAt
       ) {
}