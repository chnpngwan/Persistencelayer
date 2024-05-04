## 表和Entity映射设置
在HelloWorld中，我们演示了如何定义一个Entity和数据库表关联起来，并且加上注解 @FluentMybatis 就获得了强大的增删改查的能力。
但如何解决下面几个问题呢？
1. 如何在FluentMybatis中体现数据库的主键？ 
2. 如果数据库字段和entity属性不符合下划线命名到驼峰命名规则转换，改怎么办。

### @TableId注解
在MyBatis中，数据库主键的映射主要体现在插入的时候，比如下面的xml映射：
```xml
<mapper namespace="x,z.z.AbcEntity">
<!-- keyProperty (可选配置, 仅对 insert 和 update 有用）唯一标记一个属性
MyBatis 会通过 getGeneratedKeys 的返回值或者通过 insert 语句的 selectKey 子元素设置它的键值 -->       
<!-- keyColumn (可选配置,仅对 insert 和 update 有用）通过生成的键值设置表中的列名。 -->
<!-- useGeneratedKeys (可选配置， 默认为false, 仅对 insert 和 update 有用）
MyBatis 使用 JDBC 的 getGeneratedKeys 方法来取出由数据库内部生成的主键, 默认值：false。  -->
<insert id="insert" keyProperty="id" keyColumn="id" useGeneratedKeys="true">
    <!--具体insert语句-->
</insert>
</mapper>
```
或者，在Mapper接口上的注解说明
``` java
@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
```
在MyBatis中，你并没有写任何xml文件或者自定义Mapper接口，如何实现上面原生的语法功能呢？

So Easy,你只需要在Entity类的主键字段上加上注解 @TableId,就实现了上述的编码功能。
```java
@FluentMybatis
public class HelloWorldEntity implements IEntity {
    /**
     * 根据@TableId, MyBatis在生成Mapper接口insert方法时, 会自动设置相关属性
     */
    @TableId
    private Long id;

    private String sayHello;

    private String yourName;
    
    // 其他字段 + get, set, toString等方法

    @Override
    public Class<? extends IEntity> entityClass() {
        return HelloWorld1Entity.class;
    }
}
```
下面是mybatis编译生成的Mapper文件中insert方法注解
```java
@Mapper
@Qualifier("helloWorldMapper")
public interface HelloWorldMapper extends IEntityMapper<HelloWorldEntity> {
  String ResultMap = "HelloWorldEntityResultMap";

  @Override
  @InsertProvider(
      type = HelloWorldSqlProvider.class,
      method = "insert"
  )
  @Options(
      useGeneratedKeys = true,
      keyProperty = "id",
      keyColumn = "id"
  )
  int insert(HelloWorldEntity entity);
    
  // 其它方法
}
```
写一个测试来验证一下 @TableId的功能
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HelloWorldConfig.class)
public class TableIdTest {
    @Autowired
    HelloWorldMapper mapper;

    @Test
    public void testInsert() {
        /**
         * 为了演示方便，先删除数据
         */
        mapper.delete(mapper.query()
            .where.sayHello().eq("hello world").end());
        /**
         * 插入数据
         */
        HelloWorldEntity entity = new HelloWorldEntity();
        /** 不设置id的值**/
        entity.setId(null);
        entity.setSayHello("hello world");
        entity.setYourName("fluent mybatis");
        entity.setIsDeleted(false);
        mapper.insert(entity);

        HelloWorldEntity entity1 = mapper.findOne(mapper.query()
            .where.sayHello().eq("hello world").end());
        /**
         * 这里entity的id已经根据数据库自增id赋值了
         */
        System.out.println("entity id:" + entity1.getId());
        /**
         * 我们也可以根据主键直接查询数据
         */
        HelloWorldEntity entity2 = mapper.findById(entity1.getId());
        System.out.println("entity:" + entity2.toString());
    }
}
```
查看一下控制台输出:
```text
entity id:2
entity:HelloWorldEntity{id=2, sayHello='hello world', yourName='fluent mybatis', gmtCreate=null, gmtModified=null, isDeleted=false}
```
这里id的值，是根据你数据库中id自增序列赋值的。

ok，我们已经解决了主键映射的问题，现在来看一下Entity属性和表字段名称不一致的情况。

### @TableField注解
如果Entity的属性名称和数据库的字段名称不符合下划线到驼峰命名的规则，可以使用@TableField显式声明
如下，我们把hello_world字段在Entity里声明为sayHi，your_name字段在Entity里声明为fullName
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HelloWorldConfig.class)
public class TableFieldTest {
    @Autowired
    HelloWorldMapper mapper;

    @Test
    public void testInsert() {
        mapper.delete(mapper.query()
            .where.sayHi().like("hi").end());

        HelloWorldEntity entity = new HelloWorldEntity();
        entity.setSayHi("hi: fluent mybatis");
        entity.setFullName("fluent mybatis");
        mapper.insert(entity);

        HelloWorldEntity result = mapper.findOne(mapper.query()
            .where.sayHi().likeRight("fluent mybatis")
            .and.fullName().eq("fluent mybatis").end());

        System.out.println("entity:" + result.toString());
    }
}
```

#### 设置字段的insert或update时默认值
某些字段，希望在插入数据时，如果没有显式的指定值，希望给插入一个默认值。比如记录创建时间，或者逻辑删除标识.
默认值的行为，可以通过早期在数据库表初始化定义上处理，也可以通过fluent mybatis进行统一管理。

- 插入的默认值在 @TableField的insert属性上声明
- 更新的默认值在 @TableField的update属性上声明

示例
```java
@FluentMybatis
public class HelloWorldEntity implements IEntity {
    @TableId
    private Long id;

    private String sayHello;

    private String yourName;

    @TableField(insert = "now()")
    private Date gmtCreate;

    @TableField(insert = "now()", update = "now()")
    private Date gmtModified;

    @TableField(insert = "0")
    private Boolean isDeleted;
}
```
写测试验证

```java
    @Test
    public void testInsertDefaultValue() {
        // 全表清空
        mapper.delete(mapper.query().where.apply("1=1").end());
        // 插入记录，未设置gmtCreated, gmtModified, isDeleted3个字段
        mapper.insert(new HelloWorldEntity()
            .setSayHello("hello")
            .setYourName("fluent mybatis")
        );
        // 查询并在控制台输出记录
        HelloWorldEntity entity = mapper.findOne(mapper.query()
            .where.sayHello().eq("hello")
            .and.yourName().eq("fluent mybatis").end()
        );
        System.out.println(entity);
    }
```
查看控制台输出log，可以看到插入语句，以及具体的对象输出

```shell
Preparing: INSERT INTO hello_world(say_hello, your_name, gmt_create, gmt_modified, is_deleted) VALUES (?, ?, now(), now(), 0)  
2517 [main] DEBUG c.o.a.f.m.d.e.m.H.insert - ==> Parameters: hello(String), fluent mybatis(String) 
2518 [main] DEBUG c.o.a.f.m.d.e.m.H.insert - <==    Updates: 1 

HelloWorldEntity(id=7, sayHello=hello, yourName=fluent mybatis, gmtCreate=Fri Oct 09 22:35:06 CST 2020, gmtModified=Fri Oct 09 22:35:06 CST 2020, isDeleted=false)
```

### 在@FluentMybatis主键属性中显式声明对应的表
Entity名称和表名称也遵循驼峰命名到下划线命名的转换，如果需要显式指定，可以如下设置
```java
// table = "显式指定的表名称"
@FluentMybatis(table = "hello_world")
public class HelloWorldEntity implements IEntity {
    
}
```

上面演示了Entity类如何使用Fluent Mybatis的功能, 
下面我们介绍一下如何用fluent mybatis生成Entity实体类文件

[Fluent Mybatis生成Entity文件](https://gitee.com/fluent-mybatis/fluent-mybatis-docs/blob/master/03-entity-generator/README.md
)

