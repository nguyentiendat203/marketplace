# [SecondLife Marketplace]

## Overview

SecondLife là một nền tảng thương mại điện tử trung gian dạng C2C (Consumer-to-Consumer) dành riêng cho việc mua bán và giao dịch đồ cũ giữa các cá nhân. Ứng dụng giải quyết hai bài toán lớn nhất của thị trường đồ cũ hiện nay: rủi ro lừa đảo và sự thiếu minh bạch về tình trạng sản phẩm. Bằng cách tích hợp quy trình xác thực danh tính người dùng nghiêm ngặt (KYC) cùng giải pháp thanh toán trung gian (Escrow Payment) qua Stripe, SecondLife đảm bảo dòng tiền của người mua chỉ được giải ngân cho người bán khi sản phẩm được giao đúng mô tả, tạo dựng một môi trường an toàn tuyệt đối cho người tham gia.

## Goals

Project hướng tới việc xây dựng một sàn thương mại điện tử đồ cũ minh bạch và tin cậy nhờ quy trình kiểm soát KYC bắt buộc đối với các nhà bán hàng quy mô lớn, kết hợp giải pháp thanh toán giữ tiền trung gian qua Stripe bảo mật tuyệt đối với tỷ lệ lưu vết mã định danh đơn hàng đạt 100%. Không chỉ mang lại trải nghiệm linh hoạt trong việc quản lý và đăng bán sản phẩm với hệ thống thuộc tính động phong phú (size, màu sắc, tình trạng sử dụng...), hệ thống còn ứng dụng trí tuệ nhân tạo (AI) chuyên sâu để tối ưu hóa hành trình mua sắm. Thông qua tương tác chat trực tiếp với trợ lý thông minh AI, người dùng có thể dễ dàng tìm kiếm, lọc thông tin nhanh gọn và nhận những gợi ý mua sắm cá nhân hóa một cách trực quan nhất.

## Core User Flow

1. **Xác thực & Phân quyền (Auth & RBAC):** Người dùng truy cập -> Đăng ký/Đăng nhập -> Hệ thống cấp JWT và phân quyền mặc định là `USER_NORMAL` (chỉ được xem, chưa được bán).

2. **Định danh nâng cao (KYC Flow):** User muốn đăng bán đồ cũ -> Hệ thống yêu cầu KYC -> User gửi ảnh giấy tờ lên MinIO -> Admin duyệt -> Hệ thống cập nhật quyền lên `USER_VERIFIED` (được phép đăng bán).

3. **Đăng tải sản phẩm (Seller Flow):** Người bán (`USER_VERIFIED`) tạo bài đăng -> Chọn danh mục, thuộc tính động -> Upload ảnh sản phẩm lên MinIO công khai.

4. **Đặt hàng & Giữ tiền trung gian (Buyer & Escrow Flow):** Người mua xem sản phẩm (hệ thống check xem có bị người bán block không) -> Bấm mua -> Thanh toán qua Stripe -> Stripe giữ tiền (Escrow) -> Hệ thống chuyển trạng thái đơn hàng sang "Chờ giao hàng".

5. **Hoàn tất & Giải ngân (Payout Flow):** Người bán giao hàng -> Người mua nhận hàng & xác nhận -> Hệ thống gọi Stripe giải ngân tiền cho người bán -> Kết thúc chu trình giao dịch.

6. **Real-time Chat**: Giao tiếp thương lượng trực tiếp giữa Người mua và Người bán qua WebSocket.

7. **Notification System**: Hệ thống bắn thông báo thời gian thực và lưu trữ lịch sử thông báo qua hàng đợi Redis.

## Database Schema Specification

Hệ thống cơ sở dữ liệu được thiết kế dựa trên sơ đồ ERD tổng quan, tối ưu hóa tên trường viết chuẩn tiếng Anh và phân loại tường minh theo từng module nghiệp vụ để phục vụ cho kiến trúc Package-by-Feature.

### 1. Module Auth, User & Phân quyền (RBAC)

#### Bảng `user` (Quản lý người dùng)

| Field Name                | Data Type | Key / Constraint        | Description                                                       |
| :------------------------ | :-------- | :---------------------- | :---------------------------------------------------------------- |
| `id`                      | PK(uuid)  | Primary Key             | Định danh duy nhất của user                                       |
| `role_id`                 | FK(uuid)  | Foreign Key (`role.id`) | Liên kết phân quyền chức vụ                                       |
| `email`                   | varchar   | UNIQUE                  | Email dùng để đăng nhập và nhận OTP quên mật khẩu                 |
| `password`                | varchar   |                         | Mật khẩu (đã được mã hóa bảo mật BCrypt)                          |
| `user_name`               | varchar   |                         | Tên đăng nhập / Tên hiển thị công khai                            |
| `user_avatar`             | varchar   |                         | Đường dẫn ảnh đại diện                                            |
| `user_is_verified`        | boolean   |                         | Trạng thái tích xanh xác thực danh tính                           |
| `seller_level`            | smallint  |                         | Cấp độ uy tín của người bán                                       |
| `user_rating_avg`         | float     |                         | Điểm đánh giá trung bình từ người mua                             |
| `user_view_count`         | int       |                         | Tổng lượt xem trang cá nhân                                       |
| `user_bio`                | text      |                         | Đoạn giới thiệu bản thân                                          |
| `user_listing_count`      | int       |                         | Số lượng sản phẩm đang đăng bán                                   |
| `user_followers_count`    | int       |                         | Số lượng người theo dõi _(Đã chuẩn hóa lỗi chính tả từ ERD)_      |
| `user_following_count`    | int       |                         | Số lượng người đang theo dõi _(Đã chuẩn hóa lỗi chính tả từ ERD)_ |
| `user_stripe_customer_id` | varchar   |                         | ID định danh khách hàng trên hệ thống Stripe                      |

#### Bảng `role` (Vai trò hệ thống)

| Field Name         | Data Type | Key / Constraint | Description                                  |
| :----------------- | :-------- | :--------------- | :------------------------------------------- |
| `id`               | PK(uuid)  | Primary Key      | Định danh vai trò (Admin, User, Verified...) |
| `role_name`        | varchar   |                  | Tên vai trò                                  |
| `role_description` | varchar   |                  | Mô tả chi tiết về quyền hạn vai trò          |

#### Bảng `permission` (Quyền hạn API)

| Field Name       | Data Type | Key / Constraint | Description                               |
| :--------------- | :-------- | :--------------- | :---------------------------------------- |
| `id`             | PK(uuid)  | Primary Key      | Định danh quyền hạn chi tiết              |
| `pms_name`       | varchar   |                  | Tên quyền (Ví dụ: "CREATE_PRODUCT")       |
| `pms_api_path`   | varchar   |                  | Đường dẫn API bảo mật cần chặn            |
| `pms_api_method` | varchar   |                  | Phương thức HTTP (GET, POST, PUT, DELETE) |
| `pms_api_module` | varchar   |                  | Tên module quản lý quyền này              |

#### Bảng `permission_role` (Bảng trung gian N-N phân quyền)

| Field Name      | Data Type | Key / Constraint              | Description                    |
| :-------------- | :-------- | :---------------------------- | :----------------------------- |
| `id`            | PK(uuid)  | Primary Key                   | Định danh bản ghi trung gian   |
| `role_id`       | FK(uuid)  | Foreign Key (`role.id`)       | Tham chiếu tới vai trò         |
| `permission_id` | FK(uuid)  | Foreign Key (`permission.id`) | Tham chiếu tới quyền tương ứng |

#### Bảng `user_block` (Cơ chế cô lập hành vi chặn nhau)

| Field Name   | Data Type | Key / Constraint        | Description                     |
| :----------- | :-------- | :---------------------- | :------------------------------ |
| `id`         | PK(uuid)  | Primary Key             | Định danh lượt chặn             |
| `blocker_id` | FK(uuid)  | Foreign Key (`user.id`) | ID của người bấm nút Chặn       |
| `blocked_id` | FK(uuid)  | Foreign Key (`user.id`) | ID của người bị đối phương chặn |
| `created_at` | timestamp |                         | Thời gian thực hiện chặn        |

---

### 2. Module KYC (Xác thực danh tính)

#### Bảng `kyc_request` (Yêu cầu định danh)

| Field Name         | Data Type | Key / Constraint            | Description                                    |
| :----------------- | :-------- | :-------------------------- | :--------------------------------------------- |
| `id`               | PK(uuid)  | Primary Key                 | Định danh lượt gửi yêu cầu KYC                 |
| `user_id`          | FK(uuid)  | Foreign Key (`user.id`)     | Người dùng gửi yêu cầu                         |
| `kyc_full_name`    | varchar   |                             | Họ và tên thật trên giấy tờ                    |
| `kyc_address`      | varchar   |                             | Địa chỉ cư trú trên giấy tờ                    |
| `kyc_card_front`   | varchar   | URL ảnh (Lưu MinIO private) | Ảnh mặt trước CCCD/Hộ chiếu                    |
| `kyc_card_back`    | varchar   | URL ảnh (Lưu MinIO private) | Ảnh mặt sau CCCD/Hộ chiếu                      |
| `kyc_selfie_image` | varchar   | URL ảnh (Lưu MinIO private) | Ảnh chân dung tự chụp đối chiếu                |
| `kyc_status`       | enum      |                             | Trạng thái duyệt (PENDING, APPROVED, REJECTED) |
| `kyc_admin_note`   | text      |                             | Phản hồi từ Admin khi từ chối hồ sơ            |

---

### 3. Module Product (Quản lý danh mục & Đồ cũ)

### Bảng `product` (Gốc - Thông tin chung)

| Field Name               | Data Type  | Key / Constraint            | Description                                      |
| :----------------------- | :--------- | :-------------------------- | :----------------------------------------------- |
| `id`                     | PK(uuid)   | Primary Key                 | Định danh sản phẩm đăng bán                      |
| `category_id`            | FK(uuid)   | Foreign Key (`category.id`) | Danh mục chứa sản phẩm                           |
| `brand_id`               | FK(uuid)   | Foreign Key (`brand.id`)    | Thương hiệu của sản phẩm                         |
| `user_id`                | FK(uuid)   | Foreign Key (`user.id`)     | Người đăng bán sản phẩm (Seller)                 |
| `product_price`          | float      |                             | Giá mong muốn bán                                |
| `product_condition_note` | varchar    |                             | Ghi chú nhanh về độ mới (Ví dụ: "95%, xước nhẹ") |
| `product_status`         | enum       |                             | Trạng thái (AVAILABLE, SOLD, HIDDEN)             |
| `original_language`      | varchar(5) | Mặc định 'vi'               | Ngôn ngữ gốc lúc đăng bài (vi, ja, en)           |

### Bảng `product_translation` (Bản dịch thông tin sản phẩm)

| Field Name            | Data Type  | Key / Constraint           | Description                                       |
| :-------------------- | :--------- | :------------------------- | :------------------------------------------------ |
| `id`                  | PK(uuid)   | Primary Key                | Định danh bản ghi dịch                            |
| `product_id`          | FK(uuid)   | Foreign Key (`product.id`) | Liên kết tới sản phẩm gốc                         |
| `language_code`       | varchar(5) | UNIQUE với `product_id`    | Mã ngôn ngữ ("vi", "ja", "en")                    |
| `product_title`       | varchar    |                            | Tiêu đề tin đăng bán bằng ngôn ngữ này            |
| `product_description` | text       |                            | Mô tả chi tiết tình trạng đồ cũ bằng ngôn ngữ này |

#### Bảng `image` (Bộ sưu tập ảnh sản phẩm)

| Field Name         | Data Type | Key / Constraint           | Description                                    |
| :----------------- | :-------- | :------------------------- | :--------------------------------------------- |
| `id`               | PK(uuid)  | Primary Key                | Định danh ảnh lẻ                               |
| `product_id`       | FK(uuid)  | Foreign Key (`product.id`) | Thuộc về sản phẩm nào                          |
| `image_url`        | varchar   | URL ảnh (Lưu MinIO public) | Đường dẫn hình ảnh                             |
| `image_sort_order` | smallint  |                            | Thứ tự hiển thị (Ảnh 0 thường là ảnh đại diện) |

### Bảng `category` (Gốc - Cấu hình đệ quy)

| Field Name      | Data Type | Key / Constraint                  | Description                            |
| :-------------- | :-------- | :-------------------------------- | :------------------------------------- |
| `id`            | PK(uuid)  | Primary Key                       | Định danh danh mục sản phẩm            |
| `ctg_image`     | varchar   |                                   | Ảnh đại diện cho danh mục              |
| `ctg_parent_id` | FK(uuid)  | Tham chiếu đệ quy (`category.id`) | ID danh mục cha (Nếu là danh mục con)  |
| `ctg_level`     | smallint  |                                   | Cấp độ phân tách danh mục (1, 2, 3...) |

### Bảng `category_translation` (Bản dịch danh mục)

| Field Name      | Data Type  | Key / Constraint            | Description                                     |
| :-------------- | :--------- | :-------------------------- | :---------------------------------------------- |
| `id`            | PK(uuid)   | Primary Key                 | Định danh bản ghi dịch                          |
| `category_id`   | FK(uuid)   | Foreign Key (`category.id`) | Liên kết tới danh mục gốc                       |
| `language_code` | varchar(5) | UNIQUE với `category_id`    | Mã ngôn ngữ ("vi", "ja", "en")                  |
| `ctg_name`      | varchar    |                             | Tên danh mục đã dịch (Ví dụ: "家電", "Điện tử") |

### Bảng `brand` (Gốc - Chỉ lưu Logo)

| Field Name    | Data Type | Key / Constraint | Description           |
| :------------ | :-------- | :--------------- | :-------------------- |
| `id`          | PK(uuid)  | Primary Key      | Định danh thương hiệu |
| `brand_image` | varchar   |                  | Logo thương hiệu      |

### Bảng `brand_translation` (Bản dịch tên thương hiệu - Hỗ trợ tìm kiếm Katakana)

| Field Name      | Data Type  | Key / Constraint         | Description                                      |
| :-------------- | :--------- | :----------------------- | :----------------------------------------------- |
| `id`            | PK(uuid)   | Primary Key              | Định danh bản ghi dịch                           |
| `brand_id`      | FK(uuid)   | Foreign Key (`brand.id`) | Liên kết tới thương hiệu gốc                     |
| `language_code` | varchar(5) | UNIQUE với `brand_id`    | Mã ngôn ngữ ("vi", "ja", "en")                   |
| `brand_name`    | varchar    |                          | Tên thương hiệu dịch (Ví dụ: "Nike" vs "ナイキ") |

#### Hệ thống thuộc tính động (EAV Pattern)

### Bảng `attribute_type` (Gốc - Định danh nhóm)

| Field Name | Data Type | Key / Constraint | Description               |
| :--------- | :-------- | :--------------- | :------------------------ |
| `id`       | PK(uuid)  | Primary Key      | Định danh loại thuộc tính |

### Bảng `attribute_type_translation` (Bản dịch Loại thuộc tính)

| Field Name          | Data Type  | Key / Constraint                  | Description                                        |
| :------------------ | :--------- | :-------------------------------- | :------------------------------------------------- |
| `id`                | PK(uuid)   | Primary Key                       | Định danh bản ghi dịch                             |
| `attribute_type_id` | FK(uuid)   | Foreign Key (`attribute_type.id`) | Liên kết tới loại thuộc tính gốc                   |
| `language_code`     | varchar(5) | UNIQUE với `attribute_type_id`    | Mã ngôn ngữ ("vi", "ja", "en")                     |
| `attribute_name`    | varchar    |                                   | Tên thuộc tính hiển thị (Ví dụ: "Màu sắc" vs "色") |

### Bảng `attribute_option` (Gốc - Định danh giá trị)

| Field Name          | Data Type | Key / Constraint                  | Description                  |
| :------------------ | :-------- | :-------------------------------- | :--------------------------- |
| `id`                | PK(uuid)  | Primary Key                       | Định danh cấu hình option    |
| `attribute_type_id` | FK(uuid)  | Foreign Key (`attribute_type.id`) | Thuộc về nhóm thuộc tính nào |

### Bảng `attribute_option_translation` (Bản dịch Giá trị lựa chọn)

| Field Name              | Data Type  | Key / Constraint                    | Description                                      |
| :---------------------- | :--------- | :---------------------------------- | :----------------------------------------------- |
| `id`                    | PK(uuid)   | Primary Key                         | Định danh bản ghi dịch                           |
| `attribute_option_id`   | FK(uuid)   | Foreign Key (`attribute_option.id`) | Liên kết tới option gốc                          |
| `language_code`         | varchar(5) | UNIQUE với `attribute_option_id`    | Mã ngôn ngữ ("vi", "ja", "en")                   |
| `attribute_option_name` | varchar    |                                     | Giá trị dịch cụ thể (Ví dụ: "Size L", "Mới 99%") |

### Bảng `product_attribute` (Bảng trung gian ánh xạ vào sản phẩm)

| Field Name            | Data Type | Key / Constraint                    | Description                   |
| :-------------------- | :-------- | :---------------------------------- | :---------------------------- |
| `id`                  | PK(uuid)  | Primary Key                         | Định danh liên kết            |
| `product_id`          | FK(uuid)  | Foreign Key (`product.id`)          | Gán vào sản phẩm mục tiêu     |
| `attribute_option_id` | FK(uuid)  | Foreign Key (`attribute_option.id`) | Giá trị cấu hình được áp dụng |

---

### 4. Module Order & Payment (Đơn hàng & Thanh toán giữ tiền hộ)

#### Bảng `order` (Thông tin đơn mua bán và địa chỉ giao nhận Nhật Bản)

| Field Name                  | Data Type | Key / Constraint           | Description                                                  |
| :-------------------------- | :-------- | :------------------------- | :----------------------------------------------------------- |
| `id`                        | PK(uuid)  | Primary Key                | Định danh đơn hàng                                           |
| `user_id`                   | smallint  | Foreign Key (`user.id`)    | Người thực hiện mua hàng (Buyer)                             |
| `product_id`                | FK(uuid)  | Foreign Key (`product.id`) | Xác định chính xác món đồ cũ độc nhất được mua trong đơn này |
| `order_total_amount`        | float     |                            | Tổng số tiền của đơn hàng                                    |
| `order_status`              | enum      |                            | Trạng thái (PENDING, PAID_ESCROW, COMPLETED, CANCELLED)      |
| `order_shipping_name`       | varchar   |                            | Tên người nhận hàng                                          |
| `order_shipping_phone`      | varchar   |                            | Số điện thoại nhận hàng                                      |
| `order_shipping_zip_code`   | varchar   | Định dạng bưu điện Nhật    | Mã bưu điện (郵便番号 - Ví dụ: 359-1111)                     |
| `order_shipping_prefecture` | varchar   | Hệ tỉnh thành Nhật Bản     | Tỉnh thành (都道府県 - Ví dụ: Saitama-ken)                   |
| `order_shipping_city`       | varchar   | Hệ quận/huyện Nhật Bản     | Quận/Huyện/Thành phố thuộc tỉnh (市区町村)                   |
| `order_shipping_address`    | varchar   |                            | Số địa chỉ chi tiết, block nhà (番地)                        |
| `order_shipping_building`   | varchar   |                            | Tên tòa nhà, khu chung cư, số phòng (建物名・部屋番号)       |

#### Bảng `payment` (Giao dịch cổng thanh toán Stripe)

| Field Name                 | Data Type | Key / Constraint         | Description                                                 |
| :------------------------- | :-------- | :----------------------- | :---------------------------------------------------------- |
| `id`                       | PK(uuid)  | Primary Key              | Định danh giao dịch                                         |
| `order_id`                 | FK(uuid)  | Foreign Key (`order.id`) | Liên kết tới đơn hàng mua                                   |
| `stripe_payment_intent_id` | varchar   | Mã bắt buộc từ Stripe    | Stripe Payment Intent ID (`pi_...`) phục vụ Escrow giữ tiền |
| `payment_amount`           | float     |                          | Số tiền thực tế đã quẹt thẻ                                 |
| `payment_currency`         | enum      | Ví dụ: JPY, USD          | Đơn vị tiền tệ giao dịch                                    |
| `payment_status`           | enum      |                          | Trạng thái xử lý (SUCCEEDED, FAILED, REFUNDED)              |
| `payment_error_message`    | text      |                          | Ghi nhận mã lỗi trả về từ webhook Stripe nếu thất bại       |

---

### 5. Các tính năng mở rộng & Tương tác (Hỗ trợ các Phase sau)

#### Bảng `comment` (Bình luận công khai hỏi đáp sản phẩm)

| Field Name        | Data Type | Key / Constraint                 | Description                                       |
| :---------------- | :-------- | :------------------------------- | :------------------------------------------------ |
| `id`              | PK(uuid)  | Primary Key                      | Định danh bình luận                               |
| `product_id`      | FK(uuid)  | Foreign Key (`product.id`)       | Bình luận tại trang sản phẩm nào                  |
| `user_id`         | FK(uuid)  | Foreign Key (`user.id`)          | Người viết bình luận                              |
| `comment_content` | text      |                                  | Nội dung text trao đổi                            |
| `parent_id`       | FK(uuid)  | Tham chiếu đệ quy (`comment.id`) | Phục vụ luồng phản hồi lồng nhau (Thread comment) |

#### Bảng `review` (Đánh giá uy tín sau giao dịch thành công)

| Field Name       | Data Type | Key / Constraint           | Description                                    |
| :--------------- | :-------- | :------------------------- | :--------------------------------------------- |
| `id`             | PK(uuid)  | Primary Key                | Định danh lượt đánh giá                        |
| `order_id`       | FK(uuid)  | Foreign Key (`order.id`)   | Đánh giá cho đơn hàng nào                      |
| `product_id`     | FK(uuid)  | Foreign Key (`product.id`) | Sản phẩm nhận đánh giá                         |
| `reviewer_id`    | FK(uuid)  | Foreign Key (`user.id`)    | Người chấm điểm và viết review                 |
| `reviewee_id`    | FK(uuid)  | Foreign Key (`user.id`)    | Đối tượng được chấm (Người bán hoặc Người mua) |
| `review_rating`  | smallint  | Thang điểm 1 - 5           | Số sao chấm điểm                               |
| `review_comment` | text      |                            | Nhận xét chi tiết                              |

#### Bảng `message` (Hộp thư Chat Realtime - Tạm thời Out of Scope)

| Field Name        | Data Type | Key / Constraint        | Description                         |
| :---------------- | :-------- | :---------------------- | :---------------------------------- |
| `id`              | PK(uuid)  | Primary Key             | Định danh tin nhắn lẻ               |
| `msg_sender_id`   | FK(uuid)  | Foreign Key (`user.id`) | Người gửi tin                       |
| `msg_receiver_id` | FK(uuid)  | Foreign Key (`user.id`) | Người nhận tin                      |
| `msg_content`     | text      |                         | Nội dung đoạn chat                  |
| `msg_is_read`     | boolean   |                         | Trạng thái đầu nhận đã đọc hay chưa |

#### Bảng `notification` (Hộp thư thông báo hệ thống - Tạm thời Out of Scope)

| Field Name     | Data Type | Key / Constraint        | Description                                     |
| :------------- | :-------- | :---------------------- | :---------------------------------------------- |
| `id`           | PK(uuid)  | Primary Key             | Định danh thông báo                             |
| `user_id`      | FK(uuid)  | Foreign Key (`user.id`) | Người dùng nhận thông báo                       |
| `noti_title`   | varchar   |                         | Tiêu đề thông báo nhanh                         |
| `noti_content` | text      |                         | Nội dung chi tiết thông báo hệ thống            |
| `noti_type`    | varchar   |                         | Phân loại thông báo (SYSTEM, ORDER, INFOMATION) |
| `noti_is_read` | boolean   |                         | Trạng thái người dùng đã bấm đọc hay chưa       |

### 📌 Quy định về các trường dùng chung

Các bảng trong hệ thống khi triển khai thực tế đều mặc định sở hữu 6 trường dữ liệu sau đây:

| Field Name   | Data Type      | Constraint                   | Description                                                      |
| :----------- | :------------- | :--------------------------- | :--------------------------------------------------------------- |
| `id`         | PK(uuid)       | Primary Key                  | Khóa chính của table                                             |
| `created_at` | timestamp      | Mặc định `CURRENT_TIMESTAMP` | Thời gian tạo bản ghi hệ thống                                   |
| `created_by` | uuid / varchar | Cho phép NULL                | Định danh người tạo bản ghi                                      |
| `updated_at` | timestamp      | Tự động cập nhật khi UPDATE  | Thời gian chỉnh sửa bản ghi gần nhất                             |
| `updated_by` | uuid / varchar | Cho phép NULL                | Định danh người chỉnh sửa gần nhất                               |
| `deleted_at` | timestamp      | Mặc định NULL                | Thời gian xóa mềm (Nếu có giá trị nghĩa là bản ghi đã bị ẩn/xóa) |
| `deleted_by` | uuid / varchar | Cho phép NULL                | Định danh người thực hiện lệnh xóa mềm                           |

---

## Scope (Phạm vi dự án)

### In Scope: Danh sách tính năng theo từng Module

#### 1. Module Auth & User

- **Đăng ký / Đăng nhập:** Đăng ký tài khoản (mã hóa mật khẩu) và Đăng nhập xác thực cấp mã mã hóa JWT.
- **Quên mật khẩu (Forgot Password):** API nhận email, sinh mã xác thực (OTP) lưu vào Redis kèm thời gian hết hạn (TTL), và tích hợp `JavaMailSender` để gửi email template chứa mã OTP cho user.
- **Xác thực OTP & Đặt lại mật khẩu:** API đối chiếu mã OTP từ user nhập vào. Nếu hợp lệ, cho phép gọi API đặt lại mật khẩu mới (Reset Password).
- **Đổi mật khẩu (Change Password):** API dành cho User đã đăng nhập, yêu cầu nhập mật khẩu cũ và mật khẩu mới để thay đổi.
- **Phân quyền (RBAC):** CRUD danh sách Role & Permission, gán quyền cho User trực tiếp trong module này.
- **Chặn người dùng (User Block):** API Block/Unblock đối phương; viết bộ lọc check danh sách chặn trước khi cho phép tương tác.

#### 2. Module KYC

- **Gửi hồ sơ KYC:** API cho User upload ảnh giấy tờ cá nhân lên hệ thống.
- **Phê duyệt KYC:** Giao diện cho Admin xem danh sách hồ sơ -> Bấm nút "Duyệt" (Tự động nâng quyền User lên `USER_VERIFIED`) hoặc "Từ chối".

#### 3. Module Product

- **Đăng bán sản phẩm:** API/Giao diện điền thông tin, chọn danh mục, hãng và upload bộ ảnh. Sau khi đăng bán thì phải đợi admin phê duyệt bài đăng, nếu được chấp thuận thì status
- **Thuộc tính động:** Cho phép chọn/nhập các thuộc tính linh hoạt tùy theo sản phẩm (Size, Màu sắc, Tình trạng cũ/mới %).
- **Tìm kiếm & Bộ lọc:** Giao diện xem danh sách sản phẩm, hỗ trợ tìm kiếm theo từ khóa và lọc theo danh mục, thương hiệu.

#### 4. Module Order & Payment (Đơn hàng & Thanh toán)

- **Tạo đơn hàng:** Người mua bấm đặt hàng -> Tạo bản ghi `order` (Trạng thái: Chờ thanh toán).
- **Thanh toán Stripe (Escrow):** Tích hợp Stripe SDK tạo Payment Intent (Test Mode) để người mua nhập thẻ test thanh toán, tiền sẽ được giữ ở trạng thái trung gian.
- **Stripe Webhook:** API lắng nghe tín hiệu từ Stripe, tự động cập nhật trạng thái đơn hàng sang "Đã thanh toán / Giữ tiền" ngay khi user thanh toán thành công.
- **Xử lý tranh chấp thủ công:** Giao diện Admin hiển thị các đơn hàng bị khiếu nại -> Admin bấm nút để hệ thống tự gọi Stripe giải ngân (Payout) cho Người bán hoặc Hoàn tiền (Refund) cho Người mua.

### Out of Scope: Tính năng nâng cao (Sẽ phát triển sau khi móng vững)

Đây là các tính năng mở rộng, mang tính chất tối ưu trải nghiệm hoặc tích hợp công nghệ cao. Hệ thống sẽ tạm thời gác lại toàn bộ phần này ở giai đoạn đầu và chỉ tiến hành implement làm tính năng gia tăng khi phần "In Scope" ở trên đã chạy mượt mà:

- **Tương tác Real-time (Chat & Notify):** Tích hợp WebSocket (STOMP) để chat thương lượng trực tiếp giữa Người mua/Người bán và cấu hình Redis Queue để bắn thông báo tự động (Dữ liệu lịch sử tin nhắn và thông báo trong bảng `message` và `notification` tạm thời để trống chưa xử lý).
- **Trợ lý thông minh (AI Integration):** Sử dụng Spring AI (OpenAI hoặc Gemini API) để quét phân tích hình ảnh sản phẩm, làm chatbot tư vấn hoặc hỗ trợ gợi ý giá bán đồ cũ.
- **Tự động hóa với bên thứ ba:** Kết nối API với các hãng vận chuyển thực tế (Yamato, Sagawa...), hệ thống tự động tính phí ship dựa trên khoảng cách địa lý, hệ thống ví điện tử nội bộ, hoặc tự động hoàn tiền mà không cần Admin can thiệp.
- **Ứng dụng di động (Mobile App):** Phát triển phiên bản ứng dụng chạy trên iOS/Android (Dự án này chỉ tập trung tối ưu bản Web SPA).
- **Hạ tầng Production lớn:** Cấu hình deploy hệ thống lên Cloud (AWS/GCP), cài đặt Kubernetes (K8s), Multi-region Database, hoặc thiết lập các đường ống CI/CD tự động quy mô lớn.
