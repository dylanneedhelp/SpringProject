package com.mypay.customer_service.controllers;

import com.mypay.customer_service.dtos.request.KycRequest;
import com.mypay.customer_service.dtos.response.ApiResponse;
import com.mypay.customer_service.entities.KycProfile;
import com.mypay.customer_service.services.KycService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class KycController {

    private final KycService kycService;


    @PostMapping("/customers/kyc")
    public ResponseEntity<ApiResponse<String>> submitKyc(Principal principal, @RequestBody @Valid KycRequest request) {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(kycService.submitKyc(principal.getName(), request))
                .build());
    }

    @GetMapping("/customers/kyc")
    public ResponseEntity<ApiResponse<KycProfile>> getMyKyc(Principal principal) {
        return ResponseEntity.ok(ApiResponse.<KycProfile>builder()
                .code(1000)
                .message("Success")
                .data(kycService.getMyKycStatus(principal.getName()))
                .build());
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/kyc/{kycId}/verify")
    public ResponseEntity<ApiResponse<String>> verifyKyc(
            @PathVariable String kycId,
            @RequestParam boolean isApproved,
            @RequestParam(required = false) String rejectionReason) {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(kycService.verifyKyc(kycId, isApproved, rejectionReason))
                .build());
    }
}