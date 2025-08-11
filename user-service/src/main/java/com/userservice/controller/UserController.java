package com.userservice.controller;

import com.core.dto.response.DataResponse;
import com.userservice.dto.request.UpdatePasswordRequest;
import com.userservice.dto.request.UpdateUserRequest;
import com.userservice.security.CustomUserDetail;
import com.userservice.service.abs.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public DataResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        return new DataResponse(userService.findByUsername(userDetail.getUsername()));
    }

    @GetMapping("/validate")
    public DataResponse validate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof CustomUserDetail) {
            return new DataResponse(true);
        }

        return DataResponse.builder()
                .code(HttpStatus.UNAUTHORIZED)
                .data(false)
                .build();
    }

    @PutMapping()
    public DataResponse update(@RequestBody UpdateUserRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        return new DataResponse(userService.updateAccount(userDetail.getUsername(), request));
    }

    @PutMapping("/update-password")
    public DataResponse updatePassword(@RequestBody UpdatePasswordRequest request) throws NoSuchAlgorithmException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        userService.updatePassword(userDetail.getUsername(), request);

        return new DataResponse("Cập nhật mật khẩu thành công!");
    }

}