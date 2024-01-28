package com.project.brandprotection.mappers;

import com.project.brandprotection.dtos.requests.SignupRequestDto;
import com.project.brandprotection.dtos.responses.UserResponseDto;
import com.project.brandprotection.models.Role;
import com.project.brandprotection.models.User;
import com.project.brandprotection.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ObjectMapperUtils objectMapperUtils;

    public User mapToEntity(SignupRequestDto signupRequestDto, Role role, String encodedPassword) {
        User user = objectMapperUtils.mapToEntityOrDto(signupRequestDto, User.class);
        user.setRole(role);
        user.setPassword(encodedPassword);
        return user;
    }

    public UserResponseDto mapToDto(User user) {
        return objectMapperUtils.mapToEntityOrDto(user, UserResponseDto.class);
    }
}
