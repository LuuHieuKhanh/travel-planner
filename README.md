# ✈️ AI Travel Planner

![Travel Planner Banner](https://via.placeholder.com/1200x400.png?text=AI+Travel+Planner+Banner)

> 🚀 Tạo lịch trình du lịch cá nhân hóa chỉ trong vài giây!

## Giới thiệu dự án

**AI Travel Planner** là một ứng dụng web thông minh giúp người dùng lên kế hoạch cho chuyến du lịch một cách dễ dàng và hiệu quả. Bằng cách sử dụng mô hình ngôn ngữ lớn mạnh mẽ của Google (Gemini), ứng dụng sẽ tự động tạo ra một lịch trình chi tiết, phù hợp với nhu cầu, sở thích và ngân sách của bạn.

Bạn chỉ cần cung cấp các thông tin cơ bản như điểm đến, ngày đi, số người, và ngân sách. Sau đó, **AI Travel Planner** sẽ xử lý và đưa ra:

* **Lịch trình chi tiết từng ngày** với các hoạt động, thời gian và chi phí ước tính.
* **Gợi ý về các loại phương tiện di chuyển** (máy bay, tàu hỏa, ô tô) kèm theo thông tin chi phí và đường link đặt vé.
* **Gợi ý các khách sạn** phù hợp với ngân sách, bao gồm cả khoảng cách và chi phí taxi từ sân bay/nhà ga.
* **Phân tích chi phí** chi tiết cho toàn bộ chuyến đi (di chuyển, lưu trú, ăn uống, v.v.).
* **Dự báo thời tiết** và các lưu ý quan trọng.

## Các tính năng chính

* **Tạo lịch trình tự động**: Tạo lịch trình du lịch đầy đủ và chi tiết dựa trên dữ liệu đầu vào.
* **Gợi ý thông minh**: Đưa ra các đề xuất về chuyến bay, chuyến tàu và khách sạn.
* **Phân tích ngân sách**: Đảm bảo lịch trình nằm trong khoảng ngân sách bạn đã đặt ra.
* **Xuất PDF**: Hỗ trợ tải xuống lịch trình dưới dạng tệp PDF để dễ dàng chia sẻ hoặc in ấn.
* **Giao diện thân thiện**: Thiết kế đơn giản, dễ sử dụng, tương thích trên mọi thiết bị.

## Công nghệ sử dụng

* **Back-end**:
    * **Spring Boot**: Xây dựng ứng dụng web mạnh mẽ và ổn định.
    * **Google Gemini API**: Lõi trí tuệ nhân tạo để tạo ra lịch trình.
    * **Lombok**: Tự động hóa việc tạo getters, setters, constructors.
* **Front-end**:
    * **HTML5 / CSS3**: Cấu trúc và thiết kế giao diện.
    * **Bootstrap 5**: Xây dựng layout và các component UI hiện đại.
    * **JavaScript (Fetch API)**: Xử lý tương tác front-end với back-end.
* **Tích hợp**:
    * **Maven**: Quản lý các thư viện và dependencies.
    * **Jackson**: Chuyển đổi đối tượng Java thành JSON và ngược lại.
    * **Html2PDF**: Chuyển đổi nội dung HTML thành tệp PDF.

## Hướng dẫn cài đặt và chạy ứng dụng

### Yêu cầu hệ thống

* Java Development Kit (JDK) 17 hoặc cao hơn.
* Maven.
* Một IDE hỗ trợ Java (ví dụ: IntelliJ IDEA, VS Code).
* Khoá API của Google Gemini.

### Các bước thực hiện

1.  **Clone repository:**
    ```bash
    git clone [https://github.com/](https://github.com/)<LuuHieuKhanh>/<travel-planner>.git
    cd <travel-planner>
    ```

2.  **Cấu hình API Key:**
    * Tạo tệp `src/main/resources/application.properties` (nếu chưa có).
    * Thêm khóa API của Google Gemini vào tệp:
        ```properties
        google.ai.api.key=YOUR_GOOGLE_GEMINI_API_KEY
        ```

3. **Chạy ứng dụng:**
    Sử dụng Maven để chạy ứng dụng từ terminal:
    ```bash
    mvn spring-boot:run
    ```

5.  **Truy cập ứng dụng:**
    Mở trình duyệt và truy cập vào địa chỉ:
    ```
    http://localhost:8080
    ```
---