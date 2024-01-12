package dev.limjustin.security.auth.service;

import dev.limjustin.security.auth.refreshtoken.service.RefreshTokenService;
import dev.limjustin.security.auth.refreshtoken.jwt.TokenProvider;
import dev.limjustin.security.domain.User;
import dev.limjustin.security.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;  // RefreshToken 테이블에서 사용자 아이디 가져오는 용도
    private final UserRepository userRepository;  // 사용자 아이디 통해 실제 사용자 객체 가져오는 용도

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        // 토큰의 유효성이 확인되면 사용자 정보 가져오기
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));

        // 토큰 생성
        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
