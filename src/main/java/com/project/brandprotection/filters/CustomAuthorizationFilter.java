package com.project.brandprotection.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.brandprotection.dtos.responses.ApiErrorResponse;
import com.project.brandprotection.models.Permission;
import com.project.brandprotection.models.User;
import com.project.brandprotection.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (checkPermission(request, authentication)) {
            filterChain.doFilter(request, response);
            return;
        }
        String errMsg = "You don't have permission to access the resource!";
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpServletResponse.SC_FORBIDDEN, errMsg);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(toJson(errorResponse));
    }

    private boolean checkPermission(HttpServletRequest request, Authentication authentication) {
        String path = request.getServletPath();
        String method = request.getMethod();
        if (authentication.getPrincipal() instanceof User user) {
            List<Permission> permissions = userRepository.findAllPermissions(user.getEmail());
            return permissions.stream().anyMatch(permission -> method.equals(permission.getMethod().name()) && matcher.match(permission.getPath(), path));
        }
        return false;
    }

    private String toJson(ApiErrorResponse response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return ""; // Return an empty string if serialization fails
        }
    }

}
