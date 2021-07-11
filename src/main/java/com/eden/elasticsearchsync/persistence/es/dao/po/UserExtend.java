package com.eden.elasticsearchsync.persistence.es.dao.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class UserExtend extends BaseEsPo {

    private Long id;
    private String headPortrait;
    private String imgs;
}
