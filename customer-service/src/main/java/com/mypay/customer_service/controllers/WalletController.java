package com.mypay.customer_service.controllers;

import com.mypay.customer_service.dtos.request.BankLinkRequest;
import com.mypay.customer_service.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/my-wallet")
    public ResponseEntity<?> getMyWallet(Principal principal) {
        String accountId = principal.getName();
        return ResponseEntity.ok(walletService.getWalletInfo(accountId));
    }

    @PostMapping("/link-bank")
    public ResponseEntity<?> linkBank(Principal principal, @RequestBody BankLinkRequest request) {
        String accountId = principal.getName();
        return ResponseEntity.ok(walletService.linkBankAccount(accountId, request));
    }
}