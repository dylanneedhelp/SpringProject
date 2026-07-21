package com.mypay.identity.dtos.Request;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {
    private String token;
}