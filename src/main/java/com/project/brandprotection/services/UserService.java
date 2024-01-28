package com.project.brandprotection.services;

import com.project.brandprotection.dtos.requests.UserUpdateDto;
import com.project.brandprotection.dtos.responses.UserResponseDto;
import org.springframework.security.core.Authentication;

public interface UserService {

    UserResponseDto getUserDetails(Authentication authentication);

    UserResponseDto updateUser(Authentication authentication, UserUpdateDto userUpdateDto);
}
