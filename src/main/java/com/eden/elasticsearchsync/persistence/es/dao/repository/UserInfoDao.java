package com.eden.elasticsearchsync.persistence.es.dao.repository;


import com.eden.elasticsearchsync.persistence.es.dao.po.UserInfo;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
public interface UserInfoDao {

    /**
     * 新增或更新用户基本信息
     *
     * @param userInfo 用户基本信息
     */
    void insertOrUpdate(UserInfo userInfo);

}
