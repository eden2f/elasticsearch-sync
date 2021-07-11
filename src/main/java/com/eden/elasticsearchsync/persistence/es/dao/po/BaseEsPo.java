package com.eden.elasticsearchsync.persistence.es.dao.po;

import lombok.Data;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Data
public abstract class BaseEsPo {

    private String esId;

    private Long userId;

    public void checkElseThrow() {
        if (userId == null || userId == 0) {
            throw new RuntimeException("userId不能为空");
        }
    }

}
