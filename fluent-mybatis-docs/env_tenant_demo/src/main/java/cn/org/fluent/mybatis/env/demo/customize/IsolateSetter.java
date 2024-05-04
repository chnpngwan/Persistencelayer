package cn.org.fluent.mybatis.env.demo.customize;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.fluent.mybatis.env.demo.utils.EnvUtils;
import cn.org.fluent.mybatis.env.demo.utils.TenantUtils;

/**
 * 增删改查中，环境和租户隔离设置
 */
public interface IsolateSetter extends IDefaultSetter {
    /**
     * 插入的entity,如果没有显式设置环境和租户，根据工具类进行默认设置
     *
     * @param entity
     */
    @Override
    default void setInsertDefault(IEntity entity) {
        IsolateEntity isolateEntity = (IsolateEntity) entity;
        if (isolateEntity.getEnv() == null) {
            isolateEntity.setEnv(EnvUtils.currEnv());
        }
        if (isolateEntity.getTenant() == null) {
            isolateEntity.setTenant(TenantUtils.findUserTenant());
        }
    }

    /**
     * 查询条件追加环境隔离和租户隔离
     *
     * @param query
     */
    @Override
    default void setQueryDefault(IQuery query) {
        query.where()
            .apply("env", SqlOp.EQ, EnvUtils.currEnv())
            .apply("tenant", SqlOp.EQ, TenantUtils.findUserTenant());
    }

    /**
     * 更新条件追加环境隔离和租户隔离
     *
     * @param updater
     */
    @Override
    default void setUpdateDefault(IUpdate updater) {
        updater.where()
            .apply("env", SqlOp.EQ, EnvUtils.currEnv())
            .apply("tenant", SqlOp.EQ, TenantUtils.findUserTenant());
    }
}
