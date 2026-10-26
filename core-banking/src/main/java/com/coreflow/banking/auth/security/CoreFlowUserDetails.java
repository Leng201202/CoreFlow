package com.coreflow.banking.auth.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.coreflow.banking.auth.enums.UserStatus;


public class CoreFlowUserDetails implements UserDetails{

    private final Long userId;

    private final String email;

    private final String passwordHash;

    private final List<GrantedAuthority> authorities;

    private final UserStatus status;

    public CoreFlowUserDetails(
        Long userId,
        String email,
        String passwordHash,
        List<? extends GrantedAuthority> authorities,
        UserStatus status
    ){
        this.userId=userId;
        this.email=email;
        this.passwordHash=passwordHash;
        this.authorities=List.copyOf(authorities);
        this.status=status;
    }

    public Long getUserId(){
        return userId;
    }
    @Override 
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }
    @Override 
    public String getPassword(){
        return passwordHash;
    }
    @Override 
    public String getUsername(){
        return email;
    }
    public boolean isAccountLocked(){
        return status!=UserStatus.LOCKED;
    }

    @Override 
    public boolean isEnabled(){
        return status==UserStatus.ACTIVE;
    }





    
    
}
