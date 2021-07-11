package com.eden.elasticsearchsync.persistence.redis.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Slf4j
@Component
public class RedisUtil {

    @Resource
    private JedisPool jedisPool;

    private static final Integer REPETITION_REQUEST_EXPIRE_TIME = 3;

    public void repetitionRequestLockOrElseThrow(String key, int expire, String alarmContent) {
        String defaultAlarm = "处理中，请勿重复点击！";
        if (StringUtils.isNotBlank(alarmContent)) {
            defaultAlarm = alarmContent;
        }
        try (Jedis jedis = jedisPool.getResource()) {
            String res = jedis.set(key, "1", SetParams.setParams().ex(expire).nx());
            if (Objects.isNull(res)) {
                throw new RuntimeException(defaultAlarm);
            }
        } catch (Exception e) {
            throw new RuntimeException(defaultAlarm, e);
        }
    }

    public void repetitionRequestLockOrElseThrow(String key, int expire) {
        repetitionRequestLockOrElseThrow(key, expire, null);
    }


    public void repetitionRequestLockOrElseThrow(String key) {
        repetitionRequestLockOrElseThrow(key, REPETITION_REQUEST_EXPIRE_TIME);
    }

    public Long zadd(String key, double score, String member) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zadd(key, score, member);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param key
     * @param scoreMembers
     * @return
     */
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zadd(key, scoreMembers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long zrem(String key, String... member) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrem(key, member);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Double zscore(String key, String member) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zscore(key, member);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Long zcard(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zcard(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置redis缓存
     *
     * @param key       redisKey
     * @param value     缓存内容
     * @param expireSec 过期时间
     * @return 响应值
     */
    public String set(String key, String value, Integer expireSec) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        String res;
        try (Jedis jedis = jedisPool.getResource()) {
            res = jedis.set(key, value, SetParams.setParams().ex(expireSec));
        }
        return res;
    }

    public String getOrElseIgnore(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            log.info("获取缓存失败,key={}", key, e);
            return null;
        }
    }

    public String get(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            log.info("获取缓存失败,key={}", key, e);
            throw e;
        }
    }

    public long remove(String key) {
        long ret = 0;
        try (Jedis jedis = jedisPool.getResource()) {
            ret = jedis.del(key);
        }
        return ret;
    }

}
