package com.project.brandprotection.controllers;

import com.project.brandprotection.dtos.requests.UserUpdateDto;
import com.project.brandprotection.dtos.responses.UserResponseDto;
import com.project.brandprotection.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        UserResponseDto userResponseDto = userService.getUserDetails(authentication);
        return ResponseEntity.ok(userResponseDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserDetails(@RequestBody UserUpdateDto userUpdateDto, Authentication authentication) {
        UserResponseDto userResponseDto = userService.updateUser(authentication, userUpdateDto);
        return ResponseEntity.ok(userResponseDto);
    }

}

