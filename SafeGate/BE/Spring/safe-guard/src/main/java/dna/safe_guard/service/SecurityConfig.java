package dna.safe_guard.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistRepository tokenBlacklistRepository; // 주입 추가

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/register", "/user/login", "/user/token").permitAll()
                        .anyRequest().authenticated()
                )
                // 필터 생성 시 두 개의 의존성 주입
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, tokenBlacklistRepository),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    // ... PasswordEncoder 생략 ...
}