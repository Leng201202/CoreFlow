package com.coreflow.banking.auth.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service 
public class AuthService {
    private final AuthenticationManager authenticationManager;
    
    public AuthService(AuthenticationManager authenticationManager){
        this.authenticationManager=authenticationManager;
    }

    public Authentication authenticate(String email, String password){
        UsernamePasswordAuthenticationToken authenticationRequest=
                UsernamePasswordAuthenticationToken.unauthenticated(email, password);
        return authenticationManager.authenticate(authenticationRequest);
    }
}
