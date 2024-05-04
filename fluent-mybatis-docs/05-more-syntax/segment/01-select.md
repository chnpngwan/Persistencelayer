# 查询字段设置
### fluent mybatis默认不用指定查询字段，默认查询实体类中的所有字段

```java
@Repository
public class UserDaoImpl extends UserBaseDao implements UserDao {
    /**
     * 查询积分点数大于等于指定值，并且未逻辑删除的所有用户
     *
     * @param minBonusPoints 最少积分
     * @return 符合条件的用户列表
     */
    public List<UserEntity> selectUsers(int minBonusPoints) {
        UserQuery query = new UserQuery()
            .where.bonusPoints().ge(minBonusPoints)
            .and.isDeleted().eq(false).end();
        return super.mapper.listEntity(query);
    }
}
```
    
1. 示例中先构造一个UserQuery对象
2. 没有显式指定select字段，默认查询UserEntity所有字段
3. 构造where条件， bonus_points >= #{minBonusPoints} and is_deleted = false
4. 执行mapper的listEntity方法，查询符合条件的用户
    
#### 只查询主键字段: selectId()
```java
@Repository
public class UserDaoImpl extends UserBaseDao implements UserDao {
    public List<Long> selectUserIds(int minBonusPoints) {
        return super.query()
            .selectId()
            .where.bonusPoints().ge(minBonusPoints)
            .and.isDeleted().eq(false).end()
            .execute(mapper::listObjs);
    }
}
``` 

### 显式指定查询字段的几个方法  
    显式指定查询引用内置变量 .select, 并且以函数 .end()结束
    1. 指定字段: .select.apply(FieldMapping... 指定字段) 
    2. 聚合函数: .select.column().聚合函数()
    3. 自定义: .select.apply(String... 自定义字段或聚合函数)
  
#### 显式指定字段名称    
```java
@Repository
public class UserDaoImpl extends UserBaseDao implements UserDao {
    /**
     * 只查询id, user_name字段，使用生成的辅助类*Mapping显式指定字段名称
     *
     * @param minBonusPoints
     * @return
     */
    public List<UserEntity> selectUserNames(int minBonusPoints) {
        return super.query()
            .select.id().userName().end()
            .where.bonusPoints().ge(minBonusPoints)
            .and.isDeleted().eq(false).end()
            .execute(mapper::listEntity);
    }
}
```

以上语句只查询id, user_name 2个字段
```sql
select id, user_name from user where ...
```

#### 指定字段特征(Predicate)查询 .select(boolean includePrimary, FieldPredicate predicate)
```java
@Repository
public class UserDaoImpl extends UserBaseDao implements UserDao {    
    /**
     * 查询 主键id, 以及gmt开头的时间字段
     **/
    public List<UserEntity> selectPredicateUser(int minBonusPoints) {
        UserQuery query = super.query()
            .selectId()
            .select.apply(f -> f.getColumn().startsWith("gmt")).end()
            .where.bonusPoints().ge(minBonusPoints)
            .and.isDeleted().eq(false).end();
        return super.mapper.listEntity(query);
    }
}
```
   验证查询语句, 没有查询所有字段，只包括了id, gmt_create, gmt_modified 3个字段
```java
class UserDaoImplTest extends BaseTest {
    @Autowired
    private UserDao userDao;

    @Test
    void selectPredicateUser() {
        userDao.selectPredicateUser(30);
        db.sqlList().wantFirstSql()
            .eq("SELECT id, gmt_create, gmt_modified FROM user " +
                "WHERE bonus_points >= ? AND is_deleted = ?");
    }
}
```
#### 查询非大字段列表（在批量查询中，排除大字段的查询，有利于提高查询效率）

```java
class UserDaoImplTest extends BaseTest {
    @Autowired
    private UserDao userDao;

    /**
     * 非大字段查询
     * 排除注解为 @TableField(notLarge = false)的Entity字段查询
     **/
    public List<UserEntity> selectUserWithNotLarge(int minBonusPoints) {
        UserQuery query = super.query()
            .select
            .apply(FieldMeta::isNotLarge).end()
            .where
            .bonusPoints().ge(minBonusPoints)
            .isDeleted().eq(false)
            .end();
        return super.mapper.listEntity(query);
    }
}
```

**是否大字段，在Entity的@TableField注解上声明，在代码生成Entity对象时也可以声明大字段类型**

### distinct查询
使用在select方法前面加 .distinct()方法， 示例代码
```java
@Repository
public class UserDaoImpl extends UserBaseDao implements UserDao {

    /**
     * 求表中所有状态列表
     */
    @Override
    public List<String> findAllStatus() {
        return super.query()
            .distinct()
            .select.apply(status).end()
            .execute(mapper::listObjs);
    }
}
```
测试代码
```java
class UserDaoImplTest extends BaseTest {
    @Autowired
    private UserDao userDao;

    @DisplayName("准备5条数据，共2个状态，验证distinct效果")
    @Test
    void findAllStatus() {
        db.table(ITable.t_user).clean()
            .insert(TM.user.create(5)
                .status.values("status1", "status3", "status1", "status3", "status1")
            );
        List<String> allStatus = userDao.findAllStatus();
        db.sqlList().wantFirstSql()
            .eq("SELECT DISTINCT status FROM user");
        want.list(allStatus).eqReflect(new String[]{"status1", "status3"});
    }
}
```
