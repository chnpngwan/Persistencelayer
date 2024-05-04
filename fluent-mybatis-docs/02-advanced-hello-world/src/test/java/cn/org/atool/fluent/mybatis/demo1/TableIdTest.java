package cn.org.atool.fluent.mybatis.demo1;

import cn.org.atool.fluent.mybatis.HelloWorldConfig;
import cn.org.atool.fluent.mybatis.demo1.entity.HelloWorld1Entity;
import cn.org.atool.fluent.mybatis.demo1.mapper.HelloWorld1Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Fluent Mybatis简单验证测试类
 *
 * @author darui.wu@163.com
 */
@ContextConfiguration(classes = HelloWorldConfig.class)
public class TableIdTest {
    @Autowired
    HelloWorld1Mapper mapper;

    @Test
    public void testInsert() {
        /**
         * 为了演示方便，先删除数据
         */
        mapper.delete(mapper.query()
            .where.sayHello().eq("hello world").end());
        /**
         * 插入数据
         */
        HelloWorld1Entity entity = new HelloWorld1Entity();
        /** 不设置id的值**/
        entity.setId(null);
        entity.setSayHello("hello world");
        entity.setYourName("fluent mybatis");
        entity.setIsDeleted(false);
        mapper.insert(entity);

        HelloWorld1Entity entity1 = mapper.findOne(mapper.query()
            .where.sayHello().eq("hello world").end());
        /**
         * 这里entity的id已经根据数据库自增id赋值了
         */
        System.out.println("entity id:" + entity1.getId());
        /**
         * 我们也可以根据主键直接查询数据
         */
        HelloWorld1Entity entity2 = mapper.findById(entity1.getId());
        System.out.println("entity:" + entity2.toString());
    }
}