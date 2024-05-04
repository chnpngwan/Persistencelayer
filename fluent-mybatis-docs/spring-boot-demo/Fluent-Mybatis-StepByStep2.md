# FluentMybatis入门介绍二
上篇FluentMybatis入门介绍一，介绍了框架提供的Mapper方法，其中有几个重要的方法用到了IQuery和IUpdate对象。
这2个对象是FluentMybatis实现复杂和动态sql的构造类，通过这2个对象fluent mybatis可以不用写具体的xml文件，
直接通过java api可以构造出比较复杂的业务sql语句，做到代码逻辑和sql逻辑的合一。

下面接着介绍如何通过IQuery和IUpdate定义强大的动态SQL语句。

## 先构造一个业务场景

- 表结构
假如有学生成绩表结构如下:
```sql
create table `student_score`
(
    id           bigint auto_increment comment '主键ID' primary key,
    student_id   bigint            not null comment '学号',
    gender_man   tinyint default 0 not null comment '性别, 0:女; 1:男',
    school_term  int               null comment '学期',
    subject      varchar(30)       null comment '学科',
    score        int               null comment '成绩',
    gmt_create   datetime          not null comment '记录创建时间',
    gmt_modified datetime          not null comment '记录最后修改时间',
    is_deleted   tinyint default 0 not null comment '逻辑删除标识'
) engine = InnoDB default charset=utf8;
```
- 需求
现在有需求: 
**统计2000年到2019年, 三门学科('英语', '数学', '语文')分数按学期,学科统计最低分，最高分和平均分,统计结果按学期和学科排序**
实现这个需求的SQL语句如下
```sql
select school_term, subject, count(score), min(score), max(score), avg(score)
from student_score
where school_term between 2000 and 2019
  and subject in ('英语', '数学', '语文')
  and is_deleted = 0
group by school_term, subject
order by school_term, subject
```
现在我们通过FluentMybatis来进行具体实现

1. 在StudentScoreDao类上定义接口
```java
@Data
public class ScoreStatistics {
    private int schoolTerm;
    private String subject;
    private long count;
    private Integer minScore;
    private Integer maxScore;
    private BigDecimal avgScore;
}
```
```java
public interface StudentScoreDao extends IBaseDao<StudentScoreEntity> {
    /**
     * 统计从fromYear到endYear年间学科subjects的统计数据
     *
     * @param fromYear 统计年份区间开始
     * @param endYear  统计年份区间结尾
     * @param subjects 统计的学科列表
     * @return 统计数据
     */
    List<ScoreStatistics> statistics(int fromYear, int endYear, String[] subjects);
}
```

2. 在StudentScoreDaoImpl上实现业务逻辑

```java
@Repository
public class StudentScoreDaoImpl extends StudentScoreBaseDao implements StudentScoreDao {
    @Override
    public List<ScoreStatistics> statistics(int fromSchoolTerm, int endSchoolTerm, String[] subjects) {
        return super.listPoJos(ScoreStatistics.class, super.query()
            .select.schoolTerm().subject()
            .count("count")
            .min.score("min_score")
            .max.score("max_score")
            .avg.score("avg_score")
            .end()
            .where.isDeleted().isFalse()
            .and.schoolTerm().between(fromSchoolTerm, endSchoolTerm)
            .and.subject().in(subjects)
            .end()
            .groupBy.schoolTerm().subject().end()
            .orderBy.schoolTerm().asc().subject().asc().end()
        );
    }
}
```
 DaoImpl实现中，除了根据条件返回统计结果，还讲结果按照下划线转驼峰的规则自动转换为ScoreStatistics对象返回。
 
3. 写个测试验证下

```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuickStartApplication.class)
public class StudentScoreDaoImplTest {
    @Autowired
    private StudentScoreDao dao;

    @Test
    public void statistics() {
        List<ScoreStatistics> list = dao.statistics(2000, 2019, new String[]{"语文", "数学", "英语"});
        System.out.println(list);
    }
}
```

4. 查看控制台输出结果
```text
DEBUG - ==>  Preparing: SELECT school_term, subject, count(*) AS count, MIN(score) AS min_score, MAX(score) AS max_score, AVG(score) AS avg_score 
    FROM student_score 
    WHERE is_deleted = ? 
    AND school_term BETWEEN ? AND ? 
    AND subject IN (?, ?, ?) 
    GROUP BY school_term, subject 
    ORDER BY school_term ASC, subject ASC  
DEBUG - ==> Parameters: false(Boolean), 2000(Integer), 2019(Integer), 语文(String), 数学(String), 英语(String) 
DEBUG - <==      Total: 30 
[ScoreStatistics(schoolTerm=2000, subject=数学, count=17, minScore=1, maxScore=93, avgScore=36.0588), 
 ...
 ScoreStatistics(schoolTerm=2009, subject=语文, count=24, minScore=3, maxScore=100, avgScore=51.2500)]
```

上面通过例子大致预览了FluentMybatis动态构造查询的逻辑，下面我们具体讲解

- [where条件构造](../05-more-syntax/segment/03-where.md)
- [嵌套条件构造](../05-more-syntax/segment/04-nested-where.md)
- [group by条件构造](../05-more-syntax/segment/05-groupBy.md)
- [order by条件构造](../05-more-syntax/segment/06-orderBy.md)
- [update动态构造](../05-more-syntax/segment/08-update.md)
- [设置select字段](../05-more-syntax/segment/01-select.md)
- [select聚合函数](../05-more-syntax/segment/02-select-aggregate.md)
- [limit设置](../05-more-syntax/segment/07-limit.md)