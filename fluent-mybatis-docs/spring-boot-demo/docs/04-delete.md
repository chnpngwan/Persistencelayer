#### deleteById
根据主键Id物理删除记录
- 查看deleteById对应的SqlProvider语句构造方法
```java
public class YourSqlProvider {
    public String deleteById(Serializable id) {
        MapperSql sql = new MapperSql();
        sql.DELETE_FROM("your_table");
        sql.WHERE("id = #{id}");
        return sql.toString();
    }
}
```
- deleteById的SQL构造比较简单，我们直接看测试演示例子
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void deleteById(){
        int count = yourMapper.deleteById(3L);
        System.out.println("count:" + count);
    }
}
```
- 查看控制台输出log:
```text
DEBUG - ==>  Preparing: DELETE FROM your_table WHERE id = ?  
DEBUG - ==> Parameters: 3(Long) 
DEBUG - <==    Updates: 1 
count:1
```

#### deleteByIds
按id列表批量删除, 用法同deleteById
- 直接写个测试验证下
``` java
@Test
void deleteByIds() {
    int count = yourMapper.deleteByIds(Arrays.asList(1L, 2L, 3L));
    System.out.println("count:" + count);
}
```
- 控制台输出
```text
DEBUG - ==>  Preparing: DELETE FROM your_table WHERE id IN (?, ?, ?)  
DEBUG - ==> Parameters: 1(Long), 2(Long), 3(Long) 
```

#### delete
delete, 按自定义Query条件删除记录
- Mapper定义
```java
public interface YourMapper extends IEntityMapper<YourEntity> {
    @DeleteProvider(
        type = YourSqlProvider.class,
        method = "delete"
    )
    int delete(@Param(Param_EW) IQuery wrapper);
}
```
入参是一个IQuery对象，出参是删除记录数

- 验证示例
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void delete() {
        int count = yourMapper.delete(new YourQuery()
            .where.id().in(new int[]{1, 2, 3}).end()
        );
        System.out.println("count:" + count);
    }
}
```

- 查看控制台输出
```text
DEBUG - ==>  Preparing: DELETE FROM your_table WHERE id IN (?, ?, ?)  
DEBUG - ==> Parameters: 1(Integer), 2(Integer), 3(Integer) 
DEBUG - <==    Updates: 3 
count:3
```

#### deleteByMap
deleteByMap: 根据map中key=value条件集更新记录

- Mapper定义
```java
public interface YourMapper extends IEntityMapper<YourEntity> {
    @DeleteProvider(
        type = YourSqlProvider.class,
        method = "deleteByMap"
    )
    int deleteByMap(@Param(Param_CM) Map<String, Object> cm);
}
```

- 测试演示例子
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void deleteByMap() {
        int count = yourMapper.deleteByMap(new HashMap<String, Object>() {
            {
                this.put("name", "Fluent Mybatis");
                this.put("email", "darui.wu@163.com");
            }
        });
        System.out.println("count:" + count);
    }
}
```

- 查看控制台输出
```text
DEBUG - ==>  Preparing: DELETE FROM your_table WHERE name = ? AND email = ?  
DEBUG - ==> Parameters: Fluent Mybatis(String), darui.wu@163.com(String) 
DEBUG - <==    Updates: 2 
count:2
```

