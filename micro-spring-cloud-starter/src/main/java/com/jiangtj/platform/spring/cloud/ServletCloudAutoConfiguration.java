
package com.jiangtj.platform.spring.cloud;


import com.jiangtj.platform.spring.cloud.core.CoreInstanceApi;
import com.jiangtj.platform.spring.cloud.core.CoreTokenInterceptor;
import com.jiangtj.platform.spring.cloud.jwt.AuthKeyLocator;
import com.jiangtj.platform.spring.cloud.jwt.ServletJWTExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletCloudAutoConfiguration {
    @Bean
    public ServletJWTExceptionHandler servletJWTExceptionHandler() {
        return new ServletJWTExceptionHandler();
    }

    @Bean
    public CoreTokenInterceptor coreTokenInterceptor() {
        return new CoreTokenInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public CoreInstanceApi coreInstanceApi(LoadBalancerInterceptor loadBalancerInterceptor,
                                           CoreTokenInterceptor coreTokenInterceptor) {
        RestClient restClient = RestClient.builder()
            .baseUrl("lb://core-server/")
            .requestInterceptor(loadBalancerInterceptor)
            .requestInterceptor(coreTokenInterceptor)
            .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(CoreInstanceApi.class);
    }

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

}
