package com.eden.elasticsearchsync.persistence.es.dao.repository;

import com.eden.elasticsearchsync.persistence.es.dao.po.UserNestedOperationLog;
import com.eden.elasticsearchsync.persistence.es.dao.po.UserOperationLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Slf4j
@Service
public class UserOperationLogDaoImpl extends BaseDao implements UserOperationLogDao {


    @Override
    public void insertOrUpdate(Long userId, List<UserOperationLog> userOperationLogs) {
        UserNestedOperationLog nested = new UserNestedOperationLog();
        nested.setUserId(userId);
        nested.setUserOperationLogs(userOperationLogs);
        insertOrUpdate(nested, UserNestedOperationLog.class);
    }

}
