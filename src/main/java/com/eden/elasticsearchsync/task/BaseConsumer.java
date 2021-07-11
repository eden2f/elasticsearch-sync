package com.eden.elasticsearchsync.task;

import com.alibaba.fastjson.JSON;
import com.eden.elasticsearchsync.enums.DelStatus;
import com.eden.elasticsearchsync.persistence.es.exception.EsSyncConcurrentLockException;
import com.eden.elasticsearchsync.persistence.redis.util.RedisUtil;
import com.eden.elasticsearchsync.service.UserSyncService;
import com.eden.elasticsearchsync.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Set;


/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Slf4j
public abstract class BaseConsumer {

    @Resource
    protected RedisUtil redisUtil;
    @Value("${es.consumer.queue.size.alarm.enable:0}")
    protected Integer queueSizeAlarmEnable;
    @Value("${es.consumer.queue.size.threshold.multiple:10}")
    protected Integer queueSizeThresholdMultiple;

    private final UserSyncService userSyncService;

    protected BaseConsumer(UserSyncService userSyncService) {
        this.userSyncService = userSyncService;
    }

    public void consume(Integer enable, Integer consumeSize, String queueName) {
        long startTime = System.currentTimeMillis();
        String taskName = this.getClass().getSimpleName();
        log.info("taskName = {}, consume start, enable = {}, consumeSize = {}, queueName = {}", taskName, enable, consumeSize, queueName);
        if (enable != null && enable == 0) {
            log.info("taskName = {}, consume end, cost : {} ms", taskName, System.currentTimeMillis() - startTime);
            return;
        }
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        Set<String> uniqueValueString = redisUtil.zrangeByScore(queueName, 0, currentSecond, 0, consumeSize);
        log.info("taskName = {}, userIdStrings size = {}, content = {}", taskName, uniqueValueString.size(), JSON.toJSONString(uniqueValueString));
        uniqueValueString.removeIf(Objects::isNull);
        uniqueValueString.removeIf(item -> Long.parseLong(item) == 0);
        for (String uniqueValue : uniqueValueString) {
            Long userId = null;
            try {
                userId = Long.valueOf(uniqueValue);
                userSyncService.syncByUserId(userId);
                redisUtil.zrem(queueName, uniqueValue);
            } catch (Throwable throwable) {
                if (throwable instanceof EsSyncConcurrentLockException) {
                    double updateTimeDouble = LocalDateTime.now().toEpochSecond(DateUtil.ZONE_OFFSET) + 60;
                    redisUtil.zadd(queueName, updateTimeDouble, uniqueValue);
                    log.info("并发同步ES, userId = {}", userId);
                } else {
                    throw throwable;
                }
            }
        }
        if (DelStatus.DELETED.getCode() == queueSizeAlarmEnable) {
            Long queueSize = redisUtil.zcard(queueName);
            if ((long) queueSizeThresholdMultiple * consumeSize < queueSize) {
                log.info("taskName = {}, 生产堆积, queueName = {}", taskName, queueName);
            }
        }
        log.info("taskName = {}, consume end, cost : {} ms", taskName, System.currentTimeMillis() - startTime);
    }

}
