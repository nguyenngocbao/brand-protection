package com.project.brandprotection.configs;

import com.project.brandprotection.filters.CustomAuthorizationFilter;
import com.project.brandprotection.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAuthorizationFilter customAuthorizationFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(customAuthorizationFilter, JwtTokenFilter.class)
                .cors(httpSecurityCorsConfigurer -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("*"));
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                    configuration.setExposedHeaders(List.of("x-auth-token"));
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", configuration);
                    httpSecurityCorsConfigurer.configurationSource(source);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(
                                        String.format("%s/auth/**", apiPrefix),
                                        String.format("%s/healthcheck/**", apiPrefix),
                                        "/api-docs",
                                        "/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/webjars/swagger-ui/**",
                                        "/swagger-ui/index.html"

                                )
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        String.format("%s/roles/**", apiPrefix)).permitAll()

                                .requestMatchers(HttpMethod.GET,
                                        String.format("%s/categories/**", apiPrefix)).permitAll()

                                .requestMatchers(HttpMethod.GET,
                                        String.format("%s/products/**", apiPrefix)).permitAll()

                                .requestMatchers(HttpMethod.GET,
                                        String.format("%s/products/images/*", apiPrefix)).permitAll()

                                .requestMatchers(HttpMethod.GET,
                                        String.format("%s/orders/**", apiPrefix)).permitAll()

                                .requestMatchers(HttpMethod.GET,
                                        String.format("%s/order_details/**", apiPrefix)).permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        String.format("%s/coupons/**", apiPrefix)).permitAll()

                                .anyRequest()
                                .authenticated())
                .build();
    }

}
