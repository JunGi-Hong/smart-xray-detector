package dna.safe_guard.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private long accessTokenValidTime = 30 * 60 * 1000L; // 30분
    private long refreshTokenValidTime = 24 * 60 * 60 * 1000L; // 24시간

    public String createAccessToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String email) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(key)
                .compact();
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getExpiration(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();

            long now = new Date().getTime();
            return expiration.getTime() - now; // 남은 시간(밀리초)
        } catch (Exception e) {
            return 0;
        }
    }
}