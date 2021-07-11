SET NAMES utf8mb4;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`
(
    `id`          bigint   NOT NULL COMMENT '主键ID',
    `userId`      bigint            DEFAULT '0' COMMENT '用户id',
    `name`        varchar(30)       DEFAULT NULL COMMENT '姓名',
    `age`         int               DEFAULT NULL COMMENT '年龄',
    `email`       varchar(50)       DEFAULT NULL COMMENT '邮箱',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `id` (`id`),
    UNIQUE KEY `userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `user` (`id`, `userId`, `name`, `age`, `email`, `update_time`)
VALUES (1, 1, 'Jone', 18, 'test1@baomidou.com', '2021-07-11 06:04:54'),
       (2, 2, 'Jack', 20, 'test2@baomidou.com', '2021-07-11 06:04:54'),
       (3, 3, 'Tom', 28, 'test3@baomidou.com', '2021-07-11 06:04:55'),
       (4, 4, 'Sandy', 21, 'test4@baomidou.com', '2021-07-11 06:04:56'),
       (5, 5, 'Billie', 23, 'test5@baomidou.com', '2021-07-11 15:25:00');

DROP TABLE IF EXISTS `user_extend`;

CREATE TABLE `user_extend`
(
    `id`           bigint   NOT NULL COMMENT '主键ID',
    `userId`       bigint            DEFAULT '0' COMMENT '用户id',
    `headPortrait` varchar(200)      DEFAULT NULL COMMENT '头像',
    `imgs`         varchar(200)      DEFAULT NULL COMMENT '图集',
    `update_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `id` (`id`),
    UNIQUE KEY `userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `user_extend` (`id`, `userId`, `headPortrait`, `imgs`, `update_time`)
VALUES (1, 1, 'sfsadfdsafs', 'fasfdsfsadfsd', '2021-07-11 07:35:20'),
       (2, 2, 'dsgfdsgds', 'fdsgfdgfdsgg', '2021-07-11 07:35:28'),
       (3, 3, '3', '3', '2021-07-11 07:35:41'),
       (4, 4, '44', '444', '2021-07-11 15:48:00');

DROP TABLE IF EXISTS `user_operation_log`;

CREATE TABLE `user_operation_log`
(
    `id`          bigint   NOT NULL COMMENT '主键ID',
    `userId`      bigint            DEFAULT '0' COMMENT '用户id',
    `desc`        varchar(200)      DEFAULT NULL COMMENT '操作内容',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `id` (`id`),
    KEY           `userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `user_operation_log` (`id`, `userId`, `desc`, `update_time`)
VALUES (1, 1, '吃饭', '2021-07-11 08:12:24'),
       (2, 1, '睡觉', '2021-07-11 08:12:31'),
       (3, 1, '练吉他', '2021-07-11 08:12:41'),
       (4, 2, '跳街舞', '2021-07-11 08:12:56');