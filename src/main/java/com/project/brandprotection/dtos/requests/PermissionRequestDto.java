package com.project.brandprotection.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequestDto {
    private Long id;
    private boolean enabled;
}
