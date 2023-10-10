package com.fairgoods.webshop.config;

import com.fairgoods.webshop.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import com.fairgoods.webshop.security.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //Admin
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.DELETE, "/product/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/product").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/product").hasRole("ADMIN")
                //Role
                .requestMatchers("/login", "/login/**","/user", "/user/**", "/product").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new AuthenticationFilter(tokenService),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}