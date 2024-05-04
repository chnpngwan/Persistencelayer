package cn.org.atool.fluent.mybatis.demo2;

import cn.org.atool.fluent.mybatis.HelloWorldConfig;
import cn.org.atool.fluent.mybatis.demo2.entity.HelloWorld2Entity;
import cn.org.atool.fluent.mybatis.demo2.mapper.HelloWorld2Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Fluent Mybatis简单验证测试类
 *
 * @author darui.wu@163.com
 */
@ContextConfiguration(classes = HelloWorldConfig.class)
public class TableFieldTest {
    @Autowired
    HelloWorld2Mapper mapper;

    @Test
    public void testInsert() {
        mapper.delete(mapper.query()
            .where.sayHi().like("hi").end());

        HelloWorld2Entity entity = new HelloWorld2Entity();
        entity.setSayHi("hi: fluent mybatis");
        entity.setFullName("fluent mybatis");
        mapper.insert(entity);

        HelloWorld2Entity result = mapper.findOne(mapper.query()
            .where.sayHi().likeRight("fluent mybatis")
            .and.fullName().eq("fluent mybatis").end());

        System.out.println("entity:" + result.toString());
    }
}