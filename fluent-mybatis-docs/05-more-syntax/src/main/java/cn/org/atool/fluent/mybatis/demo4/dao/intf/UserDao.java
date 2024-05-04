package cn.org.atool.fluent.mybatis.demo4.dao.intf;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.demo4.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author generate code
 * @ClassName UserDao
 * @Description UserEntity数据操作接口
 */
public interface UserDao extends IBaseDao<UserEntity> {
    List<UserEntity> findByBirthdayAndBonusPoints(Date birthday, Long points, String status);
}