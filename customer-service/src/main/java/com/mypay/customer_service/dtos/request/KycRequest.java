package com.mypay.customer_service.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KycRequest {
    @NotBlank(message = "Số CCCD/CMND không được để trống")
    private String idCardNumber;

    @NotBlank(message = "Ảnh mặt trước là bắt buộc")
    private String frontImageUrl;

    @NotBlank(message = "Ảnh mặt sau là bắt buộc")
    private String backImageUrl;

    private String faceVideoUrl;
}
