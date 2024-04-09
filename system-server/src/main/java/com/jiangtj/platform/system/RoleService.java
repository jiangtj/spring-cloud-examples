package com.jiangtj.platform.system;

import com.jiangtj.platform.auth.KeyUtils;
import com.jiangtj.platform.auth.servlet.AuthHolder;
import com.jiangtj.platform.spring.cloud.AuthServer;
import com.jiangtj.platform.spring.cloud.server.ServerContextImpl;
import com.jiangtj.platform.spring.cloud.system.Role;
import com.jiangtj.platform.spring.cloud.system.RoleSyncDto;
import com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator;
import com.jiangtj.platform.system.jooq.tables.records.SystemRoleCreatorRecord;
import com.jiangtj.platform.web.BaseExceptionUtils;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Stream;

import static com.jiangtj.platform.system.jooq.Tables.SYSTEM_ROLE;
import static com.jiangtj.platform.system.jooq.Tables.SYSTEM_ROLE_CREATOR;

@Slf4j
@Service
public class RoleService {

    @Resource
    private DSLContext create;
    @Value("${spring.application.name}")
    String selfName;
    @Resource
    RestClient.Builder loadBalancedClient;
    @Resource
    AuthServer authServer;

    @Getter
    List<ServerRole> serverRoles;

    public void syncRole(List<RoleSyncDto> list) {
        if (AuthHolder.getAuthContext() instanceof ServerContextImpl sctx) {
            String issuer = sctx.getIssuer();
            // 清理曾经的自动创建记录
            create.deleteFrom(SYSTEM_ROLE_CREATOR)
                .where(SYSTEM_ROLE_CREATOR.CREATOR.eq(issuer))
                .and(SYSTEM_ROLE_CREATOR.AUTO_CREATE.eq((byte) 1))
                .execute();
            // 注册角色
            list.forEach(item -> {
                String key = KeyUtils.toKey(item.getKey());
                registerRole(new SystemRoleCreator(key, issuer, item.getKey(), item.getName(), (byte) 1));
            });
        } else {
            throw BaseExceptionUtils.badRequest("not support");
        }
    }

    public void registerRole(SystemRoleCreator roleCreator) {
        create.insertInto(SYSTEM_ROLE, SYSTEM_ROLE.KEY)
            .values(roleCreator.roleKey())
            .onDuplicateKeyIgnore()
            .execute();
        SystemRoleCreatorRecord record = create.newRecord(SYSTEM_ROLE_CREATOR, roleCreator);
        record.store();
    }

    public Stream<ServerRole> getServerRoleKeysStream() {
        return serverRoles.stream()
            .map(sr -> {
                List<String> list = sr.roles().stream().map(KeyUtils::toKey).toList();
                return new ServerRole(sr.server(), list);
            });
    }

    public List<ServerRole> getServerRoleKeys() {
        return getServerRoleKeysStream().toList();
    }

    public List<Role> getRoleInfo(String key) {
        return getServerRoleKeysStream()
            .filter(sr -> sr.roles().contains(key))
            .map(sr -> loadBalancedClient.build().get()
                .uri("http://" + sr.server() + "/actuator/role/" + key)
                .retrieve()
                .body(Role.class))
            .toList();
                /*.onErrorResume(WebClientResponseException.class, e -> {
                    ProblemDetail detail = e.getResponseBodyAs(ProblemDetail.class);
                    if (detail == null) {
                        detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    return Mono.error(new BaseException(detail));
                }))*/
    }

}
