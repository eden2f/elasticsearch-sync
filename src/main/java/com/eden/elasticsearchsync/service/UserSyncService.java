package com.eden.elasticsearchsync.service;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
public interface UserSyncService {

    /**
     * 根据userId同步人物画像
     *
     * @param userId userId
     */
    void syncByUserId(Long userId);
}
