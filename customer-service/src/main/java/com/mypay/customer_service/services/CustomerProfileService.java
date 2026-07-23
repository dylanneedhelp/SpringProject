package com.mypay.customer_service.services;


import com.mypay.customer_service.Exception.AppException;
import com.mypay.customer_service.Exception.ErrorCode;
import com.mypay.customer_service.dtos.request.CustomerProfileRequest;
import com.mypay.customer_service.dtos.request.CustomerUpdateRequest;
import com.mypay.customer_service.dtos.response.CustomerResponse;
import com.mypay.customer_service.dtos.response.LinkedBankAccountResponse;
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
import java.util.List;

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
            throw new AppException(ErrorCode.CUSTOMER_PROFILE_EXISTED);
        }
        if (customerRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
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
    public CustomerResponse getCustomerByAccountId(String accId){
        Customer customer = customerRepository.findByAccountId(accId).orElse(null);
        if (customer == null) {
            return null;
        }
        CustomerResponse response = new CustomerResponse();

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


        return response;
    }

    public ProfileResponse getProfileByAccountId(String accId){
        Customer customer = customerRepository.findByAccountId(accId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Wallet wallet = walletRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));


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


       kycProfileRepository.findByCustomerId(customer.getId()).ifPresent(kycProfile -> {
            response.setKycPrfId(kycProfile.getId());
            response.setStatusKycPrf(kycProfile.getStatus());
            response.setIdCardNumberKycPrf(kycProfile.getIdCardNumber());
            response.setFrontImageUrlKycPrf(kycProfile.getFrontImageUrl());
            response.setBackImageUrlKycPrf(kycProfile.getBackImageUrl());
            response.setRejectionReasonKycPrf(kycProfile.getRejectionReason());
            response.setVerifiedAtKycPrf(kycProfile.getVerifiedAt());
            response.setFaceVideoUrlKycPrf(kycProfile.getFaceVideoUrl());
        });
        List<LinkedBankAccount> linkedBanks = linkedBankAccountRepository.findAllByWalletId(wallet.getId());
        if (linkedBanks != null && !linkedBanks.isEmpty()) {
            List<LinkedBankAccountResponse> bankResponses = linkedBanks.stream().map(bank ->
                    LinkedBankAccountResponse.builder()
                            .linkBankAccountId(bank.getId())
                            .linkBankAccountBankCode(bank.getBankCode())
                            .linkBankAccountAccountNumber(bank.getAccountNumber())
                            .linkBankAccountAccountName(bank.getAccountName())
                            .linkBankAccountIsPrimary(bank.isPrimary())
                            .linkBankAccountLinkedAt(bank.getLinkedAt())
                            .build()
            ).toList();

            response.setLinkedBankAccounts(bankResponses);
        }

        return response;
    }
    @Transactional
    public String updateProfile(String accountId, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_PROFILE_NOT_FOUND));

        if (request.getFullName() != null) customer.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null) {
            if (!customer.getPhoneNumber().equals(request.getPhoneNumber()) && customerRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
            }
            customer.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getDob() != null) customer.setDob(request.getDob());
        if (request.getGender() != null) customer.setGender(request.getGender());
        if (request.getAddress() != null) customer.setAddress(request.getAddress());
        if (request.getAvatarUrl() != null) customer.setAvatarUrl(request.getAvatarUrl());

        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);

        return "Cập nhật hồ sơ thành công!";
    }

}