package com.mypay.customer_service.controllers;

import com.mypay.customer_service.dtos.request.BankLinkRequest;
import com.mypay.customer_service.dtos.response.ApiResponse;
import com.mypay.customer_service.entities.LinkedBankAccount;
import com.mypay.customer_service.entities.Wallet;
import com.mypay.customer_service.services.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/my-wallet")
    public ResponseEntity<ApiResponse<Wallet>> getMyWallet(Principal principal) {
        return ResponseEntity.ok(ApiResponse.<Wallet>builder()
                .code(1000)
                .message("Success")
                .data(walletService.getWalletInfo(principal.getName()))
                .build());
    }

    // 2. Khóa hoặc Mở khóa ví khẩn cấp (status: ACTIVE hoặc BLOCKED)
    @PutMapping("/status")
    public ResponseEntity<ApiResponse<String>> updateWalletStatus(Principal principal, @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(walletService.updateWalletStatus(principal.getName(), status))
                .build());
    }

    // 3. Lấy danh sách ngân hàng liên kết
    @GetMapping("/banks")
    public ResponseEntity<ApiResponse<List<LinkedBankAccount>>> getLinkedBanks(Principal principal) {
        return ResponseEntity.ok(ApiResponse.<List<LinkedBankAccount>>builder()
                .code(1000)
                .message("Success")
                .data(walletService.getLinkedBanks(principal.getName()))
                .build());
    }

    // 4. Thêm mới tài khoản ngân hàng liên kết
    @PostMapping("/banks")
    public ResponseEntity<ApiResponse<String>> linkBank(Principal principal, @RequestBody @Valid BankLinkRequest request) {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(walletService.linkBankAccount(principal.getName(), request))
                .build());
    }


    @PutMapping("/banks/{id}/primary")
    public ResponseEntity<ApiResponse<String>> setPrimaryBank(Principal principal, @PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(walletService.setPrimaryBank(principal.getName(), id))
                .build());
    }

    // 6. Hủy liên kết ngân hàng
    @DeleteMapping("/banks/{id}")
    public ResponseEntity<ApiResponse<String>> deleteLinkedBank(Principal principal, @PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data(walletService.deleteLinkedBank(principal.getName(), id))
                .build());
    }
}