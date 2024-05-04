package cn.org.fluent.mybatis.springboot.demo.step3;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.JoinBuilder;
import cn.org.fluent.mybatis.springboot.demo.QuickStartApplication;
import cn.org.fluent.mybatis.springboot.demo.mapper.StudentMapper;
import cn.org.fluent.mybatis.springboot.demo.wrapper.CountyDivisionQuery;
import cn.org.fluent.mybatis.springboot.demo.wrapper.StudentQuery;
import cn.org.fluent.mybatis.springboot.demo.wrapper.StudentScoreQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = QuickStartApplication.class)
public class JoinSelectDemo {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_join_student_county() {
        IQuery query = JoinBuilder
            .from(new StudentQuery("t1")
                .select.userName().age().genderMan().end()
                .where.isDeleted().isFalse()
                .and.genderMan().eq(1).end())
            .join(new CountyDivisionQuery("t2")
                .select.province().city().county().end()
                .where.isDeleted().isFalse()
                .and.province().eq("浙江省")
                .and.city().eq("杭州市").end())
            .on(l -> l.where.homeCountyId(), r -> r.where.id()).endJoin()
            .build();
        mapper.listMaps(query);
    }

    @Test
    public void test_3_table_join() {
        IQuery query = JoinBuilder.from(
            new StudentQuery("t1")
                .select.userName().end()
                .where.isDeleted().isFalse().end())
            .join(
                new CountyDivisionQuery("t2")
                    .where.isDeleted().isFalse()
                    .and.province().eq("浙江省")
                    .and.city().eq("杭州市").end())
            .on(l -> l.where.homeCountyId(), r -> r.where.id()).endJoin()
            .join(
                new StudentScoreQuery("t3")
                    .select.subject().score().end()
                    .where.isDeleted().isFalse()
                    .and.schoolTerm().eq(2019)
                    .and.subject().in(new String[]{"语文", "数学"})
                    .and.score().ge(90).end())
            .on(l -> l.where.id(), r -> r.where.studentId()).endJoin()
            .build();
        mapper.listMaps(query);
    }
}
