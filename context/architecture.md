# Architecture Context

## Stack

| Layer           | Technology                         | Role                                                                                 |
| --------------- | ---------------------------------- | ------------------------------------------------------------------------------------ |
| Frontend        | React (TypeScript)                 | Xây dựng giao diện Single Page Application (SPA) tương tác cao.                      |
| State Mgmt      | Redux Toolkit                      | Quản lý trạng thái toàn cục (Global State)                                           |
| UI / Styling    | Tailwind CSS + shadcn/ui           | Đảm bảo ngôn ngữ thiết kế nhất quán, giao diện gọn gàng, responsive.                 |
| Backend         | Java Spring Boot 4.0.6             | Xây dựng hệ thống RESTful API, xử lý nghiệp vụ lõi (Core Business Logic).            |
| Auth & Security | JWT (OAuth2 Resource Server)       | Xác thực và phân quyền phi trạng thái (Stateless), bảo mật API bằng token mã hóa.    |
| Database        | PostgreSQL                         | Lưu trữ dữ liệu quan hệ có cấu trúc (User, Product, Order, KYC, Permissions).        |
| Cache & Queue   | Redis                              | Gộp 3 nhiệm vụ: Cache dữ liệu, quản lý Socket Session và làm Hàng đợi thông báo.     |
| Realtime Chat   | Spring WebSocket (STOMP)           | Xử lý giao tiếp hai chiều thời gian thực cho tính năng Chat giữa các User và Notify. |
| Payment Gate    | Stripe API                         | Xử lý cổng thanh toán trung gian, lưu giữ tiền (Escrow) và nhận Webhook từ Stripe.   |
| AI Integration  | Spring AI (OpenAI / Gemini API)    | Tích hợp trợ lý AI thông minh phân tích sản phẩm, gợi ý giá hoặc hỗ trợ tìm kiếm.    |
| Storage         | MinIO (Self-hosted Object Storage) | Lưu trữ tập trung bộ ảnh sản phẩm (`image`) và ảnh giấy tờ xác thực (`kyc_request`). |
| Deployment      | Docker & Docker Compose            | Đóng gói và chạy toàn bộ hệ thống dưới dạng các Container cô lập (Tối ưu RAM).       |

## Container Architecture (Docker Compose)

Hệ thống vận hành tối ưu trong cùng một mạng Docker Network, gồm 5 containers cốt lõi:

1. `frontend` - Chạy ứng dụng ReactTS (Port 5173).
2. `backend` - Chạy ứng dụng Spring Boot API (Port 8080).
3. `postgres` - Hệ quản trị DB PostgreSQL (Port 5432). Cấu trúc bảng khởi tạo tự động qua script SQL tại `/docker-entrypoint-initdb.d/`.
4. `redis` - Bộ nhớ đệm và hàng đợi (Port 6379).
5. `minio` - Lưu trữ Object Storage tương thích S3 API (Port 9000 cho API, Port 9001 cho Console).

## System Boundaries

```text
secondlife-marketplace/      # Thư mục gốc của dự án Monorepo
├── backend/                 # Mã nguồn Backend (Spring Boot 4.0.6) - Package-by-Feature
├── frontend/                # Mã nguồn Frontend (React TypeScript + Vite) - Layer-based Pages
├── docker-compose.yml       # Cấu hình khởi chạy tự động các container hạ tầng (Postgres, Redis, MinIO)
├── .env # Lưu trữ các biến môi trường chung toàn hệ thống
├── .env.example             # File mẫu định nghĩa các biến môi trường toàn cục (Chứa key Stripe, MinIO...)
└── context/                 # CONTEXT FILES (Hệ thống file đặc tả điều hướng AI)
    ├── project-overview.md      # Khái quát dự án, tính năng cốt lõi và phạm vi In/Out Scope
    ├── architecture.md          # Chi tiết thiết kế Database (ERD), API Endpoints và Kiến trúc hệ thống
    ├── progress-tracker.md      # Lộ trình cuốn chiếu chia theo Phase và danh sách Task đang triển khai
    └── ai-workflow-rules.md     # Bản hiến pháp ép AI tuân thủ luật kiến trúc, cấm code vượt Phase
```

## Folder Structure

### Back end

Backend sử dụng kiến trúc **Module-Based Folder Structure (Package-by-Feature)**. Các tính năng được cô lập thành từng package độc lập, tạo ranh giới hệ thống rõ ràng:

```text
backend/src/main/java/vn/datnguy3n/marketplace/
├── config/                  # Cấu hình hệ thống toàn cục (Security, WebSocket, Redis, MinIO, Spring AI)
├── common/                  # Các Class dùng chung toàn hệ thống (Exception, BaseEntity, StorageService)
└── modules/                 # Nơi chứa các Module Tính năng cốt lõi (Module-Based)
    ├── auth/                # Xử lý Đăng ký, Đăng nhập, JWT, Quên mật khẩu (OTP Email), Đổi mật khẩu
    ├── user/                # Quản lý user, Tính năng Chặn người dùng (User Block)
    ├── role/                # Phân quyền user dựa theo role
    ├── permission/          # Quản lý các permission có trong hệ thống
    ├── kyc/                 # Xử lý yêu cầu gửi KYC, Upload giấy tờ lên MinIO và Admin duyệt KYC
    ├── product/             # Quản lý Danh mục đệ quy, Thương hiệu, Đăng tải Đồ cũ & Thuộc tính động
    ├── order/               # Quản lý luồng mua bán, Địa chỉ giao nhận (chuẩn Nhật Bản) và Trạng thái đơn hàng
    ├── payment/             # Liên kết Stripe API, Xử lý Webhook, Lưu giữ tiền trung gian (Escrow)
    ├── chat/                # [Out of Scope] Xử lý WebSocket kết nối chat realtime giữa các cặp người dùng
    └── notification/        # [Out of Scope] Quản lý gửi và nhận thông báo, tích hợp hàng đợi Redis Queue
    ...
```

### Front end

```text
frontend/src/
├── assets/                  # Chứa hình ảnh, logo, biểu tượng (icons) nội bộ dùng trong code
├── components/              # Các thành phần giao diện tái sử dụng (UI Components)
   ├── ui/                  # Nơi chứa các components của shadcn
   ├── admin/               # Thành phần giao diện dành riêng cho trang quản trị (Bảng biểu, Sidebar Admin)
   ├── client/              # Thành phần giao diện dành cho phía người dùng (Form nhập, Thẻ sản phẩm)
   └── share/               # Các component dùng chung cho cả 2 bên (Layout, Header, Footer, Nút bấm, Modal)
├── config/                  # Cấu hình Axios Client, Interceptor gắn Token, định nghĩa API Endpoints
├── pages/                   # Nơi chứa các trang (màn hình lớn) hiển thị của ứng dụng
   ├── admin/               # Trang quản lý của Admin (Duyệt hồ sơ KYC, Giải quyết tranh chấp đơn hàng)
   ├── auth/                # Trang Xác thực (Đăng ký, Đăng nhập, Quên mật khẩu, Đổi mật khẩu)
   ├── home/                # Trang chủ hệ thống (Hiển thị danh mục, thanh tìm kiếm và bộ lọc)
   ...
├── redux/                   # Quản lý trạng thái toàn cục (Global State) bằng Redux Toolkit
   └── slice/               # Logic xử lý dữ liệu cho từng module (Auth Slice, Product Slice, Order Slice...)
├── styles/                  # Cấu hình phong cách giao diện CSS và chủ đề hệ thống (Tailwind CSS)
└── types/                   # Định nghĩa các Interface TypeScript để ép kiểu dữ liệu nghiêm ngặt từ API Backend
```

## Storage Model

- **PostgreSQL**: Lưu trữ metadata có cấu trúc và ràng buộc chặt chẽ (User, Product, Order, KYC, Permissions).
- **Redis**: Cache dữ liệu ít biến động (Danh mục, thương hiệu), lưu session trực tuyến và làm hàng đợi (Redis Queue) xử lý thông báo ngầm.
- **MinIO**: Lưu trữ ảnh thô qua 2 buckets chính: `products` (ảnh công khai) và `kyc-vault` (ảnh giấy tờ nhạy cảm, bảo mật).

## Auth and Access Model

- Hệ thống sử dụng cơ chế JWT mã hóa đính kèm ở header `Authorization: Bearer <token>`.
- Cơ chế chặn (`user_block`): Mọi API tương tác (gửi tin nhắn, đặt mua) phải kiểm tra xem người mua có nằm trong danh sách bị người bán block hay không trước khi thực hiện logic.

## Invariants (Các quy tắc tối kỵ)

1. **Không lưu ảnh trực tiếp trong DB**: Mọi tệp tin đa phương tiện bắt buộc phải tải lên MinIO, PostgreSQL chỉ lưu chuỗi URL.
2. **Xử lý bất đồng bộ các tác vụ nặng**: Các tác vụ như gọi API đến Stripe, xử lý phân tích hình ảnh qua Spring AI, gửi Email/Notification phải được đẩy vào hàng đợi Redis hoặc luồng xử lý bất đồng bộ (`@Async`) để tránh nghẽn luồng Request chính.
3. **Kiểm tra quyền sở hữu**: Không một User nào được phép chỉnh sửa sản phẩm (`product`) hoặc cập nhật đơn hàng (`order`) nếu họ không phải là chủ sở hữu hoặc không có quyền Admin.
4. **Cấu hình môi trường qua Environment Variables**: Tất cả các thông tin nhạy cảm (Stripe Secret Key, MinIO Credentials, DB Password, AI API Key) tuyệt đối phải được truyền từ file `.env` thông qua Docker Compose.
5. **Không giao tiếp chéo Repository giữa các Module**: Module A muốn lấy dữ liệu của Module B bắt buộc phải gọi thông qua interface `Service` của Module B. Nghiêm cấm hành vi tự ý Inject trực tiếp `Repository` của module khác.
6. **Nhất quán dữ liệu thanh toán**: Trạng thái Đơn hàng (`order`) và bản ghi thanh toán (`payment`) phải luôn đồng bộ chặt chẽ thông qua Stripe Webhook. Mã `amount_stripe_pi_id` không được phép rỗng khi đơn hàng đã thanh toán.
