package cn.org.fluent.mybatis.springboot.demo.mapper;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SummaryQuery {
    private Integer schoolTerm;

    private List<String> subjects;

    private Integer score;

    private Integer minCount;
}