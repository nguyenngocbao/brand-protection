package com.project.brandprotection.mappers;

import com.project.brandprotection.dtos.requests.RoleRequestDto;
import com.project.brandprotection.dtos.responses.PermissionResponseDto;
import com.project.brandprotection.dtos.responses.RoleResponseDto;
import com.project.brandprotection.enums.Module;
import com.project.brandprotection.exceptions.ResourceNotFoundException;
import com.project.brandprotection.models.BaseEntity;
import com.project.brandprotection.models.Permission;
import com.project.brandprotection.models.Role;
import com.project.brandprotection.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final ObjectMapperUtils objectMapperUtils;

    public RoleResponseDto mapToDto(Role role, List<Permission> permissions) {
        var roleResponseDto = objectMapperUtils.mapToEntityOrDto(role, RoleResponseDto.class);
        var permissionsResponse = objectMapperUtils.mapAll(role.getPermissions(), PermissionResponseDto.class);
        addDisabledPermissions(permissions, permissionsResponse);
        Map<Module, List<PermissionResponseDto>> groupedPermissions = permissionsResponse.stream().collect(Collectors.groupingBy(PermissionResponseDto::getModule));
        roleResponseDto.setGroupedPermissions(groupedPermissions);
        return roleResponseDto;
    }

    private void addDisabledPermissions(List<Permission> permissions, List<PermissionResponseDto> permissionsResponse) {
        var permissionRequestMap = permissionsResponse.stream().collect(Collectors.toMap(PermissionResponseDto::getId, Function.identity()));
        for (Permission permission : permissions) {
            if (permissionRequestMap.containsKey(permission.getId())) {
                continue;
            }
            var permissionResponse = objectMapperUtils.mapToEntityOrDto(permission, PermissionResponseDto.class);
            permissionResponse.setEnabled(false);
            permissionsResponse.add(permissionResponse);
        }
    }

    public Role mapToEntity(RoleRequestDto roleRequestDto, List<Permission> permissions) {
        Role role = Role.builder()
                .name(roleRequestDto.getName())
                .description(roleRequestDto.getDescription())
                .active(roleRequestDto.isActive())
                .permissions(new HashSet<>())
                .build();
        var permissionMap = permissions.stream().collect(Collectors.toMap(BaseEntity::getId, Function.identity()));
        for (var permissionRequestDto : roleRequestDto.getPermissions()) {
            if (!permissionRequestDto.isEnabled()) {
                continue;
            }
            Permission permission = Optional.ofNullable(permissionMap.get(permissionRequestDto.getId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Permission", permissionRequestDto.getId()));
            role.addPermission(permission);
        }
        return role;
    }
}
