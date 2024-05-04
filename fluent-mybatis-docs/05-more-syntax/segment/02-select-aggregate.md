
### 聚合查询
    fluent mybatis内置了6个聚合函数，聚合函数都可以指定别名或不指定别名   
- 求和 sum
    1. .column.sum()
    2. .column.sum("别名")
- 求平均值 avg
    1. .column.avg()
    2. .column.avg("别名")
- 求个数 count
    1. .column.count()
    2. .column.count("别名")
- 求最大值 max
    1. .column.max()
    2. .column.max("别名")
- 求最小值 min
    1. .column.min()
    2. .column.min("别名")
- 分组连接 group_concat
    1. .column.group_concat()
    2. .column.group_concat("别名")
    
示例1: 按status分组，求分组平均积分和总积分
```java
@Repository
public class UserDaoImpl extends UserBaseDao implements UserDao {
    /**
     * 按status分组，求分组平均积分和总积分
     */
    @Override
    public List<Map<String, Object>> selectAggregate() {
        UserQuery query = super.query()
            .select
            .apply(status)
            .bonusPoints().avg()
            .bonusPoints().sum("SUM").end()
            .groupBy.status().end();
        return super.mapper.listMaps(query);
    }
}
```

    验证聚合查询sql语句
```java
class UserDaoImplTest extends BaseTest {
    @Autowired
    private UserDao userDao;

    @Test
    void selectAggregate() {
        userDao.selectAggregate();
        db.sqlList().wantFirstSql()
            .eq("SELECT status, AVG(bonus_points), SUM(bonus_points) AS SUM " +
                "FROM user GROUP BY status");
    }
}
``` 

示例2: 分组连接函数
```java
@Repository
public class UserDaoImpl extends UserBaseDao implements UserDao {
    /**
     * 分组连接函数
     */
    @Override
    public List<Map<String, Object>> selectGroupConcat() {
        UserQuery query = super.query()
            .select
            .apply(status)
            .userName().group_concat().end()
            .groupBy.status().end();
        return super.mapper.listMaps(query);
    }
}
```
    验证分组连接函数语句
```java
class UserDaoImplTest extends BaseTest {
    @Autowired
    private UserDao userDao;

    @Test
    void selectGroupConcat() {
        userDao.selectGroupConcat();
        db.sqlList().wantFirstSql()
            .eq("SELECT status, GROUP_CONCAT(user_name) " +
                "FROM user GROUP BY status");
    }
}
```

#### 自定义聚合函数
    除了使用fluent mybatis内置的聚合函数外, 你也可以自定义聚合函数
    select.apply("自定义聚合表达式")...end()
```java
@Repository
public class UserDaoImpl extends UserBaseDao implements UserDao {
    /**
     * 自定义聚合函数
     * 将分组user_name排序，并以'#'串联
     */
    @Override
    public List<String> selectCustomizedAggregate() {
        UserQuery query = super.query()
            .select
            .apply("GROUP_CONCAT(user_name order by user_name SEPARATOR '#')", "userNames").end()
            .groupBy.status().end();
        return super.mapper.listObjs(query);
    }
}
```
验证自定义聚合函数查询结果
```java
class UserDaoImplTest extends BaseTest {
    @Autowired
    private UserDao userDao;

    @DisplayName("验证自定义group_concat效果, 准备5条数据, 查询结果user_name以#连接起来")
    @Test
    void selectCustomizedAggregate() {
        db.table(ITable.t_user).clean()
            .insert(TM.user.create(5)
                .status.values("NORMAL")
                .user_name.formatAutoIncrease("user_%d")
            );
        List<String> userNames = userDao.selectCustomizedAggregate();
        db.sqlList().wantFirstSql()
            .eq("SELECT GROUP_CONCAT(user_name order by user_name SEPARATOR '#') as userNames " +
                "FROM user GROUP BY status");
        want.string(userNames.get(0)).eq("user_1#user_2#user_3#user_4#user_5");
    }
}
```
