package cn.org.fluent.mybatis.springboot.demo.step3;

import cn.org.fluent.mybatis.springboot.demo.QuickStartApplication;
import cn.org.fluent.mybatis.springboot.demo.entity.StudentEntity;
import cn.org.fluent.mybatis.springboot.demo.mapper.StudentMapper;
import cn.org.fluent.mybatis.springboot.demo.wrapper.CountyDivisionQuery;
import cn.org.fluent.mybatis.springboot.demo.wrapper.StudentQuery;
import cn.org.fluent.mybatis.springboot.demo.wrapper.StudentScoreQuery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = QuickStartApplication.class)
public class InSelectDemo {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_in_select() {
        StudentQuery query = new StudentQuery()
            .where.isDeleted().isFalse()
            .and.grade().eq(4)
            .and.homeCountyId().in(new CountyDivisionQuery()
                .selectId()
                .where.isDeleted().isFalse()
                .and.province().eq("浙江省")
                .and.city().eq("杭州市")
                .end()
            ).end();
        List<StudentEntity> students = mapper.listEntity(query);
    }

    @Test
    public void test_exists() {
        StudentQuery query = new StudentQuery()
            .where.isDeleted().isFalse()
            .and.exists(new StudentScoreQuery()
                .selectId()
                .where.isDeleted().isFalse()
                .and.schoolTerm().eq(2019)
                .and.score().lt(60)
                .and.subject().in(new String[]{"语文", "数学"})
                .and.studentId().apply("= student.id")
                .end()
            ).end();
        List<StudentEntity> students = mapper.listEntity(query);
    }
}
