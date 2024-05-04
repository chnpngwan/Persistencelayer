create database if not exists fluent_mybatis;
DROP TABLE IF EXISTS `your_table`;
create table `your_table`
(
    id           bigint auto_increment comment '主键ID' primary key,
    name         varchar(30) charset utf8 null comment '姓名',
    age          int                      null comment '年龄',
    email        varchar(50) charset utf8 null comment '邮箱',
    gmt_created   datetime                 null comment '记录创建时间',
    gmt_modified datetime                 null comment '记录最后修改时间',
    is_deleted   tinyint(2) default 0     null comment '逻辑删除标识'
) engine = InnoDB
  default charset = utf8;

drop table if exists student;
CREATE TABLE student
(
    id             bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    age            int          DEFAULT NULL COMMENT '年龄',
    grade          int          DEFAULT NULL COMMENT '年级',
    user_name      varchar(45)  DEFAULT NULL COMMENT '名字',
    gender_man     tinyint(2)   DEFAULT 0 COMMENT '性别, 0:女; 1:男',
    birthday       datetime     DEFAULT NULL COMMENT '生日',
    phone          varchar(20)  DEFAULT NULL COMMENT '电话',
    bonus_points   bigint(21)   DEFAULT 0 COMMENT '积分',
    status         varchar(32)  DEFAULT NULL COMMENT '状态(字典)',
    home_county_id bigint(21)   DEFAULT NULL COMMENT '家庭所在区县',
    address        varchar(200) DEFAULT NULL COMMENT '家庭详细住址',
    gmt_created    datetime     DEFAULT NULL COMMENT '创建时间',
    gmt_modified   datetime     DEFAULT NULL COMMENT '更新时间',
    is_deleted     tinyint(2)   DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '学生信息表';

drop table if exists county_division;
CREATE TABLE county_division
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    province     varchar(50) DEFAULT NULL COMMENT '省份',
    city         varchar(50) DEFAULT NULL COMMENT '城市',
    county       varchar(50) DEFAULT NULL COMMENT '区县',
    gmt_created  datetime    DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime    DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(2)  DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '区县';

DROP TABLE IF EXISTS `student_score`;
create table `student_score`
(
    id           bigint auto_increment primary key COMMENT '主键ID',
    student_id   bigint               NOT NULL COMMENT '学号',
    school_term  int                  NULL COMMENT '学期',
    subject      varchar(30)          NULL COMMENT '学科',
    score        int                  NULL COMMENT '成绩',
    gmt_created   datetime             NOT NULL COMMENT '记录创建时间',
    gmt_modified datetime             NOT NULL COMMENT '记录最后修改时间',
    is_deleted   tinyint(2) default 0 NOT NULL COMMENT '逻辑删除标识'
) engine = InnoDB
  default charset = utf8 COMMENT = '学生成绩';