package com.mypay.customer_service.controllers;


import com.mypay.customer_service.dtos.request.CustomerProfileRequest;
import com.mypay.customer_service.dtos.response.ApiResponse;
import com.mypay.customer_service.dtos.response.ProfileResponse;
import com.mypay.customer_service.services.CustomerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerProfileService customerProfileService;

    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<String>> resendOtp(Principal principal,
                                                         @RequestBody @Valid CustomerProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .data(customerProfileService.onboardCustomer(principal.getName(), request))
                .build());
    }
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(Principal principal){
        ProfileResponse users = customerProfileService.getCustomerByAccountId(principal.getName());
        ApiResponse<ProfileResponse> response= ApiResponse.<ProfileResponse>builder()
                .code(200)
                .message("Fetch profile successfully")
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }
}