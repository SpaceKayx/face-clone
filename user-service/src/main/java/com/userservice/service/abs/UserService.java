package com.userservice.service.abs;

import com.userservice.dto.request.RegisterUserRequest;
import com.userservice.dto.request.UpdatePasswordRequest;
import com.userservice.dto.request.UpdateUserRequest;
import com.userservice.dto.response.UserResponse;
import com.userservice.entities.User;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    void register(RegisterUserRequest request) throws NoSuchAlgorithmException;

    Optional<User> findById(UUID id);

    UserResponse updateAccount(String username, UpdateUserRequest request);

    void updatePassword(String username, UpdatePasswordRequest request) throws NoSuchAlgorithmException;

    void forgetPassword(User user, String newPassword) throws NoSuchAlgorithmException;

    UserResponse findByUsername(String username);

    User existsEmail(String email);

    void deleteAccount(UUID id);

    void lockAccount(String id);

}
