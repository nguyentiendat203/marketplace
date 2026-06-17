package vn.datnguy3n.marketplace.config.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;
import vn.datnguy3n.marketplace.common.ApiResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 1. Đặt một Message mặc định phòng trường hợp không bắt được log cụ thể
        String customMessage = "Token không hợp lệ hoặc đã hết hạn!";
        
        // 2. Kiểm tra chi tiết lỗi từ Spring Security bắn ra để dịch lại theo ý mình
        if (authException != null && authException.getMessage() != null) {
            String rawMessage = authException.getMessage();
            
            if (rawMessage.contains("Invalid signature")) {
                // 👉 ĐÂY CHÍNH LÀ NƠI HANDLE LỖI SAI CHỮ KÝ
                customMessage = "Token không hợp lệ - Chữ ký sai hoặc cấu trúc Token đã bị thay đổi!";
            } else if (rawMessage.contains("Jwt expired")) {
                // Handle luôn cho trường hợp token hết hạn
                customMessage = "Token đã hết hạn sử dụng. Vui lòng thực hiện Refresh Token hoặc Đăng nhập lại!";
            } else if (rawMessage.contains("Malformed")) {
                // Handle trường hợp chuỗi token không đúng định dạng JWT (thiếu dấu chấm, kí tự lạ)
                customMessage = "Định dạng Token không đúng cấu trúc mã hóa tiêu chuẩn!";
            } else {
                // Nếu là các lỗi xác thực khác
                customMessage = rawMessage;
            }
        }

        // 3. Đóng gói vào ApiResponse chuẩn chỉnh của ông
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
        apiResponse.setSuccess(false);
        apiResponse.setMessage(customMessage); // Bỏ cái customMessage đã dịch vào đây
        apiResponse.setData(null);

        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}
