package cn.org.fluent.mybatis.many2many.demo.generate;

import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Relation;
import cn.org.atool.generator.annotation.RelationType;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.test4j.module.database.proxy.DataSourceCreator;

public class AppEntityFullGenerator {
    static final String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useSSL=false&useUnicode=true&characterEncoding=utf-8";
    /**
     * 生成代码的package路径
     */
    static final String basePackage = "cn.org.fluent.mybatis.many2many.demo";

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
        gmtCreated = "gmt_create", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        /** 需要生成文件的表 ( 表名称:对应的Entity名称 ) **/
        tables = @Table(value = {"t_member", "t_member_love", "t_member_favorite"},
            tablePrefix = "t_"),
        relations = {
            @Relation(method = "findMyFavorite", source = "t_member", target = "t_member_favorite", type = RelationType.OneWay_0_N
                , where = "id=member_id && is_deleted=is_deleted"),
            @Relation(method = "findExFriends", source = "t_member", target = "t_member", type = RelationType.OneWay_0_N),
            @Relation(method = "findCurrFriend", source = "t_member", target = "t_member", type = RelationType.OneWay_0_1)
        }, lombokBuilder = true
    )
    static class Nothing {
    }
}