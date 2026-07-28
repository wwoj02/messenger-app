package com.wojtek.messenger.user;

import com.wojtek.messenger.user.dto.UpdateProfileRequest;
import com.wojtek.messenger.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toUserResponseList(users);
    }

    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(Integer id, UpdateProfileRequest update) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        boolean hasUsername = update.username() != null && !update.username().trim().isEmpty();
        boolean hasFirstName = update.firstName() != null && !update.firstName().trim().isEmpty();
        boolean hasLastName = update.lastName() != null && !update.lastName().trim().isEmpty();

        if (!hasUsername && !hasFirstName && !hasLastName) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No data to update"
            );
        }

        if(hasUsername) {
            user.setUsername(update.username());
        }

        if(hasFirstName) {
            user.setFirstName(update.firstName());
        }

        if(hasLastName) {
            user.setLastName(update.lastName());
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
