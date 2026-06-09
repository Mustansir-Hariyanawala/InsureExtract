package com.InsureExtract.version1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity // Enables Spring Security web support
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF (Cross-Site Request Forgery) since REST APIs don't use session cookies
                .csrf(csrf -> csrf.disable())

                // 2. Disable HTTP Basic and Form Login
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())

                // 3. Set up the URL authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Allow anyone to access the registration endpoint completely unauthenticated
                        .requestMatchers("/api/v1/users/register").permitAll()
                        // (Optional) If you build a login/auth endpoint later, you'd permit it here too:
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // Any other endpoint (like fetching all users, deleting, etc.) requires authentication
                        .anyRequest().authenticated()
                )

                // 4. Make the session stateless (required for JWT architecture)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 5. Add the JWT filter before the UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}