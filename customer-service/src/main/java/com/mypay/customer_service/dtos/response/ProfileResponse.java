package com.mypay.customer_service.dtos.response;

import com.mypay.customer_service.entities.Customer;
import com.mypay.customer_service.entities.Wallet;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {
    String customerId;
    String accountId;
    String fullName;
    String phoneNumber;
    LocalDate dob;
    String gender;
    String address;
    String avatarUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    String KycPrfId;
    String idCardNumberKycPrf;
    String frontImageUrlKycPrf;
    String backImageUrlKycPrf;
    String faceVideoUrlKycPrf;
    String statusKycPrf;
    String rejectionReasonKycPrf;
    LocalDateTime verifiedAtKycPrf;

    String walletId;
    BigDecimal walletBalance;
    BigDecimal walletFrozenBalance;
    String walletCurrency;
    String walletTier;
    String walletStatus;


    String linkBankAccountId;
    String linkBankAccountBankCode;
    String linkBankAccountAccountNumber;
    String linkBankAccountAccountName;
    boolean linkBankAccountIsPrimary = false;
    LocalDateTime linkBankAccountLinkedAt = LocalDateTime.now();
}
