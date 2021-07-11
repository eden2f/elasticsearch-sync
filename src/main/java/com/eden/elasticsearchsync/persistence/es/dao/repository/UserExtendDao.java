package com.eden.elasticsearchsync.persistence.es.dao.repository;


import com.eden.elasticsearchsync.persistence.es.dao.po.UserExtend;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
public interface UserExtendDao {

    /**
     * 新增或更新用户拓展信息
     *
     * @param userExtend 用户拓展信息
     */
    void insertOrUpdate(UserExtend userExtend);

}
