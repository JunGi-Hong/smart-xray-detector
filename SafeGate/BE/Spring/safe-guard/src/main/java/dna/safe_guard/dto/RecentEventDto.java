package dna.safe_guard.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class RecentEventDto {
    private Long id;
    private String startTime;
    private String title;
    private List<Integer> detect; // 분석에 필요한 탐지 리스트 포함
}