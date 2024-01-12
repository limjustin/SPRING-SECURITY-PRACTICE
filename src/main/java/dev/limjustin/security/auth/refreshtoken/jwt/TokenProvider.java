package dev.limjustin.security.auth.refreshtoken.jwt;

import dev.limjustin.security.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // 토큰 생성 메서드
    private String makeToken(Date expiry, User user) {
        Date now = new Date();
        return Jwts.builder()
                // Header
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                // Payload
                .setIssuer(jwtProperties.getIssuer())  // 토큰 발급자 (issuer)
                .setSubject(user.getEmail())  // 토큰 제목 (subject)
                .setExpiration(expiry)  // 토큰의 만료 시간 (expiration)
                .setIssuedAt(now)  // 토큰이 발급된 시간 (issued at)
                .claim("id", user.getId())  // 클레임 id : userId
                // Signature
                .signWith(jwtProperties.getKey(), SignatureAlgorithm.HS256)  // 암호화 된 비밀키 값 + 해시를 HS256 방식으로 암호화
                .compact();
    }

    // JWT 토큰 유효성 검사
    public boolean validToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        System.out.println("claims.getId() = " + claims.getId());
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(
                claims.getSubject(), "", authorities
        ), token, authorities);
    }

    // 토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    // 클레임 조회 메서드
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
