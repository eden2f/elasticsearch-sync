package com.eden.elasticsearchsync.service;

import com.eden.elasticsearchsync.persistence.es.dao.po.BaseEsPo;
import com.eden.elasticsearchsync.persistence.es.dao.po.UserExtend;
import com.eden.elasticsearchsync.persistence.es.dao.repository.UserExtendDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Service
public class UserExtendSyncServiceImpl extends BaseUserSyncService {

    @Resource
    private UserExtendDao userExtendDao;

    @Override
    protected BaseEsPo selectOneByUserId(Long userId) {
        return esSyncMapper.selectUserExtendByUserId(userId);
    }

    @Override
    protected void insertOrUpdate(BaseEsPo po) {
        userExtendDao.insertOrUpdate((UserExtend) po);
    }
}
