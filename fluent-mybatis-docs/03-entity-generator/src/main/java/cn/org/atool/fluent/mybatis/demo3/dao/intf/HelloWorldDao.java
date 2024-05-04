package cn.org.atool.fluent.mybatis.demo3.dao.intf;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.demo3.entity.HelloWorldEntity;
import org.springframework.stereotype.Repository;

/**
 * @ClassName HelloWorldDao
 * @Description HelloWorldEntity数据操作接口
 *
 * @author generate code
 */
@Repository
public interface HelloWorldDao extends IBaseDao<HelloWorldEntity>  {
}