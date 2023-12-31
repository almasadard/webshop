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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.fairgoods.webshop.security.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures web security settings for the application.
 *
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenService tokenService;

    /**
     * Provides a password encoder bean.
     *
     * @return PasswordEncoder object to be used in the application.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures security filter chain.
     *
     * @param httpSecurity to modify.
     * @return SecurityFilterChain object that has been modified.
     * @throws Exception in case of any errors.
     */
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
                .requestMatchers(HttpMethod.POST, "/product", "/files").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/product").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/user/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/user").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/user/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/isAdmin").hasRole("ADMIN")
                //Role
                .requestMatchers("/login", "/login/**","/user", "/user/**", "/product/**", "/files/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new AuthenticationFilter(tokenService),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}