package cn.org.fluent.mybatis.many2many.demo.test;

import cn.org.fluent.mybatis.many2many.demo.Application;
import cn.org.fluent.mybatis.many2many.demo.entity.MemberEntity;
import cn.org.fluent.mybatis.many2many.demo.entity.MemberFavoriteEntity;
import cn.org.fluent.mybatis.many2many.demo.mapper.MemberFavoriteMapper;
import cn.org.fluent.mybatis.many2many.demo.mapper.MemberMapper;
import cn.org.fluent.mybatis.many2many.demo.wrapper.MemberFavoriteQuery;
import cn.org.fluent.mybatis.many2many.demo.wrapper.MemberQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = Application.class)
public class FindMemberFavoriteTest {
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberFavoriteMapper favoriteMapper;

    @BeforeEach
    public void setup() {
        memberMapper.delete(new MemberQuery());
        favoriteMapper.delete(new MemberFavoriteQuery());
        memberMapper.save(new MemberEntity()
            .setId(1L)
            .setUserName("FluentMybatis")
        );
        favoriteMapper.save(Arrays.asList(
            new MemberFavoriteEntity()
                .setMemberId(1L)
                .setFavorite("爬山"),
            new MemberFavoriteEntity()
                .setMemberId(1L)
                .setFavorite("电影")
        ));
    }

    @Test
    public void findMyFavorite() {
        MemberEntity member = memberMapper.findById(1L);
        List<MemberFavoriteEntity> favorites = member.findMyFavorite();
        System.out.println("爱好项: " + favorites.size());
    }
}