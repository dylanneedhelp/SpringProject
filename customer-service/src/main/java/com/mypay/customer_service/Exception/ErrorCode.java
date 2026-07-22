package com.mypay.customer_service.Exception;

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
