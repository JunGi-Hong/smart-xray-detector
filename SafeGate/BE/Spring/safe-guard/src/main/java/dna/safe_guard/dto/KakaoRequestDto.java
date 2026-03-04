package dna.safe_guard.dto;

import lombok.Getter;
import lombok.Setter;

public class KakaoRequestDto {

    @Getter
    @Setter
    public static class AuthCode {
        private String code; // 카카오 인가 코드
    }
}
