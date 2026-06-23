Tôi muốn tối ưu hóa hệ thống Backend Spring Boot, giải quyết triệt để hai vấn đề: Vòng lặp vô hạn JSON (Infinite Recursion) khi lấy dữ liệu và Lỗi mất dữ liệu khi cập nhật một phần (Partial Update) trên toàn bộ dự án.

Hãy giúp tôi rà soát lại tất cả các Entity, Controller, ServiceImpl, Base Service và thực hiện sửa đổi theo 4 yêu cầu nghiêm ngặt sau:

1. **Cắt vòng lặp ở Entity:** - Thêm các annotation `@JsonIgnore` hoặc `@JsonIgnoreProperties` hợp lý tại các trường quan hệ `@OneToMany` (`translations`, `images`, `attributes`) và các trường `@ManyToOne` tham chiếu ngược ở các thực thể con để bảo vệ hệ thống khỏi lỗi serialize.

2. **Áp dụng DTO Pattern nghiêm ngặt:** - Tạo các lớp Response DTO phẳng và sạch sẽ như `ProductResponse`, `CategoryResponse`, `BrandResponse`.
   - Khi gọi API lấy danh sách (`getAll`), mặc định các DTO này **CHỈ chứa thông tin cơ bản**, tuyệt đối không tự động map các danh sách List liên quan vào nếu tôi không chủ động yêu cầu.

3. **Cơ chế Chủ động lấy (Explicit Fetching):**
   - Viết các hàm chuyển đổi dữ liệu (Mapping) trong Service hoặc dùng MapStruct sao cho: Chỉ khi nào tôi chủ động gọi API lấy chi tiết (`getById`) hoặc truyền thêm cờ điều kiện, hệ thống mới map danh sách `images` hoặc `attributes` vào DTO trả về.

4. **Refactor hàm Update tại Base Service và tích hợp MapStruct để xử lý Partial Update:**
   - Hiện tại hàm `update(UUID id, T entity)` trong `BaseCRUDServiceImpl` đang lưu đè trực tiếp đối tượng nhận được từ Client, dẫn đến việc các trường không truyền lên sẽ bị mang giá trị `null` và đè chết dữ liệu cũ trong DB.
   - Hãy refactor lại hàm `update` ở lớp `BaseCRUDServiceImpl` theo luồng: Lấy entity cũ đầy đủ từ DB lên (`T existingEntity = getById(id);`), sau đó gọi một hàm hook trống là `protected void mergeEntity(T source, T target)`. Cuối cùng mới lưu `existingEntity` đã được merge sạch dữ liệu xuống DB.
   - Định nghĩa các interface MapStruct Mapper (ví dụ: `ProductMapper`) cho từng module với cấu hình chiến lược `nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE` để tự động bỏ qua các trường `null`.
   - Tại các Service con (như `ProductServiceImpl`), hãy override lại hàm `mergeEntity` và tiêm Mapper tương ứng vào để thực hiện đè dữ liệu một cách an toàn.

Hãy rà soát toàn bộ hệ thống (Product, Category, Brand, Attribute) và sinh code chuẩn chỉnh, sạch sẽ, không làm lỗi compile dự án giúp tôi.
