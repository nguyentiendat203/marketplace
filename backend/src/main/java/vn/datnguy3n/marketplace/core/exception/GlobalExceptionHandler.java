package vn.datnguy3n.marketplace.core.exception;

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import lombok.extern.slf4j.Slf4j;
import vn.datnguy3n.marketplace.common.ApiResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Bắt lỗi validation @Valid và trả về dữ liệu lỗi chi tiết cho client
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return  buildResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, String.join("; ", errors));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        log.warn("Missing request part: {}", ex.getRequestPartName());
        return buildResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                "Thiếu trường bắt buộc: " + ex.getRequestPartName());
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex) {
        log.warn("Business exception [{}]: {}", ex.getStatus(), ex.getMessage());
        return buildResponse(ex.getStatus().value(), ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        log.warn("🚨 Runtime exception: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        log.error("💥 Lỗi hệ thống: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
                "Hệ thống có lỗi xảy ra, vui lòng liên hệ Admin hoặc thử lại sau!");
    }

    private ResponseEntity<ApiResponse<Object>> buildResponse(int statusCode, HttpStatus httpStatus, String message) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setStatusCode(statusCode);
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        return ResponseEntity.status(httpStatus).body(response);
    }
}
