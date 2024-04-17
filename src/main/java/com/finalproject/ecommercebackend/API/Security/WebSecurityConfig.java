package com.finalproject.ecommercebackend.API.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable().cors().disable();
        http.authorizeHttpRequests().anyRequest().permitAll();
        return http.build();
    }
}