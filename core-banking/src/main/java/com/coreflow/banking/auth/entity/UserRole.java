package com.coreflow.banking.auth.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity 
@Table (name="user_roles")
@Getter 
public class UserRole {

    @EmbeddedId 
    private UserRoleId id;

    @MapsId ("userId")
    @ManyToOne (fetch = FetchType.LAZY,optional = false)
    @JoinColumn (name="user_id",nullable = false)
    private UserAccount user;
    
    @MapsId ("roleId") 
    @ManyToOne (fetch = FetchType.LAZY,optional = false)
    @JoinColumn (name="role_id",nullable = false)
    private Role role;

    protected UserRole(){

    }
    public UserRole(UserAccount user,Role role){
        if(user==null || role== null) throw new IllegalArgumentException("User and Role must not be null");
        if(user.getId()==null || role.getId()==null) throw new IllegalArgumentException("User and Role must be persisted before assigning a role");
        this.user=user;
        this.role=role;

        this.id=new UserRoleId(user.getId(),role.getId());
    }

}
