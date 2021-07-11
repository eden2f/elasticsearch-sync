package com.eden.elasticsearchsync.persistence.mysql.mapper;

import com.eden.elasticsearchsync.persistence.es.dao.po.UserExtend;
import com.eden.elasticsearchsync.persistence.es.dao.po.UserInfo;
import com.eden.elasticsearchsync.persistence.es.dao.po.UserOperationLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author huangminpeng
 */
@Repository
public interface EsSyncMapper {

    /**
     * 查询新更新的数据标识
     *
     * @param uniqueColumnName 唯一标识字段名称
     * @param tableName        表明
     * @param updateColumnName 更新标识字段名称
     * @param updateTimeStart  起始更新时间
     * @param maxSize          最大获取数量
     * @return 更新的数据集合
     */
    List<Map<String, Object>> selectUpdatedData(@Param("uniqueColumnName") String uniqueColumnName,
                                                @Param("tableName") String tableName,
                                                @Param("updateColumnName") String updateColumnName,
                                                @Param("updateTimeStart") String updateTimeStart,
                                                @Param("maxSize") Integer maxSize);

    /**
     * 查询新更新的数据标识
     *
     * @param primaryKeyColumnName 主键字段名称
     * @param tableName            表明
     * @param updateColumnName     更新标识字段名称
     * @param updateTimeStart      起始更新时间
     * @param maxSize              最大获取数量
     * @return 更新的数据集合
     */
    List<Long> selectUpdatedDataPrimaryKey(@Param("primaryKeyColumnName") String primaryKeyColumnName,
                                           @Param("tableName") String tableName,
                                           @Param("updateColumnName") String updateColumnName,
                                           @Param("updateTimeStart") String updateTimeStart,
                                           @Param("updateTimeEnd") String updateTimeEnd,
                                           @Param("latestMaxPrimaryKey") Long latestMaxPrimaryKey,
                                           @Param("maxSize") Integer maxSize);

    /**
     * 查询新更新的数据标识
     *
     * @param primaryKeyColumnName 主键字段名称
     * @param tableName            表明
     * @param maxSize              最大获取数量
     * @return 更新的数据集合
     */
    List<Map<String, Object>> selectUniqueKeyByPrimaryKey(@Param("primaryKeyColumnName") String primaryKeyColumnName,
                                                          @Param("tableName") String tableName,
                                                          @Param("uniqueColumnName") String uniqueColumnName,
                                                          @Param("updateColumnName") String updateColumnName,
                                                          @Param("primaryKeyValues") List<Long> primaryKeyValues,
                                                          @Param("maxSize") Integer maxSize);


    /**
     * 根据userId查询UserInfo信息
     *
     * @param userId userId
     * @return UserInfo
     */
    UserInfo selectUserInfoByUserId(@Param("userId") Long userId);

    /**
     * 根据userId查询UserExtend信息
     *
     * @param userId userId
     * @return UserExtend
     */
    UserExtend selectUserExtendByUserId(@Param("userId") Long userId);

    /**
     * 根据userId查询UserOperationLog信息
     *
     * @param userId userId
     * @return UserOperationLog
     */
    List<UserOperationLog> selectUserOperationLogByUserId(@Param("userId") Long userId);

}