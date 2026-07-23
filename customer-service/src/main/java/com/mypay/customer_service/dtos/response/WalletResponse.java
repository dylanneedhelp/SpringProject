package com.mypay.customer_service.dtos.response;

import com.mypay.customer_service.entities.Customer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletResponse {
    String id;
    BigDecimal balance;
    BigDecimal frozenBalance;
    String currency;
    String tier;
    String status;
}
