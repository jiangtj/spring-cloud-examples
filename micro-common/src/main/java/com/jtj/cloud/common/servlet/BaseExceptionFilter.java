package com.jtj.cloud.common.servlet;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.jtj.cloud.common.servlet.BaseExceptionFilter.ORDER;


@Slf4j
@Order(ORDER)
public class BaseExceptionFilter extends OncePerRequestFilter {

    public final static int ORDER = -100;

    @Resource
    ServletExceptionHandler servletExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException ex) {
            servletExceptionHandler.handle(ex, request, response);
        }
    }
}