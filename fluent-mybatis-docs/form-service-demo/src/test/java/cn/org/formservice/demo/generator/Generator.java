package cn.org.formservice.demo.generator;

import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import cn.org.formservice.demo.config.AppDefaultSetting;

public class Generator {
    static final String URL = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    static final String SrcDir = "src/main/java";

    static final String TestDir = "src/test/java";

    static final String BasePack = "cn.org.formservice.demo.shared";

    public static void main(String[] args) {
        FileGenerator.build(A.class);
    }


    @Tables(url = URL, username = "root", password = "password",
        srcDir = SrcDir, testDir = TestDir, basePack = BasePack,
        gmtCreated = "gmt_created", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        tables = {
            @Table(value = {"student"},
                defaults = AppDefaultSetting.class
            )}
    )
    static class A {
    }
}