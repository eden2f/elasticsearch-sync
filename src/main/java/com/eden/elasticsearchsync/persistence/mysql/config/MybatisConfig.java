package com.eden.elasticsearchsync.persistence.mysql.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Eden
 *
 * @date : 2021/7/11
 */
@Configuration
@MapperScan("com.eden.elasticsearchsync.persistence.mysql.mapper")
public class MybatisConfig {
}
