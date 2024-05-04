package cn.org.fluent.mybatis.env.demo.generate;

import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import cn.org.fluent.mybatis.env.demo.customize.IsolateEntity;
import cn.org.fluent.mybatis.env.demo.customize.IsolateSetter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.test4j.module.database.proxy.DataSourceCreator;
import org.test4j.module.database.proxy.DataSourceCreatorFactory;

import javax.sql.DataSource;

public class FluentGenerateMain {
    static final String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useSSL=false&useUnicode=true&characterEncoding=utf-8";
    /**
     * 生成代码的package路径
     */
    static final String basePackage = "cn.org.fluent.mybatis.env.demo";

    /**
     * 使用 test/resource/init.sql文件自动初始化测试数据库
     */
    @BeforeAll
    static void runDbScript() {
        DataSourceCreator.create("dataSource");
    }

    @Test
    public void generate() {
        FileGenerator.build(Nothing.class);
    }

    @Tables(
        /** 数据库连接信息 **/
        url = url, username = "root", password = "password",
        /** Entity类parent package路径 **/
        basePack = basePackage,
        /** Entity代码源目录 **/
        srcDir = "src/main/java",
        /** 如果表定义记录创建，记录修改，逻辑删除字段 **/
        gmtCreated = "gmt_created", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        /** 需要生成文件的表 ( 表名称:对应的Entity名称 ) **/
        tables = @Table(value = {"student"},
            entity = IsolateEntity.class,
            defaults = IsolateSetter.class)
    )
    static class Nothing {
    }
}