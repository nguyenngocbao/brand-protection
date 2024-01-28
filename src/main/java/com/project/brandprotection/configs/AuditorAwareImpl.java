package com.project.brandprotection.configs;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return Optional.of("");
        }

        if (auth.getPrincipal() instanceof String principal) {
            return Optional.of(principal);
        }

        UserDetails currentAuditor = (UserDetails) auth.getPrincipal();
        return Optional.ofNullable(currentAuditor.getUsername());
    }

}
