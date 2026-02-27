package dna.safe_guard.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
@Builder
public class RecentEventDto {
    private Map<Integer, Integer> detect;
}