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

    // 1. 여기에 시크릿 키를 넣습니다. (ON으로 설정한 '카카오 로그인'용 코드를 쓰세요)
    @Value("${kakao.client-secret}")
    private String clientSecret;

    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Transactional
    public UserResponseDto.Token kakaoLogin(String code) {
        KakaoResponseDto.Token kakaoToken = getKakaoToken(code);
        KakaoResponseDto.UserInfo userInfo = getKakaoUserInfo(kakaoToken.getAccessToken());
        User user = findOrCreateUser(userInfo);

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        return UserResponseDto.Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getName())
                .build();
    }

    private KakaoResponseDto.Token getKakaoToken(String code) {
        System.out.println("DEBUG >>> clientId: [" + clientId + "]");
        System.out.println("DEBUG >>> redirectUri: [" + redirectUri + "]");
        System.out.println("DEBUG >>> code: [" + code + "]");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        // 2. 파라미터에 시크릿 키 추가 (ON 상태일 때 필수)
        params.add("client_secret", clientSecret);

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

    // ... (이하 getKakaoUserInfo, findOrCreateUser 메서드는 동일)
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

    private User findOrCreateUser(KakaoResponseDto.UserInfo userInfo) {
        String email = userInfo.getEmail();
        if (email == null) {
            email = "kakao_" + userInfo.getId() + "@kakao.com";
        }

        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            return existing.get();
        }

        String nickname = userInfo.getNickname() != null ? userInfo.getNickname() : "카카오사용자";
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(email);
        newUser.setName(nickname);
        newUser.setPassword(UUID.randomUUID().toString());
        return userRepository.save(newUser);
    }
}