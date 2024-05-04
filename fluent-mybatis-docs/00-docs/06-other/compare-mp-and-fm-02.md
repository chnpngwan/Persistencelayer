## Fluent Mybatis动态SQL构造的机理

Fluent Mybatis构造动态SQL语句的方式是直接使用mybatis3中的SQLProvider功能。

- @SelectProvider, @UpdateProvider, @InsertProvider, @DeleteProvider
这些Provider是声明在Mapper接口类上的, 比如你定义了一个YourEntity实体类, fluent mybatis在编译时，会生成对应的Mapper文件和SqlProvider文件

```java
@Mapper
@Component("yourMapper")
public interface YourMapper extends IEntityMapper<YourEntity> {
    @InsertProvider(
      type = YourSqlProvider.class,
      method = "insert"
    )
    int insert(YourEntity entity);

    @DeleteProvider(
      type = YourSqlProvider.class,
      method = "deleteById"
    )
    int deleteById(Serializable id);

    @UpdateProvider(
      type = YourSqlProvider.class,
      method = "updateBy"
    )
    int updateBy(@Param(Param_EW) IUpdate update);

    @SelectProvider(
      type = YourSqlProvider.class,
      method = "findOne"
    )
    @ResultMap(ResultMap)
    YourEntity findOne(@Param(Param_EW) IQuery query);
    
    // 省略 ... ...
}
```

1. insert的@InsertProvider, 声明了动态sql提供类是 YourSqlProvider 的 insert 同名方法

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
        if (entity.getSample() != null) {
            columns.add("your_sample_column");
            values.add("#{sample}");
        }
        sql.INSERT_COLUMNS(columns);
        sql.VALUES();
        sql.INSERT_VALUES(values);
        return sql.toString();
    }
    // 省略其他......
}
```
YourSqlProvider的insert方法中，通过判断entity的属性值是否为null，动态添加需要insert的列和值。

2. deleteById的@DeleteProvider, 声明了sql提供类是 YourSqlProvider 的deleteById同名方法
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

3. updateBy的@UpdateProvider, 声明了sql提供类是 YourSqlProvider 的updateBy同名方法
```java
public class YourSqlProvider {
    public String updateBy(Map<String, Object> map) {
        WrapperData data = getWrapperData(map, "ew");
        MapperSql sql = new MapperSql();
        Map<String, String> updates = data.getUpdates();
        assertNotEmpty("updates", updates);
        sql.UPDATE("your_table");
        // 省略 1. update set部分动态构造
        // 省略 2. update where条件部分动态构造
        return sql.toString();
    }
}
```

4. findOne方法的@SelectProvider, 声明了动态sql提供类是 YourSqlProvider 的 findOne 同名方法
```java
public class YourSqlProvider {
    public String findOne(Map map) {
        WrapperData data = getWrapperData(map, "ew");
        MapperSql sql = new MapperSql();
        // 设置要select的字段列表, 或者默认的全字段列表
        sql.SELECT("your_table", data, "id, your_sample_column");
        // 设置通过fluent api定义的查询条件, having by, group by, order by等条件
        sql.WHERE_GROUP_ORDER_BY(data);
        // 根据Entity @FluentMybatis注解上声明的数据库类型, 处理分页语法
        return byPaged(DbType.MYSQL, data, sql.toString());
    }
}
```

5. 上面分别挑了insert, update, select, delete方法各一个，说明了fluent mybatis是构造出动态sql语句的，其他的方法大家可以直接看生成的SqlProvider类。
如果大家在编码过程中碰到sql问题，也可以很方便的SqlProvider类上设置断点，进行跟踪debug处理。

- FluentMybatis动态SQL语句总结
简而言之, FluentMybatis只是对mybatis做了一些语法糖封装，没有对mybatis做一丝一毫的改变，完全是利用mybatis3自有的功能。
在应用中，只需要把编译生成的Mapper类加入到@MapperScan路径中就可以使用fluent mybatis封装的语法糖。

fluent mybatis封装的语法糖也只是提供了构造SQL的便利性，本身没有对mybatis做任何改造，也就不存在性能问题和不兼容问题，可以随意的升级mybatis版本。
也不会对你原来工作中写的mapper，或xml配置文件产生任何影响。

- 总结一下FluentMybatis和mybatis plus的区别

| - | Mybatis Plus | Fluent Mybatis |
| --- | --- | --- |
| 代码生成 | 生成 Entity, Mapper, Wrapper等文件 | 只生成Entity, 再通过编译生成 Mapper, Query, Update 和 SqlProvider |
| 和Mybatis的共生关系 | 需要替换原有的SqlSessionFactoryBean | 对Mybatis没有任何修改,原来怎么用还是怎么用 |
| 动态SQL构造方式 | 应用启动时, 根据Entity注解信息构造动态xml片段，注入到Mybatis解析器 | 应用编译时，根据Entity注解，编译生成对应方法的SqlProvider，利用mybatis的Mapper上@InsertProvider @SelectProvider @UpdateProvider注解关联 |
| 动态SQL结果是否容易DEBUG跟踪 | 不容易debug | 容易，直接定位到SQLProvider方法上，设置断点即可 |
| 动态SQL构造 | 通过硬编码字段名称, 或者利用Entity的get方法的lambda表达式 | 通过编译手段生成对应的方法名，直接调用方法即可 |
| 字段变更后的错误发现 | 通过get方法的lambda表达的可以编译发现，通过字段编码的无法编译发现 | 编译时便可发现 |
| 不同字段动态SQL构造方法 | 通过接口参数方式 | 通过接口名称方式, FluentAPI的编码效率更高 |
| 语法渲染特点 | 无 | 通过关键变量select, update, set, and, or可以利用IDE语法渲染, 可读性更高 | 

