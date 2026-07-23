package com.mypay.loan_service.repositories;

import com.mypay.loan_service.entities.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, String> {
    List<LoanApplication> findAllByAccountId(String accountId);
}
