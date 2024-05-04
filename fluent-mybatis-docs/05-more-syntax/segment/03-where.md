
## 查询where条件设置
where条件设置语法形式如下, 以where开头，以end()方法结束
在内置变量where后面，可以自动列出可以设置的表字段(方法), 在字段后可以紧跟着比较符和比较值。

``` java
    .where
    .字段().条件(条件参数)
    .end()
```
### eq: 等于 = ?
- 相等条件设置示例 

``` java
@Test
public void column_eq_value() {
    UserQuery query = new UserQuery()
        .where.userName().eq("darui.wu")
        .and.eMail().eq("darui.wu@163.com").end();
    mapper.findOne(query);

    db.sqlList().wantFirstSql().eq(
        "SELECT id, gmt_create, gmt_modified, is_deleted, account, avatar, birthday, bonus_points, e_mail, password, phone, status, user_name " +
            "FROM user " +
            "WHERE user_name = ? " +
            "AND e_mail = ?",
        StringMode.SameAsSpace);
}
```
- 使用带条件判断方法: eq(Object value, Predicate when)

当预言when为真时, 才会执行 eq(value)判断

示例:
``` java
    /**
     * 根据条件查询用户列表
     * 如果设置了积分, 加上积分条件
     * 如果设置了状态, 加上状态条件
     *
     * @return
     */
    @Override
    public List<UserEntity> findByBirthdayAndBonusPoints(Date birthday, Long points, String status) {
        UserQuery query = super.query()
            .where.birthday().eq(birthday)
            .and.bonusPoints().ge(points, If::notNull)
            .and.status().eq(status, If::notBlank).end();
        return mapper.listEntity(query);
    }
```
上面的示例，相当于下面常见的java条件设置
``` java
    public List<UserEntity> findByBirthdayAndBonusPoints(Date birthday, Long points, String status) {
        UserQuery query = super.query();
        query.where.birthday().eq(birthday);
        if (points != null) {
            query.where.bonusPoints().ge(points);
        }
        if (status != null && !status.trim().isEmpty()) {
            query.where.status().eq(status).end();
        }
        return mapper.listEntity(query);
    }
```
或者，使用mybatis的条件设置
```xml
<select id="findByBirthdayAndBonusPoints" parameterType="java.util.Map" resultMap="UserEntity">
    SELECT ... FROM user
    WHERE birthday = #{birthday}
    <if test="bonus_points != null ">
        AND bonus_points = #{bonusPoints}
    </if>
    <if test="status != null and status != '' ">
        AND status = #{status}
    </if>
</select>
```
使用fluent mybatis的条件语句可以达到上面java判断和xml判断的同样效果，同时，如果birthday如果传入一个空串，还会进行判空，抛出异常，
防止传参错误，导致的非预期条件查询。

### ne: 不等于 <> ?
``` java
@Test
public void ne() {
    UserQuery query = new UserQuery()
        .where
        .age().ne(34)
        .end();
    mapper.count(query);
    db.sqlList().wantFirstSql().end("WHERE age <> ?");
}
```
### gt: 大于 > ?
``` java
@Test
public void gt() {
    UserQuery query = new UserQuery()
        .where.age().gt(34).end();
    mapper.count(query);
    db.sqlList().wantFirstSql().end("WHERE age > ?");
}
```
### ge: 大于等于 >= ?
``` java
@Test
public void ge() {
    UserQuery query = new UserQuery()
        .where.age().ge(34).end();
    mapper.count(query);
    db.sqlList().wantFirstSql().end("WHERE age >= ?");
}
```
### lt: 小于 < ?
``` java
@Test
public void lt() {
    UserQuery query = new UserQuery()
        .where.age().lt(34).end();
    mapper.count(query);
    db.sqlList().wantFirstSql().end("WHERE age < ?");
}
```
### le: 小于等于 <= ?
``` java
@Test
public void le() {
    UserQuery query = new UserQuery()
        .where.age().le(34).end();
    mapper.count(query);
    db.sqlList().wantFirstSql().end("WHERE age <= ?");
}
```
### between: BETWEEN ? AND ?
``` java
@Test
public void between() {
    UserQuery query = new UserQuery()
        .where.age().between(23, 40).end();
    mapper.count(query);
    db.sqlList().wantFirstSql().end("WHERE age BETWEEN ? AND ?");
}
```
### notBetween: NOT BETWEEN ? AND ?
``` java
@Test
public void not_between() {
    UserQuery query = new UserQuery()
        .where.age().notBetween(23, 40).end();
    mapper.count(query);
    db.sqlList().wantFirstSql().end("WHERE age NOT BETWEEN ? AND ?");
}
```
### like: LIKE ?,  ? 为 "%value%"
``` java
@Test
void like() {
    mapper.listEntity(new UserQuery()
        .where
        .userName().like("abc")
        .end()
    );
    // ? 值为:  '%abc%'
    db.sqlList().wantFirstSql().end("WHERE user_name LIKE ?");
}
```
### notLike: NOT LIKE ?, ? 为 "%value%"
``` java
@Test
void not_like() {
    mapper.listEntity(new UserQuery()
        .where
        .userName().notLike("abc")
        .end()
    );
    // ? 值为:  '%abc%'
    db.sqlList().wantFirstSql().end("WHERE user_name NOT LIKE ?");
}
```
### likeLeft: LIKE ?,  ? 为 "value%"
### likeRight: LIKE ?,  ? 为 "%value"
### isNull: column IS NULL
``` java
@Test
void isNull() {
    mapper.listEntity(new UserQuery()
        .where.age().isNull().end()
    );
    db.sqlList().wantFirstSql().end("WHERE age IS NULL");
}
```
### isNotNull: column IS NOT NULL
``` java
@Test
void isNotNull() {
    mapper.listEntity(new UserQuery()
        .where.age().isNotNull().end()
    );
    db.sqlList().wantFirstSql()
        .end("WHERE age IS NOT NULL");
}
```
### in: 在...之中  IN (?, ..., ?)
``` java
@Test
public void in_collection() {
    UserQuery query = new UserQuery()
        .where
        .age().in(Arrays.asList(34, 35))
        .end();
    mapper.count(query);
    db.sqlList().wantFirstSql().end("WHERE age IN (?, ?)");
    db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
}

@Test
public void in_array() {
    UserQuery query = new UserQuery()
        .where
        .age().in(new int[]{34, 35})
        .end();
    mapper.count(query);
    db.sqlList().wantFirstSql().end("WHERE age IN (?, ?)");
    db.sqlList().wantFirstPara().eqReflect(new Object[]{34, 35});
}
```
### notIn: 不在...之中 NOT IN (?, ..., ?)

以上条件，框架会自动判断参数不允许为空，否则会抛出异常，防止全表扫描
