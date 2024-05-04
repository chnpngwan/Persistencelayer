package cn.org.fluent.mybatis.many2many.demo.test;

import cn.org.fluent.mybatis.many2many.demo.Application;
import cn.org.fluent.mybatis.many2many.demo.entity.MemberEntity;
import cn.org.fluent.mybatis.many2many.demo.entity.MemberLoveEntity;
import cn.org.fluent.mybatis.many2many.demo.mapper.MemberLoveMapper;
import cn.org.fluent.mybatis.many2many.demo.mapper.MemberMapper;
import cn.org.fluent.mybatis.many2many.demo.wrapper.MemberLoveQuery;
import cn.org.fluent.mybatis.many2many.demo.wrapper.MemberQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = Application.class)
public class FindExFriendsTest {
    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void findExBoyFriends() {
        MemberEntity member = memberMapper.findById(1L);
        System.out.println("是否女孩:" + member.getIsGirl());
        List<MemberEntity> boyFriends = member.findExFriends();
        System.out.println(boyFriends);
    }

    @Autowired
    private MemberLoveMapper loveMapper;

    @BeforeEach
    public void setup() {
        memberMapper.delete(new MemberQuery());
        loveMapper.delete(new MemberLoveQuery());
        memberMapper.save(Arrays.asList(
            MemberEntity.builder()
                .id(1L)
                .isGirl(true)
                .userName("mary")
                .isDeleted(false)
                .age(40)
                .build(),
            MemberEntity.builder()
                .id(2L)
                .isGirl(false)
                .userName("mike")
                .isDeleted(false)
                .age(34)
                .build()
        ));
        loveMapper.save(new MemberLoveEntity()
            .setGirlId(1L)
            .setBoyId(2L)
            .setStatus("前任")
            .setIsDeleted(false)
        );
    }
}
