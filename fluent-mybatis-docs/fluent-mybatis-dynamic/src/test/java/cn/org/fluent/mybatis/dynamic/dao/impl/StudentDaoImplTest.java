package cn.org.fluent.mybatis.dynamic.dao.impl;

import cn.org.fluent.mybatis.dynamic.AppMain;
import cn.org.fluent.mybatis.dynamic.dao.intf.StudentDao;
import cn.org.fluent.mybatis.dynamic.entity.StudentEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.junit5.Test4J;

@SpringBootTest(classes = AppMain.class)
public class StudentDaoImplTest extends Test4J {
    @Autowired
    StudentDao studentDao;

    @DisplayName("只有名字时的查询")
    @Test
    void selectByNameOrEmail_onlyName() {
        studentDao.selectByNameOrEmail("明", null);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "SELECT `id`, `is_deleted`, `email`, `gender`, `locked`, `name`, `phone`, `gmt_created`, `gmt_modified` " +
                "FROM `student` " +
                "WHERE `name` LIKE ?",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"%明%"});
    }

    @DisplayName("只有性别时的查询")
    @Test
    void selectByNameOrEmail_onlyGender() {
        studentDao.selectByNameOrEmail(null, false);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "SELECT `id`, `email`, `gender`, `locked`, `name`, `phone`, `gmt_created`, `gmt_modified`, `is_deleted` " +
                "FROM `student` " +
                "WHERE `gender` = ?",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{false});
    }

    @DisplayName("姓名和性别同时存在的查询")
    @Test
    void selectByNameOrEmail_both() {
        studentDao.selectByNameOrEmail("明", false);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "SELECT `id`, `email`, `gender`, `locked`, `name`, `phone`, `gmt_created`, `gmt_modified`, `is_deleted` " +
                "FROM `student` " +
                "WHERE `name` LIKE ? " +
                "AND `gender` = ?",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"%明%", false});
    }

    @Test
    void updateByPrimaryKeySelective() {
        StudentEntity student = new StudentEntity()
            .setId(1L)
            .setName("test")
            .setPhone("13866668888");
        studentDao.updateByPrimaryKeySelective(student);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE `student` " +
                "SET `gmt_modified` = now(), " +
                "`name` = ?, " +
                "`phone` = ? " +
                "WHERE `id` = ?",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"test", "13866668888", 1L});
    }

    @DisplayName("有 ID 则根据 ID 获取")
    @Test
    void selectByIdOrName_byId() {
        StudentEntity student = new StudentEntity();
        student.setName("小飞机");
        student.setId(1L);

        StudentEntity result = studentDao.selectByIdOrName(student);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "SELECT `id`, `email`, `gender`, `locked`, `name`, `phone`, `gmt_created`, `gmt_modified`, `is_deleted` " +
                "FROM `student` " +
                "WHERE `id` = ?",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{1L});
    }

    @DisplayName("没有 ID 则根据 name 获取")
    @Test
    void selectByIdOrName_byName() {
        StudentEntity student = new StudentEntity();
        student.setName("小飞机");
        student.setId(null);

        StudentEntity result = studentDao.selectByIdOrName(student);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "SELECT `id`, `email`, `gender`, `locked`, `name`, `phone`, `gmt_created`, `gmt_modified`, `is_deleted` " +
                "FROM `student` " +
                "WHERE `name` = ?",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"小飞机"});
    }

    @DisplayName("没有 ID 和 name, 返回 null")
    @Test
    void selectByIdOrName_null() {
        StudentEntity student = new StudentEntity();
        StudentEntity result = studentDao.selectByIdOrName(student);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("" +
                "SELECT `id`, `email`, `gender`, `locked`, `name`, `phone`, `gmt_created`, `gmt_modified`, `is_deleted` " +
                "FROM `student` " +
                "WHERE 1=2",
            StringMode.SameAsSpace);
        // 验证sql参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{});
    }
}
