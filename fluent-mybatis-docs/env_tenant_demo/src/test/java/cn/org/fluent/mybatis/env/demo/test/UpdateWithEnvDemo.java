package cn.org.fluent.mybatis.env.demo.test;

import cn.org.fluent.mybatis.env.demo.AppMain;
import cn.org.fluent.mybatis.env.demo.entity.StudentEntity;
import cn.org.fluent.mybatis.env.demo.mapper.StudentMapper;
import cn.org.fluent.mybatis.env.demo.wrapper.StudentQuery;
import cn.org.fluent.mybatis.env.demo.wrapper.StudentUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        mapper.updateBy(StudentUpdate.updater()
            .set.address().is("回到地球").end()
            .where.userName().eq("FluentMybatis").end()
        );
    }
}
