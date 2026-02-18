package dna.safe_guard.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class RecentEventDto {
    // 도넛 차트 계산을 위한 탐지 물품 번호 리스트만 포함합니다.
    private List<Integer> detect;
}