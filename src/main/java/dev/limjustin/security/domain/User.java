package dev.limjustin.security.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;  // 비밀번호는 반드시 암호화하여 저장

    @Builder
    public User(String email, String password, String auth) {
        this.email = email;
        this.password = password;
    }

    @Override  // 사용자가 가지고 있는 권한 목록 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override  // 사용자 id 반환
    public String getUsername() {
        return email;
    }

    @Override  // (1) 계정 잠금 여부 반환
    public boolean isAccountNonLocked() {
        return true;  // "User account is locked"
    }

    @Override  // (2) 계정 사용 가능 여부 반환
    public boolean isEnabled() {
        return true;  // "User is not enabled"
    }

    @Override  // (3) 계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        return true;  // "User account has expired"
    }

    @Override  // (4) 패스워드 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        return true;  // "User credentials have expired"
    }
}
