package com.jiangtj.cloud.auth.system;

import com.jiangtj.cloud.auth.context.RoleProvider;
import org.springframework.lang.Nullable;

import java.util.List;

public interface SystemRoleProvider extends RoleProvider {
    /**
     * Def roles such as system system-read etc.
     * Note: role key will remove `-` ` ` `_`e, such as system-R_ead key is systemread
     */
    List<String> getRoles();

    /**
     * Def roles such as system system-read etc.
     */
    @Nullable
    Role getRole(String key);

}
