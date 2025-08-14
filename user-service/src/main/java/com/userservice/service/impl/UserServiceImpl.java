package com.userservice.service.impl;

import com.core.constants.FConstants;
import com.core.kafka.message.BaseMessage;
import com.core.kafka.producer.BaseProducerHandler;
import com.core.utils.PasswordSaltUtil;
import com.userservice.dto.request.RegisterUserRequest;
import com.userservice.dto.request.UpdatePasswordRequest;
import com.userservice.dto.request.UpdateUserRequest;
import com.userservice.dto.response.UserResponse;
import com.userservice.entities.User;
import com.userservice.enums.DeletedEnum;
import com.userservice.enums.LockedEnum;
import com.userservice.exception.BaseException;
import com.userservice.exception.ErrorCode;
import com.userservice.mapper.UserMapper;
import com.userservice.repository.UserRepository;
import com.userservice.security.CustomUserDetail;
import com.userservice.service.abs.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    UserRepository userRepository;
    UserMapper userMapper;
    BaseProducerHandler kafka;

    @Override
    public void register(RegisterUserRequest request) throws DataIntegrityViolationException {
        try {
            User user = userMapper.mapToUser(request);
            String salt = PasswordSaltUtil.generateSalt();
            String encodedPassword = PasswordSaltUtil.encodeWithBCrypt(user.getPassword(), salt); // SHA256 + BCrypt

            user.setSalt(salt);
            user.setPassword(encodedPassword); // Lưu BCrypt
            user = userRepository.save(user);

            this.sendMessageToKafka(user.getId().toString(), user);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException(ex.getMessage());
        } catch (Exception ex) {
            throw new BaseException(ErrorCode.REGISTER_ERROR);
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @CacheEvict(value = "users", key = "#username")
    @Override
    public UserResponse updateAccount(String username, UpdateUserRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(ErrorCode.ACCOUNT_NOTFOUND));

        userMapper.updateUserFromRequest(request, user);

        this.sendMessageToKafka(user.getId().toString(), user);

        return userMapper.mapToUserResponse(userRepository.save(user));
    }

    @Override
    public void updatePassword(String username, UpdatePasswordRequest request) throws NoSuchAlgorithmException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(ErrorCode.ACCOUNT_NOTFOUND));
        if (PasswordSaltUtil.checkPassword(request.getOldPassword(), user.getSalt(), user.getPassword())) {
            try {
                String hashedPassword = PasswordSaltUtil.encodeWithBCrypt(request.getNewPassword(), user.getSalt());
                user.setPassword(hashedPassword);
            } catch (NoSuchAlgorithmException e) {
                throw new BaseException(ErrorCode.PASSWORD_ERROR);
            }
            userRepository.save(user);
        } else {
            throw new BaseException(ErrorCode.PASSWORD_INVALID);
        }
    }

    @Override
    public void forgetPassword(User user, String newPassword) throws NoSuchAlgorithmException {
        try {
            String hashedPassword = PasswordSaltUtil.encodeWithBCrypt(newPassword, user.getSalt());
            user.setPassword(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new BaseException(ErrorCode.PASSWORD_ERROR);
        }

        userRepository.save(user);
    }

    @Override
    public void deleteAccount(UUID id) {
        User user = findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.ACCOUNT_NOTFOUND));
        user.setDeleted(DeletedEnum.DELETED.getValue());

        userRepository.save(user);
        this.sendMessageToKafka(user.getId().toString(), user);
    }

    @Override
    public void lockAccount(String id) {
        User user = findById(UUID.fromString(id))
                .orElseThrow(() -> new BaseException(ErrorCode.ACCOUNT_NOTFOUND));
        user.setLocked(LockedEnum.LOCKED.getValue());

        userRepository.save(user);
        this.sendMessageToKafka(user.getId().toString(), user);
    }

    @Override
    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(ErrorCode.ACCOUNT_NOTFOUND));

        return userMapper.mapToUserResponse(user);
    }

    @Override
    public User existsEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.EMAIL_NOTFOUND));
    }

    @Cacheable(value = "users", key = "#username")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.EMAIL_NOTFOUND.getMessage()));

        return new CustomUserDetail(user);
    }

    private void sendMessageToKafka(String key, User user) {
        kafka.send(BaseMessage.builder()
                .topic(FConstants.TOPIC_USER_CACHE)
                .key(key)
                .value(user)
                .build());
    }

}
