#### updateById
updateById 根据Entity id值，更新Entity中非空属性

- Mapper定义
```java
public interface YourMapper extends IEntityMapper<YourEntity> {
  @UpdateProvider(
      type = YourSqlProvider.class,
      method = "updateById"
  )
  int updateById(@Param(Param_ET) YourEntity entity);
}
```
入参是Entity对象， 出参是更新记录数，这里返回值只可能是0: 不存在id记录,更新失败；1: 更新id记录成功。

- 动态SQL组装
```java
public class YourSqlProvider {
    public String updateById(Map<String, Object> map) {
        YourEntity entity = getParas(map, "et");
        MapperSql sql = new MapperSql();
        sql.UPDATE("your_table");
        List<String> sets = new ArrayList<>();
        if (entity.getGmtCreate() != null) {
            sets.add("gmt_create = #{et.gmtCreate}");
        }
        if (entity.getGmtModified() != null) {
            sets.add("gmt_modified = #{et.gmtModified}");
        } else {
            sets.add("gmt_modified = now()");
        }
        if (entity.getIsDeleted() != null) {
            sets.add("is_deleted = #{et.isDeleted}");
        }
        if (entity.getAge() != null) {
            sets.add("age = #{et.age}");
        }
        if (entity.getEmail() != null) {
            sets.add("email = #{et.email}");
        }
        if (entity.getName() != null) {
            sets.add("name = #{et.name}");
        }
        sql.SET(sets);
        sql.WHERE("id = #{et.id}");
        return sql.toString();
    }
}
```
我们看到，在设置set时，会判断entity对象是否为null；但如果在Entity对象上设置了 @TableField( update = 'update默认值')，
则entity属性是空的情况下，会使用默认值代替，比如上面gmtModified属性
``` java
if (entity.getGmtModified() != null) {
    sets.add("gmt_modified = #{et.gmtModified}");
} else {
    sets.add("gmt_modified = now()");
}
```

where条件部分则比较简单: id = #{et.id}

- 演示验证例子
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;
    
    @Test
    void updateById() {
        int count = yourMapper.updateById(new YourEntity()
            .setId(2L)
            .setName("Powerful Fluent Mybatis")
        );
        System.out.println(count);
    }
}
```

- 查看控制台输出
```text
DEBUG - ==>  Preparing: UPDATE your_table SET gmt_modified = now(), name = ? WHERE id = ?  
DEBUG - ==> Parameters: Powerful Fluent Mybatis(String), 2(Long) 
DEBUG - <==    Updates: 1 
1
```
我们看到update set部分，除了设置了name=?，还设置了 gmt_modified = now()

#### updateBy
updateBy, 根据自定义set语句，where条件执行更新操作

- Mapper定义
```java
public interface YourMapper extends IEntityMapper<YourEntity> {
  @UpdateProvider(
      type = YourSqlProvider.class,
      method = "updateBy"
  )
  int updateBy(@Param(Param_EW) IUpdate update);
}
```

入参是一个IUpdate对象，出参是更新成功的记录数。

- 动态SQL构造
```java
public class YourSqlProvider {
    public String updateBy(Map<String, Object> map) {
        WrapperData data = getWrapperData(map, "ew");
        MapperSql sql = new MapperSql();
        Map<String, String> updates = data.getUpdates();
        assertNotEmpty("updates", updates);
        sql.UPDATE("your_table");
        List<String> sets = new ArrayList<>();
        if (!updates.containsKey("gmtModified")) {
            sets.add("gmt_modified = now()");
        }
        sets.add(data.getUpdateStr());
        sql.SET(sets);
        sql.WHERE_GROUP_ORDER_BY(data);
        sql.LIMIT(data, true);
        return sql.toString();
    }
}
```
动态构造语句中对 @TableField( update = 'update默认值')字段（这里是gmtModified）做了单独判断, 
如果条件中不包含gmtModified，则追加默认值更新。

- 写个例子验证
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void updateBy() {
        int count = yourMapper.updateBy(new YourUpdate()
            .set.name().is("Powerful Fluent mybatis")
            .set.email().is("darui.wu@163.com")
            .set.age().is(1).end()
            .where.id().eq(2).end()
        );
        System.out.println(count);
    }
}
```

- 查看控制台输出
```text
DEBUG - ==>  Preparing: UPDATE your_table SET gmt_modified = now(), name = ?, email = ?, age = ? WHERE id = ?  
DEBUG - ==> Parameters: Powerful Fluent mybatis(String), darui.wu@163.com(String), 1(Integer), 2(Integer) 
DEBUG - <==    Updates: 1 
1
```
注意 gmt_modified = now()更新默认值部分