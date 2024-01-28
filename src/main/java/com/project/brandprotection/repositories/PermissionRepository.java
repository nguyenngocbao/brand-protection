package com.project.brandprotection.repositories;

import com.project.brandprotection.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
