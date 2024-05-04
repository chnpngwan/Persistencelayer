# limit设置
``` java
    .limit(offset, maxSize)
```
或
``` java
    .limit(maxSize)
```

示例代码1
``` java
@Test
public void test_limit_offset() throws Exception {
    UserQuery query = new UserQuery()
        .where
        .age().eq(10).end()
        .limit(10, 20);

    mapper.listEntity(query);
    db.sqlList().wantFirstSql().end("WHERE age = ? LIMIT ?, ?");
}
```
控制台输出
```shell script
[main] DEBUG ...UserMapper.listEntity - ==>  Preparing: SELECT id, address_id, age, gmt_created, gmt_modified, grade, is_deleted, user_name, version FROM t_user WHERE age = ? LIMIT ?, ? 
[main] DEBUG ...UserMapper.listEntity - ==> Parameters: 10(Integer), 10(Integer), 20(Integer)
```

示例代码2
``` java
@Test
public void test_limit_maxSize() throws Exception {
    UserQuery query = new UserQuery()
        .where
        .age().eq(2).end()
        .limit(20);

    mapper.listEntity(query);
    db.sqlList().wantFirstSql().end("WHERE age = ? LIMIT ?, ?");
}
```

控制台输出
```shell script
[main] DEBUG ...UserMapper.listEntity - ==>  Preparing: SELECT id, address_id, age, gmt_created, gmt_modified, grade, is_deleted, user_name, version FROM t_user WHERE age = ? LIMIT ?, ? 
[main] DEBUG ...UserMapper.listEntity - ==> Parameters: 2(Integer), 0(Integer), 20(Integer)
```