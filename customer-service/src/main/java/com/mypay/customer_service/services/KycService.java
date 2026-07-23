package com.mypay.customer_service.services;

import com.mypay.customer_service.Exception.AppException;
import com.mypay.customer_service.Exception.ErrorCode;
import com.mypay.customer_service.dtos.request.KycRequest;
import com.mypay.customer_service.dtos.response.KycProfileResponse;
import com.mypay.customer_service.entities.Customer;
import com.mypay.customer_service.entities.KycProfile;
import com.mypay.customer_service.repositories.CustomerRepository;
import com.mypay.customer_service.repositories.KycProfileRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KycService {

    private final KycProfileRepository kycProfileRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public String submitKyc(String accountId, KycRequest request) {
        Customer customer = customerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_PROFILE_NOT_FOUND));

        if (kycProfileRepository.findByCustomerId(customer.getId()).isPresent()) {
            throw new AppException(ErrorCode.KYC_ALREADY_EXISTS);
        }

        KycProfile kycProfile = KycProfile.builder()
                .customer(customer)
                .idCardNumber(request.getIdCardNumber())
                .frontImageUrl(request.getFrontImageUrl())
                .backImageUrl(request.getBackImageUrl())
                .faceVideoUrl(request.getFaceVideoUrl())
                .status("PENDING") // Trạng thái chờ duyệt
                .build();

        kycProfileRepository.save(kycProfile);
        return "Nộp hồ sơ eKYC thành công, vui lòng chờ hệ thống phê duyệt!";
    }

    public KycProfileResponse getMyKycStatus(String accountId) {
        Customer customer = customerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_PROFILE_NOT_FOUND));

        KycProfile kycProfile = kycProfileRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new AppException(ErrorCode.KYC_NOT_FOUND));
        KycProfileResponse response = new KycProfileResponse();
        response.setId(kycProfile.getId());
        response.setStatus(kycProfile.getStatus());
        response.setIdCardNumber(kycProfile.getIdCardNumber());
        response.setFrontImageUrl(kycProfile.getFrontImageUrl());
        response.setBackImageUrl(kycProfile.getBackImageUrl());
        response.setRejectionReason(kycProfile.getRejectionReason());
        response.setVerifiedAt(kycProfile.getVerifiedAt());
        response.setFaceVideoUrl(kycProfile.getFaceVideoUrl());
        return response;
    }

    @Transactional
    public String verifyKyc(String kycId, boolean isApproved, String rejectionReason) {
        KycProfile kyc = kycProfileRepository.findById(kycId)
                .orElseThrow(() -> new AppException(ErrorCode.KYC_NOT_FOUND));

        if (isApproved) {
            kyc.setStatus("APPROVED");
            kyc.setVerifiedAt(LocalDateTime.now());
            kyc.setRejectionReason(null);
        } else {
            kyc.setStatus("REJECTED");
            kyc.setRejectionReason(rejectionReason);
        }

        kycProfileRepository.save(kyc);
        return isApproved ? "Đã duyệt hồ sơ eKYC thành công!" : "Đã từ chối hồ sơ eKYC.";
    }
}