package com.emailcode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全配置类
 * - 配置请求权限规则
 * - 密码加密策略
 */
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/verification/**", 
                    "/api/auth/**",
                    "/swagger-ui/**",         // 放行所有swagger UI资源
                    "/v3/api-docs/**",       // 放行API文档端点
                    "/webjars/**"            // 放行swagger依赖的webjars
                ).permitAll()
                .anyRequest().authenticated()
            )
//            .httpBasic().disable() // 禁用基本认证
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
    
    /**
     * BCrypt密码加密器
     * @return 密码加密器实例
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}