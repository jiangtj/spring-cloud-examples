/*
 * This file is generated by jOOQ.
 */
package com.jiangtj.platform.system.jooq.tables.daos;


import com.jiangtj.platform.sql.jooq.PageUtils;
import com.jiangtj.platform.system.jooq.tables.Databasechangeloglock;
import com.jiangtj.platform.system.jooq.tables.records.DatabasechangeloglockRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
@Repository
public class DatabasechangeloglockDao extends DAOImpl<DatabasechangeloglockRecord, com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock, Integer> {

    /**
     * Create a new DatabasechangeloglockDao without any configuration
     */
    public DatabasechangeloglockDao() {
        super(Databasechangeloglock.DATABASECHANGELOGLOCK, com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock.class);
    }

    /**
     * Create a new DatabasechangeloglockDao with an attached configuration
     */
    @Autowired
    public DatabasechangeloglockDao(Configuration configuration) {
        super(Databasechangeloglock.DATABASECHANGELOGLOCK, com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock.class, configuration);
    }

    @Override
    public Integer getId(com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock object) {
        return object.id();
    }

    /**
     * Fetch records that have <code>ID BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock> fetchRangeOfId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Databasechangeloglock.DATABASECHANGELOGLOCK.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>ID IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock> fetchById(Integer... values) {
        return fetch(Databasechangeloglock.DATABASECHANGELOGLOCK.ID, values);
    }

    /**
     * Fetch a unique record that has <code>ID = value</code>
     */
    public com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock fetchOneById(Integer value) {
        return fetchOne(Databasechangeloglock.DATABASECHANGELOGLOCK.ID, value);
    }

    /**
     * Fetch a unique record that has <code>ID = value</code>
     */
    public Optional<com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock> fetchOptionalById(Integer value) {
        return fetchOptional(Databasechangeloglock.DATABASECHANGELOGLOCK.ID, value);
    }

    /**
     * Fetch records that have <code>LOCKED BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock> fetchRangeOfLocked(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(Databasechangeloglock.DATABASECHANGELOGLOCK.LOCKED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>LOCKED IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock> fetchByLocked(Byte... values) {
        return fetch(Databasechangeloglock.DATABASECHANGELOGLOCK.LOCKED, values);
    }

    /**
     * Fetch records that have <code>LOCKGRANTED BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock> fetchRangeOfLockgranted(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Databasechangeloglock.DATABASECHANGELOGLOCK.LOCKGRANTED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>LOCKGRANTED IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock> fetchByLockgranted(LocalDateTime... values) {
        return fetch(Databasechangeloglock.DATABASECHANGELOGLOCK.LOCKGRANTED, values);
    }

    /**
     * Fetch records that have <code>LOCKEDBY BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock> fetchRangeOfLockedby(String lowerInclusive, String upperInclusive) {
        return fetchRange(Databasechangeloglock.DATABASECHANGELOGLOCK.LOCKEDBY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>LOCKEDBY IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock> fetchByLockedby(String... values) {
        return fetch(Databasechangeloglock.DATABASECHANGELOGLOCK.LOCKEDBY, values);
    }

    /**
     * Fetch pages with pageable and conditions.
     */
    public Page<com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock> fetchPage(Pageable pageable, Condition... conditions) {
        return PageUtils.selectFrom(ctx(), getTable())
        .conditions(conditions)
        .pageable(pageable)
        .fetchPage(getType());
    }
}
