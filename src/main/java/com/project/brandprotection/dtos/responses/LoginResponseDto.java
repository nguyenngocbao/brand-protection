package com.project.brandprotection.dtos.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {
    private String token;
    private String refreshToken;
    private UserResponseDto userInfo;
}
