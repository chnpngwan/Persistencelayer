package cn.org.fluent.mybatis.springboot.demo.dao.intf;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.fluent.mybatis.springboot.demo.entity.StudentScoreEntity;
import cn.org.fluent.mybatis.springboot.demo.model.ScoreStatistics;

import java.util.List;

/**
 * StudentScoreDao: 数据操作接口
 *
 * @author Powered By Fluent Mybatis
 */
public interface StudentScoreDao extends IBaseDao<StudentScoreEntity> {
    /**
     * 统计从fromYear到endYear年间学科subjects的统计数据
     *
     * @param fromSchoolTerm 统计年份区间开始
     * @param endSchoolTerm  统计年份区间结尾
     * @param subjects       统计的学科列表
     * @return 统计数据
     */
    List<ScoreStatistics> statistics(int fromSchoolTerm, int endSchoolTerm, String[] subjects);
}
