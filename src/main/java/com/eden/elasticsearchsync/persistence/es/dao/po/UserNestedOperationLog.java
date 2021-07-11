package com.eden.elasticsearchsync.persistence.es.dao.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class UserNestedOperationLog extends BaseEsPo {

    private List<UserOperationLog> userOperationLogs;
}
