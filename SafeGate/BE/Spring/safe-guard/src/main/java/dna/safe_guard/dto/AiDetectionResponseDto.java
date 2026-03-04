package dna.safe_guard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiDetectionResponseDto {

    @JsonProperty("image_file")
    private String imageFile;

    @JsonProperty("total_detected")
    private Integer totalDetected;

    private List<AiDetectionItemDto> detections;

    @JsonProperty("image_base64")
    private String imageBase64;

    @JsonProperty("event-id")
    private Long eventId;

}