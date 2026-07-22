package com.mypay.customer_service.repositories;


import com.mypay.customer_service.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, String> {
    Optional<Wallet> findByCustomerId(String customerId);
}