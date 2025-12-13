package com.algorithm.pki_ca_backend.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.security.Security;

@Configuration  // Spring配置类声明
public class BouncyCastleConfig {

    // Bean初始化后的执行方法
    @PostConstruct
    public void addProvider() {
        // 检查BouncyCastle提供者是否已注册
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());  // 将BouncyCastle添加到JVM安全提供者列表
        }
        // 注册后应用可使用BouncyCastle的加密算法
    }
}