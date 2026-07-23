package com.mypay.customer_service.repositories;

import com.mypay.customer_service.entities.LinkedBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LinkedBankAccountRepository extends JpaRepository<LinkedBankAccount,String> {
    Optional<LinkedBankAccount> findByWalletId(String walletId);
    List<LinkedBankAccount> findAllByWalletId(String walletId);
    Optional<LinkedBankAccount> findByIdAndWalletId(String id, String walletId);
    boolean existsByWalletIdAndAccountNumber(String walletId, String accountNumber);
    Optional<LinkedBankAccount> findByWalletIdAndIsPrimaryTrue(String walletId);
}