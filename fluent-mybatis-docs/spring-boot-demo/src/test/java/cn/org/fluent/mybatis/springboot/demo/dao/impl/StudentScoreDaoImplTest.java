package cn.org.fluent.mybatis.springboot.demo.dao.impl;


import cn.org.fluent.mybatis.springboot.demo.QuickStartApplication;
import cn.org.fluent.mybatis.springboot.demo.dao.intf.StudentScoreDao;
import cn.org.fluent.mybatis.springboot.demo.model.ScoreStatistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = QuickStartApplication.class)
public class StudentScoreDaoImplTest {
    @Autowired
    private StudentScoreDao dao;

    @Test
    public void statistics() {
        List<ScoreStatistics> list = dao.statistics(2000, 2019, new String[]{"语文", "数学", "英语"});
        System.out.println(list);
    }
}
