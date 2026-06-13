# Code Standards

## General Principles

- **Single Responsibility Principle (SRP):** Mỗi file, component, method hoặc class chỉ thực hiện đúng một nhiệm vụ duy nhất. Quy mô module phải nhỏ và dễ cô lập để test.
- **Root Cause Resolution:** Luôn luôn giải quyết triệt để tận gốc nguyên nhân gây lỗi (Root Cause). Tuyệt đối không viết code "bọc lót" (workarounds) hoặc bắt ngoại lệ chung chung để che giấu lỗi logic.
- **Separation of Concerns:** Không trộn lẫn các tầng xử lý dữ liệu với nhau. Logic giao diện (UI), logic nghiệp vụ (Business Logic), và logic gọi API phải tách biệt hoàn toàn.

---

## Naming Conventions

Để tránh lỗi biên dịch và xung đột hệ thống, toàn bộ dự án phải tuân thủ nghiêm ngặt quy tắc đặt tên sau:

### 1. Backend (Java Spring Boot)

- **Package Name:** Sử dụng chữ thường toàn bộ, không chứa dấu gạch dưới `_` hay gạch ngang `-` (Ví dụ: `modules.auth.dto`, `modules.product`).
- **Class Name:** Sử dụng `PascalCase` (Ví dụ: `LoginRequest.java`, `ProductService.java`).
- **Method & Variable Name:** Sử dụng `camelCase` (Ví dụ: `generateJwtToken()`, `userId`).
- **Database Table & Column:** Sử dụng `snake_case` chữ thường toàn bộ (Ví dụ: `user_block`, `stripe_payment_intent_id`).

### 2. Frontend (ReactTS)

- **Component Files & Folders:** Sử dụng `PascalCase` cho cả tên thư mục component và tên file `.tsx` (Ví dụ: `LoginForm/LoginForm.tsx`, `ProductCard.tsx`).
- **Hooks & Utilities & Slices:** Sử dụng `camelCase` hoặc `kebab-case` cho các file không chứa UI (Ví dụ: `useAuth.ts`, `axiosCustomize.ts`, `authSlice.ts`).
- **TypeScript Interface/Type:** Sử dụng `PascalCase` và đặt tên tường minh, không dùng tiền tố `I` (Ví dụ: `UserDTO`, `ProductAttribute`).

---

## ReactTS (TypeScript) Rules

- **Strict Mode Enforcement:** Cấu hình `strict: true` trong `tsconfig.json` là bắt buộc. Không lạm dụng kiểu dữ liệu `any`. Khi gặp dữ liệu chưa rõ ràng, bắt buộc dùng `unknown` kết hợp Type Guard hoặc Type Assertion an toàn.
- **Component Structure:** 100% sử dụng Functional Component phối hợp với Arrow Functions. Không sử dụng Class Component.
- **State Management (Redux Toolkit):** - Tránh nâng cấp State lên Redux Store nếu dữ liệu đó chỉ dùng cục bộ trong một Component (ví dụ: trạng thái đóng mở Modal, giá trị Input đang nhập).
  - Tất cả các lệnh gọi API bất đồng bộ phải được đóng gói qua Async Thunk nằm trong thư mục `src/redux/slice/`.
- **Boundary Validation:** Mọi dữ liệu trả về từ API (External Input) phải được kiểm tra cấu hình hoặc ép kiểu an toàn tại Header/Interceptors của Axios trước khi phân phối vào Redux Store hay Component.

---

## Java Spring Boot Rules

- **Architecture Boundary:** Tuân thủ cấu trúc Package-by-Feature (`modules/[feature_name]`). Code của module nào phải nằm trọn vẹn trong module đó.
- **Dependency Injection Guard:** Tuyệt đối không cho phép một Module này `@Autowired` hoặc Inject trực tiếp `@Repository` của một Module khác. Mọi tương tác liên thông bắt buộc phải đi qua tầng `@Service` tương ứng để đảm bảo tính toàn vẹn dữ liệu.
- **BaseEntity Extension Constraint:** - 100% các lớp `@Entity` (ngoại trừ các bảng trung gian thuần túy của mối quan hệ Many-to-Many) không được tự định nghĩa trường `id` hay các trường thời gian một cách thủ công.
  - Tất cả bắt buộc phải `extends BaseEntity` để kế thừa cấu trúc gồm 7 trường chuẩn hóa: `id`, `created_at`, `created_by`, `updated_at`, `updated_by`, `deleted_at`, và `deleted_by`.
  - Tích hợp các Annotation `@SoftDelete` hoặc `@Where` của Hibernate để tự động lọc bỏ các bản ghi có `deleted_at != null` trong mọi câu lệnh truy vấn (Select).
- **Data Transfer Objects (DTO):** Không bao giờ trả trực tiếp thực thể Database (`@Entity`) về cho client ở tầng Controller. Mọi Request/Response phải được map qua các lớp DTO riêng biệt để bảo mật thông tin.
- **Lombok Usage Caution:** Sử dụng `@Getter`, `@Setter` và `@RequiredArgsConstructor` thay cho `@Data`. Tuyệt đối không dùng `@EqualsAndHashCode` hoặc `@ToString` trên các Entity có mối quan hệ `@OneToMany` hoặc `@ManyToMany` để tránh lỗi tràn bộ nhớ (Infinite Loop / StackOverflowError).
- **Validation:** Bắt buộc sử dụng các Annotation đi kèm như `@Valid`, `@NotBlank`, `@Size`, `@Min` tại tầng Controller để chặn đứng dữ liệu rác ngay ở cửa ngõ API.
- **Generic CRUD Enforcement:**
  - 100% các APIs CRUD cơ bản của các module không được viết lại code thủ công.
  - Bắt buộc phải triển khai và kế thừa thông qua bộ khung Generics tại gói core: `BaseCRUDService`, `BaseCRUDServiceImpl`, và `BaseCRUDController` sử dụng kiểu định danh `UUID`.

---

## Styling (Tailwind CSS)

- **Pure Utility-First:** Ưu tiên sử dụng 100% các class tiện ích của Tailwind CSS trực tiếp trên thẻ. Không lồng CSS thuần (`.css` files) ngoại trừ cấu hình theme toàn cục.
- **Dynamic Class Constraint:** Không sử dụng cơ chế cộng chuỗi string tự chế để tạo class Tailwind động (Ví dụ không làm: `text-${color}-500`). Luôn luôn viết tường minh toàn bộ tên class (Ví dụ nên làm: `color === 'red' ? 'text-red-500' : 'text-blue-500'`) để tránh bị trình biên dịch Tailwind xóa nhầm class (Purge CSS).

---

## API Design Standards

- **Strict Input Parsing:** Mọi API thay đổi trạng thái (POST, PUT, DELETE) phải parse và validate dữ liệu đầu vào trước khi thực thi bất kỳ dòng logic nghiệp vụ nào.
- **Security & Ownership Check:** Trước khi thực hiện các hành vi chỉnh sửa (Mutation) như sửa sản phẩm, xóa comment, hủy đơn... Backend phải kiểm tra quyền hạn của user có được phép mutation trên tài nguyên đó không (JWT).
- **Predictable Response Shape:** Toàn bộ API của hệ thống phải trả về một cấu trúc JSON đồng nhất chuẩn hóa:
  ```json
  {
    "success": true/false,
    "statusCode": {...} ,
    "message": "Thông báo phản hồi trực quan",
    "data": { ... } // Hoặc null nếu lỗi
  }
  ```
