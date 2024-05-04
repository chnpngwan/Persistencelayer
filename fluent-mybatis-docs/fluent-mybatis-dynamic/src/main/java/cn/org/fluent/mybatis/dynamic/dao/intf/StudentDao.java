package cn.org.fluent.mybatis.dynamic.dao.intf;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.fluent.mybatis.dynamic.entity.StudentEntity;

import java.util.List;

/**
 * StudentDao: 数据操作接口
 * <p>
 * 这只是一个减少手工创建的模板文件
 * 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>@author Powered By Fluent Mybatis
 */
public interface StudentDao extends IBaseDao<StudentEntity> {
    /**
     * 根据输入的学生信息进行条件检索
     * 1. 当只输入用户名时， 使用用户名进行模糊检索；
     * 2. 当只输入性别时， 使用性别进行完全匹配
     * 3. 当用户名和性别都存在时， 用这两个条件进行查询匹配的用
     *
     * @param name   姓名,模糊匹配
     * @param isMale 性别
     * @return
     */
    List<StudentEntity> selectByNameOrEmail(String name, Boolean isMale);

    /**
     * 根据主键更新非空属性
     *
     * @param student
     * @return
     */
    int updateByPrimaryKeySelective(StudentEntity student);

    /**
     * 1. 当 id 有值时， 使用 id 进行查询；
     * 2. 当 id 没有值时， 使用 name 进行查询；
     * 3. 否则返回空
     */
    StudentEntity selectByIdOrName(StudentEntity student);
}
