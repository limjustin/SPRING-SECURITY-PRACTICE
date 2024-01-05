package dev.limjustin.security.service;

import dev.limjustin.security.domain.User;
import dev.limjustin.security.domain.UserRepository;
import dev.limjustin.security.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(UserRequestDto requestDto) {
        return userRepository.save(User.builder()
                .email(requestDto.getEmail())
                .password(bCryptPasswordEncoder.encode(requestDto.getPassword()))
                .build()).getId();
    }
}
