# groupBy条件设置
## groupBy条件设置语法形式如下, 以groupBy开头，以end()方法结束
``` java
    .groupBy
    .column1().column2()
    .end()
```

示例1:
``` java
@Test
public void test_groupBy() throws Exception {
    UserQuery query = new UserQuery()
        .selectId()
        .where.id().eq(24L).end()
        .groupBy.userName().age().end();
    mapper.listEntity(query);
    db.sqlList().wantFirstSql()
        .eq("SELECT id FROM t_user WHERE id = ? GROUP BY user_name, age");
}
```

## having条件
``` java
    .groupBy.column1().column2().end().end()
    .having
    .聚合函数1.column1().条件(条件值)
    .聚合函数2.column2().条件(条件值)
    .end()
```
这里的条件和where部分类似, 支持eq, ge, gt等，示例
``` java
    .where.id().eq(24L).end()
    .groupBy.id().end()
    .having.sum.age().between(2, 10)
    .and.count.id().gt(2)
    .and.avg.age().in(new int[]{2, 3})
    .and.min.age().gt(10)
    .and.max.age().lt(20)
    .end();
```
对应sql语句
```sql
GROUP BY id 
HAVING SUM(age) BETWEEN ? AND ? 
AND COUNT(id) > ? 
AND AVG(age) IN (?, ?) 
AND MIN(age) > ? 
AND MAX(age) < ?
```
[详见where条件设置](03-where.md)
 
示例: having.字段().聚合函数().条件(条件值)
``` java
@DisplayName("按级别grade统计年龄在15和25之间的人数在10人以上，该条件内最大、最小和平均年龄")
@Test
public void test_count_gt_10_groupByGrade() throws Exception {
    UserQuery query = new UserQuery()
        .select
        .apply(grade)
        .id().count()
        .age().max()
        .age().min()
        .age().avg().end()
        .where
        .age().between(15, 25).end()
        .groupBy.apply(grade).end()
        .having
        .id().count().gt(10)
        .end();
    mapper.listEntity(query);
    db.sqlList().wantFirstSql()
        .eq("SELECT grade, COUNT(id), MAX(age), MIN(age), AVG(age) " +
            "FROM t_user " +
            "WHERE age BETWEEN ? AND ? " +
            "GROUP BY grade " +
            "HAVING COUNT(id) > ?");
}
```

示例2: having.apply("聚合函数").条件(条件值)
``` java
@Test
public void test_groupBy_having() throws Exception {
    UserQuery query = new UserQuery()
        .select
        .apply("count(1)", "sum(1)").end()
        .where
        .id().eq(24L).end()
        .groupBy
        .userName().age().end()
        .having
        .apply("count(1)").gt(10)
        .apply("sum(age)").gt(3)
        .end();
    mapper.listEntity(query);
    db.sqlList().wantFirstSql()
        .eq("SELECT count(1), sum(1) FROM t_user " +
            "WHERE id = ? GROUP BY user_name, age " +
            "HAVING count(1) > ? AND sum(age) > ?");
}
```