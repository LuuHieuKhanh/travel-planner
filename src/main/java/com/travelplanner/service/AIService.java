package com.travelplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelplanner.model.TravelPlan;
import com.travelplanner.model.TravelRequest;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatLanguageModel chatModel;

    public AIService(@Value("${google.ai.api.key}") String apiKey) {
        this.chatModel = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .build();
    }

    public TravelPlan generatePlan(TravelRequest request) {
        String prompt = buildPrompt(request);
        try {
            String jsonResponse = chatModel.generate(prompt);
            String cleanedJsonResponse = cleanJsonString(jsonResponse);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(cleanedJsonResponse, TravelPlan.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi tạo lịch trình AI. Vui lòng kiểm tra lại cấu hình hoặc thử lại sau.");
        }
    }

    private String cleanJsonString(String json) {
        String cleaned = json.replaceAll("^\\s*```json|```\\s*$", "").trim();
        cleaned = cleaned.replaceAll("^`", "").replaceAll("`$", "");
        return cleaned;
    }

    private String buildPrompt(TravelRequest request) {
        String transportationType = request.getTransportation();

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Bạn là một trợ lý AI chuyên tạo lịch trình du lịch chi tiết và chính xác. ");
        promptBuilder.append("Bạn phải phản hồi bằng một và chỉ một đối tượng JSON. ");
        promptBuilder.append("Tạo một lịch trình du lịch cho chuyến đi từ ").append(request.getStartDestination());
        promptBuilder.append(" tới ").append(request.getDestination());
        promptBuilder.append(" từ ").append(request.getStartDate()).append(" đến ").append(request.getEndDate());
        promptBuilder.append(" cho ").append(request.getNumberOfPeople()).append(" người, với ngân sách dao động từ ").append(request.getMinBudget()).append(" VNĐ đến ").append(request.getMaxBudget()).append(" VNĐ. ");
        promptBuilder.append("Đảm bảo rằng tổng chi phí trong mục costBreakdown nằm trong khoảng ngân sách đã cho. ");
        promptBuilder.append("Phí vào cửa, chi phí ăn uống của từng địa điểm phải được thêm vào mục mô tả. ");
        promptBuilder.append("Các trường chi phí phải được hiển thị dưới dạng khoảng giá, ví dụ: \"1.000.000 - 2.000.000 VNĐ\". ");
        promptBuilder.append("Mỗi hoạt động hoặc địa điểm trong dailySchedules.activities phải có thêm trường \"imageUrl\" chứa link ảnh thực tế chất lượng cao, liên quan đến địa điểm hoặc hoạt động đó. ");
        promptBuilder.append("Ảnh nên được lấy từ nguồn công khai trên Pinterest. Không sử dụng của Wikipedia.\n");

        if (transportationType.equals("may-bay")) {
            promptBuilder.append("Đối với phương tiện là máy bay, hãy: ");
            promptBuilder.append("1. Gợi ý một số chuyến bay từ sân bay gần ").append(request.getStartDestination()).append(" đến sân bay gần ").append(request.getDestination()).append(".\n");
            promptBuilder.append("2. Cung cấp tên sân bay gần nhất tại điểm đến. \n");
            promptBuilder.append("3. Tính toán khoảng cách và chi phí taxi từ sân bay đến khách sạn.\n");
            promptBuilder.append("4. Cung cấp đường dẫn đặt vé cho các chuyến bay.\n");
            promptBuilder.append("5. Ghi rõ giá vé ước tính cho các chuyến bay. \n");
        } else if (transportationType.equals("tau-hoa")) {
            promptBuilder.append("Đối với phương tiện là tàu hỏa, hãy: ");
            promptBuilder.append("1. Gợi ý một số chuyến tàu phù hợp từ ga gần ").append(request.getStartDestination()).append(" đến ga gần ").append(request.getDestination()).append(".\n");
            promptBuilder.append("2. Cung cấp tên ga tàu gần nhất tại điểm đến.\n");
            promptBuilder.append("3. Tính toán khoảng cách và chi phí taxi từ ga tàu đến khách sạn.\n");
            promptBuilder.append("4. Cung cấp đường dẫn đặt vé cho các chuyến tàu.\n");
            promptBuilder.append("5. Ghi rõ giá vé ước tính cho các chuyến tàu. \n");
        } else { // ô tô/xe máy
            promptBuilder.append("Đối với phương tiện là ").append(transportationType).append(", hãy: ");
            promptBuilder.append("1. Cung cấp tổng khoảng cách từ ").append(request.getStartDestination()).append(" đến ").append(request.getDestination()).append(" và các điểm tham quan.\n");
            promptBuilder.append("2. Tính toán chi phí xăng xe ước tính cho toàn bộ hành trình.\n");
            promptBuilder.append("3. Gợi ý khách sạn và tính toán khoảng cách từ khách sạn đến địa điểm tham quan.\n");
        }

        promptBuilder.append("Định dạng JSON phải tuân thủ nghiêm ngặt theo mẫu sau: \n\n");

        promptBuilder.append("{\n");
        promptBuilder.append("  \"weatherInfo\": \"<thông_tin_thời_tiết>\",\n");
        promptBuilder.append("  \"suggestions\": \"<gợi_ý_chung>\",\n");
        promptBuilder.append("  \"dailySchedules\": [\n");
        promptBuilder.append("    {\"dayNumber\": 1, \"date\": \"<ngày>\", \"activities\": [{\"time\": \"<thời_gian>\", \"description\": \"<mô_tả_hoạt_động>\", \"fee\": \"<chi_phí_nơi_đến>\"}]}\n");
        promptBuilder.append("  ],\n");
        promptBuilder.append("  \"contingencyPlan\": \"<kế_hoạch_dự_phòng>\",\n");

        if (transportationType.equals("may-bay") || transportationType.equals("tau-hoa")) {
            promptBuilder.append("  \"hotels\": [\n");
            promptBuilder.append("    {\"name\": \"<tên_khách_sạn>\", \"description\": \"<mô_tả>\", \"priceRange\": \"<khoảng_giá>\", \"bookingUrl\": \"<đường_dẫn_đặt_phòng>\", \"distanceFromTransportation\": \"<khoảng_cách_đến_ga/sân_bay>\", \"taxiCostFromTransportation\": \"<chi_phí_taxi>\"}\n");
            promptBuilder.append("  ],\n");
        } else {
            promptBuilder.append("  \"hotels\": [\n");
            promptBuilder.append("    {\"name\": \"<tên_khách_sạn>\", \"description\": \"<mô_tả>\", \"priceRange\": \"<khoảng_giá>\", \"bookingUrl\": \"<đường_dẫn_đặt_phòng>\"}\n");
            promptBuilder.append("  ],\n");
        }

        promptBuilder.append("  \"costBreakdown\": {\n");
        promptBuilder.append("    \"transportationType\": \"").append(transportationType).append("\",\n");

        if (transportationType.equals("may-bay")) {
            promptBuilder.append("    \"nearestStation\": \"<tên_sân_bay_gần_nhất>\",\n");
            promptBuilder.append("    \"flightSuggestions\": [\n");
            promptBuilder.append("      {\"airline\": \"<tên_hãng_bay>\", \"flightNumber\": \"<số_hiệu_chuyến_bay>\", \"estimatedPrice\": \"<giá_vé_khứ_hồi_ước_tính_VNĐ>\", \"bookingUrl\": \"<link_đặt_vé>\"}\n");
            promptBuilder.append("    ],\n");
        } else if (transportationType.equals("tau-hoa")) {
            promptBuilder.append("    \"nearestStation\": \"<tên_ga_tàu_gần_nhất>\",\n");
            promptBuilder.append("    \"trainSuggestions\": [\n");
            promptBuilder.append("      {\"trainName\": \"<tên_tàu>\", \"estimatedPrice\": \"<giá_vé_khứ_hồi_ước_tính_VNĐ>\", \"bookingUrl\": \"<link_đặt_vé>\"}\n");
            promptBuilder.append("    ],\n");
        } else { // o-to, xe-may
            promptBuilder.append("    \"totalDistance\": \"<tổng_quãng_đường_khứ_hồi>\",\n");
        }

        promptBuilder.append("    \"transportationCost\": \"<chi_phí_di_chuyển_chính_dạng_khoảng_giá>\",\n");
        promptBuilder.append("    \"taxiCost\": \"<chi_phí_taxi_và_phương_tiện_khác_dạng_khoảng_giá>\",\n");
        promptBuilder.append("    \"accommodation\": \"<chi_phí_khách_sạn_dạng_khoảng_giá>\",\n");
        promptBuilder.append("    \"foodAndActivities\": \"<chi_phí_ăn_uống_và_hoạt_động_dạng_khoảng_giá>\",\n");
        promptBuilder.append("    \"miscellaneous\": \"<chi_phí_linh_tinh_dạng_khoảng_giá>\",\n");
        promptBuilder.append("    \"total\": \"<tổng_chi_phí_dạng_khoảng_giá>\"\n");
        promptBuilder.append("  }\n");
        promptBuilder.append("}\n");

        return promptBuilder.toString();
    }
}