package dna.safe_guard.controller;

import dna.safe_guard.dto.UserRequestDto;
import dna.safe_guard.dto.UserResponseDto;
import dna.safe_guard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
