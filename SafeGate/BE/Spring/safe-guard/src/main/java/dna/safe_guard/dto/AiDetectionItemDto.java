package dna.safe_guard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AiDetectionItemDto {

    private String item;

    @JsonProperty("probability_percent")
    private Double probabilityPercent;
}