package com.mypay.identity.dtos.Request;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequest {
    private String accessToken;
    private String refreshToken;
}