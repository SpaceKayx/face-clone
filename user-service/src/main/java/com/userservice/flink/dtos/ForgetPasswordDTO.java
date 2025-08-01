package com.userservice.flink.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ForgetPasswordDTO {
    Long id;
    String userId;
    String fullName;
    String email;
    String verificationCode;
    Instant createdAt;
    Instant expiresAt;
    boolean used;
    /*
    * LocalDateTime to Instant
    * */
}
