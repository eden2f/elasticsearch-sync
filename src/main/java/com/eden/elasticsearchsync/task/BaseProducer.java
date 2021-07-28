package com.eden.elasticsearchsync.task;

import com.alibaba.fastjson.JSON;
import com.eden.elasticsearchsync.enums.DelStatus;
import com.eden.elasticsearchsync.persistence.mysql.mapper.EsSyncMapper;
import com.eden.elasticsearchsync.persistence.redis.util.RedisUtil;
import com.eden.elasticsearchsync.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Slf4j
public abstract class BaseProducer {

    @Resource
    private EsSyncMapper esSyncMapper;
    @Resource
    private RedisUtil redisUtil;
    @Value("${es.producer.log.enable:0}")
    protected Integer logEnable;


    public void produce(Integer enable, String producerQueueRedisKey, String updateTimeStartRedisKey,
                        String defaultUpdateTimeStart, Integer maxSize, String primaryKeyColumnName,
                        String uniqueColumnName, String tableName, String updateColumnName) {
        long startTime = System.currentTimeMillis();
        log.info("produce start");
        String taskName = this.getClass().getSimpleName();
        if (enable != null && enable == 0) {
            log.info("taskName = {}, produce disable end, cost : {} ms", taskName, System.currentTimeMillis() - startTime);
            return;
        }
        String updateTimeStart = redisUtil.get(updateTimeStartRedisKey);
        if (StringUtils.isBlank(updateTimeStart)) {
            updateTimeStart = defaultUpdateTimeStart;
        }
        Date updateTimeEndDate = new Date();
        long updateTimeEndeSecond = updateTimeEndDate.getTime() / 1000;
        String updateTimeEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateTimeEndDate);
        Long latestMaxPrimaryKey = 0L;
        List<Long> updatedDataPrimaryKeys;
        double maxScore = 0;
        do {
            log.info("taskName = {}, primaryKeyColumnName = {}, tableName = {}, updateColumnName = {}, updateTimeStart = {}, updateTimeEnd = {}, latestMaxPrimaryKey = {}, maxSize = {}",
                    taskName, primaryKeyColumnName, tableName, updateColumnName, updateTimeStart, updateTimeEnd, latestMaxPrimaryKey, maxSize);
            updatedDataPrimaryKeys = esSyncMapper.selectUpdatedDataPrimaryKey(primaryKeyColumnName, tableName, updateColumnName, updateTimeStart, updateTimeEnd, latestMaxPrimaryKey, maxSize);
            log.info("taskName = {}, updatedDataPrimaryKeys size = {}", taskName, updatedDataPrimaryKeys.size());
            if (CollectionUtils.isNotEmpty(updatedDataPrimaryKeys)) {
                if (DelStatus.DELETED.getCode() == logEnable) {
                    log.info("updatedDataPrimaryKeys = {}", JSON.toJSONString(updatedDataPrimaryKeys));
                }
                if (CollectionUtils.size(updatedDataPrimaryKeys) >= maxSize) {
                    log.info("更新数量超过阈值, maxSize = {}", maxSize);
                }
                Map<String, Double> scoreMembers = new HashMap<>(updatedDataPrimaryKeys.size());
                List<Map<String, Object>> updatedDatas = esSyncMapper.selectUniqueKeyByPrimaryKey(primaryKeyColumnName, tableName, uniqueColumnName, updateColumnName, updatedDataPrimaryKeys, maxSize);
                if (CollectionUtils.isNotEmpty(updatedDatas)) {
                    for (Map<String, Object> updatedData : updatedDatas) {
                        Object userIdObject = updatedData.get(uniqueColumnName);
                        String userIdString = String.valueOf(userIdObject);
                        Double score = redisUtil.zscore(producerQueueRedisKey, userIdString);
                        LocalDateTime updateTime = (LocalDateTime) updatedData.get(updateColumnName);
                        double updateTimeDouble = (double) (updateTime == null ? 0L : updateTime.toEpochSecond(DateUtil.ZONE_OFFSET));
                        if (score != null) {
                            log.info("score != null, userIdString = {}", userIdString);
                        } else {
                            log.info("score == null, userIdString = {}", userIdString);
                            scoreMembers.put(userIdString, updateTimeDouble);
                        }
                        if (updateTimeDouble > maxScore && updateTimeDouble <= updateTimeEndeSecond) {
                            maxScore = updateTimeDouble;
                        }
                    }
                    if (!scoreMembers.isEmpty()) {
                        if (DelStatus.DELETED.getCode() == logEnable) {
                            log.info("scoreMembers = {}", JSON.toJSONString(scoreMembers));
                        }
                        redisUtil.zadd(producerQueueRedisKey, scoreMembers);
                    }
                }
                latestMaxPrimaryKey = updatedDataPrimaryKeys.get(updatedDataPrimaryKeys.size() - 1);
            } else {
                latestMaxPrimaryKey = 0L;
            }
        } while (CollectionUtils.size(updatedDataPrimaryKeys) >= maxSize && latestMaxPrimaryKey > 0L);

        if (maxScore > 0) {
            Date maxScoreDate = new Date((long) (maxScore * 1000));
            String maxScoreDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(maxScoreDate);
            log.info("taskName = {}, maxScoreDateString = {}, updateTimeEnd = {}", taskName, maxScoreDateString, updateTimeEnd);
            redisUtil.set(updateTimeStartRedisKey, updateTimeEnd, 60 * 60 * 24 * 30);
        }
        log.info("taskName = {}, produce end, cost : {} ms", taskName, System.currentTimeMillis() - startTime);
    }
}
