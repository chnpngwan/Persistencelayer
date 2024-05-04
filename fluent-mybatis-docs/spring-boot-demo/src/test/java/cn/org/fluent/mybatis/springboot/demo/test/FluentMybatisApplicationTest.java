package cn.org.fluent.mybatis.springboot.demo.test;

import cn.org.fluent.mybatis.springboot.demo.QuickStartApplication;
import cn.org.fluent.mybatis.springboot.demo.entity.YourEntity;
import cn.org.fluent.mybatis.springboot.demo.mapper.YourMapper;
import cn.org.fluent.mybatis.springboot.demo.wrapper.YourQuery;
import cn.org.fluent.mybatis.springboot.demo.wrapper.YourUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest(classes = QuickStartApplication.class)
public class FluentMybatisApplicationTest {
    @Autowired
    private YourMapper yourMapper;

    @Test
    public void contextLoads() {
        List<YourEntity> list = yourMapper.listEntity(yourMapper.query());
        for (YourEntity entity : list) {
            System.out.println(entity);
        }
    }

    @Test
    public void insert() {
        // 构造一个对象
        YourEntity entity = new YourEntity();
        entity.setName("Fluent Mybatis");
        entity.setAge(1);
        entity.setEmail("darui.wu@163.com");
        entity.setIsDeleted(false);
        // 插入操作
        int count = yourMapper.insert(entity);
        System.out.println("count:" + count);
        System.out.println("entity:" + entity);
    }

    @Test
    public void insertBatch() {
        List<YourEntity> entities = new ArrayList<>();
        entities.add(new YourEntity().setName("Fluent Mybatis").setEmail("darui.wu@163.com"));
        entities.add(new YourEntity().setName("Fluent Mybatis Demo").setEmail("darui.wu@163.com"));
        entities.add(new YourEntity().setName("Test4J").setEmail("darui.wu@163.com"));
        int count = yourMapper.insertBatch(entities);
        System.out.println("count:" + count);
        System.out.println("entity:" + entities);
    }

    @Test
    public void deleteById() {
        int count = yourMapper.deleteById(3L);
        System.out.println("count:" + count);
    }

    @Test
    public void deleteByIds() {
        int count = yourMapper.deleteByIds(Arrays.asList(1L, 2L, 3L));
        System.out.println("count:" + count);
    }

    @Test
    public void delete() {
        int count = yourMapper.delete(new YourQuery()
            .where.id().in(new int[]{1, 2, 3}).end()
        );
        System.out.println("count:" + count);
    }

    @Test
    public void deleteByMap() {
        int count = yourMapper.deleteByMap(true, new HashMap<String, Object>() {
            {
                this.put("name", "Fluent Mybatis");
                this.put("email", "darui.wu@163.com");
            }
        });
        System.out.println("count:" + count);
    }

    @Test
    public void findById() {
        YourEntity entity = yourMapper.findById(8L);
        System.out.println(entity);
    }

    @Test
    public void listByIds() {
        List<YourEntity> entities = yourMapper.listByIds(Arrays.asList(8L, 9L));
        System.out.println(entities);
    }

    @Test
    public void findOne() {
        YourEntity entity = yourMapper.findOne(new YourQuery()
            .where.id().eq(4L).end()
        );
        System.out.println(entity);
    }

    @Test
    public void findOne2() {
        YourEntity entity = yourMapper.findOne(new YourQuery()
            .where.name().eq("Fluent Mybatis").end()
        );
        System.out.println(entity);
    }

    @Test
    public void listByMap() {
        List<YourEntity> entities = yourMapper.listByMap(true, new HashMap<String, Object>() {
            {
                this.put("name", "Fluent Mybatis");
                this.put("is_deleted", false);
            }
        });
        System.out.println(entities);
    }

    @Test
    public void listEntity() {
        List<YourEntity> entities = yourMapper.listEntity(new YourQuery()
            .select.name().age().email().end()
            .where.id().lt(6L)
            .and.name().like("Fluent").end()
            .orderBy.id().desc().end()
        );
        System.out.println(entities);
    }


    @Test
    public void listMaps() {
        List<Map<String, Object>> maps = yourMapper.listMaps(new YourQuery()
            .select.name().age().email().end()
            .where.id().lt(6L)
            .and.name().like("Fluent").end()
            .orderBy.id().desc().end()
        );
        System.out.println(maps);
    }


    @Test
    public void listObjs() {
        List<String> ids = yourMapper.listObjs(new YourQuery()
            .select.name().age().email().end()
            .where.id().lt(6L)
            .and.name().like("Fluent").end()
            .orderBy.id().desc().end()
        );
        System.out.println(ids);
    }

    @Test
    public void count() {
        int count = yourMapper.count(new YourQuery()
            .where.id().lt(1000L)
            .and.name().like("Fluent").end()
            .limit(0, 10)
        );
        System.out.println(count);
    }

    @Test
    public void countNoLimit() {
        int count = yourMapper.countNoLimit(new YourQuery()
            .where.id().lt(1000L)
            .and.name().like("Fluent").end()
            .limit(0, 10)
        );
        System.out.println(count);
    }

    @Test
    public void updateById() {
        int count = yourMapper.updateById(new YourEntity()
            .setId(2L)
            .setName("Powerful Fluent Mybatis")
        );
        System.out.println(count);
    }


    @Test
    public void updateBy() {
        int count = yourMapper.updateBy(new YourUpdate()
            .set.name().is("Powerful Fluent mybatis")
            .set.email().is("darui.wu@163.com")
            .set.age().is(1).end()
            .where.id().eq(2).end()
        );
        System.out.println(count);
    }
}