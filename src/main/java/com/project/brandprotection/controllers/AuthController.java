package com.project.brandprotection.controllers;

import com.project.brandprotection.dtos.requests.SignupRequestDto;
import com.project.brandprotection.dtos.requests.TokenRefreshRequestDto;
import com.project.brandprotection.dtos.requests.UserLoginDto;
import com.project.brandprotection.dtos.responses.LoginResponseDto;
import com.project.brandprotection.events.SignupCompleteEvent;
import com.project.brandprotection.models.User;
import com.project.brandprotection.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ApplicationEventPublisher publisher;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        User user = authService.signup(requestDto);
        publisher.publishEvent(new SignupCompleteEvent(user, getVerifyEmailUrl()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        LoginResponseDto loginResponseDto = authService.login(userLoginDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        String result = authService.verifyToken(token);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequestDto request) {
        String requestRefreshToken = request.refreshToken();
        var tokenRefreshResponseDto = authService.refreshToken(requestRefreshToken);
        return ResponseEntity.ok(tokenRefreshResponseDto);
    }

    private String getVerifyEmailUrl() {
        return MvcUriComponentsBuilder.fromMethodName(AuthController.class, "verifyEmail", "").build().toUriString();
    }

}
