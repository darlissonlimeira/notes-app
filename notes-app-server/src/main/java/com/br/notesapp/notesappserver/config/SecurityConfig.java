package com.br.notesapp.notesappserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final BearerAuthenticationFilter bearerAuthenticationFilter;

    public SecurityConfig(BearerAuthenticationFilter bearerAuthenticationFilter) {
        this.bearerAuthenticationFilter = bearerAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain configuration(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/api/auth/**").permitAll();
            authorize.requestMatchers(HttpMethod.POST, "/api/notes/**").hasAuthority("EMPLOYEE");
            authorize.requestMatchers(HttpMethod.GET, "/api/notes/**").hasAnyAuthority("EMPLOYEE", "ADMIN", "MANAGER");
            authorize.requestMatchers(HttpMethod.PUT, "/api/notes/**").hasAnyAuthority("EMPLOYEE", "ADMIN", "MANAGER");
            authorize.requestMatchers(HttpMethod.DELETE, "/api/notes/**").hasAnyAuthority("ADMIN", "MANAGER");
            authorize.requestMatchers("/api/users/**").hasAnyAuthority("ADMIN", "MANAGER");
        });

        http.addFilterBefore(bearerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring().requestMatchers("/swagger-ui/**", "/v3/api-docs/**");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
