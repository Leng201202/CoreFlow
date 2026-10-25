package com.coreflow.banking.auth.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.coreflow.banking.auth.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name="user_accounts")
@AllArgsConstructor 
@NoArgsConstructor 
@Getter 
@Setter 
public class UserAccount {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name="uuid",nullable= false,unique = true,updatable = false)
    private UUID uuid;

    @Column (name="email",nullable = false,unique = true,length = 255)
    private String email;

    @Column (name="password_hash",nullable = false)
    private String passwordHashed;

    @Enumerated (EnumType.STRING)
    @Column (name="status",nullable = false,length = 30)
    private UserStatus status;

    @Column (name="customer_id",unique = true)
    private Long customerId;

    @Column (name="last_login_at")
    private OffsetDateTime lastLoginAt;

    @PrePersist 
    protected void onCreate(){
        if(uuid==null){
            uuid=UUID.randomUUID();
        }
    }
}
