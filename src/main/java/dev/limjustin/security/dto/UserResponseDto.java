package dev.limjustin.security.dto;

import dev.limjustin.security.domain.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private String email;

    public UserResponseDto(User user) {
        this.email = user.getEmail();
    }
}
