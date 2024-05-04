#### findById
根据id查找单条数据
- 系统生成的Mapper方法定义

```java
public interface YourMapper extends IEntityMapper<YourEntity> {
    String ResultMap = "YourEntityResultMap";

    @SelectProvider(
        type = YourSqlProvider.class,
        method = "findById"
    )
    @Results(
        id = ResultMap,
        value = {
            @Result(column = "id", property = "id", javaType = Long.class, id = true),
            @Result(column = "gmt_create", property = "gmtCreate", javaType = Date.class),
            @Result(column = "gmt_modified", property = "gmtModified", javaType = Date.class),
            @Result(column = "is_deleted", property = "isDeleted", javaType = Boolean.class),
            @Result(column = "age", property = "age", javaType = Integer.class),
            @Result(column = "email", property = "email", javaType = String.class),
            @Result(column = "name", property = "name", javaType = String.class)
        }
    )
    YourEntity findById(Serializable id);
}
```

在findById上，除了定义了提供动态SQL语句的SQLProvider类和方法外，还定义的数据映射关系 @Results。
这个ResultMap映射在单个Mapper里是通用的，其他的查询方法返回Entity对象时也会用到。

- 系统生成的动态sql构造方法
```java
public class YourSqlProvider {
    public String findById(Serializable id) {
        assertNotNull("id", id);
        MapperSql sql = new MapperSql();
        sql.SELECT("your_table", ALL_ENTITY_FIELDS);
        sql.WHERE("id = #{id}");
        return sql.toString();
    } 
}
```
这个SQL拼接比较简单
1. 根据Entity字段拼接了查询字段列表
2. 设置 id = #{id}

- 写个测试实际使用下
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void findById(){
        YourEntity entity = yourMapper.findById(8L);
        System.out.println(entity);
    }
}
```

- 查看控制台输出log
```text
DEBUG - ==>  Preparing: SELECT id, gmt_create, gmt_modified, is_deleted, age, email, name FROM your_table WHERE id = ?  
DEBUG - ==> Parameters: 8(Long) 
DEBUG - <==      Total: 1 
YourEntity(id=8, gmtCreate=null, gmtModified=null, isDeleted=false, age=1, email=darui.wu@163.com, name=Fluent Mybatis)
```

#### listByIds
根据id列表批量查询实例
- Mapper定义
```java
public interface YourMapper extends IEntityMapper<YourEntity> {
    String ResultMap = "YourEntityResultMap";

    @Override
    @SelectProvider(
        type = YourSqlProvider.class,
        method = "listByIds"
    )
    @ResultMap(ResultMap)
    List<YourEntity> listByIds(@Param(Param_Coll) Collection ids);
}
```
输入是一个id列表集合，返回是一个Entity列表, 数据的映射复用了findById中定义的ResultMap。

- 动态SQL提供方法
```java
public class YourSqlProvider {
    public String listByIds(Map map) {
        Collection ids = getParas(map, "coll");
        MapperSql sql = new MapperSql();
        sql.SELECT("your_table", ALL_ENTITY_FIELDS);
        sql.WHERE_PK_IN("id", ids.size());
        return sql.toString();
    }
}
```
1. 根据Entity字段拼接了查询字段列表
2. 根据传入的id数量(size), 设置 id IN (#{coll[0]}, ..., #{coll[size - 1]})

- 写测试验证下

```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void listByIds() {
        List<YourEntity> entities = yourMapper.listByIds(Arrays.asList(8L, 9L));
        System.out.println(entities);
    }
}
```

- 查看控制台输出

```text
DEBUG - ==>  Preparing: SELECT id, gmt_create, gmt_modified, is_deleted, age, email, name FROM your_table WHERE id IN (?, ?)  
DEBUG - ==> Parameters: 8(Long), 9(Long) 
DEBUG - <==      Total: 2 
[YourEntity(id=8, gmtCreate=null, gmtModified=null, isDeleted=false, age=1, email=darui.wu@163.com, name=Fluent Mybatis), 
 YourEntity(id=9, gmtCreate=null, gmtModified=null, isDeleted=false, age=1, email=darui.wu@163.com, name=Fluent Mybatis)]
```

#### findOne
根据自定义条件查询单条记录

- Mapper方法定义
```java
public interface YourMapper extends IEntityMapper<YourEntity> {
    @SelectProvider(
        type = YourSqlProvider.class,
        method = "findOne"
    )
    @ResultMap(ResultMap)
    YourEntity findOne(@Param(Param_EW) IQuery query);
}
```

- 动态sql组装
```java
public class YourSqlProvider {
    public String findOne(Map map) {
        WrapperData data = getWrapperData(map, "ew");
        MapperSql sql = new MapperSql();
        sql.SELECT("your_table", data, ALL_ENTITY_FIELDS);
        sql.WHERE_GROUP_ORDER_BY(data);
        return byPaged(DbType.MYSQL, data, sql.toString());
    }
}
```
动态SQL组装做了以下几件事：
1. 根据query是否显式设置了查询字段，设置select字段列表，如果未设置，则取默认拼装Entity全字段。
2. 根据query里面的where, group by, having by和order by设置查询条件: sql.WHERE_GROUP_ORDER_BY(data)
3. 根据是否设置了分页信息和数据库类型，组装分页查询语法: byPaged(DbType.MYSQL, data, sql.toString())

- 写个测试验证下
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void findOne() {
        YourEntity entity = yourMapper.findOne(new YourQuery()
            .where.id().eq(4L).end()
        );
    }
}
```
查看控制台的输出:
```text
DEBUG - ==>  Preparing: SELECT id, gmt_create, gmt_modified, is_deleted, age, email, name FROM your_table WHERE id = ?  
DEBUG - ==> Parameters: 4(Long) 
DEBUG - <==      Total: 1 
YourEntity(id=4, gmtCreate=null, gmtModified=null, isDeleted=false, age=1, email=darui.wu@163.com, name=Fluent Mybatis)
```
这种情况下，数据库中满足条件的数据有一条或0条；如果符合条件的数据大于一条，情况会怎样呢，我们再写一个测试实验一下。

- 如果findOne，符合条件数据大于2条

```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void findOne2() {
        YourEntity entity = yourMapper.findOne(new YourQuery()
            .where.name().eq("Fluent Mybatis").end()
        );
        System.out.println(entity);
    }
}
```
因为数据库中有多条name='Fluent Mybatis'的数据，调用这个方法会抛出异常
```text
DEBUG - ==>  Preparing: SELECT id, gmt_create, gmt_modified, is_deleted, age, email, name FROM your_table WHERE name = ?  
DEBUG - ==> Parameters: Fluent Mybatis(String) 
DEBUG - <==      Total: 14 

org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.exceptions
.TooManyResultsException: Expected one result (or null) to be returned by selectOne(), 
but found: 14
```

#### listByMap
- Mapper方法定义
```java
public interface YourMapper extends IEntityMapper<YourEntity> {
    String ResultMap = "YourEntityResultMap";
    @SelectProvider(
        type = YourSqlProvider.class,
        method = "listByMap"
    )
    @ResultMap(ResultMap)
    List<YourEntity> listByMap(@Param(Param_CM) Map<String, Object> columnMap);
}
```
入参Map<String, Object>, 用来表示查询数据的条件。具体条件是 key = value 的AND关系。

- 动态SQL拼接
```java
public class YourSqlProvider {
    public String listByMap(Map map) {
        Map<String, Object> where = getParas(map, "cm");
        MapperSql sql = new MapperSql();
        sql.SELECT("your_table", ALL_ENTITY_FIELDS);
        sql.WHERE("cm", where);
        return sql.toString();
    }
}
```

1. 查询Entity所有字段
2. 组装map条件, (key1 = value1) AND (key2 = value2)

- 写个测试demo验证下

```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;
    @Test
    void listByMap() {
        List<YourEntity> entities = yourMapper.listByMap(new HashMap<String, Object>() {
            {
                this.put("name", "Fluent Mybatis");
                this.put("is_deleted", false);
            }
        });
        System.out.println(entities);
    }
}
```
- 查看控制台输出
```text
DEBUG - ==>  Preparing: SELECT id, gmt_create, gmt_modified, is_deleted, age, email, name FROM your_table WHERE is_deleted = ? AND name = ?  
DEBUG - ==> Parameters: false(Boolean), Fluent Mybatis(String) 
DEBUG - <==      Total: 5 
[YourEntity(id=4, gmtCreate=null, gmtModified=null, isDeleted=false, age=1, email=darui.wu@163.com, name=Fluent Mybatis), 
 YourEntity(id=5, gmtCreate=null, gmtModified=null, isDeleted=false, age=1, email=darui.wu@163.com, name=Fluent Mybatis), 
 YourEntity(id=6, gmtCreate=null, gmtModified=null, isDeleted=false, age=1, email=darui.wu@163.com, name=Fluent Mybatis), 
 YourEntity(id=7, gmtCreate=null, gmtModified=null, isDeleted=false, age=1, email=darui.wu@163.com, name=Fluent Mybatis), 
 YourEntity(id=8, gmtCreate=null, gmtModified=null, isDeleted=false, age=1, email=darui.wu@163.com, name=Fluent Mybatis)]
```

#### listEntity
根据自定义条件查询数据，并把数据映射为对应的Entity类

- Mapper方法定义
```java
public interface YourMapper extends IEntityMapper<YourEntity> {
    @SelectProvider(
        type = YourSqlProvider.class,
        method = "listEntity"
    )
    @ResultMap(ResultMap)
    List<YourEntity> listEntity(@Param(Param_EW) IQuery query);
}
```

- 动态SQL组装
```java
public class YourSqlProvider {
    public String listEntity(Map map) {
        WrapperData data = getWrapperData(map, "ew");
        MapperSql sql = new MapperSql();
        sql.SELECT("your_table", data, ALL_ENTITY_FIELDS);
        sql.WHERE_GROUP_ORDER_BY(data);
        return byPaged(DbType.MYSQL, data, sql.toString());
    }
}
```
同findOne方法, 动态SQL组装做了下面几件事:
1. 根据query是否显式设置了查询字段，设置select字段列表，如果未设置，则取默认拼装Entity全字段。
2. 根据query里面的where, group by, having by和order by设置查询条件: sql.WHERE_GROUP_ORDER_BY(data)
3. 根据是否设置了分页信息和数据库类型，组装分页查询语法: byPaged(DbType.MYSQL, data, sql.toString())

- 写个测试看下效果
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void listEntity() {
        List<YourEntity> entities = yourMapper.listEntity(new YourQuery()
            .select.name().age().email().end()
            .where.id().lt(6L)
            .and.name().like("Fluent").end()
            .orderBy.id().desc().end()
        );
        System.out.println(entities);
    }
}
```

- 查看控制台log
```text
DEBUG - ==>  Preparing: SELECT name, age, email FROM your_table WHERE id < ? AND name LIKE ? ORDER BY id DESC  
DEBUG - ==> Parameters: 6(Long), %Fluent%(String) 
DEBUG - <==      Total: 2 
[YourEntity(id=null, gmtCreate=null, gmtModified=null, isDeleted=null, age=1, email=darui.wu@163.com, name=Fluent Mybatis),
 YourEntity(id=null, gmtCreate=null, gmtModified=null, isDeleted=null, age=1, email=darui.wu@163.com, name=Fluent Mybatis)]
```

自定义查询定义了
1. 要查询的字段: name, age, email3个字段
2. 定义了具体条件: id < ? AND name LIKE ?
3. 定义了按id倒序排

#### listMaps
listMaps参数构造和listEntity一样，不同的时返回时不映射为Entity，而且映射成Map对象
- 写个测试验证下
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void listMaps() {
        List<Map<String,Object>> maps = yourMapper.listMaps(new YourQuery()
            .select.name().age().email().end()
            .where.id().lt(6L)
            .and.name().like("Fluent").end()
            .orderBy.id().desc().end()
        );
        System.out.println(maps);
    }
}
```

- 查看控制台输出信息
```text
DEBUG - ==>  Preparing: SELECT name, age, email AS EMail FROM your_table WHERE id < ? AND name LIKE ? ORDER BY id DESC  
DEBUG - ==> Parameters: 6(Long), %Fluent%(String) 
DEBUG - <==      Total: 2 
[{name=Fluent Mybatis, EMail=darui.wu@163.com}, 
 {name=Fluent Mybatis, EMail=darui.wu@163.com}]
```

#### listObjs
listObjs查询参数构造和listEntity、listMaps一样，但只返回查询对象的第一列，其余列被舍弃。

- 验证例子
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void listObjs() {
        List<String> ids = yourMapper.listObjs(new YourQuery()
            .select.name().age().email().end()
            .where.id().lt(6L)
            .and.name().like("Fluent").end()
            .orderBy.id().desc().end()
        );
        System.out.println(ids);
    }
}
```
- 查看控制台输出信息
```text
DEBUG - ==>  Preparing: SELECT name, age, email AS EMail FROM your_table WHERE id < ? AND name LIKE ? ORDER BY id DESC  
DEBUG - ==> Parameters: 6(Long), %Fluent%(String) 
DEBUG - <==      Total: 2 
[Fluent Mybatis, Fluent Mybatis]
```
我们看到，控制台只打印出了查询字段的第一列name： [Fluent Mybatis, Fluent Mybatis]

#### count
count, 返回符合条件的记录数
- Mapper定义
```java
public interface YourMapper extends IEntityMapper<YourEntity> {
  @SelectProvider(
      type = YourSqlProvider.class,
      method = "count"
  )
  Integer count(@Param(Param_EW) IQuery query);
}
```

- 验证示例
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void count() {
        int count = yourMapper.count(new YourQuery()
            .where.id().lt(1000L)
            .and.name().like("Fluent").end()
            .limit(0, 10)
        );
        System.out.println(count);
    }
}
```

- 查看控制台输出信息
```text
DEBUG - ==>  Preparing: SELECT COUNT(*) FROM your_table WHERE id < ? AND name LIKE ? LIMIT ?, ?  
DEBUG - ==> Parameters: 1000(Long), %Fluent%(String), 0(Integer), 10(Integer) 
DEBUG - <==      Total: 1 
5
```

#### countNoLimit
使用方法同count,只是SQL语句部分舍弃了limit设置（如果你设置了）
- 验证示例
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void countNoLimit() {
        int count = yourMapper.countNoLimit(new YourQuery()
            .where.id().lt(1000L)
            .and.name().like("Fluent").end()
            .limit(0, 10)
        );
        System.out.println(count);
    }
}
```
- 查看控制台输出
```text
DEBUG - ==>  Preparing: SELECT COUNT(*) FROM your_table WHERE id < ? AND name LIKE ?  
DEBUG - ==> Parameters: 1000(Long), %Fluent%(String) 
DEBUG - <==      Total: 1 
5
```
我们看到打印出的SQL语句和count方法相比，少了limit部分。
