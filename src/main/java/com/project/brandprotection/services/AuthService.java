package com.project.brandprotection.services;

import com.project.brandprotection.dtos.requests.SignupRequestDto;
import com.project.brandprotection.dtos.requests.UserLoginDto;
import com.project.brandprotection.dtos.responses.LoginResponseDto;
import com.project.brandprotection.dtos.responses.TokenRefreshResponseDto;
import com.project.brandprotection.models.User;

public interface AuthService {

    LoginResponseDto login(UserLoginDto userLoginDto);

    String verifyToken(String token);

    TokenRefreshResponseDto refreshToken(String requestRefreshToken);

    void saveUserVerificationToken(User user, String verificationToken);

    User signup(SignupRequestDto requestDto);
}
