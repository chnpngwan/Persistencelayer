# 什么是环境隔离和多租户隔离

我们在实际的业务开发中，经常会碰到环境逻辑隔离和租户数据逻辑隔离的问题。

- 环境隔离
我们的开发系统过程中，经常会涉及到日常开发环境，测试环境，预发环境和线上环境，如何区隔这些环境，有些方案是采用独立的数据库，有些是采用同一套数据库（比如线下多个测试环境使用同一个数据库，预发环境和线上环境使用同一个数据库），然后对数据进行打标的办法，来区分不同环境的数据。

- 多租户管理
在复杂的业务系统中，比如SaaS应用中，在多用户环境下共用相同的系统或程序组件，如何确保各用户间数据的隔离性。简单讲：在一台服务器上运行单个应用实例，它为多个租户（客户）提供服务。从定义中我们可以理解：多租户是一种架构，是为了让多用户环境下使用同一套程序，但要保证用户间数据隔离。那如何进行多租户的重点就是同一套程序下实现多用户数据的隔离，做法其实和环境隔离是同一个道理。

这里采用多环境多租户共用数据表的场景，来探讨下FluentMybatis是如何支持多环境和多租户管理的。

# 环境隔离和多租户隔离需要做的事情
比如我们有下面表
```sql
create table student
(
    id              bigint(21) unsigned auto_increment comment '主键id'
        primary key,
    age             int                  null comment '年龄',
    grade           int                  null comment '年级',
    user_name       varchar(45)          null comment '名字',
    gender_man      tinyint(2) default 0 null comment '性别, 0:女; 1:男',
    birthday        datetime             null comment '生日',
    phone           varchar(20)          null comment '电话',
    bonus_points    bigint(21) default 0 null comment '积分',
    status          varchar(32)          null comment '状态(字典)',
    home_county_id  bigint(21)           null comment '家庭所在区县',
    home_address_id bigint(21)           null comment 'home_address外键',
    address         varchar(200)         null comment '家庭详细住址',
    version         varchar(200)         null comment '版本号',
    env             varchar(10)          NULL comment '数据隔离环境',
    tenant          bigint               NOT NULL default 0 comment '租户标识',
    gmt_created     datetime             null comment '创建时间',
    gmt_modified    datetime             null comment '更新时间',
    is_deleted      tinyint(2) default 0 null comment '是否逻辑删除'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
    COMMENT '学生信息表';
```
注意其中的2个字段

1. env, 表示应用部署的环境， 环境的区隔一般是采用应用部署的机器环境变量。
2. tenant, 表示数据所属租户，租户的隔离一般是通过登录用户信息获取的。

对环境和租户的隔离，主要是CRUD过程中，需要带上环境变量和租户信息。如果没有框架的支持，就需要在构造SQL的过程中，手动设置env和tenant。这就存在一个严重的弊端: 在编码过程中，需要时刻注意sql语句中不要漏了这2个条件，否则就会产生逻辑错误和信息泄露。

为了减少错误，我们都会将逻辑进行收拢，下面我们演示fluent mybatis如何统一处理。

# 环境隔离和租户隔离工具类
为了进行环境隔离和租户隔离，我们一般会统一定义获取环境变量和租户信息的工具类。

- 环境隔离工具类
```java
/**
 * 应用部署环境工具类
 */
public class EnvUtils {
    public static String currEnv() {
        // 应用启动时, 读取的机器部署环境变量, 这里简化为返回固定值演示
        return "test1";
    }
}
```

- 租户隔离工具类
```java
/**
 * 获取用户所属租户信息工具类
 */
public class TenantUtils {
    /**
     * 租户A
     */
    static final long A_TENANT = 111111L;
    /**
     * 租户B
     */
    static final long B_TENANT = 222222L;

    /**
     * 租户信息一般根据登录用户身份来判断, 这里简化为偶数用户属于租户A, 奇数用户属于租户B
     *
     * @return
     */
    public static long findUserTenant() {
        long userId = loginUserId();
        if (userId % 2 == 0) {
            return A_TENANT;
        } else {
            return B_TENANT;
        }
    }

    /**
     * 当前登录的用户id, 一般从Session中获取
     *
     * @return
     */
    public static long loginUserId() {
        return 1L;
    }
}
```

# 隔离前准备工作

- Entity隔离属性基类
为了方便对所有需要隔离的Entity进行统一的环境和租户信息的设置和读取，我们把Entity的环境和租户的属性的getter和setter方法定义到一个接口上。

```java
/**
 * Entity类隔离属性基类
 */
public interface IsolateEntity {
    /**
     * 返回entity env属性值
     *
     * @return
     */
    String getEnv();

    /**
     * 设置entity env属性值
     *
     * @param env
     * @return
     */
    IsolateEntity setEnv(String env);

    /**
     * 返回entity 租户信息
     *
     * @return
     */
    Long getTenant();

    /**
     * 设置entity 租户信息
     *
     * @param tenant
     * @return
     */
    IsolateEntity setTenant(Long tenant);
}
```
这样所有需要隔离的Entity只要继承这个接口就可以在需要隔离操作的地方把具体的entity当作IsolateEntity对象来操作。

- 隔离属性和默认条件设置

有了统一的接口，我们还需要一个默认进行设置的操作，fluent mybatis提供了一个IDefaultSetter 接口，可以对Entity，Query和Update进行拦截操作。

```java
/**
 * 增删改查中，环境和租户隔离设置
 */
public interface IsolateSetter extends IDefaultSetter {
    /**
     * 插入的entity,如果没有显式设置环境和租户，根据工具类进行默认设置
     *
     * @param entity
     */
    @Override
    default void setInsertDefault(IEntity entity) {
        IsolateEntity isolateEntity = (IsolateEntity) entity;
        if (isolateEntity.getEnv() == null) {
            isolateEntity.setEnv(EnvUtils.currEnv());
        }
        if (isolateEntity.getTenant() == null) {
            isolateEntity.setTenant(TenantUtils.findUserTenant());
        }
    }

    /**
     * 查询条件追加环境隔离和租户隔离
     *
     * @param query
     */
    @Override
    default void setQueryDefault(IQuery query) {
        query.where()
            .apply("env", SqlOp.EQ, EnvUtils.currEnv())
            .apply("tenant", SqlOp.EQ, TenantUtils.findUserTenant());
    }

    /**
     * 更新条件追加环境隔离和租户隔离
     *
     * @param updater
     */
    @Override
    default void setUpdateDefault(IUpdate updater) {
        updater.where()
            .apply("env", SqlOp.EQ, EnvUtils.currEnv())
            .apply("tenant", SqlOp.EQ, TenantUtils.findUserTenant());
    }
}
```
为了避免使用不当导致线程安全问题(变量共享), fluent mybatis只允许在应用中定义接口（比如这里的IsolateSetter）继承IDefaultSetter, 不允许定义成类。

- 代码生成设置

怎么让fluent mybatis识别到哪些Entity可以继承IsolateEntity，哪些Entity操作需要进行IsolateSetter统一拦截呢？

在@FluentMybatis上有个属性defaults(), 我们把defaults值设置为 IsolateSetter.class就可以了。

```java
public @interface FluentMybatis {
    /**
     * entity, query, updater默认值设置实现
     *
     * @return
     */
    Class<? extends IDefaultSetter> defaults() default IDefaultSetter.class;
}
```

当然，我们并不需要手动去修改Entity类，只需要在代码生成上设置。
```java
public class FluentGenerateMain {
    static final String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useSSL=false&useUnicode=true&characterEncoding=utf-8";
    /**
     * 生成代码的package路径
     */
    static final String basePackage = "cn.org.fluent.mybatis.many2many.demo";

    public static void main(String[] args) {
        FileGenerator.build(Noting.class);
    }

    @Tables(
        /** 数据库连接信息 **/
        url = url, username = "root", password = "password",
        /** Entity类parent package路径 **/
        basePack = basePackage,
        /** Entity代码源目录 **/
        srcDir = "example/many2many_demo/src/main/java",
        /** 如果表定义记录创建，记录修改，逻辑删除字段 **/
        gmtCreated = "gmt_created", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        /** 需要生成文件的表 ( 表名称:对应的Entity名称 ) **/
        tables = @Table(value = {"student"},
            entity = IsolateEntity.class,
            defaults = IsolateSetter.class)
    )
    static class Noting {
    }
}
```
注意，对比之前的代码生成，@Table上多了2个属性设置
``` java
// 标识对应的Entity类需要继承的接口
entity = IsolateEntity.class        
```

``` java
// 标识对应的Entity类CRUD过程中需要进行的默认设置操作
defaults = IsolateSetter.class
```
执行代码生成，Entity代码如下：
```java
@FluentMybatis(
    table = "student",
    defaults = IsolateSetter.class
)
public class StudentEntity extends RichEntity implements IsolateEntity {
    // ... 省略
}
```
我们看到@FluentMybatis设置了defaults属性，Entity类继承了IsolateEntity接口。

接下来，我们进行具体的增删改查演示。

# 增删改查环境和租户隔离演示
## 新增数据
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppMain.class)
public class InsertWithEnvDemo {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void insertEntity() {
        mapper.delete(new StudentQuery());
        mapper.insert(new StudentEntity()
            .setAddress("宇宙深处")
            .setUserName("FluentMybatis")
        );
        StudentEntity student = mapper.findOne(StudentQuery.query()
            .where.userName().eq("FluentMybatis").end()
            .limit(1));
        System.out.println(student.getUserName() + ", env:" + student.getEnv() + ", tenant:" + student.getTenant());
    }
}
```
查看控制台输出log
```text
DEBUG - ==>  Preparing: 
    INSERT INTO student(gmt_created, gmt_modified, is_deleted, address, env, tenant, user_name) 
    VALUES (now(), now(), 0, ?, ?, ?, ?)  
DEBUG - ==> Parameters: 宇宙深处(String), test1(String), 222222(Long), FluentMybatis(String) 
DEBUG - <==    Updates: 1 
DEBUG - ==>  Preparing: SELECT id, gmt_created, gmt_modified, is_deleted, address, age, birthday, bonus_points, env, gender_man, grade, home_address_id, home_county_id, phone, status, tenant, user_name, version 
    FROM student WHERE user_name = ? LIMIT ?, ?  
DEBUG - ==> Parameters: FluentMybatis(String), 0(Integer), 1(Integer) 
DEBUG - <==      Total: 1 
FluentMybatis, env:test1, tenant:222222
```
在演示例子中，我们虽然只显式设置了userName和address2个属性，但插入数据中设置了7个属性，其中包括env和tenant。

注意，这里的查询条件并没有带上环境变量

## 查询数据
fluent mybatis提供了2种构造查询器的方式
1. XyzQuery.query(): 全新的不带任何条件的查询。
2. XyzQuery.defaultQuery(): 按照@FluentMybatis defaults属性指定的接口，设置好默认查询条件。

上面默认插入的例子已经演示了不带条件的query()查询，我们现在演示下设置了默认条件的查询。
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppMain.class)
public class QueryWithEnvDemo {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void testQueryWithEnv(){
        mapper.delete(new StudentQuery());
        mapper.insert(new StudentEntity()
            .setAddress("宇宙深处")
            .setUserName("FluentMybatis")
        );
        StudentEntity student = mapper.findOne(mapper.defaultQuery()
            .where.userName().eq("FluentMybatis").end()
            .limit(1));
        System.out.println(student.getUserName() + ", env:" + student.getEnv() + ", tenant:" + student.getTenant());
    }
}
```

查看控制log输出
```text
DEBUG - ==>  Preparing: SELECT id, gmt_created, ... , tenant, user_name, version 
    FROM student 
    WHERE env = ? 
    AND tenant = ? 
    AND user_name = ? 
    LIMIT ?, ?  
DEBUG - ==> Parameters: test1(String), 222222(Long), FluentMybatis(String), 0(Integer), 1(Integer) 
DEBUG - <==      Total: 1 
FluentMybatis, env:test1, tenant:222222
```
我们看到，查询条件中除了有我们设置好的user_name，还包括在IsolateSetter接口中设置好的env和tenant字段。

## 更新数据
和Query一样，Updater同样提供了2个方法来构造Updater
1. XyzUpdate.updater() : 不带任何条件的更新。
2. XyzUpdate.defaultUpdater(): 根据IsolateSetter#setUpdateDefault方法设置好更新条件。

演示例子
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppMain.class)
public class UpdateWithEnvDemo {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void testQueryWithEnv() {
        mapper.delete(new StudentQuery());
        mapper.insert(new StudentEntity()
            .setAddress("宇宙深处")
            .setUserName("FluentMybatis")
        );
        mapper.updateBy(StudentUpdate.defaultUpdater()
            .set.address().is("回到地球").end()
            .where.userName().eq("FluentMybatis").end()
        );
    }
}
```
查看控制台log输出
```text
DEBUG - ==>  Preparing: UPDATE student 
    SET gmt_modified = now(), address = ? 
    WHERE env = ? 
    AND tenant = ? 
    AND user_name = ?  
DEBUG - ==> Parameters: 回到地球(String), test1(String), 222222(Long), FluentMybatis(String) 
DEBUG - <==    Updates: 1 
```
更新条件中自动带上了设置好的默认条件 env 和 tenant。

# 总结
Fluent Mybatis通过自定义接口继承IDefaultSetter，赋予了你进行数据隔离操作的强大功能。默认值的赋值是通过编译生成的XyzDefaults类来进行的，大家可以具体查看编译生成的代码。
