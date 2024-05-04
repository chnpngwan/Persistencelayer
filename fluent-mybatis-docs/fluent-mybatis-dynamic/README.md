# Fluent MyBatis动态SQL，写SQL更爽
MyBatis 令人喜欢的一大特性就是动态 SQL。在使用 JDBC 的过程中， 根据条件进行 SQL 的拼接是很麻烦且很容易出错的,
MyBatis虽然提供了动态拼装的能力，但这些写xml文件，也确实折磨开发。Fluent MyBatis提供了更贴合Java语言特质的，
对程序员友好的Fluent拼装能力。

## 数据准备
为了后面的演示， 创建了一个 Maven 项目 fluent-mybatis-dynamic, 创建了对应的数据库和表

```sql
DROP TABLE IF EXISTS `student`;

CREATE TABLE `student`
(
    `id`           bigint(21) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name`         varchar(20) DEFAULT NULL COMMENT '姓名',
    `phone`        varchar(20) DEFAULT NULL COMMENT '电话',
    `email`        varchar(50) DEFAULT NULL COMMENT '邮箱',
    `gender`       tinyint(2)  DEFAULT NULL COMMENT '性别',
    `locked`       tinyint(2)  DEFAULT NULL COMMENT '状态(0:正常,1:锁定)',
    `gmt_created`  datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '存入数据库的时间',
    `gmt_modified` datetime    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改的时间',
    `is_deleted`   tinyint(2)  DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='学生表';
```

## 代码生成
使用Fluent Mybatis代码生成器，生成对应的Entity文件

```java
public class Generator {
    static final String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useSSL=false&useUnicode=true&characterEncoding=utf-8";
    /**
     * 生成代码的package路径
     */
    static final String basePackage = "cn.org.fluent.mybatis.dynamic";

    /**
     * 使用 test/resource/init.sql文件自动初始化测试数据库
     */
    @BeforeAll
    static void runDbScript() {
        DataSourceCreatorFactory.create("dataSource");
    }

    @Test
    void test() {
        FileGenerator.build(Nothing.class);
    }

    @Tables(
        /** 数据库连接信息 **/
        url = url, username = "root", password = "password",
        /** Entity类parent package路径 **/
        basePack = basePackage,
        /** Entity代码源目录 **/
        srcDir = "src/main/java",
        /** 如果表定义记录创建，记录修改，逻辑删除字段 **/
        gmtCreated = "gmt_created", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        /** 需要生成文件的表 ( 表名称:对应的Entity名称 ) **/
        tables = @Table(value = {"student"})
    )

    public static class Nothing {
    }
}
```

编译项目，ok，下面我们开始动态SQL构造旅程

### 在 WHERE 条件中使用动态条件
在mybatis中，if 标签是大家最常使用的。在查询、删除、更新的时候结合 test 属性联合使用。

- 示例：根据输入的学生信息进行条件检索
    1. 当只输入用户名时， 使用用户名进行模糊检索；
    2. 当只输入性别时， 使用性别进行完全匹配
    3. 当用户名和性别都存在时， 用这两个条件进行查询匹配查询
    
- mybatis动态 SQL写法

```xml
<select id="selectByStudentSelective" resultMap="BaseResultMap" parameterType="com.homejim.mybatis.entity.Student">
    select
    <include refid="Base_Column_List" />
    from student
    <where>
        <if test="name != null and name !=''">
          and name like concat('%', #{name}, '%')
        </if>
        <if test="sex != null">
          and sex=#{sex}
        </if>
    </where>
</select>
```

- fluent mybatis动态写法

```java
@Repository
public class StudentDaoImpl extends StudentBaseDao implements StudentDao {
   /**
    * 根据输入的学生信息进行条件检索
    * 1. 当只输入用户名时， 使用用户名进行模糊检索；
    * 2. 当只输入性别时， 使用性别进行完全匹配
    * 3. 当用户名和性别都存在时， 用这两个条件进行查询匹配的用
    *
    * @param name   姓名,模糊匹配
    * @param isMale 性别
    * @return
    */
    @Override
    public List<StudentEntity> selectByNameOrEmail(String name, Boolean isMale) {
        return super.defaultQuery()
            .where.name().like(name, If::notBlank)
            .and.gender().eq(isMale, If::notNull).end()
            .execute(super::listEntity);
    }
}
```

FluentMyBatis的实现方式至少有下面的好处
1. 逻辑就在方法实现上，不需要额外维护xml，割裂开来
2. 所有的编码通过IDE智能提示，没有字符串魔法值编码
3. 编译检查，拼写错误能立即发现

- 测试

```java
@SpringBootTest(classes = AppMain.class)
public class StudentDaoImplTest extends Test4J {
    @Autowired
    StudentDao studentDao;

    @DisplayName("只有名字时的查询")
    @Test
    void selectByNameOrEmail_onlyName() {
        studentDao.selectByNameOrEmail("明", null);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "SELECT id, gmt_created, gmt_modified, is_deleted, email, gender, locked, name, phone " +
                "FROM student " +
                "WHERE name LIKE ?",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"%明%"});
    }

    @DisplayName("只有性别时的查询")
    @Test
    void selectByNameOrEmail_onlyGender() {
        studentDao.selectByNameOrEmail(null, false);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "SELECT id, gmt_created, gmt_modified, is_deleted, email, gender, locked, name, phone " +
                "FROM student " +
                "WHERE gender = ?",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{false});
    }

    @DisplayName("姓名和性别同时存在的查询")
    @Test
    void selectByNameOrEmail_both() {
        studentDao.selectByNameOrEmail("明", false);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "SELECT id, gmt_created, gmt_modified, is_deleted, email, gender, locked, name, phone " +
                "FROM student " +
                "WHERE name LIKE ? " +
                "AND gender = ?",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"%明%", false});
    }
}
```

### 在 UPDATE 使用动态更新
只更新有变化的字段， 空值不更新

- mybatis xml写法

```xml
<update id="updateByPrimaryKeySelective" parameterType="...">
    update student
    <set>
      <if test="name != null">
        `name` = #{name,jdbcType=VARCHAR},
      </if>
      <if test="phone != null">
        phone = #{phone,jdbcType=VARCHAR},
      </if>
      <if test="email != null">
        email = #{email,jdbcType=VARCHAR},
      </if>
      <if test="gender != null">
        gender = #{gender,jdbcType=TINYINT},
      </if>
      <if test="gmtModified != null">
        gmt_modified = #{gmtModified,jdbcType=TIMESTAMP},
      </if>
    </set>
    where id = #{id,jdbcType=INTEGER}
</update>
```

- fluent mybatis实现

```java
@Repository
public class StudentDaoImpl extends StudentBaseDao implements StudentDao {
    /**
     * 根据主键更新非空属性
     *
     * @param student
     * @return
     */
    @Override
    public int updateByPrimaryKeySelective(StudentEntity student) {
        return super.defaultUpdater()
            .set.name().is(student.getName(), If::notBlank)
            .set.phone().is(student.getPhone(), If::notBlank)
            .set.email().is(student.getEmail(), If::notBlank)
            .set.gender().is(student.getGender(), If::notNull)
            .end()
            .where.id().eq(student.getId()).end()
            .execute(super::updateBy);
    }    
}
```

- 测试

```java
@SpringBootTest(classes = AppMain.class)
public class StudentDaoImplTest extends Test4J {
    @Autowired
    StudentDao studentDao;

    @Test
    void updateByPrimaryKeySelective() {
        StudentEntity student = new StudentEntity()
            .setId(1L)
            .setName("test")
            .setPhone("13866668888");
        studentDao.updateByPrimaryKeySelective(student);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE student " +
                "SET gmt_modified = now(), " +
                "name = ?, " +
                "phone = ? " +
                "WHERE id = ?",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"test", "13866668888", 1L});
    }
}
```

### choose 标签
在mybatis中choose when otherwise 标签可以帮我们实现 if else 的逻辑。

- 查询条件，假设 name 具有唯一性， 查询一个学生
    1. 当 id 有值时， 使用 id 进行查询；
    2. 当 id 没有值时， 使用 name 进行查询；
    3. 否则返回空

- mybatis xml实现

```xml
<select id="selectByIdOrName" resultMap="BaseResultMap" parameterType="...">
    select
    <include refid="Base_Column_List" />
    from student
    <where>
        <choose>
          <when test="id != null">
            and id=#{id}
          </when>
          <when test="name != null and name != ''">
            and name=#{name}
          </when>
          <otherwise>
            and 1=2
          </otherwise>
        </choose>
    </where>
</select>
```

- fluent mybatis实现方式

```java
@Repository
public class StudentDaoImpl extends StudentBaseDao implements StudentDao {

    /**
     * 1. 当 id 有值时， 使用 id 进行查询；
     * 2. 当 id 没有值时， 使用 name 进行查询；
     * 3. 否则返回空
     */ 
    @Override
    public StudentEntity selectByIdOrName(StudentEntity student) {
       return super.defaultQuery()
           .where.id().eq(student.getId(), If::notNull)
           .and.name().eq(student.getName(), name -> isNull(student.getId()) && notBlank(name))
           .and.apply("1=2", () -> isNull(student.getId()) && isBlank(student.getName()))
           .end()
           .execute(super::findOne).orElse(null);
    }
}
```

- 测试

```java
@SpringBootTest(classes = AppMain.class)
public class StudentDaoImplTest extends Test4J {
    @Autowired
    StudentDao studentDao;

    @DisplayName("有 ID 则根据 ID 获取")
    @Test
    void selectByIdOrName_byId() {
        StudentEntity student = new StudentEntity();
        student.setName("小飞机");
        student.setId(1L);

        StudentEntity result = studentDao.selectByIdOrName(student);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "SELECT id, gmt_created, gmt_modified, is_deleted, email, gender, locked, name, phone " +
                "FROM student " +
                "WHERE id = ?",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{1L});
    }

    @DisplayName("没有 ID 则根据 name 获取")
    @Test
    void selectByIdOrName_byName() {
        StudentEntity student = new StudentEntity();
        student.setName("小飞机");
        student.setId(null);

        StudentEntity result = studentDao.selectByIdOrName(student);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "SELECT id, gmt_created, gmt_modified, is_deleted, email, gender, locked, name, phone " +
                "FROM student " +
                "WHERE name = ?",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"小飞机"});
    }

    @DisplayName("没有 ID 和 name, 返回 null")
    @Test
    void selectByIdOrName_null() {
        StudentEntity student = new StudentEntity();
        StudentEntity result = studentDao.selectByIdOrName(student);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "SELECT id, gmt_created, gmt_modified, is_deleted, email, gender, locked, name, phone " +
                "FROM student " +
                "WHERE 1=2",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{});
    }
}
```

## 参考
[示例代码地址](https://gitee.com/fluent-mybatis/fluent-mybatis-docs/tree/master/fluent-mybatis-dynamic)

[Fluent MyBatis地址](https://gitee.com/fluent-mybatis/fluent-mybatis)

[Fluent MyBatis文档](https://gitee.com/fluent-mybatis/fluent-mybatis-docs)

[掘金系列文章](https://juejin.cn/user/1811586730696142/posts)

[Test4J框架](https://gitee.com/tryternity/test4j)

本文例子对应掘金网友文章 [MyBatis动态SQL，写SQL更爽](https://juejin.cn/post/6920211484383543303#heading-33)改写。
