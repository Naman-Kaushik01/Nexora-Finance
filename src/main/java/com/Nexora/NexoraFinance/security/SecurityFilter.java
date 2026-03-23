package com.Nexora.NexoraFinance.security;

import com.Nexora.NexoraFinance.exceptions.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityFilter {
    private final AuthFilter authFilter;
    private final  CustomAccessDenialHandler accessDenialHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
}
