package com.smart.procurement1.config;

import com.smart.procurement1.Service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ✅ VERY IMPORTANT
                .cors(Customizer.withDefaults())

                .csrf(csrf -> csrf.disable())

                .userDetailsService(userDetailsService)

                .authorizeHttpRequests(auth -> auth
                        // ✅ ALLOW PREFLIGHT REQUESTS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public APIs
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/chat/**").permitAll()
                        .requestMatchers("/api/portal/**").permitAll()

                        // Role-based APIs
                        .requestMatchers("/api/vendor/**").hasAuthority("ROLE_VENDOR")
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")

                        .anyRequest().authenticated()
                )

                // ✅ BASIC AUTH (since JWT removed)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
