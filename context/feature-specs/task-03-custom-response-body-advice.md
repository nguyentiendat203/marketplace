Hãy REFACTOR dự án Spring Boot theo các yêu cầu cốt lõi sau:

1. Auto-wrap Response (CustomResponseBodyAdvice):

- Tạo class `@RestControllerAdvice` triển khai `ResponseBodyAdvice<Object>`.
- Tự động bọc mọi response thành object `ApiResponse<T>` với các trường: `statusCode` (từ HTTP status), `success` (true), `message` ("Thao tác thành công"), và `data` (gán bằng body gốc).
- Bỏ qua (giữ nguyên body gốc) nếu body là: null, đã là instance của `ApiResponse`, hoặc là các class Exception/Error.
- Xử lý riêng kiểu `String`: Dùng `ObjectMapper` convert `ApiResponse` thành JSON String trước khi return để tránh ClassCastException.

2. Cấu trúc Phân trang (ResultPaginationResponse):

- Tạo cấu trúc class lồng nhau (nested class) chính xác như sau:
  @Getter @Setter
  public class ResultPaginationResponse {
  private Meta meta;
  private Object result;

      @Getter @Setter
      public static class Meta {
          private int page;
          private int pageSize;
          private int pages;
          private long total;
      }

  }

3. Dynamic Filter (JPA Specification):

- Cấu hình các Repository liên quan kế thừa `JpaSpecificationExecutor<T>`.
- Tạo bộ parse các Query Parameters từ URL thành đối tượng `Specification<T>`.
- Cập nhật các hàm `getAll` ở Controller và Service: Nhận vào `Specification<T>` + `Pageable`, thực hiện truy vấn, map kết quả từ `Page<T>` sang `ResultPaginationResponse` (đổ dữ liệu phân trang vào `Meta` và danh sách vào `result`).

Yêu cầu: Code sạch, dùng đúng cấu trúc ApiResponse và ResultPaginationResponse được cung cấp, không dùng Lombok `@Data` (chỉ dùng `@Getter`, `@Setter`), đảm bảo dự án compile thành công.
