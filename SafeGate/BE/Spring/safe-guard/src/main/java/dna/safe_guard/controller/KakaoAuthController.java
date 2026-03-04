package dna.safe_guard.controller;

import dna.safe_guard.dto.KakaoRequestDto;
import dna.safe_guard.dto.UserResponseDto;
import dna.safe_guard.service.KakaoOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    /**
     * POST /user/kakao-login
     * Body: { "code": "카카오_인가_코드" }
     *
     * 프론트엔드에서 카카오 로그인 후 받은 인가 코드를 서버로 전달하면,
     * 서버가 직접 카카오 API를 호출하여 사용자 정보를 가져오고 JWT를 발급합니다.
     */
    @PostMapping("/kakao-login")
    public ResponseEntity<UserResponseDto.Token> kakaoLogin(
            @RequestBody KakaoRequestDto.AuthCode dto) {
        UserResponseDto.Token token = kakaoOAuthService.kakaoLogin(dto.getCode());
        return ResponseEntity.ok(token);
    }

    /**
     * GET /user/kakao-callback
     * 카카오 redirect_uri를 백엔드로 직접 설정한 경우 사용합니다.
     * redirect_uri를 프론트엔드로 설정했다면 이 엔드포인트는 사용하지 않아도 됩니다.
     */

    @GetMapping("/kakao-callback")
    public ResponseEntity<UserResponseDto.Token> kakaoCallback(
            @RequestParam("code") String code) {
        UserResponseDto.Token token = kakaoOAuthService.kakaoLogin(code);
        return ResponseEntity.ok(token);
    }
}
