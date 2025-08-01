package com.userservice.repository;

import com.userservice.entities.ForgetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword, Long> {
    Optional<ForgetPassword> findFirstByVerificationCodeAndUserIdOrderByCreatedAtDesc(String code, String userId);
    Optional<ForgetPassword> findByUserId(String userId);
}
