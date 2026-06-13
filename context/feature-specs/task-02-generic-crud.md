# Task Specification: Implement Generic CRUD Architecture

## 1: Context Priming (MANDATORY)

1. Read `AGENTS.md` at the project root.
2. `context/code-standards.md` – Focus on the **Generic CRUD Enforcement**, **BaseEntity Extension**, and API Response shape.

---

## 2. Core Components to Create (`vn.datnguy3n.marketplace.core.crud`)

### A. BaseCRUDService<T>

- Standard CRUD methods using `UUID` keys and a single Entity type `<T>`:
  - `T create(T entity);`
  - `T update(UUID id, T entity);`
  - `T getById(UUID id);`
  - `Page<T> getAll(Pageable pageable);`
  - `void delete(UUID id);`

### B. BaseCRUDServiceImpl<T>

- Implement `BaseCRUDService<T>`, inject `JpaRepository<T, UUID>` qua Constructor.
- Direct Entity Operations: Xử lý trực tiếp trên Entity `<T>`, không có DTO và hoàn toàn không có logic mapping.
- Core Transaction: `@Transactional` (Các hàm Read sử dụng `readOnly = true`).
- Protected Lifecycle Hooks (Template Pattern): `beforeCreate(T)`, `afterCreate(T)`, `beforeUpdate(T)`, `afterUpdate(T)` với body trống để các module con tự override khi cần bổ sung nghiệp vụ riêng.
- Soft Delete: `delete(UUID)` gọi `repository.deleteById(id)` để kích hoạt `@SQLDelete`.

### C. BaseCRUDController<T>

- Expose REST Endpoints: `POST /`, `PUT /{id}`, `GET /{id}`, `GET /`, `DELETE /{id}` nhận và trả về kiểu dữ liệu `<T>`.
- Enforce Uniform Response: Toàn bộ kết quả phải được bọc trong `ResponseEntity<ApiResponse<T>>`.

## 3. Strict Implementation Rules

- **No Lombok `@Data`:** Chỉ dùng `@Getter`, `@Setter`, và `@RequiredArgsConstructor`.
- **Exception Handling:** Nếu không tìm thấy record, throw `RuntimeException("Record not found with id: " + id)`.
- **ApiResponse Stub:** Nếu chưa có `ApiResponse<T>`, tự khởi tạo trong `core.dto` với 3 trường: `success` (boolean), `message` (String), `data` (T).
