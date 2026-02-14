package dna.safe_guard.controller;

import dna.safe_guard.dto.UserRequestDto;
import dna.safe_guard.dto.UserResponseDto;
import dna.safe_guard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserResponseDto.Message> logout() {
        // 실제로는 클라이언트에서 토큰을 지우거나 블랙리스트 처리를 합니다.
        return ResponseEntity.ok(new UserResponseDto.Message("success"));
    }
}
