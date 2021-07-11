package com.eden.elasticsearchsync.persistence.es.dao.repository;

import com.eden.elasticsearchsync.persistence.es.dao.po.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Slf4j
@Service
public class UserInfoDaoImpl extends BaseDao implements UserInfoDao {


    @Override
    public void insertOrUpdate(UserInfo userInfo) {
        insertOrUpdate(userInfo, UserInfo.class);
    }

}
