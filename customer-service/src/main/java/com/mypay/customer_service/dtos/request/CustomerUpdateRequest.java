package com.mypay.customer_service.dtos.request;

import lombok.Data;

import java.time.LocalDate;
@Data
public class CustomerUpdateRequest {
    private String fullName;
    private String phoneNumber;
    private LocalDate dob;
    private String gender;
    private String address;
    private String avatarUrl;
}
