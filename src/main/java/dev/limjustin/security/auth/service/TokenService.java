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
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));

        return tokenProvider.generatedToken(user, Duration.ofHours(2));
    }
}
