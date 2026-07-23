package com.mypay.loan_service.repositories;

import com.mypay.loan_service.entities.RepaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule,String> {
}
