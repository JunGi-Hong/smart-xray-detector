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
}