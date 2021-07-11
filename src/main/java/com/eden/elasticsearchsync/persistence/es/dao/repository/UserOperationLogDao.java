package com.eden.elasticsearchsync.persistence.es.dao.repository;


import com.eden.elasticsearchsync.persistence.es.dao.po.UserOperationLog;

import java.util.List;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
public interface UserOperationLogDao {

    /**
     * 新增或更新用户操作日志
     *
     * @param userId            userId
     * @param userOperationLogs 用户操作日志
     */
    void insertOrUpdate(Long userId, List<UserOperationLog> userOperationLogs);

}
