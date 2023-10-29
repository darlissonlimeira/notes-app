package com.br.notesapp.notesappserver.security;

import com.br.notesapp.notesappserver.security.filters.BearerAuthorizationFilter;
import com.br.notesapp.notesappserver.security.filters.JWTAuthenticationFilter;
import com.br.notesapp.notesappserver.security.filters.RefreshJWTAuthenticationFilter;
import com.br.notesapp.notesappserver.security.handlers.ApiLogoutHandler;
import com.br.notesapp.notesappserver.security.handlers.ApiLogoutSuccessHandler;
import com.br.notesapp.notesappserver.security.jwt.BearerTokenResolver;
import com.br.notesapp.notesappserver.security.jwt.JWTProvider;
import com.br.notesapp.notesappserver.security.providers.RefreshJWTAuthenticationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AccountDetailsService userDetailsService;

    private final ApiLogoutHandler logoutHandler;

    private final ApiLogoutSuccessHandler logoutSuccessHandler;

    @Value("${REFRESH_TOKEN_SECRET}")
    private String REFRESH_TOKEN_SECRET;

    @Value("${ACCESS_TOKEN_SECRET}")
    private String ACCESS_TOKEN_SECRET;


    public SecurityConfig(AccountDetailsService userDetailsService, ApiLogoutHandler apiLogoutHandler, ApiLogoutSuccessHandler logoutSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.logoutHandler = apiLogoutHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    @Bean
    public SecurityFilterChain configuration(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        http.httpBasic().disable();
        http.authorizeHttpRequests().requestMatchers("/api/**").authenticated();
        http.addFilter(jwtAuthenticationFilter());
        http.addFilter(jwtRefreshAuthenticationFilter());
        http.addFilterAfter(authorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.logout()
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring().requestMatchers("/swagger-ui/**", "/v3/api-docs/**");
        };
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(jwtAuthenticationProvider(), refreshJWTAuthenticationProvider());
    }


    public DaoAuthenticationProvider jwtAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    public RefreshJWTAuthenticationProvider refreshJWTAuthenticationProvider() {
        JWTProvider jwtProvider = new JWTProvider(ACCESS_TOKEN_SECRET, REFRESH_TOKEN_SECRET);
        return new RefreshJWTAuthenticationProvider(userDetailsService, jwtProvider);
    }

    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        JWTProvider jwtProvider = new JWTProvider(ACCESS_TOKEN_SECRET, REFRESH_TOKEN_SECRET);
        return new JWTAuthenticationFilter(authenticationManager(), jwtProvider);
    }

    public RefreshJWTAuthenticationFilter jwtRefreshAuthenticationFilter() {
        JWTProvider jwtProvider = new JWTProvider(ACCESS_TOKEN_SECRET, REFRESH_TOKEN_SECRET);
        return new RefreshJWTAuthenticationFilter(authenticationManager(), jwtProvider);
    }

    public BearerAuthorizationFilter authorizationFilter() {
        BearerTokenResolver bearerTokenResolver = new BearerTokenResolver();
        JWTProvider jwtProvider = new JWTProvider(ACCESS_TOKEN_SECRET, REFRESH_TOKEN_SECRET);
        return new BearerAuthorizationFilter(bearerTokenResolver, jwtProvider, authenticationManager());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
