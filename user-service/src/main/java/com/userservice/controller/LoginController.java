package com.userservice.controller;

import com.core.dto.response.DataResponse;
import com.core.utils.JwtTokenProvider;
import com.userservice.dto.request.LoginRequest;
import com.userservice.security.CustomUserDetail;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {

    private AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public DataResponse loginGet() {
        return DataResponse.builder()
                .code(HttpStatus.METHOD_NOT_ALLOWED)
                .message("Please use POST method to login with username and password.")
                .build();
    }

    @PostMapping
    public DataResponse login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

            return new DataResponse(jwtTokenProvider.generateToken(user.getUserId()));
        } catch (BadCredentialsException e) {
            return DataResponse.builder()
                    .code(HttpStatus.UNAUTHORIZED)
                    .data("Sai tên đăng nhập hoặc mật khẩu")
                    .build();
        }
    }

    @GetMapping("/success")
    public DataResponse success() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        return DataResponse.builder()
                .data(jwtTokenProvider.generateToken(user.getUserId()))
                .message("Đăng nhập thành công!")
                .build();
    }

}
