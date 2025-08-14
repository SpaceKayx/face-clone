package com.core.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Slf4j
public class PasswordSaltUtil {

    static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Tạo salt ngẫu nhiên
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 128-bit
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        log.debug("Generated salt: {}", encodedSalt);
        return encodedSalt;
    }

    // Băm SHA-256(password + salt)
    public static String hashPasswordWithSHA256(String password, String salt) throws NoSuchAlgorithmException {
        String combined = password + salt;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(combined.getBytes());
        String hashed = Base64.getEncoder().encodeToString(hash);
        log.debug("SHA-256(password + salt) = {}", hashed);
        return hashed;
    }

    // Mã hóa bằng BCrypt(SHA256(password + salt))
    public static String encodeWithBCrypt(String rawPassword, String salt) throws NoSuchAlgorithmException {
        String sha256 = hashPasswordWithSHA256(rawPassword, salt);
        String bcrypt = encoder.encode(sha256);
        log.debug("BCrypt(SHA256(password + salt)) = {}", bcrypt);
        return bcrypt;
    }

    /**
     * @param rawPassword          Mật khẩu gốc người dùng vừa nhập (chưa mã hóa).
     * @param storedSalt           Salt đã lưu trong DB, được dùng khi hash mật khẩu ban đầu.
     * @param storedHashedPassword Mật khẩu đã mã hóa lưu trong DB (được hash bằng SHA-256 + salt + BCrypt).
     * @return true nếu mật khẩu đúng, false nếu sai.
     * @throws NoSuchAlgorithmException nếu thuật toán SHA-256 không được hỗ trợ (trường hợp rất hiếm).
     **/
    public static boolean checkPassword(String rawPassword, String storedSalt, String storedHashedPassword) throws NoSuchAlgorithmException {
        return encoder.matches(
                hashPasswordWithSHA256(rawPassword, storedSalt),
                storedHashedPassword);
    }

}
