package cn.org.atool.fluent.mybatis.demo4.where;

import cn.org.atool.fluent.mybatis.demo4.BaseTest;
import cn.org.atool.fluent.mybatis.demo4.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo4.wrapper.ReceivingAddressQuery;
import cn.org.atool.fluent.mybatis.demo4.wrapper.UserQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.List;

/**
 * fluent mybatis嵌套查询示例
 */
public class NestQueryTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @DisplayName("嵌套查询和主查询的表是同一个")
    @Test
    void test_in_same_table_query() {
        UserQuery query = new UserQuery()
            .where.id().in(q -> q.selectId()
                .where.id().eq(3L).end())
            .and.userName().like("user")
            .and.age().gt(23).end();

        List list = mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .end("FROM `user` WHERE `id` IN " +
                "   (SELECT `id` FROM `user` WHERE `id` = ?) " +
                "AND `user_name` LIKE ? " +
                "AND `age` > ?", StringMode.SameAsSpace);
    }

    @DisplayName("嵌套查询和主查询的表是不同")
    @Test
    void test_in_difference_table_query() {
        UserQuery query = new UserQuery()
            .selectId()
            .where.addressId().in(new ReceivingAddressQuery().selectId()
                .where.id().in(new int[]{1, 2}).end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` " +
                "FROM `user` " +
                "WHERE `address_id` IN " +
                "   (SELECT `id` FROM `receiving_address` WHERE `id` IN (?, ?))", StringMode.SameAsSpace);
    }

    @DisplayName("EXISTS查询")
    @Test
    void test_exists_query() {
        UserQuery query = new UserQuery()
            .where.exists(new ReceivingAddressQuery().select("1")
                .where.detailAddress().like("杭州")
                .and.id().apply(" = user.address_id").end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id`, `account`, `address_id`, `age`, `avatar`, `birthday`, `bonus_points`, `e_mail`, `password`, `phone`, `status`, `user_name`, `gmt_created`, `gmt_modified`, `is_deleted` " +
                    "FROM `user` " +
                    "WHERE EXISTS (SELECT 1" +
                    "   FROM `receiving_address`" +
                    "   WHERE `detail_address` LIKE ?" +
                    "   AND `id` = user.address_id)",
                StringMode.SameAsSpace);
    }
}