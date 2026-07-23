package com.mypay.customer_service.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankLinkRequest {
    @NotBlank(message = "Mã ngân hàng không được để trống (Ví dụ: VCB, TCB, MB)")
    private String bankCode;

    @NotBlank(message = "Số tài khoản ngân hàng không được để trống")
    private String accountNumber;

    @NotBlank(message = "Tên chủ tài khoản không được để trống")
    private String accountName;

}
