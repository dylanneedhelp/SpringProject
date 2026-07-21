package com.mypay.identity.dtos.Request;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
}