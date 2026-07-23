package com.mypay.loan_service.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
//@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống chưa được phân loại", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(1002, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1003, "Tài khoản đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_ALREADY_EXISTS(1004, "Username đã tồn tại", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTS(1005, "Số điện thoại này đã được sử dụng", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(1006, "Email đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1007, "Tên đăng nhập phải có ít nhất 3 ký tự", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1008, "Mật khẩu phải có ít nhất 4 ký tự", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1009, "Định dạng Email không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_PHONE(1010, "Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST),
    BLANK_USERNAME(1011, "Tên đăng nhập không được để trống", HttpStatus.BAD_REQUEST),
    BLANK_PASSWORD(1012, "Mật khẩu không được để trống", HttpStatus.BAD_REQUEST),
    BLANK_PHONE(1013, "Số điện thoại không được để trống", HttpStatus.BAD_REQUEST),
    CUSTOMER_PROFILE_NOT_FOUND(1016, "Chưa có hồ sơ khách hàng. Vui lòng cập nhật Profile!", HttpStatus.NOT_FOUND),
    DEFAULT_ROLE_NOT_FOUND(1017,"Không thấy ROLE USER",HttpStatus.NOT_FOUND),
    EMAIL_NOT_FOUND(1018, "Không thấy Email", HttpStatus.NOT_FOUND),
    ACCOUNT_ACTIVED(1022, "Tài khoản đã được kích hoạt rồi", HttpStatus.BAD_REQUEST),
    ERROR_SEND_EMAIL(1023, "Lỗi hệ thống gửi mail", HttpStatus.INTERNAL_SERVER_ERROR),

    // --- CÁC MÃ LỖI MỚI BỔ SUNG CHO ACCOUNT SERVICE ---
    OTP_EXPIRED_OR_NOT_FOUND(1024, "Mã OTP đã hết hạn hoặc không tồn tại!", HttpStatus.BAD_REQUEST),
    OTP_INVALID(1025, "Mã OTP nhập vào không chính xác!", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(1026, "Tài khoản hoặc mật khẩu không chính xác", HttpStatus.BAD_REQUEST),
    ACCOUNT_LOCKED(1027, "Tài khoản đã bị khóa do nhập sai nhiều lần. Vui lòng thử lại sau 15 phút.", HttpStatus.FORBIDDEN),
    ACCOUNT_NOT_VERIFIED(1028, "Tài khoản chưa được xác thực. Vui lòng kiểm tra email để nhập mã OTP.", HttpStatus.FORBIDDEN),
    ACCOUNT_BANNED(1029, "Tài khoản của bạn đã bị vô hiệu hóa bởi Admin.", HttpStatus.FORBIDDEN),
    INVALID_REFRESH_TOKEN(1030, "Refresh token không hợp lệ", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_EXPIRED(1031, "Refresh token đã hết hạn hoặc bị thu hồi. Vui lòng đăng nhập lại.", HttpStatus.UNAUTHORIZED),
    OLD_PASSWORD_INCORRECT(1032, "Mật khẩu cũ không chính xác", HttpStatus.BAD_REQUEST),
    JWT_GENERATION_FAILED(1033, "Không thể tạo Token JWT", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_ACCESS_TOKEN(1034, "Access Token không hợp lệ!", HttpStatus.UNAUTHORIZED),
    KYC_ALREADY_EXISTS(1035, "Hồ sơ eKYC đã tồn tại hoặc đã được gửi trước đó", HttpStatus.BAD_REQUEST),
    KYC_NOT_FOUND(1036, "Không tìm thấy hồ sơ eKYC", HttpStatus.NOT_FOUND),
    BANK_ACCOUNT_ALREADY_EXISTS(1037, "Tài khoản ngân hàng này đã được liên kết trước đó", HttpStatus.BAD_REQUEST),
    BANK_ACCOUNT_NOT_FOUND(1038, "Không tìm thấy thông tin tài khoản ngân hàng liên kết", HttpStatus.NOT_FOUND),
    WALLET_ALREADY_BLOCKED(1039, "Ví điện tử đã ở trạng thái bị khóa", HttpStatus.BAD_REQUEST),
    WALLET_NOT_FOUND_BY_CUSTOMER(1040, "Không tìm thấy ví tương ứng với mã khách hàng này", HttpStatus.NOT_FOUND),
    // --- LOAN SERVICE
    LOAN_APPLICATION_NOT_FOUND(2001, "Không tìm thấy hồ sơ yêu cầu vay", HttpStatus.NOT_FOUND),
    INVALID_APPLICATION_STATUS(2002, "Trạng thái hồ sơ không hợp lệ để thực hiện thao tác này", HttpStatus.BAD_REQUEST),
    LOAN_NOT_FOUND(2003, "Không tìm thấy khoản vay", HttpStatus.NOT_FOUND),
    LOAN_ALREADY_DISBURSED(2004, "Khoản vay này đã được giải ngân rồi", HttpStatus.BAD_REQUEST),
    VALUATION_REPORT_NOT_FOUND(2005, "Không tìm thấy báo cáo Thẩm định & Định giá", HttpStatus.NOT_FOUND),
    // Security & JWT chung
    UNAUTHENTICATED(1014, "Bạn chưa đăng nhập hoặc Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1015, "Bạn không có quyền truy cập chức năng này", HttpStatus.FORBIDDEN),

    // Customer & Wallet
    WALLET_NOT_FOUND(1019, "Không tìm thấy Ví điện tử", HttpStatus.NOT_FOUND),
    CUSTOMER_PROFILE_EXISTED(1020, "Tài khoản này đã có hồ sơ khách hàng", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_FUNDS(1021, "Số dư trong ví không đủ", HttpStatus.BAD_REQUEST);
;
    ErrorCode(int code, String message, HttpStatusCode  httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode  httpStatusCode;


}
