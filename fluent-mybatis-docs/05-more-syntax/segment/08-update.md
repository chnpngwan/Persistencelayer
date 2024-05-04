# 更新设置
语法：以set开头，更新对应的字段值，以end()结束
``` java
    .set.字段1().is(设置值)
    .set.字段2().is(设置值)
    .set.字段3().is(设置值)
    .end()
    .where.条件设置.end()  
```

示例代码
``` java
@Test
void test_update() {
    mapper.updateBy(new UserUpdate()
        .set.age().is(34).end()
        .where.id().eq(2).end()
    );
}
```
对应sql语句
```sql
UPDATE t_user SET gmt_modified = now(), age = ? WHERE id = ?
```
说明：
示例显式指定字段age的更新值, 但gmt_modified字段设置了默认更新值now(),
所以在执行语句时同时更新了gmt_modified字段
控制台log输出
```shell script
...UserMapper.updateBy - ==>  Preparing: UPDATE t_user SET gmt_modified = now(), age = ? WHERE id = ? 
...UserMapper.updateBy - ==> Parameters: 34(Integer), 2(Integer)
```