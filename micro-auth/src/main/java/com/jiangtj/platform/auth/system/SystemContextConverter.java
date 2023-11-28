package com.jiangtj.platform.auth.system;

import com.jiangtj.platform.auth.TokenType;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.context.AuthContextConverter;
import com.jiangtj.platform.auth.context.RoleProvider;
import io.jsonwebtoken.Claims;

public class SystemContextConverter implements AuthContextConverter {

    private final RoleProvider roleProvider;

    public SystemContextConverter(RoleProvider roleProvider) {
        this.roleProvider = roleProvider;
    }

    @Override
    public String type() {
        return TokenType.SYSTEM_USER;
    }

    @Override
    public AuthContext convert(String token, Claims body) {
        return new SystemUserContextImpl(token, body, roleProvider);
    }
}