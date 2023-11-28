package com.jiangtj.platform.auth.reactive;

import com.jiangtj.platform.auth.AuthKeyLocator;
import com.jiangtj.platform.auth.PublicKeyCachedService;
import io.jsonwebtoken.Header;
import jakarta.annotation.Resource;

import java.security.Key;

public class ReactiveAuthKeyLocator implements AuthKeyLocator {

    @Resource
    private PublicKeyCachedService publicKeyCachedService;

    @Override
    public Key locate(Header header) {
        Object kid = header.get("kid");
        return publicKeyCachedService.getPublicJwk((String) kid).toKey();
    }
}