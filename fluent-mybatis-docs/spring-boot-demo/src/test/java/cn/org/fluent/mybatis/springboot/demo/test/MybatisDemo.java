package cn.org.fluent.mybatis.springboot.demo.test;

import cn.org.fluent.mybatis.springboot.demo.QuickStartApplication;
import cn.org.fluent.mybatis.springboot.demo.mapper.MyStudentScoreMapper;
import cn.org.fluent.mybatis.springboot.demo.mapper.SummaryQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = QuickStartApplication.class)
public class MybatisDemo {
    @Autowired
    private MyStudentScoreMapper mapper;

    @Test
    public void mybatis_demo() {
        // 构造查询参数
        SummaryQuery paras = new SummaryQuery()
            .setSchoolTerm(2000)
            .setSubjects(Arrays.asList("英语", "数学", "语文"))
            .setScore(60)
            .setMinCount(1);

        List<Map<String, Object>> summary = mapper.summaryScore(paras);
        System.out.println(summary);
    }
}