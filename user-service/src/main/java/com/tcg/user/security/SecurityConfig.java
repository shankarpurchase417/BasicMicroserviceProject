package com.tcg.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 🔑 CSRF disable for REST APIs
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Actuator endpoints ke liye authentication required
                .requestMatchers("/actuator/**").hasRole("ACTUATOR")
                // Baaki sabhi endpoints (jaise /users) free access
                .anyRequest().permitAll()
            )
            .httpBasic(); // Basic Auth sirf actuator ke liye
        return http.build();
    }

    // In-memory user for Actuator
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails actuatorUser = User.withUsername("actuator")
                .password("act@123")
                .roles("ACTUATOR")
                .build();
        return new InMemoryUserDetailsManager(actuatorUser);
    }

    // Password encoder (no-op, sirf demo ke liye)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}

