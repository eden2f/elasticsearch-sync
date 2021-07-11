package com.eden.elasticsearchsync.service;

import com.eden.elasticsearchsync.persistence.es.dao.po.BaseEsPo;
import com.eden.elasticsearchsync.persistence.es.dao.po.UserInfo;
import com.eden.elasticsearchsync.persistence.es.dao.repository.UserInfoDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Service
public class UserInfoSyncServiceImpl extends BaseUserSyncService {

    @Resource
    private UserInfoDao userInfoDao;

    @Override
    protected BaseEsPo selectOneByUserId(Long userId) {
        return esSyncMapper.selectUserInfoByUserId(userId);
    }

    @Override
    protected void insertOrUpdate(BaseEsPo po) {
        userInfoDao.insertOrUpdate((UserInfo) po);
    }
}
