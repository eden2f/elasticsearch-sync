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
public class UserExtendProducer extends BaseProducer {

    @Value("${UserExtendProducer.enable:0}")
    private Integer enable;
    @Value("${UserExtendProducer.default.updateTimeStart:2021-07-09 14:00:00}")
    private String defaultUpdateTimeStart;
    @Value("${UserExtendProducer.maxSize:1}")
    private Integer maxSize;
    @Value("${UserExtendProducer.queue.redis.key:UserExtendProducer:queue:9}")
    private String producerQueueRedisKey;
    @Value("${UserExtendProducer.update.time.start.redis.key:UserExtendProducer:updateTimeStart:9}")
    private String producerUpdateTimeStartRedisKey;

    @Scheduled(initialDelay = 10000, fixedDelayString = "${UserExtendProducer.fixedDelayString:1000}")
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
