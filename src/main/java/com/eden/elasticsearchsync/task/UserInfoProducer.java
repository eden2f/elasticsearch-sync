package com.eden.elasticsearchsync.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Slf4j
@Configuration
@EnableScheduling
public class UserInfoProducer extends BaseProducer {

    @Value("${UserInfoProducer.enable:0}")
    private Integer enable;
    @Value("${UserInfoProducer.default.updateTimeStart:2021-07-09 14:00:00}")
    private String defaultUpdateTimeStart;
    @Value("${UserInfoProducer.maxSize:1}")
    private Integer maxSize;
    @Value("${UserInfoProducer.queue.redis.key:UserInfoProducer:queue:9}")
    private String producerQueueRedisKey;
    @Value("${UserInfoProducer.update.time.start.redis.key:UserInfoProducer:updateTimeStart:9}")
    private String producerUpdateTimeStartRedisKey;

    @Scheduled(initialDelay = 10000, fixedDelayString = "${UserInfoProducer.fixedDelayString:1000}")
    public void produce() {
        try {
            String primaryKeyColumnName = "id";
            String uniqueColumnName = "userId";
            String tableName = "user_extend";
            String updateColumnName = "update_time";
            produce(enable, producerQueueRedisKey, producerUpdateTimeStartRedisKey,
                    defaultUpdateTimeStart, maxSize, primaryKeyColumnName,
                    uniqueColumnName, tableName, updateColumnName);
        } catch (Throwable throwable) {
            log.error(String.format("UserInfoProducer:发生未知异常, e = %s", throwable), throwable);
        }
    }

}
