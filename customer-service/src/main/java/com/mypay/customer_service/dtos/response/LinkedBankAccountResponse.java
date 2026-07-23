package com.mypay.customer_service.dtos.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LinkedBankAccountResponse {
    String linkBankAccountId;
    String linkBankAccountBankCode;
    String linkBankAccountAccountNumber;
    String linkBankAccountAccountName;
    boolean linkBankAccountIsPrimary = false;
    LocalDateTime linkBankAccountLinkedAt = LocalDateTime.now();
}
