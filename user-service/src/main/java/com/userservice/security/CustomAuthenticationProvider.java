package com.userservice.security;

import com.core.utils.PasswordSaltUtil;
import com.userservice.exception.BaseException;
import com.userservice.exception.ErrorCode;
import com.userservice.service.impl.UserServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomAuthenticationProvider implements AuthenticationProvider {
    // custom login
    UserServiceImpl userServiceImpl;
    PasswordSaltUtil passwordSaltUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Do Authenticate");
        try {
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();
            UserDetails userDetails = userServiceImpl.loadUserByUsername(username);
            if (!(userDetails instanceof CustomUserDetail customUserDetail)) {
                throw new AuthenticationException("UserDetails is not an instance of CustomUserDetail") {
                };
            }

            if (!passwordSaltUtil.checkPassword(password, customUserDetail.getSalt(), userDetails.getPassword().trim())) {
                throw new BaseException(ErrorCode.LOGIN_ERROR);
            } else if (customUserDetail.isEnabled()) {
                throw new BaseException(ErrorCode.ACCOUNT_LOCKED);
            }

            log.info("Login successful");
            return new UsernamePasswordAuthenticationToken
                    (userDetails, null, userDetails.getAuthorities());
        } catch (NoSuchAlgorithmException e) {
            log.error("Login failed: ", e);
            throw new RuntimeException("Error checking password", e);
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

