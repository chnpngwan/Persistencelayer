package cn.org.fluent.mybatis.springboot.demo.test;

import cn.org.fluent.mybatis.springboot.demo.QuickStartApplication;
import cn.org.fluent.mybatis.springboot.demo.entity.StudentScoreEntity;
import cn.org.fluent.mybatis.springboot.demo.mapper.StudentScoreMapper;
import cn.org.fluent.mybatis.springboot.demo.wrapper.StudentScoreQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisDemo {
    @Autowired
    private StudentScoreMapper mapper;

    @Test
    public void fluent_mybatis_demo() {
        mapper.delete(new StudentScoreQuery());
        // 批量插入1000条随机记录
        mapper.insertBatch(this.newScores(1000));
        StudentScoreQuery query = new StudentScoreQuery()
            .select
            .schoolTerm()
            .subject()
            .count.score("count")
            .min.score("min_score")
            .max.score("max_score")
            .avg.score("avg_score")
            .end()
            .where.schoolTerm().ge(2000)
            .and.subject().in(new String[]{"英语", "数学", "语文"})
            .and.score().ge(60)
            .and.isDeleted().isFalse()
            .end()
            .groupBy.schoolTerm().subject().end()
            .having.count.score().gt(1).end()
            .orderBy.schoolTerm().asc().subject().asc().end();
        List<Map<String, Object>> summary = mapper.listMaps(query);
        System.out.println(summary);
    }

    private List<StudentScoreEntity> newScores(int size) {
        Random r = new Random();
        List<StudentScoreEntity> scores = new ArrayList<>(size);
        for (int index = 0; index < size; index++) {
            scores.add(new StudentScoreEntity()
                .setStudentId(r.nextLong())
                .setSchoolTerm(2000 + r.nextInt(10))
                .setScore(r.nextInt(101))
                .setSubject(getSubject(r.nextInt(4)))
                .setIsDeleted(false)
            );
        }
        return scores;
    }

    private String getSubject(int subject) {
        switch (subject) {
            case 0:
                return "英语";
            case 1:
                return "数学";
            case 2:
                return "语文";
            default:
                return "其他";
        }
    }
}