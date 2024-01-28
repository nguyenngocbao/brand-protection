package com.project.brandprotection.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role extends BaseEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();

    public static String ADMIN = "ADMIN";
    public static String USER = "USER";

    public void addPermission(Permission permission) {
        if (CollectionUtils.isEmpty(this.permissions)) {
            this.permissions = new HashSet<>();
        }
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);
            permission.getRoles().add(this);
        }
    }

    public void removePermission(Permission permission) {
        if (CollectionUtils.isEmpty(this.permissions)) {
            return;
        }
        if (this.permissions.contains(permission)) {
            this.permissions.remove(permission);
            permission.getRoles().remove(this);
        }
    }

}
