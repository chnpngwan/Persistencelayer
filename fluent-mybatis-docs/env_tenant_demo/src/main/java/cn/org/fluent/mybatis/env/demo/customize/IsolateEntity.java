package cn.org.fluent.mybatis.env.demo.customize;

import cn.org.atool.fluent.mybatis.base.IEntity;

/**
 * Entity类隔离属性基类
 */
public interface IsolateEntity extends IEntity {
    /**
     * 返回entity env属性值
     *
     * @return
     */
    String getEnv();

    /**
     * 设置entity env属性值
     *
     * @param env
     * @return
     */
    IsolateEntity setEnv(String env);

    /**
     * 返回entity 租户信息
     *
     * @return
     */
    Long getTenant();

    /**
     * 设置entity 租户信息
     *
     * @param tenant
     * @return
     */
    IsolateEntity setTenant(Long tenant);
}
