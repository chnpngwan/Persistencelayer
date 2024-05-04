package cn.org.fluent.mybatis.many2many.demo.dao;

import cn.org.fluent.mybatis.many2many.demo.IEntityRelation;
import cn.org.fluent.mybatis.many2many.demo.Ref;
import cn.org.fluent.mybatis.many2many.demo.entity.MemberEntity;
import cn.org.fluent.mybatis.many2many.demo.wrapper.MemberLoveQuery;
import cn.org.fluent.mybatis.many2many.demo.wrapper.MemberQuery;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AllRelationQuery implements IEntityRelation {
    @Override
    public List<MemberEntity> findExFriendsOfMemberEntity(MemberEntity entity) {
        return findExFriendsOfMemberEntity3(entity);
    }

    public List<MemberEntity> findExFriendsOfMemberEntity3(MemberEntity entity) {
        return new MemberQuery()
            .where.isDeleted().isFalse()
            .and.id().in(new MemberLoveQuery()
                .select(entity.getIsGirl() ? Ref.Field.MemberLove.boyId.column : Ref.Field.MemberLove.girlId.column)
                .where.status().eq("前任")
                .and.isDeleted().isFalse()
                .and.girlId().eq(entity.getId(), o -> entity.getIsGirl())
                .and.boyId().eq(entity.getId(), o -> !entity.getIsGirl())
                .end())
            .end().to().listEntity();
    }

    public List<MemberEntity> findExFriendsOfMemberEntity2(MemberEntity entity) {
        if (entity.getIsGirl()) {
            return new MemberQuery()
                .where.isDeleted().isFalse()
                .and.id().in(new MemberLoveQuery()
                    .select.boyId().end()
                    .where.status().eq("前任")
                    .and.isDeleted().isFalse()
                    .and.girlId().eq(entity.getId())
                    .end())
                .end().to().listEntity();
        } else {
            return new MemberQuery()
                .where.isDeleted().isFalse()
                .and.id().in(new MemberLoveQuery()
                    .select.girlId().end()
                    .where.status().eq("前任")
                    .and.isDeleted().isFalse()
                    .and.boyId().eq(entity.getId())
                    .end())
                .end().to().listEntity();
        }
    }

    public List<MemberEntity> findExFriendsOfMemberEntity1(MemberEntity entity) {
        return new MemberQuery()
            .where.isDeleted().isFalse()
            .and.id().in(entity.getIsGirl(), new MemberLoveQuery()
                .select.boyId().end()
                .where.status().eq("前任")
                .and.isDeleted().isFalse()
                .and.girlId().eq(entity.getId())
                .end())
            .and.id().in(!entity.getIsGirl(), new MemberLoveQuery()
                .select.girlId().end()
                .where.status().eq("前任")
                .and.isDeleted().isFalse()
                .and.boyId().eq(entity.getId())
                .end())
            .end().to().listEntity();
    }

    @Override
    public MemberEntity findCurrFriendOfMemberEntity(MemberEntity entity) {
        return new MemberQuery()
            .where.isDeleted().isFalse()
            .and.id().in(new MemberLoveQuery()
                .select(entity.getIsGirl() ? Ref.Field.MemberLove.boyId.column : Ref.Field.MemberLove.girlId.column)
                .where.status().eq("现任")
                .and.isDeleted().isFalse()
                .and.girlId().eq(entity.getId(), o -> entity.getIsGirl())
                .and.boyId().eq(entity.getId(), o -> !entity.getIsGirl())
                .end())
            .end().to().findOne().orElse(null);
    }
}
