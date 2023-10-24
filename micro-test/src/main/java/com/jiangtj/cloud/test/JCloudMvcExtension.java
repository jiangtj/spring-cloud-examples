package com.jiangtj.cloud.test;

import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.AuthUtils;
import org.junit.jupiter.api.extension.*;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.util.Arrays;
import java.util.List;

public class JCloudMvcExtension implements BeforeTestExecutionCallback, ParameterResolver {

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return JCloudWebClientBuilder.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(extensionContext);
        AuthServer authServer = applicationContext.getBean(AuthServer.class);
        MockMvc mvc = applicationContext.getBean(MockMvc.class);
        WebTestClient webTestClient = MockMvcWebTestClient.bindTo(mvc).build();
        WebTestClient.Builder builder = webTestClient.mutate();
        JCloudWebClientBuilder jCloudWebClientBuilder = new JCloudWebClientBuilder(authServer, builder);
        UserToken token = extensionContext.getRequiredTestMethod().getAnnotation(UserToken.class);
        if (token != null) {
            List<String> roles = Arrays.stream(token.role())
                .map(AuthUtils::toKey)
                .toList();
            jCloudWebClientBuilder.setUser(token.id(), roles);
        }
        return jCloudWebClientBuilder;
    }
}