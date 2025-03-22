package com.emailcode.controller;

import com.emailcode.dto.EmailRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.emailcode.model.User;
import com.emailcode.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 发送验证码接口
    @Operation(summary = "发送验证码", description = "向指定邮箱发送6位数字验证码")
    @ApiResponse(responseCode = "200", description = "验证码发送成功")
    @ApiResponse(responseCode = "500", description = "邮件发送失败")
    @PostMapping("/send-code")
    public ResponseEntity<?> sendVerificationCode(@Valid @RequestBody EmailRequest request) {
        String email = request.email();
        // 生成6位随机验证码
        String code = String.format("%06d", new Random().nextInt(999999));
        
        // 存储到Redis，5分钟有效
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);

        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1690544550@qq.com"); // 必须与MailConfig中的username一致
        message.setTo(email);
        message.setSubject("验证码通知");
        message.setText("您的验证码是：" + code + "，5分钟内有效");
        
        mailSender.send(message);

        return ResponseEntity.ok().body("验证码已发送");
    }

    // 验证验证码接口
    @ApiResponse(responseCode = "200", description = "验证成功")
    @ApiResponse(responseCode = "400", description = "验证码错误或过期")
    public record VerificationRequest(String email, String code) {}
    
    @PutMapping("/verify")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody VerificationRequest request) {
        // 使用 request.email() 和 request.code()
        String storedCode = redisTemplate.opsForValue().get(request.email);
        
        if (storedCode == null) {
            return ResponseEntity.badRequest().body("验证码已过期");
        }
        if (!storedCode.equals(request.code())) {
            return ResponseEntity.badRequest().body("验证码错误");
        }
        
        // 验证成功后删除验证码
        redisTemplate.delete(request.email);
        return ResponseEntity.ok().body("验证成功");
    }

    // 新增密码重置接口, 这个才是重点
    @ApiResponse(responseCode = "200", description = "密码重置成功")
    @ApiResponse(responseCode = "400", description = "验证失败或用户不存在")
    public record ResetPasswordRequest(String email, String code, String newPassword) {}

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        // 使用 request.email(), request.code(), request.newPassword()
        String storedCode = redisTemplate.opsForValue().get(request.email);
        if (storedCode == null || !storedCode.equals(request.code())) {
            return ResponseEntity.badRequest().body("验证码错误或已过期");
        }
        
        // 查找用户
        User user = userRepository.findByEmail(request.email);
        if (user == null) {
            return ResponseEntity.badRequest().body("用户不存在");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        
        // 删除验证码
        redisTemplate.delete(request.email);
        
        return ResponseEntity.ok().body("密码重置成功");
    }
}