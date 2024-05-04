package cn.org.atool.fluent.mybatis.demo4.where;

import cn.org.atool.fluent.mybatis.demo4.BaseTest;
import cn.org.atool.fluent.mybatis.demo4.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo4.wrapper.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class UpdateTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void update() {
        UserUpdate update = new UserUpdate()
            .set.account().is("xxx").end()
            .where.userName().eq("darui.wu")
            .and.eMail().eq("darui.wu@163.com").end();
        mapper.updateBy(update);

        db.sqlList().wantFirstSql().end(
            "UPDATE `user` " +
                "SET `gmt_modified` = now(), " +
                "`account` = ? " +
                "WHERE `user_name` = ? AND `e_mail` = ?",
            StringMode.SameAsSpace);
    }
}
