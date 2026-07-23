package com.mypay.loan_service.controllers;

import com.mypay.loan_service.dtos.request.AppraisalRequest;
import com.mypay.loan_service.dtos.request.DecisionRequest;
import com.mypay.loan_service.dtos.response.ApiResponse;
import com.mypay.loan_service.services.AdminLoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/admin/loans")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminLoanController {

    private final AdminLoanService adminLoanService;

    @PostMapping("/applications/{id}/appraise")
    public ResponseEntity<ApiResponse<String>> appraiseLoan(
            Principal principal,
            @PathVariable String id,
            @RequestBody @Valid AppraisalRequest request) {

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(adminLoanService.appraiseLoan(id,principal.getName(), request))
                .build());
    }

    @PutMapping("/applications/{id}/decision")
    public ResponseEntity<ApiResponse<String>> decideLoan(
            @PathVariable String id,
            @RequestBody @Valid DecisionRequest request) {

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(adminLoanService.decideLoan(id, request))
                .build());
    }

    @PostMapping("/{loanId}/disburse")
    public ResponseEntity<ApiResponse<String>> disburseLoan(@PathVariable String loanId) {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(adminLoanService.disburseLoan(loanId))
                .build());
    }
}