package dna.safe_guard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

public class UserResponseDto {

    // 1. 로그인 성공 시 응답 (기존 유지)
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Token {
        @JsonProperty("access token")
        private String accessToken;
        @JsonProperty("refresh token")
        private String refreshToken;
        private String username;
    }

    // 2. 토큰 재발급 성공 시 응답 (사진: "access token" 하나만 반환)
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefreshTokenResponse {
        @JsonProperty("access token")
        private String accessToken;
    }

    // 3. 일반적인 성공 메시지 (사진: "message": "success")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String message;
    }

    // 4. 실패 시 응답 메시지 (사진: "fail": "reason")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fail {
        private String fail;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Profile {
        private String name;
        private String email;
        @JsonProperty("tel")
        private String phoneNumber;
    }
}