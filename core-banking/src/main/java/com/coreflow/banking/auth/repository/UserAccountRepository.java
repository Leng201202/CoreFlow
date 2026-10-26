package com.coreflow.banking.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coreflow.banking.auth.entity.UserAccount;


public interface UserAccountRepository extends JpaRepository<UserAccount,Long>{
    Optional<UserAccount> findByEmail(String email);
    boolean exiexistsByEmail(String email);

}
