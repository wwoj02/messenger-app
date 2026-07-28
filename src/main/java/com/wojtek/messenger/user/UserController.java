package com.wojtek.messenger.user;

import com.wojtek.messenger.user.dto.UpdateProfileRequest;
import com.wojtek.messenger.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
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
            @RequestBody UpdateProfileRequest update) {
        return userService.updateUser(id, update);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}
