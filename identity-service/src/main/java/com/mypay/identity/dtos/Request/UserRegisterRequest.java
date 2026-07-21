package com.mypay.identity.dtos.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterRequest {
    @Size(min = 4, message = "Username phải có ít nhất 4 ký tự")
    private String username;

    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    private String password;

    @Email(message = "Invalid email format")
    private String email;

    private String fullName;
}
