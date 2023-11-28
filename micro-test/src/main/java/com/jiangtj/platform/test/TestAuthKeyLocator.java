package com.jiangtj.platform.test;

import com.jiangtj.platform.auth.AuthKeyLocator;
import com.jiangtj.platform.auth.AuthServer;
import io.jsonwebtoken.Header;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.ObjectProvider;

import java.security.Key;
import java.util.Objects;

public class TestAuthKeyLocator implements AuthKeyLocator {

    @Resource
    ObjectProvider<AuthServer> authServers;

    @Override
    public Key locate(Header header) {
        return Objects.requireNonNull(authServers.getIfUnique())
            .getPrivateJwk()
            .toPublicJwk()
            .toKey();
    }
}