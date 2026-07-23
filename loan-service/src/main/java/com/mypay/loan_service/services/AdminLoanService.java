package com.mypay.loan_service.services;

import com.mypay.loan_service.Exception.AppException;
import com.mypay.loan_service.Exception.ErrorCode;
import com.mypay.loan_service.clients.CustomerServiceClient;
import com.mypay.loan_service.dtos.request.AppraisalRequest;
import com.mypay.loan_service.dtos.request.DecisionRequest;
import com.mypay.loan_service.dtos.response.ApiResponse;
import com.mypay.loan_service.entities.*;
import com.mypay.loan_service.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminLoanService {

    private final LoanApplicationRepository applicationRepository;
    private final ValuationReportRepository valuationReportRepository;
    private final LoanRepository loanRepository;
    private final RepaymentScheduleRepository scheduleRepository;
    private final CustomerServiceClient customerServiceClient;

    // 1. Nhân viên lưu báo cáo định giá (APPRAISING)
    @Transactional
    public String appraiseLoan(String applicationId, String appraiserId, AppraisalRequest request) {
        LoanApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.LOAN_APPLICATION_NOT_FOUND));

        if (!application.getStatus().equals("PENDING")) {
            throw new AppException(ErrorCode.INVALID_APPLICATION_STATUS);
        }

        ValuationReport report = ValuationReport.builder()
                .loanApplication(application)
                .appraiserId(appraiserId)
                .valuationMethod(request.getValuationMethod())
                .estimatedValue(request.getEstimatedValue())
                .approvedAmount(request.getApprovedAmount())
                .notes(request.getNotes())
                .build();

        valuationReportRepository.save(report);

        application.setStatus("APPRAISING"); // Đổi trạng thái sang đang thẩm định
        application.setUpdatedAt(LocalDateTime.now());
        applicationRepository.save(application);

        return "Lưu báo cáo thẩm định thành công!";
    }

    // 2. Quản lý chốt duyệt hồ sơ, tự động sinh hợp đồng và lịch trả nợ
    @Transactional
    public String decideLoan(String applicationId, DecisionRequest request) {
        LoanApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new AppException(ErrorCode.LOAN_APPLICATION_NOT_FOUND));

        if (!application.getStatus().equals("APPRAISING")) {
            throw new AppException(ErrorCode.INVALID_APPLICATION_STATUS);
        }

        if (!request.getIsApproved()) {
            application.setStatus("REJECTED");
            application.setUpdatedAt(LocalDateTime.now());
            applicationRepository.save(application);
            return "Đã từ chối hồ sơ vay.";
        }

        // --- NẾU ĐƯỢC DUYỆT ---
        ValuationReport report = valuationReportRepository.findByLoanApplicationId(applicationId);

        if(report == null){
            throw new AppException(ErrorCode.VALUATION_REPORT_NOT_FOUND);
        }

        application.setStatus("APPROVED");
        application.setUpdatedAt(LocalDateTime.now());
        applicationRepository.save(application);

        // 2.1 Tạo khoản vay chính thức
        Loan loan = Loan.builder()
                .application(application)
                .accountId(application.getAccountId())
                .principalAmount(report.getApprovedAmount())
                .outstandingPrincipal(report.getApprovedAmount())
                .interestRate(request.getInterestRate())
                .status("APPROVED") // Đã duyệt nhưng CHƯA GIẢI NGÂN (ACTIVE)
                .build();
        loanRepository.save(loan);

        // 2.2 Sinh lịch trả nợ (Gốc cố định hàng tháng, lãi tính trên dư nợ giảm dần)
        generateRepaymentSchedule(loan, application.getTermMonths());

        return "Đã duyệt hồ sơ, khởi tạo khoản vay và lịch trả nợ thành công!";
    }

    private void generateRepaymentSchedule(Loan loan, int termMonths) {
        BigDecimal principalPerMonth = loan.getPrincipalAmount().divide(new BigDecimal(termMonths), 0, RoundingMode.HALF_UP);
        BigDecimal currentOutstanding = loan.getPrincipalAmount();
        BigDecimal monthlyInterestRate = loan.getInterestRate().divide(new BigDecimal(1200), 4, RoundingMode.HALF_UP); // Lãi năm chia 12, chia 100

        List<RepaymentSchedule> schedules = new ArrayList<>();
        LocalDate nextDueDate = LocalDate.now().plusMonths(1);

        for (int i = 1; i <= termMonths; i++) {
            // Lãi = dư nợ hiện tại * lãi suất tháng
            BigDecimal interestDue = currentOutstanding.multiply(monthlyInterestRate).setScale(0, RoundingMode.HALF_UP);

            // Kỳ cuối cùng phải gánh số dư lẻ (nếu có do chia làm tròn)
            BigDecimal principalDue = (i == termMonths) ? currentOutstanding : principalPerMonth;

            RepaymentSchedule schedule = RepaymentSchedule.builder()
                    .loan(loan)
                    .installmentNumber(i)
                    .dueDate(nextDueDate)
                    .principalDue(principalDue)
                    .interestDue(interestDue)
                    .status("UNPAID")
                    .build();
            schedules.add(schedule);

            currentOutstanding = currentOutstanding.subtract(principalDue);
            nextDueDate = nextDueDate.plusMonths(1);
        }
        scheduleRepository.saveAll(schedules);
    }

    // 3. Giải ngân: Gọi chéo sang Customer Service để nạp tiền vào ví
    @Transactional
    public String disburseLoan(String loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new AppException(ErrorCode.LOAN_NOT_FOUND));

        if (!loan.getStatus().equals("APPROVED")) {
            throw new AppException(ErrorCode.LOAN_ALREADY_DISBURSED);
        }

        // Bước 1: Lấy thông tin Wallet từ accountId
        ApiResponse<Map<String, Object>> walletResponse = customerServiceClient.getWalletByAccountId(loan.getAccountId());
        if (walletResponse.getCode() != 1000 || walletResponse.getData() == null) {
            throw new RuntimeException("Không tìm thấy ví của khách hàng để giải ngân!");
        }
        String walletId = (String) walletResponse.getData().get("id");

        // Bước 2: Gọi API cộng tiền vào ví
        ApiResponse<String> addBalanceResponse = customerServiceClient.addBalance(walletId, Map.of("amount", loan.getPrincipalAmount()));
        if (addBalanceResponse.getCode() != 1000) {
            throw new RuntimeException("Lỗi hệ thống khi giải ngân vào ví!");
        }

        // Bước 3: Đổi trạng thái Khoản vay thành đang hoạt động
        loan.setStatus("ACTIVE");
        loan.setDisbursedAt(LocalDateTime.now());
        loanRepository.save(loan);

        return "Giải ngân thành công số tiền " + loan.getPrincipalAmount() + " VND vào ví khách hàng!";
    }
}