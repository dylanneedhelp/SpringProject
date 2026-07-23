package com.mypay.loan_service.repositories;

import com.mypay.loan_service.entities.ValuationReport;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;

public interface ValuationReportRepository extends JpaRepository<ValuationReport,String> {
    ValuationReport findByLoanApplicationId(String applicationId);
}
