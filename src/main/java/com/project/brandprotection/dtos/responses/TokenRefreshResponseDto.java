package com.project.brandprotection.dtos.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRefreshResponseDto {
    private String accessToken;
    private String refreshToken;
}
