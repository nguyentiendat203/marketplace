# Task 10: Triển khai Module Sản phẩm, Danh mục Đa ngôn ngữ, Thuộc tính động (EAV) & Ảnh Thumbnail

Hãy giúp tôi hiện thực hóa cơ sở dữ liệu đã thiết kế sang mã nguồn Spring Boot hoàn chỉnh, hỗ trợ đa ngôn ngữ (vi, en, ja) theo mô hình Translation Table độc lập, hệ thống thuộc tính động (EAV) và cơ chế xử lý bộ sưu tập ảnh (Thumbnail & Gallery).

---

### 1. KIẾN TRÚC DATABASE & CẤU TRÚC ENTITY JPA

Hãy chuyển đổi toàn bộ các bảng dữ liệu sau thành các JPA Entity (Sử dụng Lombok, cấu hình quan hệ, Cascade, FetchType và Naming Strategy đồng bộ giữa snake_case ở DB và camelCase ở Java):

1. **Mô hình Danh mục:**
   - `Category` (Gốc): Có `ctg_image`, `ctg_parent_id` (đệ quy), `ctg_level`.
   - `CategoryTranslation`: `@ManyToOne` với `Category`, chứa `ctg_name`. Ràng buộc Unique (`category_id`, `language_code`).

2. **Mô hình Thương hiệu:**
   - `Brand` (Gốc): Chứa `brand_image`.
   - `BrandTranslation`: Chứa `brand_name`. Ràng buộc Unique (`brand_id`, `language_code`).

3. **Mô hình Thuộc tính động (EAV):**
   - `AttributeType` và `AttributeTypeTranslation` (chứa `attribute_name`).
   - `AttributeOption` (liên kết với `AttributeType`) và `AttributeOptionTranslation` (chứa `attribute_option_name`).

4. **Mô hình Sản phẩm hoàn chỉnh:**
   - `Product` (Gốc): Chứa `product_price` (Kiểu `BigDecimal`), `product_condition_note`, `product_status` (AVAILABLE, SOLD, HIDDEN), `original_language`, liên kết với `Category`, `Brand`, và `User` (Seller).
   - `ProductTranslation`: Chứa `product_title` và `product_description`. Ràng buộc Unique (`product_id`, `language_code`).
   - `Image` (Bộ sưu tập ảnh sản phẩm): Liên kết `@ManyToOne` với `Product`. Gồm các trường: `id`, `image_url` (lưu Object Key tương đối trên MinIO public) và `image_sort_order` (smallint).
     - _Quy ước đặc biệt:_ Ảnh có `image_sort_order = 0` bắt buộc là **Thumbnail** (ảnh hiển thị chính). Các ảnh phụ tiếp theo sẽ có số tự tăng `1, 2, 3...`.
   - `ProductAttribute` (Bảng trung gian mapping EAV): Liên kết `@ManyToOne` với `Product` và `@ManyToOne` với `AttributeOption`.

_Lưu ý:_ Hãy tạo Composite Index cho cặp `(entity_id, language_code)` trên tất cả các bảng dịch để tối ưu hóa tốc độ truy vấn `JOIN`.

---

### 2. CƠ CHẾ ĐA NGÔN NGỮ ĐỘNG (Language Resolution)

- Cấu hình một `WebMvcConfigurer` kèm theo `LocaleChangeInterceptor` để tự động bắt ngôn ngữ từ HTTP Request Header mang tên `Accept-Language` (Mặc định là `vi` nếu client không truyền).
- Viết một Helper Component để lấy nhanh mã ngôn ngữ hiện tại từ `LocaleContextHolder` nhằm phục vụ cho tầng Service query dữ liệu dịch.

---

### 3. CHI TIẾT CÁC API VÀ LOGIC SERVICE

Hãy viết Tầng Service xử lý các nghiệp vụ sau (Tích hợp chặt chẽ với cờ `kycVerified` của User từ Task 09):

#### API 1: Đăng bán sản phẩm (`POST /api/v1/products`)

- **Đầu vào (Multipart Form Data):** Dữ liệu sản phẩm, mảng cấu hình `attributeOptionId` được chọn, và danh sách file ảnh sản phẩm (`MultipartFile[] images`).
- **Logic xử lý ảnh đại diện (Thumbnail):** - Hệ thống quy ước file ảnh đầu tiên trong mảng (`images[0]`) hoặc file được chỉ định từ client gửi lên sẽ được gán cứng `image_sort_order = 0` để làm Thumbnail. Các file tiếp theo lưu với số thứ tự `1, 2, 3...`.
- **Luồng nghiệp vụ tổng thể:**
  1. Kiểm tra thuộc tính `kycVerified` của User hiện tại. Nếu `kycVerified == false`, ném lỗi `AccessDeniedException("Bạn phải hoàn thành xác thực danh tính (KYC) mới có quyền đăng bán sản phẩm!")`.
  2. Đi qua lớp `ImageUtils` (Task 09) để validate dung lượng (<5MB), định dạng (JPG/PNG/WEBP) và nén tất cả ảnh sản phẩm về định dạng JPEG.
  3. Đẩy toàn bộ ảnh đã nén lên **Bucket PUBLIC** `products` của MinIO. Lưu các Object Key vào bảng `image` kèm theo số thứ tự `image_sort_order` đúng như quy ước trên.
  4. Lưu thông tin gốc vào `Product`, lưu thông tin chữ vào `ProductTranslation` theo ngôn ngữ mặc định được truyền, và lưu các option vào bảng trung gian `ProductAttribute`.

#### API 2: Lấy danh sách & Chi tiết sản phẩm (`GET /api/v1/products` và `GET /api/v1/products/{id}`)

- **Yêu cầu nâng cao:** Hỗ trợ phân trang, sắp xếp và lọc động theo danh mục, khoảng giá, và mảng các `attributeOptionId`.
- **Logic dịch dữ liệu & mapping DTO:** - Sử dụng **Spring Data JPA Specification (Criteria API)** để xây dựng câu query động. Dựa vào ngôn ngữ lấy từ `LocaleContextHolder`, tự động `JOIN` bảng dịch tương ứng để lấy thông tin (Tiêu đề, danh mục, thương hiệu, tên thuộc tính và tên option).
  - _Cơ chế Fallback:_ Nếu ngôn ngữ yêu cầu không có bản dịch dưới DB, hệ thống tự động fallback về ngôn ngữ gốc (`original_language`) của bản ghi đó.
  - _Logic bóc tách ảnh trong DTO trả về:_ - Lấy ảnh có `image_sort_order = 0` nối chuỗi thành link tĩnh MinIO và gán vào trường `thumbnailUrl`.
    - Gom tất cả các ảnh có `image_sort_order > 0` (được sắp xếp tăng dần), nối chuỗi thành link tĩnh và gán vào danh sách `detailImages` (List<String>).

---

### 4. YÊU CẦU ĐẦU RA MÃ NGUỒN

Cung cấp mã nguồn chi tiết, đầy đủ không viết tắt cho:

1. Tập hợp tất cả các file Class Entity JPA được thiết kế chuẩn chỉ.
2. Các Class Specification phục vụ query động đa ngôn ngữ và thuộc tính.
3. Lớp DTO đầu ra hợp nhất cấu trúc phân tách rõ ràng giữa `thumbnailUrl` và danh sách `detailImages`.
4. `ProductServiceImpl.java` xử lý trọn vẹn luồng Đăng bán (set sort_order ảnh) và luồng Query.
