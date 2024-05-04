package cn.org.atool.fluent.mybatis.demo4;

import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.test4j.module.database.proxy.DataSourceCreator;
import org.test4j.module.database.proxy.DataSourceCreatorFactory;

public class MoreEntityMain {
    private static final String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    /**
     * 使用 test/resource/init.sql文件自动初始化测试数据库
     */
    @BeforeAll
    static void runDbScript() {
        DataSourceCreator.create("dataSource");
    }

    @Test
    public void generate() throws Exception {
        FileGenerator.build(Abc.class);
    }

    @Tables(
        url = url, username = "root", password = "password",
        basePack = "cn.org.atool.fluent.mybatis.demo4",
        srcDir = "src/main/java",
        testDir = "src/main/java",
        daoDir = "../05-more-syntax/src/main/java",
        gmtCreated = "gmt_created", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        tables = {
            @Table({"hello_world", "user", "receiving_address"})
        }
    )
    static class Abc {
    }
}