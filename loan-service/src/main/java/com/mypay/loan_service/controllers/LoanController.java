package com.mypay.loan_service.controllers;

import com.mypay.loan_service.dtos.request.LoanApplyRequest;
import com.mypay.loan_service.dtos.response.ApiResponse;
import com.mypay.loan_service.entities.LoanApplication;
import com.mypay.loan_service.services.LoanApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanApplicationService loanApplicationService;

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<String>> applyForLoan(Principal principal, @RequestBody @Valid LoanApplyRequest request) {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(loanApplicationService.applyForLoan(principal.getName(), request))
                .build());
    }

    @GetMapping("/my-applications")
    public ResponseEntity<ApiResponse<List<LoanApplication>>> getMyApplications(Principal principal) {
        return ResponseEntity.ok(ApiResponse.<List<LoanApplication>>builder()
                .code(1000)
                .message("Success")
                .data(loanApplicationService.getMyApplications(principal.getName()))
                .build());
    }
}