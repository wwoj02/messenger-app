package com.wojtek.messenger.user;

import com.wojtek.messenger.user.dto.UpdateProfileRequest;
import com.wojtek.messenger.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public UserResponse getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/user/{id}")
    public UserResponse updateUser(
            @PathVariable Integer id,
            @RequestBody UpdateProfileRequest update,
            @AuthenticationPrincipal UserPrincipal principal
            ) {
        return userService.updateUser(id, update, principal.getUsername());
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Integer id,
                           @AuthenticationPrincipal UserPrincipal principal
    ) {
        userService.deleteUser(id, principal.getUsername());
    }
}
