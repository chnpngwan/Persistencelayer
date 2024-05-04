package cn.org.atool.fluent.mybatis.demo3;

import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.test4j.module.database.proxy.DataSourceCreator;
import org.test4j.module.database.proxy.DataSourceCreatorFactory;

public class EntityGeneratorDemo1 {
    public static final String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    /**
     * 使用 test/resource/init.sql文件自动初始化测试数据库
     */
    @BeforeAll
    static void runDbScript() {
        DataSourceCreator.create("dataSource");
    }

    @Test
    public void generate() throws Exception {
        FileGenerator.build(Empty.class);
    }

    @Tables(
        // 设置数据库连接信息
        url = url, username = "root", password = "password",
        // 设置entity类生成src目录, 相对于 user.dir
        srcDir = "src/main/java",
        // 设置entity类的package值
        basePack = "cn.org.atool.fluent.mybatis.demo3",
        // 设置dao接口和实现的src目录, 相对于 user.dir
        daoDir = "src/main/java",
        // 设置哪些表要生成Entity文件
        tables = {@Table(value = {"hello_world"})}
    )
    static class Empty {
    }
}