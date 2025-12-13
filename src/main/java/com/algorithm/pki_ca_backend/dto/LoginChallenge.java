package com.algorithm.pki_ca_backend.dto;

import java.time.LocalDateTime;

// 登录挑战凭证DTO，用于存储和验证登录挑战
public class LoginChallenge {

    // 挑战字符串（通常是随机生成的令牌）
    private String challenge;
    // 挑战过期时间
    private LocalDateTime expireAt;

    public LoginChallenge(String challenge, LocalDateTime expireAt) {
        this.challenge = challenge;
        this.expireAt = expireAt;
    }

    // 获取挑战字符串
    public String getChallenge() {
        return challenge;
    }

    // 获取过期时间
    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    // 检查挑战是否已过期
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireAt);
    }
}