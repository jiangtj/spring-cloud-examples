package com.jiangtj.platform.core.pk;

import com.jiangtj.platform.auth.AuthExceptionUtils;
import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.auth.TokenType;
import com.jiangtj.platform.auth.reactive.AuthReactorHolder;
import com.jiangtj.platform.auth.reactive.AuthReactorUtils;
import com.jiangtj.platform.common.JsonUtils;
import com.jiangtj.platform.spring.cloud.AuthServer;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContext;
import com.jiangtj.platform.spring.cloud.sba.RoleInst;
import com.jiangtj.platform.web.BaseExceptionUtils;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class PublicKeyService {

    @Resource
    private AuthServer authServer;
    @Resource
    private ReactiveDiscoveryClient discoveryClient;
    @Resource
    private PKTaskProperties pkTaskProperties;
    @Value("${spring.application.name}")
    String selfName;

    private final WebClient webClient = WebClient.create();
    private final List<MicroServiceData> instanceList = new ArrayList<>();
    private final Map<String, MicroServiceData> jwtIdToInstance = new ConcurrentHashMap<>();
    private final Map<String, MicroServiceData> instanceMap = new ConcurrentHashMap<>();

    // private final Sinks.Many<MicroServiceData> sink = Sinks.many().unicast().onBackpressureBuffer();

    /*@Scheduled(initialDelayString = "${pk.task.initial-delay}", fixedDelayString = "${pk.task.delay}", timeUnit = TimeUnit.SECONDS)
    public void handlePublicKeyMap() {
        log.debug("handling public keys ...");
        discoveryClient.getServices()
                .flatMap(discoveryClient::getInstances)
                .map(this::getCoreServiceInstance)
                .subscribe(this::updateCoreServiceInstance);
    }*/

    public MicroServiceData getCoreServiceInstance(ServiceInstance si) {
        URI uri = si.getUri();
        String serviceId = si.getServiceId();
        String instanceId = si.getInstanceId();
        MicroServiceData data = instanceMap.getOrDefault(instanceId, null);
        if (data == null) {
            data = MicroServiceData.builder()
                    .server(serviceId)
                    .instanceId(instanceId)
                    .uri(uri)
                    .instant(Instant.now())
                    .status(MicroServiceData.Status.Waiting)
                    .build();
            instanceList.add(data);
            instanceMap.put(instanceId, data);
        }
        if (data.getStatus() == MicroServiceData.Status.Down) {
            data.setStatus(MicroServiceData.Status.Waiting);
        }
        log.debug(JsonUtils.toJson(data));
        return data;
    }

    public void updateCoreServiceInstance(MicroServiceData csi) {
        Instant now = Instant.now();
        if (csi.getStatus() == MicroServiceData.Status.Up
                && csi.getInstant().plusSeconds(pkTaskProperties.getUpDelay()).isAfter(now)) {
            return;
        }
        fetchPublickey(csi);
    }

    public void fetchPublickey(MicroServiceData csi) {
        fetchPublickeyFn(csi)
                .subscribe(null, e -> {
                    csi.setInstant(Instant.now());
                    csi.setStatus(MicroServiceData.Status.Down);
                    log.error("fetchPublickey error!");
                    log.error(JsonUtils.toJson(csi));
                });
    }

    public Mono<MicroServiceData> fetchPublickeyFn(MicroServiceData csi) {
        if (selfName.equals(csi.getServer())) {
            csi.setInstant(Instant.now());
            csi.setKey(authServer.getPrivateJwk().toPublicJwk());
            csi.setStatus(MicroServiceData.Status.Up);
            return Mono.just(csi);
        }
        URI actuator = csi.getUri().resolve("/actuator/publickey");
        String header = authServer.createServerToken(csi.getServer());
        return webClient.get().uri(actuator)
                .header(AuthRequestAttributes.TOKEN_HEADER_NAME, header)
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> {
                    PublicJwk<PublicKey> publicJwk = (PublicJwk<PublicKey>) Jwks.parser()
                            .build().parse(json);
                    csi.setInstant(Instant.now());
                    csi.setKey(publicJwk);
                    csi.setStatus(MicroServiceData.Status.Up);
                    jwtIdToInstance.put(publicJwk.getId(), csi);
                    log.info(JsonUtils.toJson(csi));
                    return csi;
                });
    }

    public List<MicroServiceData> getAllCoreServiceInstance() {
        return instanceList;
    }

    public Flux<MicroServiceData> getMicroServiceDatas() {
        return Flux.fromIterable(instanceList);
    }

    public Mono<PublicJwk<PublicKey>> getPublicKey(String keyId) {
        String[] split = keyId.split(":");
        String serviceId = split[0];
        if (serviceId.equals(selfName)) {
            return Mono.just(authServer.getPrivateJwk().toPublicJwk());
        }
        return AuthReactorHolder.deferAuthContext()
                .flatMap(ctx -> {
                    if (ctx instanceof JwtAuthContext jwtCtx) {
                        String tokenType = jwtCtx.type();
                        Set<String> audience = jwtCtx.claims().getAudience();
                        if (!audience.contains(selfName)) {
                            return Mono.error(AuthExceptionUtils.invalidToken("不支持访问当前服务", null));
                        }
                        if (!TokenType.SERVER.equals(tokenType)) {
                            return Mono.just(ctx).flatMap(AuthReactorUtils.hasRoleHandler(RoleInst.ACTUATOR.name()));
                        }
                        return Mono.just(ctx);
                    } else  {
                        return Mono.error(AuthExceptionUtils.invalidToken("不支持的 Auth Context", null));
                    }
                })
                .filter(ctx -> !jwtIdToInstance.containsKey(keyId))
                .flatMapMany(ctx -> discoveryClient.getInstances(serviceId))
                .map(this::getCoreServiceInstance)
                .filter(csi -> {
                    Instant now = Instant.now();
                    if (csi.getStatus() == MicroServiceData.Status.Up
                            && csi.getInstant().plusSeconds(1).isAfter(now)) {
                        return false;
                    }
                    return true;
                })
                .flatMap(this::fetchPublickeyFn)
                .then(Mono.justOrEmpty(jwtIdToInstance.get(keyId)))
                .map(MicroServiceData::getKey);
    }

    public PublicJwk<PublicKey> getPublicKeyObject(String keyId) {
        MicroServiceData data = jwtIdToInstance.getOrDefault(keyId, null);
        if (data == null) {
            String[] split = keyId.split(":");
            String serviceId = split[0];
            discoveryClient.getInstances(serviceId)
                    .map(this::getCoreServiceInstance)
                    .filter(csi -> {
                        Instant now = Instant.now();
                        if (csi.getStatus() == MicroServiceData.Status.Up
                                && csi.getInstant().plusSeconds(1).isAfter(now)) {
                            return false;
                        }
                        return true;
                    })
                    .flatMap(this::fetchPublickeyFn)
                    .subscribe();
            throw BaseExceptionUtils.badRequest("无效的kid！");
        }
        return data.getKey();
    }
}
