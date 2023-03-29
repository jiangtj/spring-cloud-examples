package com.jtj.cloud.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import javax.crypto.SecretKey;

/**
 * 2018/9/26.
 */
@Slf4j
public class AuthServer {

    @Resource
    private AuthProperties properties;
    @Resource
    private Environment environment;

    private static SecretKey randomSecretKey = null;

    public SecretKey getKey(){
        String secret = properties.getSecret();
        if (secret == null) {
            if (randomSecretKey != null) {
                return randomSecretKey;
            }
            log.warn("您未设置Key，请在配置中心设置统一的 auth.secret");
            log.warn("正在为您生成一个随机的 HS256 Key（这会导致服务间无法调用）");
            randomSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            return randomSecretKey;
        }
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public AuthProperties getProperties() {
        return properties;
    }

    public JWTVerifier verifier() {
        return new JWTVerifier(this);
    }

    public JWTBuilder builder() {
        return new JWTBuilder(this);
    }

    public String getApplicationName() {
        String applicationName = environment.getProperty("spring.application.name");
        if (applicationName != null) {
            return applicationName.toLowerCase();
        }
        return "unknown";
    }
}