
package com.jiangtj.platform.auth.servlet;


import com.jiangtj.platform.auth.AuthKeyLocator;
import com.jiangtj.platform.auth.annotations.HasLogin;
import com.jiangtj.platform.auth.annotations.HasPermission;
import com.jiangtj.platform.auth.annotations.HasRole;
import com.jiangtj.platform.auth.annotations.HasTokenType;
import com.jiangtj.platform.auth.servlet.rbac.HasLoginAdvice;
import com.jiangtj.platform.auth.servlet.rbac.HasPermissionAdvice;
import com.jiangtj.platform.auth.servlet.rbac.HasRoleAdvice;
import com.jiangtj.platform.auth.servlet.rbac.HasTokenTypeAdvice;
import com.jiangtj.platform.common.aop.AnnotationPointcut;
import feign.RequestInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletAutoConfiguration {

    @Bean
    @LoadBalanced
    @ConditionalOnProperty(prefix="auth", name = "init-load-balanced-client", havingValue = "true", matchIfMissing = true)
    RestTemplate loadBalanced() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthKeyLocator authKeyLocator() {
        return new ServletAuthKeyLocator();
    }

    @Bean
    public ServletTokenFilter servletTokenFilter() {
        return new ServletTokenFilter();
    }

    @Bean
    public ServletJWTExceptionHandler servletJWTExceptionHandler() {
        return new ServletJWTExceptionHandler();
    }

    @Bean
    @ConditionalOnClass(RequestInterceptor.class)
    @ConditionalOnProperty(prefix="auth", name = "init-load-balanced-client", havingValue = "true", matchIfMissing = true)
    public AuthFeignInterceptor authFeignInterceptor() {
        return new AuthFeignInterceptor();
    }

    @Bean
    public HasLoginAdvice hasLoginAdvice() {
        return new HasLoginAdvice();
    }

    @Bean
    public HasTokenTypeAdvice hasTokenTypeAdvice() {
        return new HasTokenTypeAdvice();
    }

    @Bean
    public HasRoleAdvice hasRoleAdvice() {
        return new HasRoleAdvice();
    }

    @Bean
    public HasPermissionAdvice hasPermissionAdvice() {
        return new HasPermissionAdvice();
    }

    @Bean
    public Advisor hasLoginAdvisor(HasLoginAdvice advice) {
        return new DefaultPointcutAdvisor(new AnnotationPointcut<>(HasLogin.class), advice);
    }

    @Bean
    public Advisor hasTokenTypeAdvisor(HasTokenTypeAdvice advice) {
        return new DefaultPointcutAdvisor(new AnnotationPointcut<>(HasTokenType.class), advice);
    }

    @Bean
    public Advisor hasRoleAdvisor(HasRoleAdvice advice) {
        return new DefaultPointcutAdvisor(new AnnotationPointcut<>(HasRole.class), advice);
    }

    @Bean
    public Advisor hasPermissionAdvisor(HasPermissionAdvice advice) {
        return new DefaultPointcutAdvisor(new AnnotationPointcut<>(HasPermission.class), advice);
    }

}