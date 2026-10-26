package com.coreflow.banking.auth.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Embeddable 
@Getter 
@AllArgsConstructor 
public class UserRoleId implements Serializable{
    @Column(name="user_id")
    private Long userId;
    
    @Column (name="role_id")
    private Long roleId;

    protected UserRoleId(){

    }
    @Override
    public boolean equals(Object object){
        if(this==object){
            return true;
        }
        if(!(object instanceof UserRoleId other)){
            return false;
        }
        return Objects.equals(userId, other.userId) && Objects.equals(roleId, other.roleId);
    }

    @Override 
    public int hashCode(){
        return Objects.hash(userId,roleId);
    }
}
