# TASK 06: TRIỂN KHAI TÍNH NĂNG ĐĂNG KÝ XÁC THỰC QUA EMAIL THẬT (JAVAMAILSENDER & THYMELEAF)

Hãy đọc kỹ file AGENTS (và các file liên quan đến cấu trúc dự án hiện tại nếu cần) để nắm rõ coding convention, quy tắc kiến trúc và cách handle Exception/Response của hệ thống.

Sau đó, dựa vào ngữ cảnh đó để thực hiện trọn vẹn TASK 06 dưới đây:

## 1. BỐI CẢNH & LUỒNG NGHIỆP VỤ ĐẦU RA (WORKFLOW)

- **Bước 1 (Đăng ký):** Người dùng gửi dữ liệu qua `POST /api/v1/auth/register`. Hệ thống kiểm tra trùng lặp email/username. Nếu hợp lệ, lưu User vào DB ở trạng thái CHƯA kích hoạt (`activated = false`) kèm mã `activationKey = UUID.randomUUID().toString()`.
- **Bước 2 (Gửi Mail Bất Đồng Bộ):** Hệ thống dùng `JavaMailSender` và `TemplateEngine` (Thymeleaf) dịch template HTML, gửi link xác thực về email thật của user. API Register phản hồi ngay lập tức chuỗi thông báo: _"Đăng ký thành công! Vui lòng kiểm tra email của bạn để thực hiện xác thực kích hoạt tài khoản."_ mà không bị nghẽn luồng (nhờ sử dụng `@Async`).
- **Bước 3 (Kích hoạt tài khoản):** Người dùng mở email, bấm nút trong email, trình duyệt gọi đến API `GET /api/v1/auth/activate?key=<mã_uuid>`. Hệ thống kiểm tra tính hợp lệ và thời hạn sống (TTL). Nếu hợp lệ, cập nhật `activated = true`, xóa mã key, lưu DB và trả về thông tin `UserResponseDTO`.

## 2. RÀNG BUỘC KIẾN TRÚC NGHIÊM NGẶT

- **Xử lý lỗi tập trung:** Link hết hạn hoặc mã kích hoạt không đúng phải `throw new BusinessException(...)` để hệ thống xử lý lỗi đồng bộ thông qua `GlobalExceptionHandler`.
- **Đồng bộ Response:** Kết quả của API kích hoạt thành công hoặc đăng ký thành công phải được tự động bọc qua `CustomResponseBodyAdvice` thành `ApiResponse<T>`.
- **Tiêu chuẩn Code:** Tuyệt đối không dùng `@Data` của Lombok (chỉ dùng `@Getter`, `@Setter`, `@RequiredArgsConstructor`).

---

## 3. YÊU CẦU TRIỂN KHAI CHI TIẾT

### BƯỚC 1: CẬP NHẬT ENTITY USER & REPOSITORY

- Bổ sung thêm 1 field vào Entity `User` hiện tại để quản lý trạng thái kích hoạt:
  ```java
  private String activationKey;
  ```
- Thêm phương thức tìm kiếm trong `UserRepository`:
  ```java
  Optional<User> findByActivationKey(String activationKey);
  ```

### BƯỚC 2: CẤU HÌNH CÁC THUỘC TÍNH MAIL TRONG APPLICATION.PROPERTIES

Khai báo đầy đủ các biến môi trường để nạp cấu hình cho `JavaMailSender` và logic nghiệp vụ:

```properties
# Spring Mail Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Custom Application Mail Properties
application.mail.activation-ttl-minutes=15
application.mail.base-url=http://localhost:8080
```

### BƯỚC 3: XEM QUA FILE TEMPLATE EMAIL HTML (Thymeleaf UI)

Review file template tại đường dẫn `src/main/resources/templates/mail/activation-email.html` sử dụng thiết kế Modern UI:

### BƯỚC 4: XÂY DỰNG MAIL SERVICE (`MailService`)

- Tạo class `MailService` đánh dấu `@Service`. Đảm bảo có `@Async` trên method gửi mail để chạy bất đồng bộ nhằm tối ưu hiệu năng (Kích hoạt `@EnableAsync` tại class Main Application nếu chưa cấu hình).
- Viết hàm `public void sendActivationEmail(User user)` thực hiện:
  1. Khởi tạo `org.thymeleaf.context.Context`.
  2. Nạp các trường thông tin động (Context variables):
     - `username`: Gán giá trị từ `user.getUsername()`.
     - `activationUrl`: Tạo chuỗi đường dẫn xác thực: `${application.mail.base-url}/api/v1/auth/activate?key=${user.getActivationKey()}`.
     - `ttlMinutes`: Giá trị nạp từ thuộc tính cấu hình `application.mail.activation-ttl-minutes`.
  3. Dùng `templateEngine.process("mail/activation-email", context)` để biên dịch thành mã HTML hoàn chỉnh.
  4. Tạo `MimeMessage` thông qua bộ trợ giúp `MimeMessageHelper(mimeMessage, true, "UTF-8")`. Thiết lập người nhận (`user.getEmail()`), tiêu đề email (`Xác thực tài khoản của bạn`), và chèn nội dung HTML (`helper.setText(htmlContent, true)`).
  5. Gọi `javaMailSender.send(mimeMessage)`. Sử dụng `log.info` khi gửi thành công và `log.error` nếu xảy ra sự cố lỗi mạng/SMTP.

### BƯỚC 5: REFACTOR API REGISTER & TRIỂN KHAI API ACTIVATE

- **Tại API Đăng ký (`POST /api/v1/auth/register`):**
  1. Giữ nguyên logic kiểm tra trùng lặp email/username hiện tại.
  2. Gán các giá trị mặc định trước khi lưu: `user.setActivated(false);` và `user.setActivationKey(UUID.randomUUID().toString());`.
  3. Lưu thông tin User xuống database.
  4. Gọi hàm bất đồng bộ `mailService.sendActivationEmail(user);` để đẩy tiến trình gửi mail chạy ngầm.
  5. Trả về thông báo thành công dưới dạng String: _"Đăng ký thành công! Vui lòng kiểm tra email của bạn để thực hiện xác thực kích hoạt tài khoản."_

- **Tại API Kích hoạt tài khoản (`GET /api/v1/auth/activate`):**
  1. Tiếp nhận tham số `@RequestParam("key") String key`.
  2. Logic xử lý: Tìm kiếm User theo key bằng `userRepository.findByActivationKey(key)`. Nếu không tồn tại, lập tức `throw new BusinessException("Mã kích hoạt không hợp lệ hoặc đã được sử dụng!", HttpStatus.BAD_REQUEST);`.
  3. **Kiểm tra thời hạn hiệu lực (TTL):** Tính toán khoảng thời gian từ lúc tạo bản ghi (`user.getCreatedAt()`) đến thời điểm hiện tại `Instant.now()`. Nếu khoảng cách này lớn hơn thời gian cấu hình `application.mail.activation-ttl-minutes`:
     - Thực hiện xóa vĩnh viễn record user chưa kích hoạt hết hạn này khỏi hệ thống bằng `userRepository.delete(user)` để làm sạch database.
     - `throw new BusinessException("Liên kết xác thực đã hết hạn! Vui lòng thực hiện đăng ký lại tài khoản.", HttpStatus.BAD_REQUEST);`.
  4. Nếu hợp lệ: Cập nhật trạng thái `user.setActivated(true);` và hủy mã dùng một lần `user.setActivationKey(null);`.
  5. Thực hiện lưu thay đổi `userRepository.save(user);`.
  6. Ánh xạ thông tin đối tượng sang `UserResponseDTO` và trả về kết quả thành công cho Client.

## 4. TIÊU CHUẨN ĐẦU RA MÃ NGUỒN

- Toàn bộ mã nguồn phải biên dịch thành công 100%. Các package tổ chức rõ ràng (`config`, `service`, `web/rest`).
- Spring Security SecurityFilterChain phải được bổ sung cấu hình để whitelist endpoint `GET /api/v1/auth/activate` nhằm cho phép truy cập công khai mà không đòi hỏi Token hay Session.
