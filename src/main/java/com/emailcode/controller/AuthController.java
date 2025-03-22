package com.emailcode.controller;

import com.emailcode.model.User;
import com.emailcode.repository.UserRepository;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")


public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @Parameter(description = "邮箱地址", example = "user@example.com")
        @RequestParam String email,
        @Parameter(description = "密码（需包含大小写字母和数字）", 
            example = "Passw0rd!")
        @RequestParam String password) {

        if (userRepository.findByEmail(email) != null) {
            return ResponseEntity.badRequest().body("邮箱已注册");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok().body("注册成功");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String email,
            @RequestParam String password) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("用户不存在");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.badRequest().body("密码错误");
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "登录成功");
        return ResponseEntity.ok(response);
    }
}
