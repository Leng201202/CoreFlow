package com.coreflow.banking.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity 
@Table (name="user_roles")
@IdClass(UserRole.class)
@NoArgsConstructor 
@AllArgsConstructor 
public class UserRole {

    @Id 
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="user_id",nullable = false)
    private UserAccount user;
    
    @Id 
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="role_id",nullable = false)
    private Role role;


}
