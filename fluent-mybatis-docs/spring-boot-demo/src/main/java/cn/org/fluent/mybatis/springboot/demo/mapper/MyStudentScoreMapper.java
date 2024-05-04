package cn.org.fluent.mybatis.springboot.demo.mapper;

import java.util.List;
import java.util.Map;

public interface MyStudentScoreMapper {
    List<Map<String, Object>> summaryScore(SummaryQuery paras);
}