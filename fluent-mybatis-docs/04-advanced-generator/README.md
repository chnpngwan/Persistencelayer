## Fluent Mybatis详细语法介绍
前面3节，简单介绍了如何使用FluentMybatis，已经生成Entity文件。为了进一步介绍FluentMybatis语法，
我们再建2张表。

```mysql
create schema fluent_mybatis;
use fluent_mybatis;

drop table if exists user;
CREATE TABLE user  (
  id bigint(21) unsigned auto_increment primary key COMMENT '主键id',
  avatar varchar(255) DEFAULT NULL COMMENT '头像',
  account varchar(45) DEFAULT NULL COMMENT '账号',
  password varchar(45) DEFAULT NULL COMMENT '密码',
  user_name varchar(45) DEFAULT NULL COMMENT '名字',
  birthday datetime DEFAULT NULL COMMENT '生日',
  e_mail varchar(45) DEFAULT NULL COMMENT '电子邮件',
  phone varchar(20) DEFAULT NULL COMMENT '电话',
  bonus_points bigint(21) DEFAULT 0 COMMENT '会员积分',
  status varchar(32) DEFAULT NULL COMMENT '状态(字典)',
  gmt_create datetime DEFAULT NULL COMMENT '创建时间',
  gmt_modified datetime DEFAULT NULL COMMENT '更新时间',
  is_deleted tinyint(2) DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '用户表';

drop table  if exists receiving_address;
CREATE TABLE receiving_address  (
  id bigint(21) unsigned auto_increment primary key COMMENT '主键id',
  user_id bigint(21) NOT NULL COMMENT '用户id',
  province varchar(50) DEFAULT NULL COMMENT '省份',
  city varchar(50) DEFAULT NULL COMMENT '城市',
  district varchar(50) DEFAULT NULL COMMENT '区',
  detail_address varchar(100) DEFAULT NULL COMMENT '详细住址',
  gmt_create datetime DEFAULT NULL COMMENT '创建时间',
  gmt_modified datetime DEFAULT NULL COMMENT '更新时间',
  is_deleted tinyint(2) DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '用户收货地址';
```

同时，为了方便演示和测试效果，单元测试中不再使用System.out.println来进行结果输出。
我们使用我开源的另外一个框架test4j进行单元测试，如果你不使用test4j，
fluent mybatis和[Test4J](https://gitee.com/tryternity/test4j)并没有绑定关系，你只需要关注fluent mybatis语法部分即可。

增加Test4J测试依赖项
```xml
<dependencies>
    <!--test jar-->
    <dependency>
        <groupId>org.test4j</groupId>
        <artifactId>fluent-mock</artifactId>
        <version>1.1.4</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
        <version>5.2.0.RELEASE</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.test4j</groupId>
        <artifactId>test4j-junit5</artifactId>
        <version>2.7.10</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

在src/test/resources根package下，增加test4j.properties文件

```properties
db.dataSource.list=dataSource
db.dataSource.script.factory=cn.org.atool.fluent.mybatis.tutorial.DataSourceScript
db.dataSource.type=mysql
db.dataSource.driver=com.mysql.jdbc.Driver
db.dataSource.url=jdbc:mysql://localhost:3306/fluent_mybatis?characterEncoding=utf8
db.dataSource.userName=root
db.dataSource.password=password
db.dataSource.schemaName=fluent_mybatis
```