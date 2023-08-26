package com.leonovalexprog.gatewayentry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .mvcMatchers("/admin").hasRole("ROLE_ADMIN")
                                .mvcMatchers("/users").hasRole("ROLE_PRIVATE")
                                .anyRequest().permitAll()
                )
                .formLogin(Customizer.withDefaults())
                .headers().frameOptions().disable();

        return http.build();
    }
}
