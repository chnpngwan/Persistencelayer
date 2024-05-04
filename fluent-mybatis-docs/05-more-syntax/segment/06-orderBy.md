# orderBy条件设置
## orderBy条件设置语法形式如下, 以orderBy开头，以end()方法结束
``` java
    .orderBy
    .asc("column1", "column2")
    .desc("column3", "column4")
    .end()
```
或者
``` java
    .orderBy
    .字段().asc()
    .字段().desc()
    .end()
```

示例代码1
``` java
@Test
public void test_orderBy() throws Exception {
    UserQuery query = new UserQuery()
        .selectId()
        .where.id().eq(24L).end()
        .orderBy.id().asc().age().desc().end();
    mapper.listEntity(query);
    db.sqlList().wantFirstSql()
        .eq("SELECT id FROM t_user WHERE id = ? ORDER BY id ASC, age DESC");
}
```

示例代码2
``` java
@Test
public void test_orderBy() {
    UserQuery query = new UserQuery()
        .where.userName().like("user").end()
        .orderBy.id().asc().addressId().desc().desc("user_name", "id+0").end();
    mapper.listEntity(query);
    db.sqlList().wantFirstSql()
        .end("ORDER BY id ASC, address_id DESC, user_name DESC, id+0 DESC");
}
```

## 根据条件设置orderBy
``` java
    .orderBy
    .apply(boolean condition, boolean isAsc, "column1", "column2")
    .end()
```
    1. condition: 排序成立条件
    2. condition成立, isAsc = true, 按正序排; isAsc = false, 按倒序排。
    
示例代码
``` java
@Test
public void orderBy_condition() {
    UserQuery query = new UserQuery()
        .where.userName().like("user").end()
        .orderBy
        .apply(true, false, UserMapping.id, UserMapping.addressId)
        .apply(false, true, UserMapping.userName)
        .asc("id+0")
        .end();
    mapper.listEntity(query);
    db.sqlList().wantFirstSql().end("ORDER BY id DESC, address_id DESC, id+0 ASC");
}
```