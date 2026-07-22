package com.mypay.customer_service.dtos.response;

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
public class CustomerResponse {
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
}
