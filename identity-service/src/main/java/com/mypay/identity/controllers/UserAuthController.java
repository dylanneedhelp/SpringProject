package com.mypay.identity.controllers;

import com.mypay.identity.dtos.Request.*;
import com.mypay.identity.dtos.Response.ApiResponse;
import com.mypay.identity.dtos.Response.TokenResponse;
import com.mypay.identity.dtos.Response.UserResponse;
import com.mypay.identity.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {
    private final AccountService _accountService;
    public UserAuthController(AccountService accountService) {
        _accountService = accountService;
    }
    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody @Valid UserRegisterRequest request) {
        return ApiResponse.<String>builder()
                .data(_accountService.register(request))
                .build();
    }
    @PostMapping("/register2")
    public ResponseEntity<ApiResponse<String>> register2(@RequestBody @Valid UserRegisterRequest request) {
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .data(_accountService.register(request))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@RequestBody @Valid VerifyOtpRequest request) {
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .data(_accountService.verifyOtp(request))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<String>> resendOtp(@RequestBody @Valid ResendOtpRequest request) {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .data(_accountService.resendOtp(request))
                .build());
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletRequest httpRequest,
            @RequestHeader(value = "User-Agent", defaultValue = "Unknown Device") String userAgent) {

        String ipAddress = httpRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = httpRequest.getRemoteAddr();
        }

        return ResponseEntity.ok(ApiResponse.<TokenResponse>builder()
                .data(_accountService.login(request, ipAddress, userAgent))
                .build());
    }
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(ApiResponse.<TokenResponse>builder()
                .data(_accountService.refreshToken(request))
                .build());
    }
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .data(_accountService.changePassword(request))
                .build());
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .data(_accountService.logout(token))
                .build());
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUser(){
        List<UserResponse> users = _accountService.getAll();
        ApiResponse<List<UserResponse>> response= ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .message("Fetch all users successfully")
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }
    @SecurityRequirement(name = "bearerAuth") // Liên kết trực tiếp với scheme "bearerAuth" đã định nghĩa ở OpenApiConfig
    @GetMapping("/token")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) throws ParseException {
        String token = authHeader.substring(7);
        UserResponse user = _accountService.getUserByToken(token);
        ApiResponse<UserResponse> response= ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Fetch all users successfully")
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }


}
