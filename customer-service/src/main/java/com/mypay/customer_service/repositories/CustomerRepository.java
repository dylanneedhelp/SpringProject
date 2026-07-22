package com.mypay.customer_service.repositories;

import com.mypay.customer_service.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByAccountId(String accountId);
    boolean existsByPhoneNumber(String phoneNumber);
}