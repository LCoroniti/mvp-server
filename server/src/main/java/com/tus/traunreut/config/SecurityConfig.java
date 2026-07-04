package com.tus.traunreut.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Protects the admin area and all data-mutating endpoints with HTTP Basic auth.
 * Public remain: the voting flow (/api/vote*), all read-only match/league/team
 * queries and the static React app. Navigating to /admin/* triggers the browser
 * login dialog; the browser then attaches the credentials to the admin page's
 * API calls automatically, so the React admin pages work unchanged.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // No session-based login and no cookies carrying auth state, so CSRF
                // protection would only break the public voting POSTs.
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**", "/api/scheduler/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/team/**", "/api/league/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/team/**", "/api/league/**", "/api/match/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/team/**").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(
            @Value("${app.admin.username}") String username,
            @Value("${app.admin.password}") String password,
            PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername(username)
                        .password(passwordEncoder.encode(password))
                        .roles("ADMIN")
                        .build());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
