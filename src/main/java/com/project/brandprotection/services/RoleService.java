package com.project.brandprotection.services;

import com.project.brandprotection.dtos.requests.RoleRequestDto;
import com.project.brandprotection.dtos.responses.PermissionResponseDto;
import com.project.brandprotection.dtos.responses.RoleResponseDto;
import com.project.brandprotection.enums.Module;

import java.util.List;
import java.util.Map;

public interface RoleService {
    List<RoleResponseDto> getRoles();

    RoleResponseDto getRoleById(Long id);

    RoleResponseDto updateRoleById(Long id, RoleRequestDto roleRequestDto);

    RoleResponseDto createRole(RoleRequestDto roleRequestDto);

    void deleteRoleById(Long id);

    Map<Module, List<PermissionResponseDto>> getModulePermissionMap();
}
