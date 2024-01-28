package com.project.brandprotection.repositories;

import com.project.brandprotection.models.Permission;
import com.project.brandprotection.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT p FROM User u JOIN u.role r JOIN r.permissions p WHERE u.email = :email")
    List<Permission> findAllPermissions(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.role r LEFT JOIN FETCH r.permissions WHERE u.email = :email")
    Optional<User> findUserWithRoleAndPermissions(String email);

}
