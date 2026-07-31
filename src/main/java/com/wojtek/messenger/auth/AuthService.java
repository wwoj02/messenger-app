package com.wojtek.messenger.auth;

import com.wojtek.messenger.auth.dto.AuthResponse;
import com.wojtek.messenger.auth.dto.LoginRequest;
import com.wojtek.messenger.auth.dto.RegisterRequest;
import com.wojtek.messenger.user.User;
import com.wojtek.messenger.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setJoinedAt(LocalDateTime.now());
        userRepository.save(user);

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username());

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found");
        }

        if (passwordEncoder.matches(request.password(), user.getPassword()))
        {
            return new AuthResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail());
        }
        else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Wrong password");
        }
    }
}
