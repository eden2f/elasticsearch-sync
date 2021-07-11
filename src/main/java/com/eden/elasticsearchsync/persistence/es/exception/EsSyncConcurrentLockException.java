package com.eden.elasticsearchsync.persistence.es.exception;

import lombok.ToString;

/**
 * @author huangminpeng
 * @date 2020/07/25
 */
@ToString
public class EsSyncConcurrentLockException extends RuntimeException {
    public EsSyncConcurrentLockException(Throwable cause) {
        super(cause);
    }
}
