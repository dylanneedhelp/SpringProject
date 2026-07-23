package com.mypay.customer_service.controllers;

import com.mypay.customer_service.dtos.response.ApiResponse;
import com.mypay.customer_service.entities.Wallet;
import com.mypay.customer_service.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/internal")
@RequiredArgsConstructor
public class InternalWalletController {

    private final WalletService walletService;

    // 1. Lấy thông tin ví và số dư theo customerId
    @GetMapping("/wallets/customer/{customerId}")
    public ResponseEntity<ApiResponse<Wallet>> getWalletByCustomer(@PathVariable String customerId) {
        Wallet wallet = walletService.getWalletByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponse.<Wallet>builder()
                .code(1000)
                .message("Success")
                .data(wallet)
                .build());
    }

    // 2. Lấy thông tin ví theo accountId
    @GetMapping("/wallets/account/{accountId}")
    public ResponseEntity<ApiResponse<Wallet>> getWalletByAccount(@PathVariable String accountId) {
        Wallet wallet = walletService.getWalletByAccountIdInternal(accountId);
        return ResponseEntity.ok(ApiResponse.<Wallet>builder()
                .code(1000)
                .message("Success")
                .data(wallet)
                .build());
    }

    // 3. Trừ tiền ví (Nhận body chứa amount)
    @PostMapping("/wallets/{walletId}/deduct")
    public ResponseEntity<ApiResponse<String>> deductBalance(
            @PathVariable String walletId,
            @RequestBody Map<String, BigDecimal> request) {
        BigDecimal amount = request.get("amount");
        String result = walletService.deductBalance(walletId, amount);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(result)
                .build());
    }

    // 4. Cộng tiền ví (Nhận body chứa amount)
    @PostMapping("/wallets/{walletId}/add")
    public ResponseEntity<ApiResponse<String>> addBalance(
            @PathVariable String walletId,
            @RequestBody Map<String, BigDecimal> request) {
        BigDecimal amount = request.get("amount");
        String result = walletService.addBalance(walletId, amount);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(result)
                .build());
    }

    // 5. Đóng băng số dư ví
    @PostMapping("/wallets/{walletId}/freeze")
    public ResponseEntity<ApiResponse<String>> freezeBalance(
            @PathVariable String walletId,
            @RequestBody Map<String, BigDecimal> request) {
        BigDecimal amount = request.get("amount");
        String result = walletService.freezeBalance(walletId, amount);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(result)
                .build());
    }
}