# Fluent MyBatis进行批量更新

## 批量更新同一张表的数据
### 更新多条数据,每条数据都不一样
- 背景描述
通常需要一次更新多条数据有两个方式
1. 在业务代码中循环遍历，逐条更新
2. 一次性更新所有数据, 采用批量sql方式，一次执行。
```
    更准确的说是一条sql语句来更新所有数据，逐条更新的操作放到数据库端，在业务代码端展现的就是一次性更新所有数据。
```
    
这两种方式各有利弊，程序中for循环实现就不说了，这里主要介绍第二种方式在fluent mybatis中的实现，以及和mybatis实现的对比。

## java中for循环实现方式
```java
public class UpdateBatchTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void testBatchJavaEach() {
        /** 构造多个更新 **/
        List<IUpdate> updates = this.newListUpdater();
        for (IUpdate update : updates) {
            mapper.updateBy(update);
        }
    }
    
    /**
     * 构造多个更新操作
     */
    private List<IUpdate> newListUpdater() {
        StudentUpdate update1 = new StudentUpdate()
            .set.userName().is("user name23").end()
            .where.id().eq(23L).end();
        StudentUpdate update2 = new StudentUpdate()
            .set.userName().is("user name24").end()
            .where.id().eq(24L).end();
        return Arrays.asList(update1, update2);
    }
}
```

这种方式在大批量更新时, 最大的问题就是效率，逐条更新，每次都会连接数据库，然后更新，再释放连接资源。

## 一条SQL，服务端逐条更新
#### mybatis实现方式
通过mybatis提供的循环标签，一次构造多条update的sql，一次提交服务器进行执行。
```xml
<update id="updateStudentBatch"  parameterType="java.util.List">  
    <update id="updateStudentBatch"  parameterType="java.util.List">
        <foreach collection="list" item="item" index="index" open="" close="" separator=";">
            update student
            <set>
                user_name=#{item.userName}
            </set>
            where id = #{item.id}
        </foreach>
    </update>   
</update>
```
定义Mapper

```java
public interface StudentBatchMapper {
    void updateStudentBatch(List list);
}
```

执行测试验证

```java
public class UpdateBatchTest extends BaseTest {
    @Autowired
    private StudentBatchMapper batchMapper;

    @Test
    public void updateStudentBatch() {
        List<StudentEntity> students = Arrays.asList(
            new StudentEntity().setId(23L).setUserName("user name23"),
            new StudentEntity().setId(24L).setUserName("user name24"));
        batchMapper.updateStudentBatch(students);
        /** 验证SQL参数 **/
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user name23", "user name24")
        );
    }
}
```

#### 使用FluentMybatis实现方式
使用fluent mybatis进行批量更新很简单，只需要在#updateBy方法中传入 IUpdate数组即可

```java
public class UpdateBatchTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @DisplayName("批量更新同一张表")
    @Test
    public void testUpdateBatch_same() {
        IUpdate[] updates = this.newListUpdater().toArray(new IUpdate[0]);
        mapper.updateBy(updates);
        /** 验证SQL语句 **/
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE student SET gmt_modified = now(), user_name = ? WHERE id = ?; " +
                "UPDATE student SET gmt_modified = now(), user_name = ? WHERE id = ?"
            , StringMode.SameAsSpace);
        /** 验证SQL参数 **/
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user name23", "user name24")
        );
    }
}
```

```NOTE
要实现批量更新，首先得设置mysql支持批量操作，在jdbc url链接中附加&allowMultiQueries=true属性 
例如： 
jdbc:mysql://localhost:3306/testdb?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true
```

## 使用mysql的Case When then方式更新

```mysql
UPDATE student 
SET gmt_modified = now(),
address = case id when 1 then 'address 1' when 2 then 'address 2' when 3 then 'address 3' end
WHERE id in (1, 2, 3)
```
上面的sql语句使用mysql的case when then语法实现的批量更新3条记录，并且根据id的值不同，设置不同的address值。

#### mybatis原生实现方式
如果使用mybatis的xml语法来实现，xml文件就需要表达为下面方式:

- xml文件

```xml
<update id="updateBatchByIds" parameterType="list">
    update student
    <trim prefix="set" suffixOverrides=",">
        <trim prefix="address =case id" suffix="end,">
            <foreach collection="list" item="item" index="index">
                <if test="item.id!=null">
                    when #{item.id} then #{item.address}
                </if>
            </foreach>
        </trim>
    </trim>
    <trim prefix="age =case id" suffix="end,">
        <foreach collection="list" item="item" index="index">
            <if test="item.id!=null">
                when #{item.id} then #{item.age}
            </if>
        </foreach>
    </trim>
    where id in
    <foreach collection="list" item="item" index="index" separator="," open="(" close=")">
        #{item.id}
    </foreach>
</update>
```

- 定义Mapper
```java
public interface StudentBatchMapper {
    int updateBatchByIds(List<StudentEntity> list);
}
```

- 验证
```java
public class CaseFuncTest extends BaseTest {
    @Autowired
    private StudentBatchMapper batchMapper;

    @Test
    public void test_mybatis_batch() {
        batchMapper.updateBatchByIds(Arrays.asList(
            new StudentEntity().setId(1L).setAddress("address 1").setAge(23),
            new StudentEntity().setId(2L).setAddress("address 2").setAge(24),
            new StudentEntity().setId(3L).setAddress("address 3").setAge(25)
        ));
        /** 验证执行的SQL语句 **/
        db.sqlList().wantFirstSql().eq("" +
                "update student " +
                "set address =case id when ? then ? when ? then ? when ? then ? end, " +
                "age =case id when ? then ? when ? then ? when ? then ? end " +
                "where id in ( ? , ? , ? )"
            , StringMode.SameAsSpace);
    }
}
```

#### 使用Fluent Mybatis实现方式

```java
public class CaseFuncTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_fluentMybatisBatch() throws Exception {
        final String CaseWhen = "case id " +
            "when 1 then ? " +
            "when 2 then ? " +
            "else ? end";
        StudentUpdate update = new StudentUpdate()
            .set.address().applyFunc(CaseWhen, "address 1", "address 2", "address 3")
            .set.age().applyFunc(CaseWhen, 23, 24, 25)
            .end()
            .where.id().in(new int[]{1, 2, 3}).end();
        mapper.updateBy(update);
        /** 验证执行的SQL语句 **/
        db.sqlList().wantFirstSql()
            .eq("UPDATE student " +
                    "SET gmt_modified = now(), " +
                    "address = case id when 1 then ? when 2 then ? else ? end, " +
                    "age = case id when 1 then ? when 2 then ? else ? end " +
                    "WHERE id IN (?, ?, ?)",
                StringMode.SameAsSpace);
    }
}
```
只需要在applyFunc中传入case when语句，和对应的参数（对应case when语句中的预编译占位符'?')

如果业务入口传入的是Entity List或者Map List，可以使用java8的stream功能处理成数组，示例如下：
```java
public class CaseFuncTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_fluentMybatisBatch2() throws Exception {
        List<StudentEntity> students = Arrays.asList(
            new StudentEntity().setId(1L).setAddress("address 1").setAge(23),
            new StudentEntity().setId(2L).setAddress("address 2").setAge(24),
            new StudentEntity().setId(3L).setAddress("address 3").setAge(25));
        final String CaseWhen = "case id " +
            "when 1 then ? " +
            "when 2 then ? " +
            "else ? end";
        StudentUpdate update = new StudentUpdate()
            .set.address().applyFunc(CaseWhen, getFields(students, StudentEntity::getAddress))
            .set.age().applyFunc(CaseWhen, getFields(students, StudentEntity::getAge))
            .end()
            .where.id().in(getFields(students, StudentEntity::getId)).end();
        mapper.updateBy(update);
        // 验证SQL语句
        db.sqlList().wantFirstSql()
            .eq("UPDATE student " +
                    "SET gmt_modified = now(), " +
                    "address = case id when 1 then ? when 2 then ? else ? end, " +
                    "age = case id when 1 then ? when 2 then ? else ? end " +
                    "WHERE id IN (?, ?, ?)",
                StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara()
            .eqReflect(new Object[]{"address 1", "address 2", "address 3", 23, 24, 25, 1L, 2L, 3L});
    }

    private Object[] getFields(List<StudentEntity> students, Function<StudentEntity, Object> getField) {
        return students.stream().map(getField).toArray(Object[]::new);
    }
}
```

使用Fluent Mybatis无需额外编写xml文件和mapper（使用框架生成的Mapper文件就够了）。在业务逻辑上不至于因为有额外的xml文件，而产生割裂感。

## 批量更新不同的表数据
上面的例子使用mybatis和fluent mybatis演示的如果通过不同方法批量更新同一张表的数据，在fluent mybatis的更新其实不限定于同一张表，
在#updateBy(IUpdate... updates)函数可以传入任意表更新.

```java
public class UpdateBatchTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @DisplayName("批量更新不同表")
    @Test
    public void testUpdateBatch_different() {
        StudentUpdate update1 = new StudentUpdate()
            .set.userName().is("user name23").end()
            .where.id().eq(23L).end();
        HomeAddressUpdate update2 = new HomeAddressUpdate()
            .set.address().is("address 24").end()
            .where.id().eq(24L).end();
        
        /** 执行不同表的批量更新 **/
        mapper.updateBy(update1, update2);
    
        /** 验证实际执行的预编译SQL语句**/
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE student SET gmt_modified = now(), user_name = ? WHERE id = ?; " +
                "UPDATE home_address SET gmt_modified = now(), address = ? WHERE id = ?", StringMode.SameAsSpace);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user name23", "user")
        );
        /** 验证实际执行预编译SQL入参值 **/
        db.table(ATM.table.homeAddress).query().eqDataMap(ATM.dataMap.homeAddress.table(2)
            .id.values(23, 24)
            .address.values("address", "address 24")
        );
    }
}
```
示例更新了2张表: student 和 home_address

## 链接