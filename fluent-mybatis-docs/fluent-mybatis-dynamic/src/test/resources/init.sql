create database if not exists fluent_mybatis;
DROP TABLE IF EXISTS `student`;

CREATE TABLE `student`
(
    `id`           bigint(21) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name`         varchar(20) DEFAULT NULL COMMENT '姓名',
    `phone`        varchar(20) DEFAULT NULL COMMENT '电话',
    `email`        varchar(50) DEFAULT NULL COMMENT '邮箱',
    `gender`       tinyint(2)  DEFAULT NULL COMMENT '性别',
    `locked`       tinyint(2)  DEFAULT NULL COMMENT '状态(0:正常,1:锁定)',
    `gmt_created`  datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '存入数据库的时间',
    `gmt_modified` datetime    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改的时间',
    `is_deleted`   tinyint(2)  DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='学生表';
