package com.mypay.identity.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
//@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception",HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(1002, "User Not Found",HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1003, "User Already Exists", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTS(1004, "Phone Already Exists", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(1005, "Email Already Exists", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1006, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1007, "Password must be at least 4 characters", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1008, "Invalid Email Address", HttpStatus.BAD_REQUEST),
    INVALID_PHONE(1009, "Invalid Phone Number", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1009,"Invalid Key", HttpStatus.BAD_REQUEST),
    BLANK_USERNAME(1010,"Username cannot be blank", HttpStatus.BAD_REQUEST),
    BLANK_PASSWORD(1011,"Password cannot be blank", HttpStatus.BAD_REQUEST),
    BLANK_PHONE(1012,"Phone cannot be blank", HttpStatus.BAD_REQUEST),
    BLANK_BIRTHDAY(1013,"Birthday cannot be blank", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1014, "You do not have permission",HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1015, "Unauthenticated",HttpStatus.UNAUTHORIZED),
    ROLE_NOT_FOUND(1016, "Role Not Found", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(1017, "Product Not Found", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(1018, "Order Not Found", HttpStatus.NOT_FOUND),
    WALLET_NOT_FOUND(1019, "Wallet Not Found", HttpStatus.NOT_FOUND),
    INSUFFICIENT_FUNDS(1020, "Insufficient Funds", HttpStatus.BAD_REQUEST),
    ALREADY_PAID(1021, "This Order Are Already Paid", HttpStatus.BAD_REQUEST),
    FEEDBACK_NOT_FOUND(1022, "Feed Back Not Found", HttpStatus.NOT_FOUND),
    ORDER_DETAIL_NOT_FOUND(1023, "Order Detail Not Found", HttpStatus.NOT_FOUND),
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
