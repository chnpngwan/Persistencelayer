package cn.org.atool.fluent.mybatis.demo4.where;

import cn.org.atool.fluent.mybatis.demo4.BaseTest;
import cn.org.atool.fluent.mybatis.demo4.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo4.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.If;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class EqTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void column_eq_value() {
        UserQuery query = new UserQuery()
            .where.userName().eq("darui.wu")
            .and.eMail().eq("darui.wu@163.com").end();
        mapper.findOne(query);

        db.sqlList().wantFirstSql().end(
            "FROM `user` " +
                "WHERE `user_name` = ? " +
                "AND `e_mail` = ?",
            StringMode.SameAsSpace);
    }

    @Test
    public void condition_eq() {
        UserQuery query = new UserQuery()
            .where.userName().eq("darui.wu")
            .and.eMail().eq("darui.wu@163.com").end();
        mapper.findOne(query);
    }

    @Test
    public void eq_null() {
        want.exception(
            () -> new UserQuery()
                .where.age().eq(null),
            RuntimeException.class
        );
    }

    @Test
    public void eq_condition_true() {
        UserQuery query = new UserQuery()
            .where.age().eq(34)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM `user` WHERE `age` = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void eq_condition_false() {
        UserQuery query = new UserQuery()
            .where.age().eq(34, o -> false)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM `user`");
        db.sqlList().wantFirstPara().sizeEq(0);
    }

    @Test
    public void eq_IfNotNull() {
        UserQuery query = new UserQuery()
            .where.userName().eq("name", If::notNull)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM `user` WHERE `user_name` = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"name"});
    }

    @Test
    public void eq_IfNull() {
        UserQuery query = new UserQuery()
            .where.userName().eq(null, If::notNull)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM `user`");
        db.sqlList().wantFirstPara().sizeEq(0);
    }
}