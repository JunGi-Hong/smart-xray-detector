package dna.safe_guard.controller;

import dna.safe_guard.dto.UserRequestDto;
import dna.safe_guard.dto.UserResponseDto;
import dna.safe_guard.security.CustomUserDetails;
import dna.safe_guard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto.Message> register(@RequestBody UserRequestDto.Register dto) {
        userService.register(dto);
        return ResponseEntity.ok(new UserResponseDto.Message("success"));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto.Token> login(@RequestBody UserRequestDto.Login dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<UserResponseDto.Message> logout(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody(required = false) Map<String, String> body) {

        String accessToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7); // "Bearer " 제거
        }

        String refreshToken = body != null ? body.get("refresh-token") : null;

        userService.logout(accessToken, refreshToken); // 블랙리스트에 추가

        return ResponseEntity.ok(new UserResponseDto.Message("success"));
    }

    @PutMapping("/edit-profile")
    public ResponseEntity<?> updateProfile(
            @RequestBody UserRequestDto.UpdateProfile dto,
            @RequestHeader("Authorization") String bearerToken) {

        try {
            // Bearer 토큰에서 실제 토큰 추출
            String token = bearerToken.substring(7);
            userService.updateProfile(dto, token);
            return ResponseEntity.ok(new UserResponseDto.Message("success"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new UserResponseDto.Fail(e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto.Profile> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserResponseDto.Profile profile = UserResponseDto.Profile.builder()
                .name(userDetails.getName())
                .email(userDetails.getEmail())
                .phoneNumber(userDetails.getPhoneNumber())
                .build();

        return ResponseEntity.ok(profile);
    }
}
