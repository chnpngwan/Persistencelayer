create database if not exists fluent_mybatis;
drop table if exists student;
CREATE TABLE student
(
    id             bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    age            int          DEFAULT NULL COMMENT '年龄',
    grade          int          DEFAULT NULL COMMENT '年级',
    user_name      varchar(45)  DEFAULT NULL COMMENT '名字',
    gender     tinyint(2)   DEFAULT 0 COMMENT '性别, 0:女; 1:男',
    birthday       datetime     DEFAULT NULL COMMENT '生日',
    phone          varchar(20)  DEFAULT NULL COMMENT '电话',
    bonus_points   bigint(21)   DEFAULT 0 COMMENT '积分',
    status         varchar(32)  DEFAULT NULL COMMENT '状态(字典)',
    home_county_id bigint(21)   DEFAULT NULL COMMENT '家庭所在区县',
    address        varchar(200) DEFAULT NULL COMMENT '家庭详细住址',
    gmt_created    datetime     DEFAULT NULL COMMENT '创建时间',
    gmt_modified   datetime     DEFAULT NULL COMMENT '更新时间',
    is_deleted     tinyint(2)   DEFAULT 0 COMMENT '是否逻辑删除',
    env          varchar(20)          NOT NULL COMMENT '环境'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '学生信息表';

DROP TABLE IF EXISTS `student_score`;
create table `student_score`
(
    id           bigint auto_increment primary key COMMENT '主键ID',
    student_id   bigint               NOT NULL COMMENT '学号',
    school_term  int                  NULL COMMENT '学期',
    subject      varchar(30)          NULL COMMENT '学科',
    score        int                  NULL COMMENT '成绩',
    gmt_created  datetime             NOT NULL COMMENT '记录创建时间',
    gmt_modified datetime             NOT NULL COMMENT '记录最后修改时间',
    is_deleted   tinyint(2) default 0 NOT NULL COMMENT '逻辑删除标识',
    env          varchar(20)          NOT NULL COMMENT '环境'
) engine = InnoDB
  default charset = utf8 COMMENT = '学生成绩';