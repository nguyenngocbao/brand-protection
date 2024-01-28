package com.project.brandprotection.models;

import com.project.brandprotection.enums.HttpMethods;
import com.project.brandprotection.enums.Module;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Permission extends BaseEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "method", nullable = false)
    @Enumerated(EnumType.STRING)
    private HttpMethods method;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "module")
    @Enumerated(EnumType.STRING)
    private Module module;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();

}
