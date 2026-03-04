package dna.safe_guard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class BoardDetailResponseDto {

    // 이미지 파일의 경로 (URL)
    private String src;

    @JsonProperty("start-time")
    private String startTime;

    // 탐지된 유해물품 번호 리스트 (예: [1, 5] -> 총, 칼)
    private List<Integer> detect;
}