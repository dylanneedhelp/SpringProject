package com.mypay.loan_service.services;

import com.mypay.loan_service.Exception.AppException;
import com.mypay.loan_service.Exception.ErrorCode;
import com.mypay.loan_service.dtos.request.LoanApplyRequest;
import com.mypay.loan_service.entities.LoanApplication;
import com.mypay.loan_service.repositories.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;

    @Transactional
    public String applyForLoan(String accountId, LoanApplyRequest request) {
        LoanApplication application = LoanApplication.builder()
                .accountId(accountId)
                .requestedAmount(request.getRequestedAmount())
                .termMonths(request.getTermMonths())
                .purpose(request.getPurpose())
                .status("PENDING")
                .updatedAt(LocalDateTime.now())
                .build();

        loanApplicationRepository.save(application);
        return "Nộp hồ sơ vay vốn thành công! Hệ thống đang tiến hành thẩm định.";
    }

    public List<LoanApplication> getMyApplications(String accountId) {
        return loanApplicationRepository.findAllByAccountId(accountId);
    }
}