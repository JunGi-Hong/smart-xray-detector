package dna.safe_guard.service;

import dna.safe_guard.dto.UserRequestDto;
import dna.safe_guard.dto.UserResponseDto;
import dna.safe_guard.entity.User;
import dna.safe_guard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 로그인 로직
     */
    @Transactional(readOnly = true)
    public UserResponseDto.Token login(UserRequestDto.Login dto) {
        // 1. 이메일로 사용자 확인
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 2. 비밀번호 일치 확인
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. Access Token & Refresh Token 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        // 4. 명세서 형식에 맞춰 반환
        return UserResponseDto.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getName()) // 성공 응답에 이름 포함
                .build();
    }

    /**
     * 토큰 재발급 로직 (/user/token)
     */
    public String reissueAccessToken(String refreshToken) {
        // 리프레시 토큰 유효성 검사
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            String email = jwtTokenProvider.getEmail(refreshToken);
            return jwtTokenProvider.createAccessToken(email);
        }
        throw new RuntimeException("리프레시 토큰이 유효하지 않습니다. 다시 로그인해주세요.");
    }
}