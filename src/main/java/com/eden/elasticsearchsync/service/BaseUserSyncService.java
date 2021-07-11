package com.eden.elasticsearchsync.service;


import com.eden.elasticsearchsync.persistence.es.dao.po.BaseEsPo;
import com.eden.elasticsearchsync.persistence.es.exception.EsSyncConcurrentLockException;
import com.eden.elasticsearchsync.persistence.mysql.mapper.EsSyncMapper;
import com.eden.elasticsearchsync.persistence.redis.util.RedisUtil;

import javax.annotation.Resource;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
public abstract class BaseUserSyncService implements UserSyncService {

    @Resource
    protected EsSyncMapper esSyncMapper;
    @Resource
    private RedisUtil redisUtil;

    private static final String REDIS_KEY_PREFIX = "UserSyncService:";

    /**
     * 根据userId同步
     *
     * @param userId userId
     */
    @Override
    public void syncByUserId(Long userId) {
        String redisKey = REDIS_KEY_PREFIX + userId;
        try {
            redisUtil.repetitionRequestLockOrElseThrow(redisKey);
        } catch (Exception e) {
            throw new EsSyncConcurrentLockException(e);
        }
        BaseEsPo po = selectOneByUserId(userId);
        insertOrUpdate(po);
        redisUtil.remove(redisKey);
    }

    /**
     * 根据userId查询一条数据
     *
     * @param userId userId
     * @return po
     */
    protected abstract BaseEsPo selectOneByUserId(Long userId);

    /**
     * 同步数据到es
     *
     * @param po 对象
     */
    protected abstract void insertOrUpdate(BaseEsPo po);
}
