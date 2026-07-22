package com.mypay.customer_service.services;


import com.mypay.customer_service.dtos.request.CustomerProfileRequest;
import com.mypay.customer_service.dtos.response.ProfileResponse;
import com.mypay.customer_service.entities.Customer;
import com.mypay.customer_service.entities.KycProfile;
import com.mypay.customer_service.entities.LinkedBankAccount;
import com.mypay.customer_service.entities.Wallet;
import com.mypay.customer_service.repositories.CustomerRepository;
import com.mypay.customer_service.repositories.KycProfileRepository;
import com.mypay.customer_service.repositories.LinkedBankAccountRepository;
import com.mypay.customer_service.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerProfileService {

    private final CustomerRepository customerRepository;
    private final WalletRepository walletRepository;
    private final KycProfileRepository kycProfileRepository;
    private final LinkedBankAccountRepository linkedBankAccountRepository;

    @Transactional
    public String onboardCustomer(String accountId, CustomerProfileRequest request) {
        if (customerRepository.findByAccountId(accountId).isPresent()) {
            throw new RuntimeException("Tài khoản này đã có hồ sơ khách hàng!");
        }
        if (customerRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Số điện thoại này đã được sử dụng!");
        }

        Customer newCustomer = Customer.builder()
                .accountId(accountId)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .dob(request.getDob())
                .gender(request.getGender())
                .address(request.getAddress())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        customerRepository.save(newCustomer);

        Wallet newWallet = Wallet.builder()
                .customer(newCustomer)
                .currency("VND")
                .tier("STANDARD")
                .status("ACTIVE")
                .build();

        walletRepository.save(newWallet);

        return "Khởi tạo hồ sơ và mở ví thành công!";
    }

    public ProfileResponse getCustomerByAccountId(String accId){
        Customer customer = customerRepository.findByAccountId(accId).orElse(null);
        Wallet wallet = walletRepository.findByCustomerId(customer.getId()).orElse(null);
        KycProfile kycProfile = kycProfileRepository.findByCustomerId(customer.getId()).orElse(null);
        LinkedBankAccount linkedBankAccount = linkedBankAccountRepository.findByWalletId(wallet.getId()).orElse(null);
        ProfileResponse response = new ProfileResponse();

        response.setCustomerId(customer.getId());
        response.setAccountId(customer.getAccountId());
        response.setAddress(customer.getAddress());
        response.setDob(customer.getDob());
        response.setAvatarUrl(customer.getAvatarUrl());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        response.setPhoneNumber(customer.getPhoneNumber());
        response.setFullName(customer.getFullName());
        response.setGender(customer.getGender());

        response.setWalletId(wallet.getId());
        response.setWalletStatus(wallet.getStatus());
        response.setWalletCurrency(wallet.getCurrency());
        response.setWalletBalance(wallet.getBalance());
        response.setWalletTier(wallet.getTier());
        response.setWalletFrozenBalance(wallet.getFrozenBalance());

        response.setKycPrfId(kycProfile.getId());
        response.setStatusKycPrf(kycProfile.getStatus());
        response.setIdCardNumberKycPrf(kycProfile.getIdCardNumber());
        response.setFrontImageUrlKycPrf(kycProfile.getFrontImageUrl());
        response.setBackImageUrlKycPrf(kycProfile.getBackImageUrl());
        response.setRejectionReasonKycPrf(kycProfile.getRejectionReason());
        response.setVerifiedAtKycPrf(kycProfile.getVerifiedAt());
        response.setFaceVideoUrlKycPrf(kycProfile.getFaceVideoUrl());

        response.setLinkBankAccountId(linkedBankAccount.getId());
        response.setLinkBankAccountAccountNumber(linkedBankAccount.getAccountNumber());
        response.setLinkBankAccountBankCode(linkedBankAccount.getBankCode());
        response.setLinkBankAccountLinkedAt(linkedBankAccount.getLinkedAt());
        response.setLinkBankAccountIsPrimary(linkedBankAccount.isPrimary());
        response.setLinkBankAccountAccountName(linkedBankAccount.getAccountName());

        return response;
    }
}