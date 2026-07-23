package com.mypay.loan_service.repositories;

import com.mypay.loan_service.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan,String> {
}
