package com.coreflow.banking.auth.services;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coreflow.banking.auth.entity.UserAccount;
import com.coreflow.banking.auth.entity.UserRole;
import com.coreflow.banking.auth.repository.UserAccountRepository;
import com.coreflow.banking.auth.repository.UserRoleRepository;
import com.coreflow.banking.auth.security.CoreFlowUserDetails;
import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class CoreFlowUserDetailsService implements UserDetailsService{

    private final UserAccountRepository userAccountRepository;
    private final UserRoleRepository userRoleRepository;


    @Override 
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException{
        UserAccount user=userAccountRepository
                .findByEmail(email)
                .orElseThrow(()->
                    new UsernameNotFoundException("Invalid Credential")
            );
        List<UserRole> userRoles=userRoleRepository.findByUser_Id(user.getId());

        List<SimpleGrantedAuthority> authorities=userRoles.stream()
            .map(userRole -> new SimpleGrantedAuthority("ROLE_"+userRole.getRole().getName()))
            .toList();

        return new CoreFlowUserDetails(
            user.getId(),
            user.getEmail(),
            user.getPasswordHashed(),
            authorities,
            user.getStatus()
            );
    }
    
}
