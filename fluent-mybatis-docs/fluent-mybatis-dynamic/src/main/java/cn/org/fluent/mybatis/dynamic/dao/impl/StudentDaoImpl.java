package cn.org.fluent.mybatis.dynamic.dao.impl;

import cn.org.atool.fluent.mybatis.If;
import cn.org.fluent.mybatis.dynamic.dao.base.StudentBaseDao;
import cn.org.fluent.mybatis.dynamic.dao.intf.StudentDao;
import cn.org.fluent.mybatis.dynamic.entity.StudentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.org.atool.fluent.mybatis.If.*;

/**
 * StudentDaoImpl: 数据操作接口实现
 * <p>
 * 这只是一个减少手工创建的模板文件
 * 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>@author Powered By Fluent Mybatis
 */
@Repository
public class StudentDaoImpl extends StudentBaseDao implements StudentDao {
    @Override
    public List<StudentEntity> selectByNameOrEmail(String name, Boolean isMale) {
        return super.query()
            .where.name().like(name, If::notBlank)
            .and.gender().eq(isMale, If::notNull).end()
            .execute(super::listEntity);
    }

    @Override
    public int updateByPrimaryKeySelective(StudentEntity student) {
        return super.updater()
            .set.name().is(student.getName(), If::notBlank)
            .set.phone().is(student.getPhone(), If::notBlank)
            .set.email().is(student.getEmail(), If::notBlank)
            .set.gender().is(student.getGender(), If::notNull).end()
            .where.id().eq(student.getId()).end()
            .of(mapper).updateBy();
    }

    @Override
    public StudentEntity selectByIdOrName(StudentEntity student) {
        return super.query()
            .where.id().eq(student.getId(), If::notNull)
            .and.name().eq(student.getName(), name -> isNull(student.getId()) && notBlank(name))
            .and.applyFunc(items -> isNull(student.getId()) && isBlank(student.getName()), "1=2")
            .end()
            .of(mapper).findOne().orElse(null);
    }
}
