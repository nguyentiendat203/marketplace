# TASK-04: GLOBAL EXCEPTION HANDLING

## 1. BỐI CẢNH & MỤC TIÊU

- Dự án hiện đã có class `ApiResponse<T>` để đồng bộ dữ liệu đầu ra và `CustomResponseBodyAdvice` (sẽ tự động cho qua nếu `statusCode > 400`).
- Nhiệm vụ của bạn là refactor/triển khai hệ thống xử lý lỗi tập trung bằng `@RestControllerAdvice`.
- Yêu cầu: **TẤT CẢ** các hàm handle lỗi phải trả về kiểu `ResponseEntity<ApiResponse<Object>>` để kiểm soát chặt chẽ cả HTTP Status ở Header và JSON Body, đồng thời **KHÔNG** làm lộ các thông tin hệ thống nhạy cảm (như SQL Exception, NullPointerException thô) ra phía Frontend.

---

## 2. YÊU CẦU TRIỂN KHAI CHI TIẾT

### Bước 1: Tạo Linh Hồn Cho Lỗi Nghiệp Vụ (`BusinessException`)

Tạo class `BusinessException` trong package phù hợp (`core/exception` hoặc tương đương) theo đặc tả sau:

- Kế thừa từ `RuntimeException`.
- Chứa thêm một thuộc tính: `private final HttpStatus status;` để định nghĩa mã lỗi động (400, 403, 404, 409...).
- Khai báo Constructor nhận vào `String message` và `HttpStatus status`.
- Chỉ sử dụng `@Getter` của Lombok để đọc dữ liệu, không dùng `@Data`.

### Bước 2: Tạo Bộ Điều Hướng Lỗi Tập Trung (`GlobalExceptionHandler`)

Tạo class `GlobalExceptionHandler` và đánh dấu annotation `@RestControllerAdvice`. Sử dụng `@Slf4j` để ghi log. Tiến hành viết các phương thức bắt lỗi (bắt buộc trả về `ResponseEntity<ApiResponse<Object>>`):

#### 2.1. Xử lý lỗi nghiệp vụ động (`BusinessException`)

- Sử dụng `@ExceptionHandler(value = BusinessException.class)`.
- Ghi log cảnh báo: `log.warn(...)` chứa message và status.
- Trả về `ResponseEntity` với HTTP Status lấy động từ `ex.getStatus()`.
- JSON Body (`ApiResponse`): `statusCode` = giá trị của status, `success` = false, `message` = câu thông báo truyền vào từ exception, `data` = null.

#### 2.2. Xử lý lỗi Runtime fallback (`RuntimeException`)

- Sử dụng `@ExceptionHandler(value = RuntimeException.class)`.
- Mục đích: Bắt các lỗi Runtime thông thường khác chưa được phân loại cụ thể.
- Ghi log cảnh báo: `log.warn(...)`.
- Trả về `ResponseEntity` có status là `HttpStatus.BAD_REQUEST` (400).
- JSON Body (`ApiResponse`): `statusCode` = 400, `success` = false, `message` = `ex.getMessage()`, `data` = null.

#### 2.3. Xử lý lỗi hệ thống nghiêm trọng (`Exception`)

- Sử dụng `@ExceptionHandler(value = Exception.class)`.
- Mục đích: Bắt tất cả các lỗi không lường trước (Lỗi DB, NullPointer, Crash hệ thống...).
- **QUAN TRỌNG:** Phải ghi log FULL stack trace ra console bằng `log.error("💥 Lỗi hệ thống: ", ex)` để phục vụ việc Debug của lập trình viên.
- **BẢO MẬT:** Giấu sạch thông tin nhạy cảm. Không được đưa `ex.getMessage()` hay stack trace vào JSON trả về.
- Trả về `ResponseEntity` có status là `HttpStatus.INTERNAL_SERVER_ERROR` (500).
- JSON Body (`ApiResponse`): `statusCode` = 500, `success` = false, `message` = `"Hệ thống có lỗi xảy ra, vui lòng liên hệ Admin hoặc thử lại sau!"`, `data` = null.

---

## 3 . TIÊU CHUẨN MÃ NGUỒN

- Đảm bảo import đúng class `ApiResponse` sẵn có của dự án.
- Code sạch đẹp, cấu trúc phân lớp rõ ràng.
- Tuyệt đối không dùng `@Data` của Lombok (chỉ dùng `@Getter`, `@Setter`, `@RequiredArgsConstructor`).
- Đảm bảo sau khi code xong, toàn bộ dự án compile thành công 100%.
