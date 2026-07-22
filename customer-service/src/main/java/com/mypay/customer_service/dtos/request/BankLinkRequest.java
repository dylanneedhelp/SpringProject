package com.mypay.customer_service.dtos.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankLinkRequest {
    String bankCode;
    String accountNumber;
    String accountName;

}
