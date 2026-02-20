package dna.safe_guard.service;

import dna.safe_guard.dto.KakaoResponseDto;
import dna.safe_guard.dto.UserResponseDto;
import dna.safe_guard.entity.User;
import dna.safe_guard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    /**
     * 인가 코드로 카카오 토큰 발급 후 로그인 처리
     */
    @Transactional
    public UserResponseDto.Token kakaoLogin(String code) {
        // 1. 인가 코드로 카카오 Access Token 발급
        KakaoResponseDto.Token kakaoToken = getKakaoToken(code);

        // 2. 카카오 Access Token으로 사용자 정보 조회
        KakaoResponseDto.UserInfo userInfo = getKakaoUserInfo(kakaoToken.getAccessToken());

        // 3. 사용자 정보로 회원가입 또는 로그인 처리
        User user = findOrCreateUser(userInfo);

        // 4. 자체 JWT 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        return UserResponseDto.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getName())
                .build();
    }

    /**
     * 카카오 인가 코드 → 카카오 Access Token 교환
     */
    private KakaoResponseDto.Token getKakaoToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoResponseDto.Token> response = restTemplate.exchange(
                KAKAO_TOKEN_URL,
                HttpMethod.POST,
                request,
                KakaoResponseDto.Token.class
        );

        if (response.getBody() == null) {
            throw new RuntimeException("카카오 토큰 발급에 실패했습니다.");
        }
        return response.getBody();
    }

    /**
     * 카카오 Access Token으로 사용자 정보 조회
     */
    private KakaoResponseDto.UserInfo getKakaoUserInfo(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoResponseDto.UserInfo> response = restTemplate.exchange(
                KAKAO_USER_INFO_URL,
                HttpMethod.GET,
                request,
                KakaoResponseDto.UserInfo.class
        );

        if (response.getBody() == null) {
            throw new RuntimeException("카카오 사용자 정보 조회에 실패했습니다.");
        }
        return response.getBody();
    }

    /**
     * 이메일로 기존 회원 조회, 없으면 자동 회원가입
     */
    private User findOrCreateUser(KakaoResponseDto.UserInfo userInfo) {
        String email = userInfo.getEmail();
        if (email == null) {
            // 카카오 계정에 이메일이 없을 경우 kakaoId 기반으로 임시 이메일 생성
            email = "kakao_" + userInfo.getId() + "@kakao.com";
        }

        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            return existing.get();
        }

        // 신규 사용자 자동 생성
        String nickname = userInfo.getNickname() != null ? userInfo.getNickname() : "카카오사용자";
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(email);
        newUser.setName(nickname);
        // 소셜 로그인 사용자는 비밀번호를 랜덤 UUID로 설정 (직접 로그인 불가)
        newUser.setPassword(UUID.randomUUID().toString());
        return userRepository.save(newUser);
    }
}
