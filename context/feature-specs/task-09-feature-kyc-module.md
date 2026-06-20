# Triển khai Module KYC (Xác thực danh tính) - Spring Boot & MinIO

### 1. Lưu ý về Cấu trúc Database (Entity KycRecord):

- Tôi đã có Entity `KycRecord` map với bảng `kyc_records` theo quy tắc Column Prefix.
- **Yêu cầu Refactor tên trường:** Hãy đổi tên các trường trong bảng `kyc_records` kết thúc bằng `Url` thành `Key` (gồm: `frontImageKey`, `backImageKey`, `selfieKey`) vì hệ thống sẽ lưu **Object Key** (đường dẫn tương đối trong private bucket của MinIO) chứ không lưu link tuyệt đối.
- Trường `documentType` nên là `enum` các loại giấy tờ hợp pháp được chấp nhận để định danh (RESIDENT_CARD,PASSPORT, DRIVER_LICENSE,... )
- Các trường `reviewNote` (lưu lý do từ chối/ghi chú) và `reviewedBy` (ID Admin duyệt) đã có sẵn trong Entity, hãy sử dụng chúng khi Admin đánh giá hồ sơ.

### 2. Chi tiết các API cần viết code:

#### API 1: User gửi hồ sơ KYC (`POST /api/v1/kyc/submit`)

- **Đầu vào (Multipart Form Data):** `documentType`, `frontImage`, `backImage` (nếu có), `selfieImage` (nếu có).
- **Logic Service:** 1. Lấy User hiện tại từ Security Context. Chặn nếu phát hiện đã có hồ sơ `PENDING` hoặc `APPROVED`. 2. Đẩy các file ảnh lên **Bucket PRIVATE** của MinIO. Đặt tên file theo format: `kyc/{userId}/{UUID}_{front|back|selfie}` rồi lấy Object Key lưu vào các trường tương ứng của `KycRecord`. 3. Tạo mới bản ghi `KycRecord` với trạng thái mặc định là `PENDING`.

#### API 2: Admin lấy danh sách hồ sơ (`GET /api/v1/admin/kyc`)

- **Phân quyền:** Chỉ `ROLE_ADMIN`. Cần hỗ trợ phân trang (Pageable) và lọc theo `status`.
- **Logic bảo mật:** Duyệt qua danh sách kết quả, sử dụng MinIO Client để tạo **Presigned URL (hạn dùng 15 phút)** từ các Object Key ảnh (`frontImageKey`, `backImageKey`, `selfieKey`) trước khi trả về Response DTO cho Frontend hiển thị.

#### API 3: Admin Duyệt/Từ chối hồ sơ (`PUT /api/v1/admin/kyc/{id}/review`)

- **Đầu vào (Request Body DTO):** `{ "status": "APPROVED/REJECTED", "reviewNote": "Lý do từ chối hoặc ghi chú..." }`
- **Logic Service:** 1. Kiểm tra bản ghi `KycRecord` theo `id`, nếu trạng thái khác `PENDING` thì báo lỗi. 2. Lưu nội dung từ chối vào `reviewNote` và cập nhật `reviewedBy` bằng ID của Admin hiện tại. 3. Nếu `REJECTED`: Cập nhật trạng thái hồ sơ thành `REJECTED`. 4. Nếu `APPROVED`: Cập nhật trạng thái hồ sơ thành `APPROVED`, đồng thời tìm Entity `User` liên kết để cập nhật cờ `kycVerified = true`.

### 3. Tiêu chuẩn Kỹ thuật & Luồng xử lý File (Bổ sung nâng cao):

#### A. Tự động khởi tạo Bucket (`MinioConfig.java`)

- Khi ứng dụng chạy (`@PostConstruct`), tự động kiểm tra và tạo 2 bucket nếu chưa có:
  1. `kyc-vault`: Cấu hình mặc định (Private) để lưu ảnh KYC nhạy cảm.
  2. `products`: Cấu hình chính sách **Public Read-Only** (sử dụng `setBucketPolicy` với JSON Allow GetObject) để lưu ảnh sản phẩm công khai, giúp Frontend truy cập trực tiếp bằng link tĩnh không cần presigned URL.

#### B. Thẩm định và Nén ảnh (`ImageUtils.java`)

- **Validate:** Chặn ngay nếu file > 5MB hoặc sai định dạng (chỉ nhận `image/jpeg`, `image/png`, `image/webp`). Nếu sai ném ngoại lệ `InvalidFileException`.
- **Nén ảnh (Compress):** Dùng thư viện chuẩn Java nén ảnh đầu vào về định dạng đích **JPEG** với chất lượng `0.75f` để tiết kiệm dung lượng ổ cứng nhưng giữ nguyên độ nét trước khi đẩy lên MinIO.

#### C. Xử lý ngoại lệ

- **Xử lý lỗi tập trung:** Các đoạn code không đúng phải `throw new BusinessException(...)` để hệ thống xử lý lỗi đồng bộ thông qua `GlobalExceptionHandler`.

### 4. Yêu cầu Đầu ra Mã nguồn:

Hãy viết code hoàn chỉnh không lược bớt cho các file:

1. File định nghĩa Enum (`KycStatus`, `KycDocumentType`).
2. Giao diện `KycRecordRepository`.
3. Bộ đôi tiện ích `ImageUtils.java` và lớp cấu hình `MinioConfig.java`.
4. Lớp xử lý logic `KycServiceImpl.java` (bao gồm tích hợp MinioClient xử lý luồng upload ảnh đã nén và sinh link presigned).
5. Hai bộ Controller `KycController` và `AdminKycController` phân quyền chặt chẽ.
