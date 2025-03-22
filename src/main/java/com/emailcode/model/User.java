package com.emailcode.model;

import jakarta.persistence.*;

/**
 * 用户实体类
 */
@Entity
@Table(name = "users")
public class User {
    
    /**
     * 用户唯一标识
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户邮箱（唯一）
     */
    @Column(unique = true)
    private String email;

    /**
     * 加密后的密码
     */
    private String password;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}