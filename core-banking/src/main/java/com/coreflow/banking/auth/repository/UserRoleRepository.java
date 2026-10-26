package com.coreflow.banking.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.coreflow.banking.auth.entity.UserRole;
import com.coreflow.banking.auth.entity.UserRoleId;

public interface UserRoleRepository extends JpaRepository<UserRole,UserRoleId>{
    boolean existsByUser_IdAndRole_Id(Long userId,Long roleId);

    @EntityGraph (attributePaths = "roles")
    List<UserRole> findByUser_Id(Long userId);
    
}
