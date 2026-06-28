Tôi muốn triển khai hệ thống Phân quyền động dựa trên cấu hình (Dynamic RBAC) sử dụng Spring Boot và Spring Security. Hệ thống này cho phép quản lý các quyền truy cập thông qua việc lưu trữ API Path và HTTP Method trực tiếp dưới Database thay vì sử dụng annotation `@PreAuthorize` cứng trong code.

Tôi đã có sẵn các Entity, hãy đọc cấu trúc giả định sau để viết logic (không cần sinh lại code cho các Entity này):

- **User:** Có quan hệ `@ManyToMany` với `Role` (`Set<Role> roles`).
- **Role:** Có trường `name` (Ví dụ: ROLE_USER, ROLE_SUPER_ADMIN) và quan hệ `@ManyToMany` với `Permission` (`Set<Permission> permissions`).
- **Permission:** Thực thể lưu thông tin API để check quyền, gồm các trường:
  - `String apiPath;` (Đường dẫn API, ví dụ: "/api/v1/products/\*\*" hoặc "/api/v1/products")
  - `String method;` (Phương thức HTTP, ví dụ: "POST", "GET", "PUT", "DELETE")

Hãy viết code hoàn chỉnh cho các thành phần logic sau:

### 1. Bộ lọc Phân quyền Động (Dynamic Authorization Filter)

- Tạo lớp `DynamicAuthorizationFilter` kế thừa từ `OncePerRequestFilter` của Spring Security.
- **Logic xử lý trong `doFilterInternal`:**
  1. Trích xuất `apiPath` (Request URI) và `httpMethod` từ `HttpServletRequest`.
  2. Lấy thông tin `Authentication` hiện tại từ `SecurityContextHolder`. Nếu user đã authenticated và không phải Anonymous:
     - Lấy ra danh sách các Roles của User.
     - **Bypass Rule:** Nếu User có quyền `ROLE_ADMIN`, cho phép đi tiếp ngay lập tức (`filterChain.doFilter`) mà không cần check DB.
  3. Nếu không phải Super Admin, gọi sang `RolePermissionService` để dò dưới DB xem với danh sách Roles hiện tại của User, có bất kỳ Permission nào khớp với cặp (`apiPath` + `httpMethod`) đang gọi hay không.
     - _Lưu ý:_ Sử dụng `AntPathMatcher` để so khớp chính xác các url dạng wildcard như `/**`.
  4. Nếu hợp lệ -> Cho qua. Nếu không hợp lệ -> Ném ra `AccessDeniedException` để Spring Security tự động xử lý qua `AccessDeniedHandler`.

### 2. Cấu hình Spring Security (`SecurityConfiguration`)

- Đăng ký `DynamicAuthorizationFilter` vào chuỗi `SecurityFilterChain`.
- Vị trí đặt filter: Đặt ngay **SAU** filter xác thực JWT (`JwtAuthenticationFilter` hoặc `UsernamePasswordAuthenticationFilter`) để đảm bảo danh tính user đã được xác thực trước khi check quyền.
- Đảm bảo các API công khai (nhên Đăng nhập, Đăng ký) được `permitAll()` trước khi đi qua bộ lọc này.

### 3. API CRUD quản lý Role và Permission

- Viết Controller, Service (Interface & Impl) thực hiện các thao tác CRUD cơ bản (Create, Read, Update, Delete, Get All) cho `Role` và `Permission`.
- **Yêu cầu đặc biệt cho API Read:** API `GET` danh sách Role và `GET` chi tiết 1 Role cần trả về kèm theo chi tiết danh sách các `Permission` đang được gán cho Role đó để thuận tiện cho việc kiểm tra dữ liệu.
- **Yêu cầu API Gán/Gỡ quyền:** Viết thêm logic API để Gán (Assign) một danh sách các Permission vào một Role, và Gỡ (Remove) Permission khỏi Role.
- **Lưu ý quan trọng (Clean Code):** Bắt buộc sử dụng **DTO (Request/Response)** để giao tiếp tại Controller, tuyệt đối không trả trực tiếp Entity ra ngoài để tránh lỗi đệ quy vòng lặp JSON (Infinite Recursion) sinh ra từ quan hệ `@ManyToMany`.

Hãy viết toàn bộ mã nguồn cho các file: Repository liên quan, Service/ServiceImpl xử lý check quyền động, Custom Filter, SecurityConfig và logic hàm register trong UserService. Đảm bảo tuân thủ Clean Code và không viết tắt.
