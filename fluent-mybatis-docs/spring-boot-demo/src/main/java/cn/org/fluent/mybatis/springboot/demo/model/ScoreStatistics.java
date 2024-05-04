package cn.org.fluent.mybatis.springboot.demo.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
public class ScoreStatistics {
    private int schoolTerm;

    private String subject;

    private long count;

    private Integer minScore;

    private Integer maxScore;

    private BigDecimal avgScore;
}
