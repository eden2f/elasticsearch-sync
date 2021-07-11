package com.eden.elasticsearchsync.service;

import com.eden.elasticsearchsync.persistence.es.dao.po.BaseEsPo;
import com.eden.elasticsearchsync.persistence.es.dao.po.UserNestedOperationLog;
import com.eden.elasticsearchsync.persistence.es.dao.po.UserOperationLog;
import com.eden.elasticsearchsync.persistence.es.dao.repository.UserOperationLogDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Service
public class UserOperationLogSyncServiceImpl extends BaseUserSyncService {

    @Resource
    private UserOperationLogDao userOperationLogDao;

    @Override
    protected BaseEsPo selectOneByUserId(Long userId) {
        UserNestedOperationLog nested = new UserNestedOperationLog();
        nested.setUserId(userId);
        List<UserOperationLog> userOperationLogs = esSyncMapper.selectUserOperationLogByUserId(userId);
        nested.setUserOperationLogs(userOperationLogs);
        return nested;
    }

    @Override
    protected void insertOrUpdate(BaseEsPo po) {
        UserNestedOperationLog nested = (UserNestedOperationLog) po;
        userOperationLogDao.insertOrUpdate(nested.getUserId(), nested.getUserOperationLogs());
    }
}
