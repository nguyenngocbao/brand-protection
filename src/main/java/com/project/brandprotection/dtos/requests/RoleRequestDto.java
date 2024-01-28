package com.project.brandprotection.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleRequestDto {
    private String name;
    private String description;
    private boolean active;
    private List<PermissionRequestDto> permissions;
}


