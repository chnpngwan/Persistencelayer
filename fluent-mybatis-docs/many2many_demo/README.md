# FluentMybatis入门介绍四: 处理多对多的男女关系
对底层数据表关联关系的处理，我们总是绕不开什么一对一，一对多，多对多这里比较烦人的关系。
业界优秀的ORM框架也都给出了自己的答案，简单来说就以下几种方式：

1. hibernate和JPA对开发基本屏蔽了底层数据的处理，只需要在model层设置数据级联关系即可。但这种设置也往往是噩梦的开始。
2. mybatis 提供了简单的@One @Many注解，然后编写xml映射关系来提供级联处理。
3. 还有一种就是干脆不依赖框架，直接应用自己掌控。

因为FluentMybatis是基于mybatis上做封装和扩展的，所以这里主要聊聊mybatis处理的方式，以及给出FluentMybatis的解放方案。

如前面几篇文章一样，我们先设定业务场景，有下面场景: 男孩和女孩，男女正常状态下只有一个现任（同时脚踩多艘船的这里不讨论，O(∩_∩)O哈哈~），但可能有多个前任; 同时，他们还有各自的爱好。

那么就可以建以下3张表：

- 数据字典
```sql
CREATE TABLE t_member
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    user_name    varchar(45) DEFAULT NULL COMMENT '名字',
    is_girl      tinyint(1)  DEFAULT 0 COMMENT '0:男孩; 1:女孩',
    age          int         DEFAULT NULL COMMENT '年龄',
    school       varchar(20) DEFAULT NULL COMMENT '学校',
    gmt_created  datetime    DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime    DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(1)  DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '成员表:女孩或男孩信息';

CREATE TABLE t_member_love
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    girl_id      bigint(21) NOT NULL COMMENT 'member表外键',
    boy_id       bigint(21) NOT NULL COMMENT 'member表外键',
    status       varchar(45) DEFAULT NULL COMMENT '状态',
    gmt_created  datetime    DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime    DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(2)  DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '成员恋爱关系';

CREATE TABLE t_member_favorite
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    member_id    bigint(21) NOT NULL COMMENT 'member表外键',
    favorite     varchar(45) DEFAULT NULL COMMENT '爱好: 电影, 爬山, 徒步...',
    gmt_created  datetime    DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime    DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(2)  DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '成员爱好';
```

- 添加项目Maven依赖
[具体pom.xml文件](https://gitee.com/fluent-mybatis/fluent-mybatis-docs/blob/master/many2many-demo/pom.xml)

- 代码生成
```java
public class AppEntityGenerator {
    static final String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useSSL=false&useUnicode=true&characterEncoding=utf-8";
    /**
     * 生成代码的package路径
     */
    static final String basePackage = "cn.org.fluent.mybatis.many2many.demo";

    public static void main(String[] args) {
        FileGenerator.build(Noting.class);
    }

    @Tables(
        /** 数据库连接信息 **/
        url = url, username = "root", password = "password",
        /** Entity类parent package路径 **/
        basePack = basePackage,
        /** Entity代码源目录 **/
        srcDir = "example/many2many_demo/src/main/java",
        /** 如果表定义记录创建，记录修改，逻辑删除字段 **/
        gmtCreated = "gmt_create", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        /** 需要生成文件的表 ( 表名称:对应的Entity名称 ) **/
        tables = @Table(value = {"t_member", "t_member_love", "t_member_favorite"}, tablePrefix = "t_")
    )
    static class Noting {
    }
}
```
这样就生成了3个Entity类: MemberEntity, MemberFavoriteEntity, MemberLoveEntity。

## 关系分析
现在我们来理一理这里面的关系

- 一对多: 一个成员可以有多个爱好
- 多对多: 一个成员可以有多个男女朋友(前任+现任)
- 一对一: 一个成员只能有一个现任男女朋友

## mybatis处理手法
mybatis提供了@One 和 @Many的注解来处理简单(**只有主键和外键依赖**)的一对一，和一对多的关系
具体到上面的关系，mybatis只能关联查询成员的爱好，对带条件的(不是只通过外键)现任男女朋友的一对一也没有办法处理。

我这里就不具体展开mybatis的配置语法了，感兴趣的读者可以直接参考掘金里面的文章:

[Mybatis一对多、多对一处理](https://juejin.im/post/6844904110135705607)

[MyBatis复杂Sql查询（一对一，一对多）](https://juejin.im/post/6844904183922081800)

[MyBatis学习总结（四），注解 & 多对一、一对多](https://juejin.im/post/6887170865369677832)

[MyBatis系列4：一对一,一对多,多对多查询及延迟加载(N+1问题)分析](https://juejin.im/post/6889278828238077965)

鉴于mybatis只能处理简单的关联关系，fluent mybatis就没有直接封装mybatis的处理方式，那fluent mybatis是如何处理上述的关联关系的。
我们先从mybatis也可以处理的一对多的爱好列表入手

## 一对多的爱好列表处理
fluent mybatis要根据MemberEntity自动返回对应的爱好列表，需要下面几个设置:

1. MemberEntity继承RichEntity基类
2. 在MemberEntity类里面增加方法 findMyFavorite()
3. 给findMyFavorite方法加上注解 @RefMethod
4. 在注解中增加关联关系: "memberId=id"，意思是 MemberFavoriteEntity.memberId等于MemberEntity.id

具体代码片段如下, **所有这些操作都可以通过代码生成，这里手工添加仅仅是为了讲解***

```java
public class MemberEntity extends RichEntity implements IEntity {
    // ...
    /**
     * 我的爱好列表
     *
     * @return
     */
    @RefMethod("memberId=id")
    public List<MemberFavoriteEntity> findMyFavorite() {
        return super.loadCache("findMyFavorite", MemberEntity.class);
    }
}
```

好了，我们已经建立好通过Member实例查询爱好列表的功能了，**重新编译项目**
在generated-sources目录下面，会多出一个文件: Refs
```java
/**
 *
 * Refs: 
 *  o - 查询器，更新器工厂类单例引用
 *  o - 应用所有Mapper Bean引用
 *  o - Entity关联对象延迟加载查询实现
 *
 * @author powered by FluentMybatis
 */
public abstract class Refs extends EntityRefQuery {
  public List<MemberFavoriteEntity> findMyFavoriteOfMemberEntity(MemberEntity entity) {
    return memberFavoriteMapper.listEntity(new MemberFavoriteQuery()
        .where.memberId().eq(entity.getId())
        .end());
  }
}
```

在这个类里面自动生成了一个方法: findMyFavoriteOfMemberEntity, 入参是MemberEntity, 出参是List<MemberFavoriteEntity>，
实现里面根据member的id查询了成员的所有爱好。

- 增加Spring Bean
我们新建一个类: AllRelationQuery(名称根据你的喜好和业务随便取), 继承Refs, 并把AllRelationQuery加入Spring管理即可。

```java
@Service
public class AllRelationQuery extends Refs {
}
```

- 老套路，写个测试验证下
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class FindMemberFavoriteTest {
    @Autowired
    private MemberMapper memberMapper;

    @Before
    public void setup() {
        // 省略数据准备部分
    }

    @Test
    public void findMyFavorite() {
        MemberEntity member = memberMapper.findById(1L);
        List<MemberFavoriteEntity> favorites = member.findMyFavorite();
        System.out.println("爱好项: " + favorites.size());
    }
}
```

- 查看控制台log输出
```text
DEBUG - ==>  Preparing: SELECT id, ..., user_name FROM t_member WHERE id = ?  
DEBUG - ==> Parameters: 1(Long) 
DEBUG - &lt;==      Total: 1 
DEBUG - ==>  Preparing: SELECT id, ..., member_id FROM t_member_favorite WHERE member_id = ?  
DEBUG - ==> Parameters: 1(Long) 
DEBUG - &lt;==      Total: 2 
爱好项: 2
```

如日志所示，Fluent Mybatis按照预期返回了爱好列表。

## 给一对多关系添点油加点醋
做过业务系统的同学都知道，数据库中业务数据一般会有一个逻辑删除标识，按照上述逻辑查询出来的数据，
我们会把已经废弃(逻辑删除掉)的爱好也一并查询出来了，那我们如何只查询出未逻辑删除(is_deleted=0)的爱好列表呢。

如果采用mybatis的方案，那我们只能耸耸肩，摊开双手说: "爱莫能助，你自己写SQL实现吧"，
但fluent mybatis对这类场景的支持的很好，我们只要给@RefMethod注解值加点条件就可以了,
**MemberFavoriteEntity.memberId=MemberEntity.id并且Favorite的逻辑删除标识和Member表一样**，具体定义如下:
```java
public class MemberEntity extends RichEntity implements IEntity {
    @RefMethod("memberId=id && isDeleted=isDeleted")
    public List<MemberFavoriteEntity> findMyFavorite() {
        return super.loadCache("findMyFavorite", MemberEntity.class);
    }    
}
```

- 重新编译项目，观察Refs代码
```java
public abstract class Refs extends EntityRefQuery {
    public List<MemberFavoriteEntity> findMyFavoriteOfMemberEntity(MemberEntity entity) {
        return memberFavoriteMapper.listEntity(new MemberFavoriteQuery()
            .where.isDeleted().eq(entity.getIsDeleted())
            .and.memberId().eq(entity.getId())
            .end());
    }
}
```
查询条件上带上了逻辑删除条件

- 跑测试，看log
```text
DEBUG - ==>  Preparing: SELECT id, ..., user_name FROM t_member WHERE id = ?  
DEBUG - ==> Parameters: 1(Long) 
DEBUG - <==      Total: 1 
DEBUG - ==>  Preparing: SELECT id, ..., member_id FROM t_member_favorite 
    WHERE is_deleted = ? 
    AND member_id = ?  
DEBUG - ==> Parameters: false(Boolean), 1(Long) 
DEBUG - <==      Total: 2 
爱好项: 2
```

FluentMybatis轻松处理了多条件关联的一对多关系, 这个在业务中不仅仅限定于逻辑删除，
还可以推广到部署环境标识(deploy_env), 租户关系等条件上，还有只有你业务中才用到的状态相关的关系上。

## Fluent Mybatis对多对多的处理
fluent mybatis可以轻松处理一对一，一对多的简单和多条件的关联关系，但对多对多也没有提供自动化代码生成的处理手段。
因为多对多，本质上涉及到3张表， A表， B表，AB关联表。
但fluent mybatis还是提供了半自动手段，对这类场景进行了支持，比如我们需要MemberEntity中返回所有前任恋人列表。
在MemberEntity中定义方法: exFriends()

```java
public class MemberEntity extends RichEntity implements IEntity {
    /**
     * 前任男(女)朋友列表
     *
     * @return
     */
    @RefMethod
    public List<MemberEntity> findExFriends() {
        return super.loadCache("findExFriends", MemberEntity.class);
    }    
}
```
和上面的自动化的一对多关系有个区别，@RefMethod上没有设置查询条件，我们重新编译项目。
我们观察Refs类，除了刚才的findMyFavoriteOfMemberEntity方法实现外，还多出一个抽象方法: findExFriendsOfMemberEntity
```java
public abstract class Refs extends EntityRefQuery {
    /**
     * 返回前任男(女)朋友列表
     */
    public abstract List<MemberEntity> findExFriendsOfMemberEntity(MemberEntity entity);
}
```

- 在动手实现代码前，我们先分析一下混乱的男女朋友关系
在member表上，我们使用了一个性别字段 is_girl来区别是男的还是女的， 在恋爱关系表上，分别有2个外键girl_id, boy_id来标识一对恋人关系。
这样，如果member是女的，要查询所有前任男朋友，那么sql语句就如下：

```sql
select * from t_member
where is_deleted=0
  and id in (select boy_id from t_memeber_love
              where status = '前任'
                and girl_id = ? -- 女孩id
                and is_deleted = 0
)  
```
如果member是男的，要查询所有前任女朋友，那么sql语句条件就要倒过来：
```sql
select * from t_member
where is_deleted=0
  and id in (select girl_id from t_memeber_love
              where status = '前任'
                and  boy_id= ? -- 男孩id
                and is_deleted = 0
)  
```


- 实现查询前男（女）朋友列表功能

一般来说，为了实现上面的分支查询，在mybatis的xml文件中需要配置<if> <else>这样的标签代码分支， 或者在java代码中实现 if(...){}else{}的代码逻辑分支。
那我们来看看fluent mybatis时如何实现上述查询的呢？我们就可以在刚才定义的Refs子类上实现findExFriendsOfMemberEntity自己的逻辑。

```java
@Service
public class AllRelationQuery extends Refs {
    @Override
    public List<MemberEntity> findExFriendsOfMemberEntity(MemberEntity entity) {
        MemberQuery query = new MemberQuery()
            .where.isDeleted().isFalse()
            .and.id().in(new MemberLoveQuery()
                .select(entity.getIsGirl() ? boyId.column : girlId.column)
                .where.status().eq("前任")
                .and.isDeleted().isFalse()
                .and.girlId().eq(entity.getId(), o -> entity.getIsGirl())
                .and.boyId().eq(entity.getId(), o -> !entity.getIsGirl())
                .end())
            .end();
        return memberMapper.listEntity(query);
    }
}
```

- 写测试看log
```java
@RunWith(SpringRunner.class)
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
}
```

控制台日志
```text
DEBUG - ==>  Preparing: SELECT id, ..., user_name FROM t_member WHERE id = ?  
DEBUG - ==> Parameters: 1(Long) 
DEBUG - <==      Total: 1 
是否女孩:true
DEBUG - ==>  Preparing: SELECT id, ..., user_name FROM t_member 
    WHERE is_deleted = ? 
    AND id IN (SELECT boy_id 
        FROM t_member_love 
        WHERE status = ? 
        AND is_deleted = ? 
        AND girl_id = ?)  
DEBUG - ==> Parameters: false(Boolean), 前任(String), false(Boolean), 1(Long) 
DEBUG - <==      Total: 1 
[MemberEntity(id=2, gmtModified=Sun Nov 08 12:31:57 CST 2020, isDeleted=false, age=null, gmtCreated=null, isGirl=false, school=null, userName=mike)]
```

如日志所示，在查询前男友列表是，条件会根据Member的是否是女孩进行分支切换，这也是fluent mybatis动态条件强大的地方。

## 代码生成设置
到这里，我们已经基本讲解了如何利用fluent mybatis来实现一对一，一对多，多对多，以及这些关系下的复杂逻辑实现，
但演示过程中Entity中的方法都是手工添加的，如果代码重新生成，Entity代码会被覆盖，提交代码的时候，就需要手动进行识别处理。

其实，上面演示用的Entity代码都可以设置成代码生成，这样就无需担心代码覆盖的问题，下面先给出完整的代码生成代码，然后再讲解各个设置作用。
```java
public class AppEntityFullGenerator {
    static final String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useSSL=false&useUnicode=true&characterEncoding=utf-8";
    /**
     * 生成代码的package路径
     */
    static final String basePackage = "cn.org.fluent.mybatis.many2many.demo";

    public static void main(String[] args) {
        FileGenerator.build(Noting.class);
    }

    @Tables(
        /** 数据库连接信息 **/
        url = url, username = "root", password = "password",
        /** Entity类parent package路径 **/
        basePack = basePackage,
        /** Entity代码源目录 **/
        srcDir = "example/many2many_demo/src/main/java",
        /** 如果表定义记录创建，记录修改，逻辑删除字段 **/
        gmtCreated = "gmt_create", gmtModified = "gmt_modified", logicDeleted = "is_deleted",
        /** 需要生成文件的表 ( 表名称:对应的Entity名称 ) **/
        tables = @Table(value = {"t_member", "t_member_love", "t_member_favorite"}, tablePrefix = "t_"),
        relations = {
             @Relation(method = "findMyFavorite", source = "t_member", target = "t_member_favorite", type = RelationType.OneWay_0_N
                , where = "id=member_id && is_deleted=is_deleted"),
             @Relation(method = "findExFriends", source = "t_member", target = "t_member", type = RelationType.OneWay_0_N),
             @Relation(method = "findCurrFriend", source = "t_member", target = "t_member", type = RelationType.OneWay_0_1)
        }
    )
    static class Noting {
    }
}
```
相比前面的生成代码，完整的生成代码在注解上多了 relations属性，relation属性就是用来在Entity代码上生成关联关系的，这里定义了3个@Relation

- findMyFavorite: 一对多关系, type= RelationType.OneWay_0_N
查找member的爱好

1. 源表对应的定义方法的Entity，这里是MemberEntity
2. 目标表是关联查询的返回Entity，这里是MemberFavoriteEntity, 并且因为关系是*对多，所以在生成代码时，返回值时List&lt;MemberFavoriteEntity>
3. where设置的是关联条件，可以有多个条件
这里值是"id=member_id && is_deleted=is_deleted"，表示
```sql
where t_member.id = t_member_favorite.id
and t_member.is_deleted = t_member_favorite.is_deleted
```
对应到MemberEntity生成代码就是findMyFavorite方法上的@RefMethod的注解值
```java
@RefMethod("isDeleted = isDeleted && memberId = id")
```

- findExFriends:
查找前任男（女）朋友列表
1. 一对多关系，源表和目标表是同一个
2. 这里没有设置where条件，表示@RefMethod上不会赋值，并且编译也不会自动生成代码，需要手工实现逻辑

- findCurrFriend
查找现任男（女）朋友
1. 一对一关系，源表和目标表是同一个
2. 没有设置where条件，需要手工实现逻辑

## 总结

fluent mybatis在处理关联关系上，功能优势还是比mybatis有所突破的，并且赋予了技术人员跟踪代码的能力。
如果大家觉的fluent mybatis不错，欢迎帮忙转发和点赞，让好的工具能普惠大家。

[文章示例代码](https://gitee.com/fluent-mybatis/fluent-mybatis-docs/tree/master/many2many_demo)

- fluent mybatis文章系列
[Fluent MyBatis入门介绍一](https://juejin.im/post/6884799624755249160)

[FluentMybatis入门介绍二](https://juejin.im/post/6887167888999792648)

[入门介绍三: 复杂查询&连表查询](https://juejin.im/post/6889804693912961038)

[Fluent Mybatis, 原生Mybatis, Mybatis Plus三者功能对比](https://juejin.im/post/6886019929519177735)

- fluent mybatis文档和源码
[Fluent Mybatis文档&示例](https://gitee.com/fluent-mybatis/fluent-mybatis-docs)

[Fluent Mybatis Gitee](https://gitee.com/fluent-mybatis/fluent-mybatis)

[Fluent Mybatis GitHub](https://github.com/atool/fluent-mybatis) 

