package cn.org.atool.fluent.mybatis.demo4.dao.impl;

import cn.org.atool.fluent.mybatis.If;

import cn.org.atool.fluent.mybatis.demo4.dao.base.UserBaseDao;
import cn.org.atool.fluent.mybatis.demo4.dao.intf.UserDao;
import cn.org.atool.fluent.mybatis.demo4.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo4.wrapper.UserQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


/**
 * @author generate code
 * @ClassName UserDaoImpl
 * @Description UserEntity数据操作实现类
 */
@Repository
public class UserDaoImpl extends UserBaseDao implements UserDao {
    /**
     * 根据条件查询用户列表
     * 如果设置了积分, 加上积分条件
     * 如果设置了状态, 加上状态条件
     *
     * @return
     */
    @Override
    public List<UserEntity> findByBirthdayAndBonusPoints(Date birthday, Long points, String status) {
        UserQuery query = super.query()
            .where.birthday().eq(birthday)
            .and.bonusPoints().ge(points, If::notNull)
            .and.status().eq(status, If::notBlank).end();
        return mapper.listEntity(query);
    }

    public List<UserEntity> findByBirthdayAndBonusPoints2(Date birthday, Long points, String status) {
        UserQuery query = super.query();
        query.where.birthday().eq(birthday);
        if (points != null) {
            query.where.bonusPoints().ge(points);
        }
        if (status != null && !status.trim().isEmpty()) {
            query.where.status().eq(status).end();
        }
        return mapper.listEntity(query);
    }

    /**
     * 查询积分点数大于等于指定值，并且未逻辑删除的所有用户
     *
     * @param minBonusPoints 最少积分
     * @return 符合条件的用户列表
     */
    public List<UserEntity> selectUsers(int minBonusPoints) {
        return super.query()
            .where.bonusPoints().ge(minBonusPoints)
            .and.isDeleted().eq(false).end()
            .execute(mapper::listEntity);
    }

    /**
     * 只查询id, user_name字段，使用生成的辅助类*Mapping显式指定字段名称
     *
     * @param minBonusPoints
     * @return
     */
    public List<UserEntity> selectUserNames(int minBonusPoints) {
        return super.query()
            //.select.apply(id, userName).end()
            .select.id().userName().end()
            .where.bonusPoints().ge(minBonusPoints)
            .and.isDeleted().eq(false).end()
            .execute(mapper::listEntity);
    }

    /**
     * 查询 主键id, 以及gmt开头的时间字段
     **/
    public List<UserEntity> selectPredicateUser(int minBonusPoints) {
        UserQuery query = super.query()
            .selectId()
            .select.apply(f -> f.getColumn().startsWith("gmt")).end()
            .where.bonusPoints().ge(minBonusPoints)
            .and.isDeleted().eq(false).end();
        return super.mapper.listEntity(query);
    }
}