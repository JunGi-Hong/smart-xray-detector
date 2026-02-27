package dna.safe_guard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.util.List;

public class ReportDto {

    @Data
    @Builder
    public static class Request {
        private String period;
        @JsonProperty("start_date")
        private String startDate;
        @JsonProperty("end_date")
        private String endDate;
        private List<EventData> events;
    }

    @Data
    @Builder
    public static class EventData {
        private String time;
        @JsonProperty("detected_items")
        private List<String> detectedItems;
    }

    @Data
    public static class Response {
        @JsonProperty("pdf_url")
        private String pdfUrl;
    }
}