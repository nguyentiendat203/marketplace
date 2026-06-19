# Cập nhật Code Standards và Refactor TOÀN BỘ Entity trong dự án theo quy tắc Column Prefix

Tôi muốn áp dụng quy tắc đặt tên cột dưới Database có tiền tố (Column Prefix) cho **tất cả các Entity hiện có trong dự án** để đồng bộ hóa và tránh xung đột khi JOIN bảng.

### 1. Cập nhật file `code-standards.md`

Hãy bổ sung quy tắc này vào mục "Naming Conventions" trong phần Backend:

- **Quy tắc:** Tất cả các thuộc tính trong Entity (ngoại trừ trường `id` và các trường audit kế thừa từ `BaseEntity`) khi map xuống Database bắt buộc phải cấu hình `@Column(name = "prefix_ten_cot")`.
- **Định dạng:** `prefix_` là chữ viết tắt (từ 3-4 ký tự, dạng snake_case) đại diện cho bảng đó.

### 2. Bảng quy ước Tiền tố (Prefix Mapping Table) cho dự án:

Để đảm bảo tính nhất quán, hãy áp dụng chính xác các tiền tố sau cho từng bảng:

- Entity `User` -> prefix: `usr_` (Ví dụ: `usr_email`, `usr_password`)
- Entity `Role` -> prefix: `role_` (Ví dụ: `role_name`)
- Entity `Product` -> prefix: `prod_` (Ví dụ: `prod_name`, `prod_price`)
- Entity `Category` -> prefix: `cat_` (Ví dụ: `cat_name`, `cat_level`)
- Entity `KycInformation` -> prefix: `kyc_` (Ví dụ: `kyc_status`, `kyc_front_image`)
- Entity `PasswordResetToken` -> prefix: `prt_` (Ví dụ: `prt_token`, `prt_expiry_date`)
- ...
- _(Đối với các Entity khác chưa có trong danh sách này, hãy tự đề xuất prefix ngắn gọn từ 3-4 ký tự dựa trên tên lớp)._

### 3. Quy tắc cấu hình Quan hệ (Relationships):

- Đối với các trường `@JoinColumn` (Khóa ngoại), tên cột phải kết hợp: `prefix_bảng_hiện_tại_` + `tên_trường_id`.
- Ví dụ: Trong class `User`, trường liên kết với `Role` sẽ là `@JoinColumn(name = "usr_role_id")`.

### 4. Hướng dẫn thực hiện:

Tôi sẽ cung cấp mã nguồn của các Entity (hoặc bạn hãy quét qua codebase). Với mỗi Entity, hãy:

1. Thêm/Sửa lại `@Column(name = "...")` cho tất cả các trường theo đúng bảng quy ước tiền tố ở trên.
2. Giữ nguyên các cấu hình khác như `nullable`, `unique`, `length` hoặc `columnDefinition`.
3. Không thêm prefix vào các trường kế thừa từ `BaseEntity` (như `id`, `createdAt`, `updatedAt`, `deletedAt`).

---
