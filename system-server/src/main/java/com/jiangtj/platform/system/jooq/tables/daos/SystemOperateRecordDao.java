/*
 * This file is generated by jOOQ.
 */
package com.jiangtj.platform.system.jooq.tables.daos;


import com.jiangtj.platform.sql.jooq.PageUtils;
import com.jiangtj.platform.system.jooq.tables.SystemOperateRecord;
import com.jiangtj.platform.system.jooq.tables.records.SystemOperateRecordRecord;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Repository
public class SystemOperateRecordDao extends DAOImpl<SystemOperateRecordRecord, com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord, Long> {

    /**
     * Create a new SystemOperateRecordDao without any configuration
     */
    public SystemOperateRecordDao() {
        super(SystemOperateRecord.SYSTEM_OPERATE_RECORD, com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord.class);
    }

    /**
     * Create a new SystemOperateRecordDao with an attached configuration
     */
    @Autowired
    public SystemOperateRecordDao(Configuration configuration) {
        super(SystemOperateRecord.SYSTEM_OPERATE_RECORD, com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord.class, configuration);
    }

    @Override
    public Long getId(com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord object) {
        return object.id();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord> fetchRangeOfId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(SystemOperateRecord.SYSTEM_OPERATE_RECORD.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord> fetchById(Long... values) {
        return fetch(SystemOperateRecord.SYSTEM_OPERATE_RECORD.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord fetchOneById(Long value) {
        return fetchOne(SystemOperateRecord.SYSTEM_OPERATE_RECORD.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord> fetchOptionalById(Long value) {
        return fetchOptional(SystemOperateRecord.SYSTEM_OPERATE_RECORD.ID, value);
    }

    /**
     * Fetch records that have <code>create_time BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord> fetchRangeOfCreateTime(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(SystemOperateRecord.SYSTEM_OPERATE_RECORD.CREATE_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>create_time IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord> fetchByCreateTime(LocalDateTime... values) {
        return fetch(SystemOperateRecord.SYSTEM_OPERATE_RECORD.CREATE_TIME, values);
    }

    /**
     * Fetch records that have <code>operator BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord> fetchRangeOfOperator(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(SystemOperateRecord.SYSTEM_OPERATE_RECORD.OPERATOR, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>operator IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord> fetchByOperator(Long... values) {
        return fetch(SystemOperateRecord.SYSTEM_OPERATE_RECORD.OPERATOR, values);
    }

    /**
     * Fetch records that have <code>content BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord> fetchRangeOfContent(String lowerInclusive, String upperInclusive) {
        return fetchRange(SystemOperateRecord.SYSTEM_OPERATE_RECORD.CONTENT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>content IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord> fetchByContent(String... values) {
        return fetch(SystemOperateRecord.SYSTEM_OPERATE_RECORD.CONTENT, values);
    }

    /**
     * Fetch pages with pageable and conditions.
     */
    public Page<com.jiangtj.platform.system.jooq.tables.pojos.SystemOperateRecord> fetchPage(Pageable pageable, Condition... conditions) {
        return PageUtils.selectFrom(ctx(), getTable())
        .conditions(conditions)
        .pageable(pageable)
        .fetchPage(getType());
    }
}