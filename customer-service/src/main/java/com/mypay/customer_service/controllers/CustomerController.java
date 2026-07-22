package com.mypay.customer_service.controllers;


import com.mypay.customer_service.dtos.request.CustomerProfileRequest;
import com.mypay.customer_service.dtos.response.ApiResponse;
import com.mypay.customer_service.dtos.response.CustomerResponse;
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
    public ResponseEntity<ApiResponse<String>> createProfileAndWallet(Principal principal,
                                                         @RequestBody @Valid CustomerProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(customerProfileService.onboardCustomer(principal.getName(), request))
                .build());
    }
    @GetMapping("/my-all-profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(Principal principal){
        ProfileResponse users = customerProfileService.getProfileByAccountId(principal.getName());
        ApiResponse<ProfileResponse> response= ApiResponse.<ProfileResponse>builder()
                .code(1000)
                .message("Fetch all profile successfully")
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/customer-profile")
    public ResponseEntity<ApiResponse<CustomerResponse>> getMyCustomerProfile(Principal principal){
        CustomerResponse customer = customerProfileService.getCustomerByAccountId(principal.getName());
        if(customer ==null){
            return ResponseEntity.ok(ApiResponse.<CustomerResponse>builder()
                    .code(1001)
                    .message("The profile has not been created")
                    .data(customer)
                    .build());
        }
        return ResponseEntity.ok(ApiResponse.<CustomerResponse>builder()
                .code(1000)
                .message("Fetch customer profile successfully")
                .data(customer)
                .build());
    }
}