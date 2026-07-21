package com.mypay.identity.dtos.Request;
import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResendOtpRequest {
    @Email(message = "Email không đúng định dạng")
    private String email;
}