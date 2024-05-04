# FluentMybatis入门介绍三: 复杂查询&连表查询
fluent mybatis处理在简单的CRUD中可以省略很多手工编码工作，即使是复杂的查询，那也是信手拈来，毫无问题。
文章中的join演示基于fluent mybatis版本 1.4.1+
```xml
<dependencies>
    <!-- fluent mybatis依赖-->
    <dependency>
        <groupId>com.github.atool</groupId>
        <artifactId>fluent-mybatis</artifactId>
        <version>1.9.7</version>
    </dependency>
    <dependency>
        <groupId>com.github.atool</groupId>
        <artifactId>fluent-mybatis-processor</artifactId>
        <version>1.9.7</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

## fluent mybatis支持的复杂查询场景
- IN 子查询
```sql
column IN (select column from ... )
```

- EXISTS 子查询
```sql
EXISTS (select 1 from .... )
```

- JOIN 查询(包括INNER JOINm, LEFT JOIN, RIGHT JOIN)
```sql
select ... from table1 a join table2 b on a.xxx = b.xxx where ...;

select ... from table1 a left join table2 b on a.xxx = b.xxx where ...;

select ... from table1 a right join table2 b on a.xxx = b.xxx where ...;
```

不仅仅是支持2张表的关联查询，还支持任意张表JOIN。

- JOIN, AND，OR大混战

## 场景设置
为了方便讲解fluent mybatis的这些复杂查询功能，我们创建3张表：学生信息表，行政区域，学生成绩单
```sql
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
) ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '学生信息表';

CREATE TABLE county_division
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    province     varchar(50) DEFAULT NULL COMMENT '省份',
    city         varchar(50) DEFAULT NULL COMMENT '城市',
    county       varchar(50) DEFAULT NULL COMMENT '区县',
    gmt_created  datetime    DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime    DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(2)  DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '区县';

create table `student_score`
(
    id           bigint auto_increment primary key COMMENT '主键ID',
    student_id   bigint               NOT NULL COMMENT '学号',
    school_term  int                  NULL COMMENT '学期',
    subject      varchar(30)          NULL COMMENT '学科',
    score        int                  NULL COMMENT '成绩',
    gmt_create   datetime             NOT NULL COMMENT '记录创建时间',
    gmt_modified datetime             NOT NULL COMMENT '记录最后修改时间',
    is_deleted   tinyint(2) default 0 NOT NULL COMMENT '逻辑删除标识'
) engine = InnoDB default charset = utf8 COMMENT = '学生成绩';
```
    
## IN 子查询
- 需求场景
查询所有浙江省杭州市，四年级学生

- 场景SQL
```sql
selct * from student
where grade = 4
and is_deleted = 0
and home_county_id in 
    (select id from county_division 
     where is_deleted = 0
     and province = '浙江省'
     and city = '杭州市')
```

- FluentMybatis实现
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuickStartApplication.class)
public class InSelectDemo {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_in_select() {
        StudentQuery query = new StudentQuery()
            .where.isDeleted().isFalse()
            .and.grade().eq(4)
            .and.homeCountyId().in(new CountyDivisionQuery()
                .selectId()
                .where.isDeleted().isFalse()
                .and.province().eq("浙江省")
                .and.city().eq("杭州市")
                .end()
            ).end();
        List<StudentEntity> students = mapper.listEntity(query);
    }
}
```

- 验证控制台log输出
```text
DEBUG - ==>  Preparing: SELECT id, gmt_modified, is_deleted, address, age, birthday, bonus_points, gender_man, gmt_created, grade, home_county_id, phone, status, user_name 
    FROM student WHERE is_deleted = ? AND grade = ? 
    AND home_county_id IN (SELECT id FROM county_division 
            WHERE is_deleted = ? AND province = ? AND city = ?)  
DEBUG - ==> Parameters: false(Boolean), 4(Integer), false(Boolean), 浙江省(String), 杭州市(String) 
DEBUG - <==      Total: 0 
```

## EXISTS 子查询
- 需求场景
查询2019学期存在语文或数学考试不及格的学生列表

- SQL实现
```sql
select * from student
 where is_deleted = 0
 and exists (select 1 from student_score
     where is_deleted = 0
     and score < 60
     and school_term = 2019
     and subject in ('语文', '数学')
     and student_id = student.id
 )
```

- fluent mybatis实现
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class InSelectDemo {
    @Autowired
    private StudentMapper mapper;
    @Test
    public void test_exists() {
        StudentQuery query = new StudentQuery()
            .where.isDeleted().isFalse()
            .and.exists(new StudentScoreQuery()
                .selectId()
                .where.isDeleted().isFalse()
                .and.schoolTerm().eq(2019)
                .and.score().lt(60)
                .and.subject().in(new String[]{"语文", "数学"})
                .and.studentId().apply("= student.id")
                .end()
            ).end();
        List<StudentEntity> students = mapper.listEntity(query);
    }
}
```

- 验证控制台log输出
```text
DEBUG - ==>  Preparing: SELECT id, gmt_modified, is_deleted, address, age, birthday, bonus_points, gender_man, gmt_created, grade, home_county_id, phone, status, user_name FROM student 
    WHERE is_deleted = ? 
    AND EXISTS (SELECT id FROM student_score 
        WHERE is_deleted = ? 
        AND school_term = ? 
        AND score < ? 
        AND subject IN (?, ?) 
        AND student_id =student.id
)  
DEBUG - ==> Parameters: false(Boolean), false(Boolean), 2019(Integer), 60(Integer), 语文(String), 数学(String) 
DEBUG - <==      Total: 0 
```

## INNER JOIN查询
- 场景需求
查出家庭是浙江省杭州市的男学生姓名，年龄和所在区县

- SQL实现
```sql
select a.user_name, a.age, a.gender_man, b.province, b.city, b.county
from student a join county_division b
on a.home_county_id = b.id
where a.is_deleted = 0
and a.gender_man = 1
and b.is_deleted = 0
and b.province = '浙江省'
and b.city = '杭州市'
```

- fluent mybatis join语法
join查询的基本语法如下：

``` java
Parameters parameters = new Parameters();
IQuery query = JoinBuilder<左表Query>
.from(new 左表Query("别名", parameters).where(设置左表查询条件).end())
.join(new 右表1Query("别名", parameters).where(设置右表1查询条件).end())
.on(l->{左表on字段}, r->{右表1on字段})
.join(new 右表2Query("别名", parameters).where(设置右表2查询条件).end())
.on(l->{左表on字段}, r->{右表2on字段})
.builder();
```

- fluent mybatis实现
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuickStartApplication.class)
public class JoinSelectDemo {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_join_student_county() {
        Parameters parameters = new Parameters();
        IQuery query = JoinBuilder
            .<StudentQuery>from(new StudentQuery("t1", parameters)
                .select.userName().age().genderMan().end()
                .where.isDeleted().isFalse()
                .and.genderMan().eq(1).end())
            .join(new CountyDivisionQuery("t2", parameters)
                .select.province().city().county().end()
                .where.isDeleted().isFalse()
                .and.province().eq("浙江省")
                .and.city().eq("杭州市").end())
            .on(l -> l.where.homeCountyId(), r -> r.where.id()).endJoin()
            .build();
        mapper.listMaps(query);
    }
}
```

- 验证控制台log输出
```text
DEBUG - ==>  Preparing: SELECT t1.user_name, t1.age, t1.gender_man, t2.province, t2.city, t2.county 
    FROM student t1 
    JOIN county_division t2 
    ON t1.home_county_id = t2.id 
    WHERE t1.is_deleted = ? 
    AND t1.gender_man = ? 
    AND t2.is_deleted = ? 
    AND t2.province = ? 
    AND t2.city = ?  
DEBUG - ==> Parameters: false(Boolean), 1(Integer), false(Boolean), 浙江省(String), 杭州市(String) 
DEBUG - <==      Total: 0 
```
我们看到fluent mybatis根据设置自动组装了join查询，设置了on条件，并给查询表设置了别名，同时根据别名进行条件设置。

## LEFT JOIN & RIGHT JOIN查询
LEFT JOIN & RIGHT JOIN查询同JOIN查询，只是把
``` java
Parameters parameters = new Parameters();
JoinBuilder
    .<StudentQuery>from(new StudentQuery("t1", parameters).where("条件设置").end())
    .join(new CountyDivisionQuery("t2", parameters).where("条件设置").end())
    .on(l->l.where.左表字段(), r->r.where.右表字段()).endJoin()
```
换成对应的
``` java
Parameters parameters = new Parameters();
JoinBuilder
    .<StudentQuery>from(new StudentQuery("t1", parameters).where("条件设置").end())
    .leftJoin(new CountyDivisionQuery("t2", parameters).where("条件设置").end())
    .on(l->l.where.左表字段(), r->r.where.右表字段()).endJoin()
```
或
``` java
Parameters parameters = new Parameters();
JoinBuilder
    .<StudentQuery>from(new StudentQuery("t1", parameters).where("条件设置").end())
    .rightJoin(new CountyDivisionQuery("t2", parameters).where("条件设置").end())
    .on(l->l.where.左表字段(), r->r.where.右表字段()).endJoin()
```

## 3张表的JOIN查询
上面演示了2张表之间的关联查询操作，那如果多于2张表关联查询呢？多张表的关联查询，对于fluent mybatis来说也是相当简单的，多设置一个join操作即可。
**在实际业务中，尽量避免多表的关联操作**

- 需求场景
关联查询出浙江省杭州市2019学期语文或数学成绩在90分以上的同学信息

- SQL实现
```sql
select a.user_name, c.subject, c.score
from student a
join county_division b on a.home_county_id = b.id
join student_score c on a.id = c.student_id
where a.is_deleted = 0
and b.is_deleted = 0
and b.province = '浙江省'
and b.city = '杭州市'
and c.is_deleted = 0
and c.school_term = 2019
and c.subject in ('语文', '数学')
and c.score >=90
```
- fluent mybatis实现
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuickStartApplication.class)
public class JoinSelectDemo {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_3_table_join() {
        Parameters parameters = new Parameters();
        IQuery query = JoinBuilder
            .<StudentQuery>from(new StudentQuery("t1", parameters)
                .select.userName().end()
                .where.isDeleted().isFalse().end())
            .join(new CountyDivisionQuery("t2", parameters)
                .where.isDeleted().isFalse()
                .and.province().eq("浙江省")
                .and.city().eq("杭州市").end())
            .on(l -> l.where.homeCountyId(), r -> r.where.id()).endJoin()
            .join(new StudentScoreQuery("t3", parameters)
                .select.subject().score().end()
                .where.isDeleted().isFalse()
                .and.schoolTerm().eq(2019)
                .and.subject().in(new String[]{"语文", "数学"})
                .and.score().ge(90).end())
            .on(l -> l.where.id(), r -> r.where.studentId()).endJoin()
            .build();
        mapper.listMaps(query);
    }
}
```

- 验证控制台输出log
```text
DEBUG - ==>  Preparing: SELECT t1.user_name, t3.subject, t3.score 
    FROM student t1 
    JOIN county_division t2 ON t1.home_county_id = t2.id 
    JOIN student_score t3 ON t1.id = t3.student_id 
    WHERE t1.is_deleted = ? 
    AND t2.is_deleted = ? AND t2.province = ? AND t2.city = ? 
    AND t3.is_deleted = ? AND t3.school_term = ? AND t3.subject IN (?, ?) AND t3.score >= ?  
DEBUG - ==> Parameters: false(Boolean), false(Boolean), 浙江省(String), 杭州市(String), false(Boolean), 2019(Integer), 语文(String), 数学(String), 90(Integer) 
DEBUG - <==      Total: 0 
```

我们看到, fluent mybatis设置了2个join操作和on条件设置，并且给每个join表都设置了别名。
三张表各自的where条件也都是根据别名进行了设置。

## JOIN, AND，OR大混战
fluent mybatis除了支持AND, IN(子查询), EXISTS(子查询), JOIN查询。
同时，还可以根据需要使用AND(多个OR条件), OR(多个AND条件)进行复杂条件的组合操作。
比如，对上面的语文或数学，我们把IN操作换成 OR条件来简单演示一下。

- AND( 条件1 OR 条件2) fluent mybatis实现
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuickStartApplication.class)
public class JoinAndOrDemo {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_and_or() {
        Parameters parameters = new Parameters();
        IQuery query = JoinBuilder
            .<StudentQuery>from(new StudentQuery("t1", parameters)
                .select.userName().end()
                .where.isDeleted().isFalse().end())
            .join(new CountyDivisionQuery("t2", parameters)
                .where.isDeleted().isFalse()
                .and.province().eq("浙江省")
                .and.city().eq("杭州市").end())
            .on(l -> l.where.homeCountyId(), r -> r.where.id()).endJoin()
            .join(new StudentScoreQuery("t3", parameters)
                .select.subject().score().end()
                .where.isDeleted().isFalse()
                .and.schoolTerm().eq(2019)
                .and(iq -> iq
                    .where.subject().eq("语文")
                    .or.subject().eq("数学").end())
                .and.score().ge(90).end())
            .on(l -> l.where.id(), r -> r.where.studentId()).endJoin()
            .build();
        mapper.listMaps(query);
    }
}
```

- 验证控制台log输出
```text
DEBUG - ==>  Preparing: SELECT t1.user_name, t3.subject, t3.score 
    FROM student t1 
    JOIN county_division t2 ON t1.home_county_id = t2.id 
    JOIN student_score t3 ON t1.id = t3.student_id 
    WHERE t1.is_deleted = ? 
    AND t2.is_deleted = ? AND t2.province = ? AND t2.city = ? 
    AND t3.is_deleted = ? AND t3.school_term = ? 
    AND ( subject = ? OR subject = ? ) 
    AND t3.score >= ?  
DEBUG - ==> Parameters: false(Boolean), false(Boolean), 浙江省(String), 杭州市(String), false(Boolean), 2019(Integer), 语文(String), 数学(String), 90(Integer) 
```

我们注意到log中输出的语句中包含**AND ( subject = ? OR subject = ? )**

## 总结
对于复杂的查询，fluent mybatis也做到了xml和mapper的零编码，并且无魔法字符串编码，fluent api式操作。

[Fluent MyBatis入门介绍一](https://juejin.im/post/6884799624755249160)

[FluentMybatis入门介绍二](https://juejin.im/post/6887167888999792648)

[Fluent Mybatis, 原生Mybatis, Mybatis Plus三者功能对比](https://juejin.im/post/6886019929519177735)

[Fluent Mybatis文档&示例](https://gitee.com/fluent-mybatis/fluent-mybatis-docs)

[Fluent Mybatis Gitee](https://gitee.com/fluent-mybatis/fluent-mybatis)

[Fluent Mybatis GitHub](https://github.com/atool/fluent-mybatis) 