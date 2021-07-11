package com.eden.elasticsearchsync.persistence.es.dao.po;

import lombok.*;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class UserInfo extends BaseEsPo {

    private Long id;
    private String name;
    private Integer age;
    private String email;
}
