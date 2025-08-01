package com.userservice.controller;

import com.core.dto.response.DataResponse;
import com.userservice.dto.request.RegisterUserRequest;
import com.userservice.service.abs.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@AllArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse createAccount(@Valid @RequestBody RegisterUserRequest request) throws NoSuchAlgorithmException {
        userService.register(request);

        return new DataResponse();
    }

}