package com.eden.elasticsearchsync.task;

import com.eden.elasticsearchsync.service.UserOperationLogSyncServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author huangminpeng
 * @date 2020/07/25
 */
@Slf4j
@Configuration
@EnableScheduling
public class UserOperationLogConsumer extends BaseConsumer {

    @Value("${UserOperationLogConsumer.enable:0}")
    private Integer enable;
    @Value("${UserOperationLogConsumer.consumeSize:1}")
    private Integer consumeSize;
    @Value("${UserOperationLogProducer.queue.redis.key:UserOperationLogProducer:queue:9}")
    private String producerQueueRedisKey;

    protected UserOperationLogConsumer(UserOperationLogSyncServiceImpl userOperationLogSyncService) {
        super(userOperationLogSyncService);
    }


    @Scheduled(initialDelay = 10000, fixedDelayString = "${UserOperationLogConsumer.fixedDelayString:1000}")
    public void consume() {
        try {
            super.consume(enable, consumeSize, producerQueueRedisKey);
        } catch (Throwable throwable) {
            log.error(String.format("UserOperationLogConsumer:发生未知异常, e = %s", throwable), throwable);
        }
    }
}
