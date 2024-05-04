drop table if exists student;
create table student
(
    id              bigint(21) unsigned auto_increment comment '主键id'
        primary key,
    desk_mate_id    bigint(21)            null comment '同桌',
    age             int                   null comment '年龄',
    grade           int                   null comment '年级',
    user_name       varchar(45)           null comment '名字',
    gender          tinyint(2)  default 0 null comment '性别, 0:女; 1:男',
    birthday        datetime              null comment '生日',
    phone           varchar(20)           null comment '电话',
    bonus_points    bigint(21)  default 0 null comment '积分',
    status          varchar(32)           null comment '状态(字典)',
    home_county_id  bigint(21)            null comment '家庭所在区县',
    home_address_id bigint(21)            null comment 'home_address外键',
    address         varchar(200)          null comment '家庭详细住址',
    version         varchar(200)          null comment '版本号',
    email           varchar(50) DEFAULT NULL COMMENT '邮箱',
    env             varchar(10)           NULL comment '数据隔离环境',
    tenant          bigint                NOT NULL default 0 comment '租户标识',
    gmt_created     datetime              null comment '创建时间',
    gmt_modified    datetime              null comment '更新时间',
    is_deleted      tinyint(2)  default 0 null comment '是否逻辑删除'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
    COMMENT '学生信息表';