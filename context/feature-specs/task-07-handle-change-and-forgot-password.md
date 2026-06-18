# Task 07: Yêu cầu triển khai API Quản lý Mật khẩu (Change Password & Forgot Password)

### 1. Bối cảnh dự án:

- Spring Boot 3.x, Spring Data JPA, Spring Security (`PasswordEncoder`).
- Các Entity đều kế thừa từ `BaseEntity` (đã có `id` kiểu `UUID`, `createdAt`, `updatedAt`,...).
- Đã có Entity `User` (chứa `email`, `password` đã mã hóa).
- Có `EmailService` để gửi mail và sử dụng Thymeleaf để render template HTML.

### 2. Yêu cầu chi tiết cho các Tác vụ:

#### Tác vụ 1: API Đổi mật khẩu (Change Password) - Dành cho User ĐÃ đăng nhập

- **Endpoint:** `POST /api/v1/users/change-password`
- **Request Body DTO:** `{ "oldPassword": "...", "newPassword": "...", "confirmPassword": "..." }`
- **Logic xử lý tại Service:**
  1. Kiểm tra `newPassword` và `confirmPassword` phải trùng khớp nhau.
  2. Lấy thông tin User hiện tại từ Security Context thông qua `ApplicationContextProvider.getCurrentUserLogin()`.
  3. Dùng `passwordEncoder.matches()` để kiểm tra `oldPassword` nhập vào có khớp với mật khẩu hiện tại trong DB không. Nếu sai ném lỗi 400.
  4. Mã hóa mật khẩu mới và lưu cập nhật vào DB.

#### Tác vụ 2: Tạo Entity lưu Token phục vụ Quên mật khẩu

- Tạo Entity `PasswordResetToken` kế thừa từ `BaseEntity`.
- Thuộc tính gồm: `token` (String - chuỗi ngẫu nhiên 6 ký tự in hoa/số để user dễ copy), `expiryDate` (Instant - thời gian hết hạn là 5 phút), và mối quan hệ `@ManyToOne User user`.

#### Tác vụ 3: Luồng Quên & Đặt lại mật khẩu - Sử dụng một Email Template MỚI TINH

- **API 1: Yêu cầu gửi mã (`POST /api/v1/auth/forgot-password`)**
  - Input DTO: `{ "email": "..." }`
  - Logic: Tìm User theo email -> Sinh mã token 6 ký tự -> Lưu vào bảng `PasswordResetToken` (ghi đè/xóa mã cũ nếu có) -> Gửi mã này vào một **Email Template hoàn toàn mới dành riêng cho khôi phục mật khẩu** (ví dụ: `password-reset-email.html`, không dùng chung với mail kích hoạt tài khoản).
- **API 2: Xác thực mã và Đặt lại mật khẩu (`POST /api/v1/auth/reset-password`)**
  - Input DTO: `{ "token": "...", "newPassword": "...", "confirmPassword": "..." }`
  - Logic: Kiểm tra trùng khớp mật khẩu mới -> Tìm và kiểm tra thời hạn của token trong DB -> Mã hóa và cập nhật mật khẩu mới cho User -> **Xóa hoàn toàn bản ghi token này khỏi DB** để mã chỉ sử dụng được 1 lần duy nhất.

### 3. Kết quả mong muốn trả về:

Hãy viết code hoàn chỉnh, cấu trúc rõ ràng cho: Entity mới, Repository, các lớp DTO request, logic xử lý chi tiết tại tầng Service, endpoint tại Controller (cấu hình `@PathVariable/@RequestBody` tường minh) và đoạn code cấu hình truyền biến mã OTP vào file HTML Template gửi mail mới.
