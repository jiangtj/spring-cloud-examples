/*
 * This file is generated by jOOQ.
 */
package com.jiangtj.platform.sql.jooq.jooq;


import com.jiangtj.platform.sql.jooq.jooq.tables.*;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SystemDb extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>system-db</code>
     */
    public static final SystemDb SYSTEM_DB = new SystemDb();

    /**
     * The table <code>system-db.DATABASECHANGELOG</code>.
     */
    public final Databasechangelog DATABASECHANGELOG = Databasechangelog.DATABASECHANGELOG;

    /**
     * The table <code>system-db.DATABASECHANGELOGLOCK</code>.
     */
    public final Databasechangeloglock DATABASECHANGELOGLOCK = Databasechangeloglock.DATABASECHANGELOGLOCK;

    /**
     * The table <code>system-db.system_operate_record</code>.
     */
    public final SystemOperateRecord SYSTEM_OPERATE_RECORD = SystemOperateRecord.SYSTEM_OPERATE_RECORD;

    /**
     * The table <code>system-db.system_user</code>.
     */
    public final SystemUser SYSTEM_USER = SystemUser.SYSTEM_USER;

    /**
     * The table <code>system-db.system_user_role</code>.
     */
    public final SystemUserRole SYSTEM_USER_ROLE = SystemUserRole.SYSTEM_USER_ROLE;

    /**
     * No further instances allowed
     */
    private SystemDb() {
        super("system-db", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Databasechangelog.DATABASECHANGELOG,
            Databasechangeloglock.DATABASECHANGELOGLOCK,
            SystemOperateRecord.SYSTEM_OPERATE_RECORD,
            SystemUser.SYSTEM_USER,
            SystemUserRole.SYSTEM_USER_ROLE
        );
    }
}