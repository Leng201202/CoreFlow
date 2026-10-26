package com.coreflow.banking.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.coreflow.banking.auth.services.CoreFlowUserDetailsService;

@Configuration 
@EnableMethodSecurity 
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http
        .csrf(csrf->csrf.disable())
        .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth->auth
            .requestMatchers(
                "api/v1/auth/login",
                "api/v1/auth/register"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form->form.disable())
        .httpBasic(basic->basic.disable());
        
        return http.build();
    }

    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
        CoreFlowUserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean 
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider provider){
        return new ProviderManager(provider);
    }
}
