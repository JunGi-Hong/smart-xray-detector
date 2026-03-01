package dna.safe_guard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class UserRequestDto {

    @Getter
    @Setter
    public static class Register {
        private String name;
        private String email;
        @JsonProperty("tel")
        private String phoneNumber;
        private String password;
        private String password2;
    }

    @Getter
    @Setter
    public static class Login {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    public static class UpdateProfile {
        private String name;
        @JsonProperty("new-pw")
        private String newPassword;
        @JsonProperty("new-pw2")
        private String newPassword2;
        @JsonProperty("access-token") // 사진에 있는 필드 추가
        private String accessToken;
        @JsonProperty("tel")
        private String phoneNumber;
    }

    @Getter
    @Setter
    public static class VerifyPassword {
        private String email;
        private String password;
    }
}