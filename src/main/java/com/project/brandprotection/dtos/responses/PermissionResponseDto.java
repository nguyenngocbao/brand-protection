package com.project.brandprotection.dtos.responses;

import com.project.brandprotection.enums.HttpMethods;
import com.project.brandprotection.enums.Module;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionResponseDto {
    private Long id;
    private String name;
    private String path;
    private HttpMethods method;
    private Module module;
    private boolean enabled = true;
}
