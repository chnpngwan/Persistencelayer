package cn.org.atool.fluent.mybatis.demo3;

import cn.org.atool.fluent.mybatis.HelloWorldConfig;
import cn.org.atool.fluent.mybatis.demo3.entity.HelloWorld3Entity;
import cn.org.atool.fluent.mybatis.demo3.mapper.HelloWorld3Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = HelloWorldConfig.class)
public class InsertTest {
    @Autowired
    HelloWorld3Mapper mapper;

    @Test
    public void testInsertDefaultValue() {
        // 全表清空
        mapper.delete(mapper.query().where.applyFunc("1=1").end());
        // 插入记录，未设置gmtCreated, gmtModified, isDeleted3个字段
        mapper.insert(new HelloWorld3Entity()
            .setSayHello("hello")
            .setYourName("fluent mybatis")
        );
        // 查询并在控制台输出记录
        HelloWorld3Entity entity = mapper.findOne(mapper.query()
            .where.sayHello().eq("hello")
            .and.yourName().eq("fluent mybatis").end()
        );
        System.out.println(entity);
    }
}