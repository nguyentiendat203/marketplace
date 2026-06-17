# MASTER TASK 05: CẤU HÌNH SPRING SECURITY OAUTH2 RESOURCE SERVER (JWT) KIẾN TRÚC JHIPSTER & TRIỂN KHAI REGISTER/LOGIN/REFRESH

## 1. BỐI CẢNH & MỤC TIÊU ĐẦU RA

Trước tiên hãy đọc file AGENTS.md ở root folder và sau đó làm theo các bước:

- Triển khai hệ thống Bảo mật & Xác thực sử dụng Spring Security và **OAuth2 Resource Server (JWT)** chuẩn cấu hình hiện đại của JHipster.
- Triển khai 3 API: Đăng ký (`/api/register`), Đăng nhập (`/api/authenticate`), và Gia hạn token (`/api/refresh`).
- **RÀNG BUỘC KIẾN TRÚC NGHIÊM NGẶT:**
  - **Xử lý lỗi tập trung:** Sử dụng `CustomAuthenticationEntryPoint` phối hợp với `GlobalExceptionHandler` để đảm bảo lỗi token, hết hạn, hoặc sai tài khoản đều trả về đúng định dạng `ApiResponse` chuẩn.
  - **Đồng bộ Response:** Mọi API thành công phải được tự động bọc qua `CustomResponseBodyAdvice` thành `ApiResponse<T>`.
  - **Tiêu chuẩn Code:** Tuyệt đối không dùng `@Data` của Lombok (chỉ dùng `@Getter`, `@Setter`, `@RequiredArgsConstructor`).

---

## 2. YÊU CẦU TRIỂN KHAI CHI TIẾT

### BƯỚC 1: CẬP NHẬT ENTITY USER ĐỂ LƯU REFRESH TOKEN

- Thêm duy nhất trường `private String refreshToken;` vào Entity `User` hiện tại để lưu chuỗi token ngẫu nhiên.
- KHÔNG tạo thêm trường expiry date trong DB. Việc kiểm tra hết hạn sẽ dựa vào thời gian cập nhật record (`updatedAt` hoặc `lastModifiedDate` của User) kết hợp với cấu hình thời gian sống trong file properties.

### BƯỚC 2: CẤU HÌNH KIẾN TRÚC OAUTH2 RESOURCE SERVER VÀ JWT BEANS

2.1. Cấu hình các thuộc tính JWT trong `application.yml`:

- `secret-key`: Chuỗi mã hóa Base64 (độ dài tối thiểu 64 ký tự để chạy thuật toán HMAC-SHA512).
- `access-token-validity-in-seconds`: Hạn access token (ví dụ: 900s).
- `refresh-token-validity-in-seconds`: Hạn refresh token (ví dụ: 2592000s).

  2.2. Cấu hình mã hóa và giải mã JWT (`JwtConfiguration`):
  Tạo các Bean mã hóa/giải mã native của Spring Security thay vì viết Filter thủ công:

- **`@Bean public JwtDecoder jwtDecoder()`**: Sử dụng `NimbusJwtDecoder.withSecretKey(spec)` với Secret Key được convert từ Base64 trong cấu hình để tự động giải mã và validate các request truyền lên.
- **`@Bean public JwtEncoder jwtEncoder()`**: Sử dụng `NimbusJwtEncoder` kết hợp với `ImmutableSecret` phục vụ việc sinh ra chuỗi JWT token mới khi user đăng nhập thành công.

  2.3. Cấu hình `SecurityConfiguration` (Khớp chính xác cấu hình Filter Chain mẫu):

- Cấu hình FilterChain nhận vào `HttpSecurity` và `CustomAuthenticationEntryPoint`.
- Cấu hình whitelist các url như: `/`, `/api/register`, `/api/authenticate`, `/api/refresh`, các endpoint swagger công khai...
- Kích hoạt OAuth2 Resource Server:
  ```java
  http.oauth2ResourceServer(oauth2 -> oauth2
      .jwt(Customizer.withDefaults())
      .authenticationEntryPoint(customAuthenticationEntryPoint)
  );
  ```
