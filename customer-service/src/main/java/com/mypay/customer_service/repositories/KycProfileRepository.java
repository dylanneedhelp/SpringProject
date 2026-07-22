package com.mypay.customer_service.repositories;

import com.mypay.customer_service.entities.KycProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KycProfileRepository extends JpaRepository<KycProfile,String> {
    Optional<KycProfile> findByCustomerId(String customerId);
}
