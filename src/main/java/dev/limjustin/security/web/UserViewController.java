package dev.limjustin.security.web;

import dev.limjustin.security.domain.User;
import dev.limjustin.security.dto.UserResponseDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal User userInfo) throws Exception {
        UserResponseDto responseDto = new UserResponseDto(userInfo);
        model.addAttribute("userInfo", responseDto);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
