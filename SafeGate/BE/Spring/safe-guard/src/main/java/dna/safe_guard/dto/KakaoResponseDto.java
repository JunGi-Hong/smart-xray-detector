package dna.safe_guard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class KakaoResponseDto {

    // 카카오 토큰 발급 응답
    @Getter
    @NoArgsConstructor
    public static class Token {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("expires_in")
        private Integer expiresIn;
    }

    // 카카오 사용자 정보 응답
    @Getter
    @NoArgsConstructor
    public static class UserInfo {
        private Long id;

        @JsonProperty("kakao_account")
        private KakaoAccount kakaoAccount;

        @Getter
        @NoArgsConstructor
        public static class KakaoAccount {
            private String email;
            private Profile profile;

            @Getter
            @NoArgsConstructor
            public static class Profile {
                private String nickname;
            }
        }

        public String getEmail() {
            return kakaoAccount != null ? kakaoAccount.getEmail() : null;
        }

        public String getNickname() {
            if (kakaoAccount != null && kakaoAccount.getProfile() != null) {
                return kakaoAccount.getProfile().getNickname();
            }
            return null;
        }
    }
}
