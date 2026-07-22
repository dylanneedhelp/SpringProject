package com.mypay.customer_service.services;

import com.mypay.customer_service.Exception.AppException;
import com.mypay.customer_service.Exception.ErrorCode;
import com.mypay.customer_service.dtos.request.BankLinkRequest;
import com.mypay.customer_service.entities.Customer;
import com.mypay.customer_service.entities.Wallet;
import com.mypay.customer_service.repositories.CustomerRepository;
import com.mypay.customer_service.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final CustomerRepository customerRepository;

    public Wallet getWalletInfo(String accountId) {
        Customer customer = customerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_PROFILE_NOT_FOUND));

        return walletRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
    }

    public String linkBankAccount(String accountId, BankLinkRequest request) {
        return "Liên kết ngân hàng thành công!";
    }
}