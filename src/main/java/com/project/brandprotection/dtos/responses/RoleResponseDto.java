package com.project.brandprotection.dtos.responses;

import com.project.brandprotection.enums.Module;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RoleResponseDto {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private Map<Module, List<PermissionResponseDto>> groupedPermissions;
}
