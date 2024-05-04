## EntityMapper提供的insert操作
#### insert
单条插入操作
- Mapper方法

```java
public interface YourMapper extends IEntityMapper<YourEntity> {
  /**
   * 插入一条记录
   *
   * @param entity
   * @return
   */
  @Override
  @InsertProvider(
      type = YourSqlProvider.class,
      method = "insert"
  )
  @Options(
      useGeneratedKeys = true,
      keyProperty = "id",
      keyColumn = "id"
  )
  int insert(YourEntity entity);
}
```

- 动态SQL组装
```java
public class YourSqlProvider {
    public String insert(YourEntity entity) {
        assertNotNull("entity", entity);
        MapperSql sql = new MapperSql();
        sql.INSERT_INTO("your_table");
        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();
        if (entity.getId() != null) {
            columns.add("id");
            values.add("#{id}");
        }
        columns.add("gmt_create");
        if (entity.getGmtCreate() != null) {
            values.add("#{gmtCreate}");
        } else {
            values.add("now()");
        }
        columns.add("gmt_modified");
        if (entity.getGmtModified() != null) {
            values.add("#{gmtModified}");
        } else {
            values.add("now()");
        }
        columns.add("is_deleted");
        if (entity.getIsDeleted() != null) {
            values.add("#{isDeleted}");
        } else {
            values.add("0");
        }
        if (entity.getAge() != null) {
            columns.add("age");
            values.add("#{age}");
        }
        if (entity.getEmail() != null) {
            columns.add("email");
            values.add("#{email}");
        }
        if (entity.getName() != null) {
            columns.add("name");
            values.add("#{name}");
        }
        sql.INSERT_COLUMNS(columns);
        sql.VALUES();
        sql.INSERT_VALUES(values);
        return sql.toString();
    }
}
```
组装过程中，对对应了 @TableField(insert="默认值")的3个字段：gmt_crate, gmt_modified, is_deleted做了特殊判断。

- 编写insert test验证下
```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    void insert() {
        // 构造一个对象
        YourEntity entity = new YourEntity();
        entity.setName("Fluent Mybatis");
        entity.setAge(1);
        entity.setEmail("darui.wu@163.com");
        entity.setIsDeleted(false);
        // 插入操作
        int count = yourMapper.insert(entity);
        System.out.println("count:" + count);
        System.out.println("entity:" + entity);
    }
}
```

- 执行insert测试方法, 查看控制台输出log信息
```text
DEBUG - ==>  Preparing: INSERT INTO your_table(gmt_create, gmt_modified, is_deleted, age, email, name) VALUES (now(), now(), ?, ?, ?, ?)  
DEBUG - ==> Parameters: false(Boolean), 1(Integer), darui.wu@163.com(String), Fluent Mybatis(String) 
DEBUG - <==    Updates: 1 
count:1
entity:YourEntity(id=18, gmtCreate=null, gmtModified=null, isDeleted=false, age=1, email=darui.wu@163.com, name=Fluent Mybatis)
```

- 这里有几个需要注意的地方

1. Entity主键值的自增和回写
根据控制台输出，可以看到Entity的id属性已经是根据数据库自增主键值回写过的。
自增主键的设置是通过 @TableId 注解来的，其属性方法auto()默认值是true。
2. fluent mybatis根据@TableId注解生成的Mapper类上@Options注解如下：
``` java
@Options(
  useGeneratedKeys = true,
  keyProperty = "id",
  keyColumn = "id"
)
```
3. gmt_created, gmt_modified, is_deleted 默认值插入处理
我们先看一下Entity上这3个字段的@TableField注解, 他们都定义了一个属性方法insert，设置了insert的默认值（即程序编码insert时，如果没有设置该字段，则使用默认值）

``` java
    @TableField(value = "gmt_create", insert = "now()")
    private Date gmtCreate;

    @TableField(value = "gmt_modified", insert = "now()", update = "now()")
    private Date gmtModified;

    @TableField(value = "is_deleted", insert = "0")
    private Boolean isDeleted;
```

在测试例子中，gmt_created和gmt_modified在初始化Entity时，没有设置任何值; is_deleted设置了值false。
在构建sql是，gmt_created, gmt_modified直接使用默认值 "now()", is_deleted使用预编译变量(?)设置(实际值false)。

```sql
INSERT INTO your_table
(gmt_create, gmt_modified, is_deleted, age, email, name) 
VALUES 
(now(), now(), ?, ?, ?, ?)
```

我们再看一下对应的SQLProvider的SQL构造, 我们只看着3个字段的构造
```java
public class YourSqlProvider {
    public String insert(YourEntity entity) {
        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();
        // 省略 ... ...
        columns.add("gmt_create");
        if (entity.getGmtCreate() != null) {
            values.add("#{gmtCreate}");
        } else {
            values.add("now()");
        }
        columns.add("gmt_modified");
        if (entity.getGmtModified() != null) {
            values.add("#{gmtModified}");
        } else {
            values.add("now()");
        }
        columns.add("is_deleted");
        if (entity.getIsDeleted() != null) {
            values.add("#{isDeleted}");
        } else {
            values.add("0");
        }
        if (entity.getAge() != null) {
            columns.add("age");
            values.add("#{age}");
        }
        // 省略... ...
        return sql.toString();
    }
}
```

我们看到，没有 insert属性的字段，只判断了是否为空; 有insert属性的字段，如果entity不为空，则把默认值赋值给sql语句。

#### insertBatch
- 批量插入, 查看Mapper对应的SqlProvider中insertBatch动态SQL的构造
```java
public class YourSqlProvider {
    public String insertBatch(Map map) {
        assertNotEmpty("map", map);
        MapperSql sql = new MapperSql();
        List<YourEntity> entities = getParas(map, "list");
        sql.INSERT_INTO("your_table");
        sql.INSERT_COLUMNS(ALL_ENTITY_FIELDS);
        sql.VALUES();
        for (int index = 0; index < entities.size(); index++) {
            if (index > 0) {
                sql.APPEND(", ");
            }
            sql.INSERT_VALUES(
                "#{list[" + index + "].id}",
                entities.get(index).getGmtCreate() == null ? "now()" : "#{list[" + index + "].gmtCreate}",
                entities.get(index).getGmtModified() == null ? "now()" : "#{list[" + index + "].gmtModified}",
                entities.get(index).getIsDeleted() == null ? "0" : "#{list[" + index + "].isDeleted}",
                "#{list[" + index + "].age}",
                "#{list[" + index + "].email}",
                "#{list[" + index + "].name}"
            );
        }
        return sql.toString();
    }
}
```
SQL构造语句是通过一个for循环遍历实体列表，构造出下列SQL语句, 其中对有insert默认值属性处理方式同单条insert一样, 这里就不再重复。
```sql
INSERT INTO your_table ('Entity对应的字段列表') VALUES ('实例1值'), ('实例2值')
```
- 写个测试看看具体效果

```java
@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;
    
    void insertBatch(){
        List<YourEntity> entities = new ArrayList<>();
        entities.add(new YourEntity().setName("Fluent Mybatis").setEmail("darui.wu@163.com"));
        entities.add(new YourEntity().setName("Fluent Mybatis Demo").setEmail("darui.wu@163.com"));
        entities.add(new YourEntity().setName("Test4J").setEmail("darui.wu@163.com"));
        int count = yourMapper.insertBatch(entities);
        System.out.println("count:" + count);
        System.out.println("entity:" + entities);
    }
}
```

- 执行测试，查看控制台输出

```text
DEBUG - ==>  Preparing: INSERT INTO your_table(id, gmt_create, gmt_modified, is_deleted, age, email, name) VALUES (?, now(), now(), 0, ?, ?, ?) , (?, now(), now(), 0, ?, ?, ?) , (?, now(), now(), 0, ?, ?, ?)  
DEBUG - ==> Parameters: null, null, darui.wu@163.com(String), Fluent Mybatis(String), null, null, darui.wu@163.com(String), Fluent Mybatis Demo(String), null, null, darui.wu@163.com(String), Test4J(String) 
DEBUG - <==    Updates: 3 
count:3
entity:[YourEntity(id=null, gmtCreate=null, gmtModified=null, isDeleted=null, age=null, email=darui.wu@163.com, name=Fluent Mybatis), YourEntity(id=null, gmtCreate=null, gmtModified=null, isDeleted=null, age=null, email=darui.wu@163.com, name=Fluent Mybatis Demo), YourEntity(id=null, gmtCreate=null, gmtModified=null, isDeleted=null, age=null, email=darui.wu@163.com, name=Test4J)]
```