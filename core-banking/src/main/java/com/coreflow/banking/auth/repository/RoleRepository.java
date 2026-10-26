package com.coreflow.banking.auth.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coreflow.banking.auth.entity.Role;
import com.coreflow.banking.auth.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role,Long>{
    Optional<Role> findByName(RoleName roleName);
}
