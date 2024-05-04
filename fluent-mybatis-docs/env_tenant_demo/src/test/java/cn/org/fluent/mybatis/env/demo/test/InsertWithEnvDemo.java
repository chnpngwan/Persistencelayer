package cn.org.fluent.mybatis.env.demo.test;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.fluent.mybatis.env.demo.AppMain;
import cn.org.fluent.mybatis.env.demo.entity.StudentEntity;
import cn.org.fluent.mybatis.env.demo.mapper.StudentMapper;
import cn.org.fluent.mybatis.env.demo.wrapper.StudentQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AppMain.class)
public class InsertWithEnvDemo {
    @Autowired
    private StudentMapper mapper;

    static {
        DbType.MYSQL.setEscapeExpress("?");
    }

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
