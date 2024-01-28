package com.project.brandprotection.services.impl;

import com.project.brandprotection.dtos.requests.SignupRequestDto;
import com.project.brandprotection.dtos.requests.UserLoginDto;
import com.project.brandprotection.dtos.responses.LoginResponseDto;
import com.project.brandprotection.dtos.responses.TokenRefreshResponseDto;
import com.project.brandprotection.dtos.responses.UserResponseDto;
import com.project.brandprotection.exceptions.BadRequestException;
import com.project.brandprotection.exceptions.DuplicateException;
import com.project.brandprotection.exceptions.ResourceNotFoundException;
import com.project.brandprotection.exceptions.TokenRefreshException;
import com.project.brandprotection.mappers.UserMapper;
import com.project.brandprotection.models.RefreshToken;
import com.project.brandprotection.models.Role;
import com.project.brandprotection.models.User;
import com.project.brandprotection.models.VerificationToken;
import com.project.brandprotection.repositories.RoleRepository;
import com.project.brandprotection.repositories.UserRepository;
import com.project.brandprotection.repositories.VerificationTokenRepository;
import com.project.brandprotection.services.AuthService;
import com.project.brandprotection.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto login(UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.email(), userLoginDto.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(userLoginDto.email());
        User user = getUserWithRoleAndPermissions(userLoginDto.email());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        UserResponseDto userResponseDto = userMapper.mapToDto(user);
        return LoginResponseDto.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
                .userInfo(userResponseDto)
                .build();
    }

    @Override
    public String verifyToken(String token) {
        String verificationResult = validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            return "Verified successfully!. You can login to the website!";
        }
        return "Fail to verify, the verification code is not correct!";
    }

    @Override
    public TokenRefreshResponseDto refreshToken(String requestRefreshToken) {
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateToken(user.getUsername());
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
                    return new TokenRefreshResponseDto(token, newRefreshToken.getToken());
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    @Override
    public void saveUserVerificationToken(User user, String token) {
        var verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public User signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateException("Email is already exist!");
        }
        Role userRole = roleRepository.findByName(Role.USER).orElseThrow(() -> new ResourceNotFoundException("User role could not be found"));
        if (!signupRequestDto.getPassword().equals(signupRequestDto.getRetypePassword())) {
            throw new BadRequestException("Password does not match!");
        }
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        User newUser = userMapper.mapToEntity(signupRequestDto, userRole, encodedPassword);
        return userRepository.save(newUser);
    }

    private String validateToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            String msg = "Invalid verification verificationToken";
            log.info(msg);
            return msg;
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            String msg = "Invalid verification verificationToken";
            log.info(msg);
            return msg;
        }
        user.setActive(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
        return "valid";
    }

    private User getUserWithRoleAndPermissions(String email) {
        return userRepository.findUserWithRoleAndPermissions(email).orElseThrow(() -> new ResourceNotFoundException("User could not be found with email: " + email));
    }

}
