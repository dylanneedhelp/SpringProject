package com.mypay.customer_service.repositories;

import com.mypay.customer_service.entities.LinkedBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkedBankAccountRepository extends JpaRepository<LinkedBankAccount,String> {
    Optional<LinkedBankAccount> findByWalletId(String walletId);
}
