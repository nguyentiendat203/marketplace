# AI Workflow Rules

## Approach

Xây dựng dự án SecondLife một cách cuốn chiếu và nghiêm ngặt (Lập trình dựa trên đặc tả). AI bắt buộc phải đọc và tuân thủ tuyệt đối cấu trúc hệ thống đã định nghĩa trong `project-overview.md`, `architecture.md`

- Không tự ý suy diễn hoặc phát minh ra các hành vi nghiệp vụ nằm ngoài đặc tả.
- Mọi dòng code sinh ra cho Backend phải tương thích hoàn toàn với **Spring Boot 4.0.6** và Frontend với **React TypeScript**.

## Scoping Rules

- Chỉ tập trung xử lý đúng 1 đơn vị tính năng (Feature Unit) hoặc 1 API route tại một thời điểm.
- Nghiêm cấm việc viết code trước cho các tính năng thuộc Phase sau (Ví dụ: Không code Chat, WebSocket, AI khi đang ở Phase 1).
- Ưu tiên các thay đổi nhỏ, có thể kiểm thử ngay thay vì viết một loạt các thay đổi lớn mang tính suy đoán.
- Không gộp nhiều ranh giới hệ thống độc lập (System Boundaries) vào chung một bước thực hiện.

## When to Split Work

AI bắt buộc phải chia nhỏ bước thực hiện nếu yêu cầu của người dùng kết hợp một trong các yếu tố sau:

- Tác vụ vừa thay đổi giao diện FE nặng vừa thay đổi logic xử lý ngầm (Background task/Async) ở BE.
- Tạo hoặc sửa đổi nhiều API Route không liên quan trực tiếp đến nhau trong cùng một lúc.
- Yêu cầu sửa đổi đồng thời cả 2 module tính năng độc lập (Ví dụ: Vừa sửa logic `auth` vừa sửa logic `kyc`).
- Nghiêm cấm triển khai khi yêu cầu nghiệp vụ chưa được định nghĩa rõ ràng trong các file context.

## Handling Missing Requirements

- Không tự ý chế ra logic sản phẩm nếu file context chưa quy định.
- Nếu yêu cầu bị mơ hồ, AI phải thảo luận với user để làm rõ và cập nhật vào file context tương ứng trước khi bắt tay vào code.

## Architectural Invariants (Luật kiến trúc tối kỵ)

- **Modular Boundaries:** Cấm tuyệt đối việc Inject trực tiếp `Repository` của module này vào `Service`/`Controller` của module khác. Module A muốn lấy dữ liệu của Module B bắt buộc phải gọi qua `Service` Interface của Module B.
- **Storage Routing:** Cấm AI tự viết logic kết nối Client MinIO hoặc upload ảnh thô bên trong các module lẻ (`product`, `kyc`). Mọi tác vụ upload bắt buộc phải gọi qua `StorageService` đặt tại package `backend/src/main/java/vn/datnguy3n/marketplace/common/storage/` và truyền đúng tên bucket chỉ định (`products` hoặc `kyc-vault`).

## Protected Files

Tuyệt đối không được tự ý chỉnh sửa các thư mục/file sau trừ khi có lệnh cụ thể từ user:

- `frontend/src/components/ui/*`: Đây là các UI components được sinh tự động bởi thư viện `shadcn/ui`.
- `backend/src/main/java/vn/datnguy3n/marketplace/common/*`: Thư mục chứa các class cấu trúc và tiện ích dùng chung toàn hệ thống .
- Các file cấu hình môi trường gốc như `docker-compose.yml` và `.env` (Nếu cần thêm biến môi trường, phải báo cho user biết).

## Before Moving to the Next Unit

Trước khi báo cáo hoàn thành và chuyển sang tính năng tiếp theo, AI phải đảm bảo:

1. Tính năng hiện tại chạy ổn định end-to-end từ API BE đến giao diện FE (trong phạm vi test).
2. Không vi phạm bất kỳ quy tắc tối kỵ nào trong mục `Architectural Invariants`.
3. Dự án build không lỗi .
