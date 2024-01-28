package com.project.brandprotection.services.impl;

import com.project.brandprotection.dtos.requests.RoleRequestDto;
import com.project.brandprotection.dtos.responses.PermissionResponseDto;
import com.project.brandprotection.dtos.responses.RoleResponseDto;
import com.project.brandprotection.enums.Module;
import com.project.brandprotection.exceptions.ResourceNotFoundException;
import com.project.brandprotection.mappers.RoleMapper;
import com.project.brandprotection.models.Permission;
import com.project.brandprotection.models.Role;
import com.project.brandprotection.repositories.PermissionRepository;
import com.project.brandprotection.repositories.RoleRepository;
import com.project.brandprotection.services.RoleService;
import com.project.brandprotection.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;
    private final ObjectMapperUtils objectMapperUtils;

    @Override
    public List<RoleResponseDto> getRoles() {
        List<Role> roles = roleRepository.findAll();
        List<Permission> permissions = getAllPermissions();
        return roles.stream().map(p -> roleMapper.mapToDto(p, permissions)).collect(Collectors.toList());
    }

    @Override
    public RoleResponseDto getRoleById(Long id) {
        Role role = getRole(id);
        List<Permission> permissions = getAllPermissions();
        return roleMapper.mapToDto(role, permissions);
    }

    @Override
    public RoleResponseDto updateRoleById(Long id, RoleRequestDto roleRequestDto) {
        Role role = getRole(id);
        role.setName(roleRequestDto.getName());
        role.setDescription(roleRequestDto.getDescription());
        role.setActive(roleRequestDto.isActive());
        updateRolePermissions(roleRequestDto, role);
        Role updatedRole = roleRepository.save(role);
        return roleMapper.mapToDto(updatedRole, getAllPermissions());
    }

    @Override
    public RoleResponseDto createRole(RoleRequestDto roleRequestDto) {
        Role role = roleMapper.mapToEntity(roleRequestDto, getAllPermissions());
        return roleMapper.mapToDto(roleRepository.save(role), getAllPermissions());
    }

    @Override
    public void deleteRoleById(Long id) {
        Role role = getRole(id);
        roleRepository.delete(role);
    }

    @Override
    public Map<Module, List<PermissionResponseDto>> getModulePermissionMap() {
        var permissions = getAllPermissions();
        var permissionsResponse = objectMapperUtils.mapAll(permissions, PermissionResponseDto.class);
        permissionsResponse.forEach(p -> p.setEnabled(false));
        return permissionsResponse.stream().collect(Collectors.groupingBy(PermissionResponseDto::getModule));
    }

    private void updateRolePermissions(RoleRequestDto roleRequestDto, Role role) {
        for (var permissionRequestDto : roleRequestDto.getPermissions()) {
            Permission permission = permissionRepository.findById(permissionRequestDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Permission", permissionRequestDto.getId()));
            if (permissionRequestDto.isEnabled()) {
                role.addPermission(permission);
                continue;
            }
            role.removePermission(permission);
        }
    }

    private Role getRole(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", id));
    }

    private List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }


}
