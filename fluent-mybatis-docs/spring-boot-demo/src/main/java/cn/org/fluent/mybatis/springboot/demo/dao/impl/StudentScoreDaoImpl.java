package cn.org.fluent.mybatis.springboot.demo.dao.impl;

import cn.org.fluent.mybatis.springboot.demo.dao.base.StudentScoreBaseDao;
import cn.org.fluent.mybatis.springboot.demo.dao.intf.StudentScoreDao;
import cn.org.fluent.mybatis.springboot.demo.model.ScoreStatistics;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * StudentScoreDaoImpl: 数据操作接口实现
 *
 * @author Powered By Fluent Mybatis
 */
@Repository
public class StudentScoreDaoImpl extends StudentScoreBaseDao implements StudentScoreDao {
    @Override
    public List<ScoreStatistics> statistics(int fromSchoolTerm, int endSchoolTerm, String[] subjects) {
        return super.listPoJos(ScoreStatistics.class, super.query()
            .select.schoolTerm().subject()
            .count("count")
            .min.score("min_score")
            .max.score("max_score")
            .avg.score("avg_score")
            .end()
            .where.isDeleted().isFalse()
            .and.schoolTerm().between(fromSchoolTerm, endSchoolTerm)
            .and.subject().in(subjects)
            .end()
            .groupBy.schoolTerm().subject().end()
            .orderBy.schoolTerm().asc().subject().asc().end()
        );
    }
}