Hiện tại, `BaseCRUDController`, `BaseCRUDService`, `BaseCRUDServiceImpl` và `BaseRepository` của tôi đang sử dụng một Generic type`T`(đại diện cho Entity).
Vấn đề là khi trả response về, Hibernate serialize toàn bộ `Entity T`thành JSON, làm lộ các thông tin rác hoặc nhạy cảm (và các Audit fields: createdBy, updatedBy,... trong BaseEntity). Hệ thống không có cơ chế lọc field vì nó chỉ nhìn thấy kiểu`T`.

Để khắc phục, tôi muốn refactor lại kiến trúc này sang mô hình Generic DTO Mapping. Yêu cầu cụ thể như sau:

1. **Thiết kế lại `BaseController` và `BaseService` cũng như `BaseCRUDServiceImpl` và `BaseRepository` để nhận vào 2 Generic Types: `<T, D>`, trong đó `T` là Entity, `D` là ResponseDTO.**
2. **Xây dựng một interface `BaseMapper<T, D>` (hoặc sử dụng abstract method) để giải quyết bài toán map từ Entity sang DTO ở tầng Base, vì Java không thể tự instantiate hay map các Generic types. (Có thể gợi ý dùng MapStruct hoặc ModelMapper nếu cần thiết).**
3. **Các hàm cơ bản như CRUD phải trả về kiểu `D` (DTO) thay vì `T` (Entity).**
4. **Viết code minh họa chi tiết cho một luồng hoàn chỉnh bao gồm:**
   - BaseMapper<T, D>
   - BaseService<T, D> và BaseServiceImpl<T, D>
   - BaseController<T, D>

5.**Tôi muốn refactor lại toàn bộ kiến trúc này sang mô hình Generic DTO Mapping sử dụng MapStruct, yêu cầu chi tiết như sau:**

**1. Tầng Mapper (MapStruct):**

- Tạo một `@MapperConfig` (ví dụ: `BaseMapperConfig`) chứa cấu hình chung: `componentModel = "spring"`, `unmappedTargetPolicy = ReportingPolicy.IGNORE`, và đặc biệt là `nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE` để phục vụ Partial Update.
- Tạo interface `BaseMapper<T, D>` (T: Entity, D: DTO). Khai báo các hàm:
- `D toDto(T entity);`
- `T toEntity(D dto);`
- `void updateEntityFromDto(D requestDto, @MappingTarget T entity);`

**2. Tầng Service (`BaseCRUDServiceImpl<T, ID, D>`):**

- Áp dụng 2 abstract methods để class con tự cung cấp: `protected abstract JpaRepository<T, ID> getRepository();` và `protected abstract BaseMapper<T, D> getMapper();`
- Viết lại hàm `update(ID id, D requestDto)` theo luồng sau:
  - Gọi `fetchEntityById(id)` để lấy Entity cũ (`existingEntity`) từ DB.
  - Gọi `getMapper().updateEntityFromDto(requestDto, existingEntity)` để đè dữ liệu mới (MapStruct sẽ tự động ignore các trường null).
  - (Không cần set lại ID thủ công).
  - Gọi các hook `beforeUpdate(existingEntity)` -> `save` -> `afterUpdate(updatedEntity)`.
  - Trả về `getMapper().toDto(updatedEntity)`.
- Xóa bỏ hoàn toàn hàm `mergeEntity` cũ.

**3. Tầng Controller (`BaseController<T, ID, D>`):**

- Các endpoint như `GET`, `POST`, `PUT/PATCH` làm việc hoàn toàn với kiểu `D` (DTO), không sử dụng kiểu `T` (Entity).

**4. Áp dụng refactor cho TẤT CẢ các module trong hệ thống:**

- Cập nhật tất cả các file Mapper (ProductMapper, OrderMapper,...) để dùng `@MapperConfig` và kế thừa `BaseMapper`, xóa bỏ các cấu hình thừa.
- Dọn dẹp sạch sẽ tất cả các class `ServiceImpl`: xóa hoàn toàn các hàm `mergeEntity` và logic update thủ công cũ. Các file này bây giờ phải cực kỳ ngắn gọn và chỉ chứa logic business đặc thù (nếu có).
- Đảm bảo tất cả Controller của các module đều giao tiếp hoàn toàn bằng DTO.

Hãy viết code thật clean, tuân thủ SOLID và sửa tất cả các Controller liên quan mà có extends BaseCRUDController.
