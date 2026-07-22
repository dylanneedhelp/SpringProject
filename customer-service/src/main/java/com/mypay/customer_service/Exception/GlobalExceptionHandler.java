package com.mypay.customer_service.Exception;

import com.mypay.customer_service.dtos.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

//import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<String>> handleException(Exception e){
        log.error("Lỗi hệ thống nghiêm trọng: ", e);
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .build();

        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getHttpStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<String>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode;

        try {
            // Thử parse cái message xem có khớp với Enum không
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException ex) {
            // Nếu dev ghi message tiếng Việt bình thường (vd: "Tên không được trống")
            // thì lấy thẳng message đó trả về luôn
            return ResponseEntity.badRequest().body(
                    ApiResponse.<String>builder()
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message(enumKey)
                            .build()
            );
        }

        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);

    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException exception) {
        log.warn("Access Denied: {}", exception.getMessage());

        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(
                ApiResponse.<String>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

//    @ExceptionHandler(RuntimeException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
//        ApiResponse<String> response = ApiResponse.<String>builder()
//                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .message("An error occurred: " + ex.getMessage())
//                .build();
//
//        // Trả về phản hồi với mã trạng thái 500
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
}
