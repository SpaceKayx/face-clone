package com.userservice.mapper;

import com.userservice.dto.request.RegisterUserRequest;
import com.userservice.dto.request.UpdateUserRequest;
import com.userservice.dto.response.UserResponse;
import com.userservice.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") // cho phep DI
public interface UserMapper {

    @Mapping(source = "request.username", target = "user.username")
    @Mapping(source = "request.firstName", target = "user.firstName")
    @Mapping(source = "request.lastName", target = "user.lastName")
    @Mapping(source = "request.address", target = "user.address")
    @Mapping(source = "request.email", target = "user.email")
    @Mapping(source = "request.gender", target = "user.gender")
    @Mapping(source = "request.phoneNumber", target = "user.phoneNumber")
    void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);

    User mapToUser(RegisterUserRequest request);

    UserResponse mapToUserResponse(User request);

}
