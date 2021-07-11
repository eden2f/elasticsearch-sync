package com.eden.elasticsearchsync.task;

import com.eden.elasticsearchsync.service.UserInfoSyncServiceImpl;
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
public class UserInfoConsumer extends BaseConsumer {

    @Value("${UserInfoConsumer.enable:0}")
    private Integer enable;
    @Value("${UserInfoConsumer.consumeSize:1}")
    private Integer consumeSize;
    @Value("${UserInfoProducer.queue.redis.key:UserInfoProducer:queue:9}")
    private String producerQueueRedisKey;

    protected UserInfoConsumer(UserInfoSyncServiceImpl userPortraitSyncService) {
        super(userPortraitSyncService);
    }


    @Scheduled(initialDelay = 10000, fixedDelayString = "${UserInfoConsumer.fixedDelayString:1000}")
    public void consume() {
        try {
            super.consume(enable, consumeSize, producerQueueRedisKey);
        } catch (Throwable throwable) {
            log.error(String.format("UserInfoConsumer:发生未知异常, e = %s", throwable), throwable);
        }
    }
}
