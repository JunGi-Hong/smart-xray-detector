package dna.safe_guard.service;

import dna.safe_guard.dto.UserRequestDto;
import dna.safe_guard.dto.UserResponseDto;
import dna.safe_guard.entity.TokenBlacklist;
import dna.safe_guard.entity.User;
import dna.safe_guard.repository.TokenBlacklistRepository;
import dna.safe_guard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import dna.safe_guard.entity.TokenBlacklist;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Transactional
    public void register(UserRequestDto.Register dto) {
        if (!dto.getPassword().equals(dto.getPassword2())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // 암호화 저장
        user.setUsername(dto.getEmail()); // 로그인 ID를 이메일로 설정
        userRepository.save(user);
    }

    public UserResponseDto.Token login(UserRequestDto.Login dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        return UserResponseDto.Token.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user.getEmail()))
                .refreshToken(jwtTokenProvider.createRefreshToken(user.getEmail()))
                .username(user.getName())
                .build();
    }

    @Transactional
    public void logout(String accessToken, String refreshToken) {
        // 1. Access Token 블랙리스트 추가
        if (accessToken != null && !accessToken.trim().isEmpty()) {
            TokenBlacklist blacklistedAccessToken = TokenBlacklist.builder()
                    .token(accessToken)
                    .expiresAt(LocalDateTime.now().plusMinutes(30)) // Access Token 만료 시간
                    .build();
            tokenBlacklistRepository.save(blacklistedAccessToken);
        }

        // 2. Refresh Token 블랙리스트 추가
        if (refreshToken != null && !refreshToken.trim().isEmpty()) {
            TokenBlacklist blacklistedRefreshToken = TokenBlacklist.builder()
                    .token(refreshToken)
                    .expiresAt(LocalDateTime.now().plusHours(24)) // Refresh Token 만료 시간
                    .build();
            tokenBlacklistRepository.save(blacklistedRefreshToken);
        }
    }

    @Transactional
    public void updateProfile(UserRequestDto.UpdateProfile dto, String token) {
        // 1. 토큰에서 이메일 추출
        String email = jwtTokenProvider.getEmail(token);

        // 2. 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 3. 이름 수정
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            user.setName(dto.getName());
        }

        // 4. 전화번호 수정
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }

        // 5. 비밀번호 수정
        if (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty()) {
            // pw1과 pw2가 일치하는지 확인
            if (!dto.getNewPassword().equals(dto.getNewPassword2())) {
                throw new IllegalArgumentException("pw inconsistency");
            }
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        // 6. 변경사항 저장
        userRepository.save(user);
    }
}