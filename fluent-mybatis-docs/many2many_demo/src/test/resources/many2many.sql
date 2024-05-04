create database if not exists fluent_mybatis;
drop table if exists t_member;
CREATE TABLE t_member
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    user_name    varchar(45) DEFAULT NULL COMMENT '名字',
    is_girl      tinyint(1)  DEFAULT 0 COMMENT '0:男孩; 1:女孩',
    age          int         DEFAULT NULL COMMENT '年龄',
    school       varchar(20) DEFAULT NULL COMMENT '学校',
    gmt_created  datetime    DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime    DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(1)  DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '成员表:女孩或男孩信息';

drop table if exists t_member_love;
CREATE TABLE t_member_love
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    girl_id      bigint(21) NOT NULL COMMENT 'member表外键',
    boy_id       bigint(21) NOT NULL COMMENT 'member表外键',
    status       varchar(45) DEFAULT NULL COMMENT '状态',
    gmt_created  datetime    DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime    DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(2)  DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '成员恋爱关系';

drop table if exists t_member_favorite;
CREATE TABLE t_member_favorite
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    member_id    bigint(21) NOT NULL COMMENT 'member表外键',
    favorite     varchar(45) DEFAULT NULL COMMENT '爱好: 电影, 爬山, 徒步...',
    gmt_created  datetime    DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime    DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(2)  DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '成员爱好';
