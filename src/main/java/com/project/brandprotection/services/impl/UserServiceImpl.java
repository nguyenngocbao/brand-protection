package com.project.brandprotection.services.impl;

import com.project.brandprotection.dtos.requests.UserUpdateDto;
import com.project.brandprotection.dtos.responses.UserResponseDto;
import com.project.brandprotection.exceptions.ResourceNotFoundException;
import com.project.brandprotection.mappers.UserMapper;
import com.project.brandprotection.models.User;
import com.project.brandprotection.repositories.UserRepository;
import com.project.brandprotection.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto getUserDetails(Authentication authentication) {
        String email = ((User) authentication.getPrincipal()).getEmail();
        User user = getUserWithRoleAndPermissions(email);
        return userMapper.mapToDto(user);
    }

    @Override
    public UserResponseDto updateUser(Authentication authentication, UserUpdateDto userUpdateDto) {
        String email = ((User) authentication.getPrincipal()).getEmail();
        //todo: implement update user profile
        return null;
    }

    private User getUserWithRoleAndPermissions(String email) {
        return userRepository.findUserWithRoleAndPermissions(email).orElseThrow(() -> new ResourceNotFoundException("User could not be found with email: " + email));
    }

}
