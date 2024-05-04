mybatis增强包
- [mybatis-plus](https://mp.baomidou.com/guide/)
- [TK-MyBatis](https://gitee.com/free/Mapper)
https://mapperhelper.github.io/docs/
https://github.com/abel533/Mapper

## 区别一
mybatis-plus和TK-MyBatis在构造条件的语法上比较接近, 基本上都是 条件操作("字段", "条件值"), 
需要对条件字段进行硬编码，这样导致了几个问题：

    1. 容易书写错误
    2. 需要记忆字段的名称
    3. 当数据库字段发生变更时，不容易第一时间发现错误

fluent-mybatis借鉴了plus和TK的想法，但通过使用更多的泛型技巧，避免了对字段设置的硬编码,
所有的操作可以一气呵成。


- mybatis plus代码
``` java
new UpdateWrapper()
.set("age", 34)
.eq("user_name", "myname");
```
   需要硬编码字段"age"和"user_name"

- TK-mybatis代码
``` java
Example example = new Example(Country.class);
example.setForUpdate(true);
example.createCriteria().andGreaterThan("id", 100).andLessThan("id",151);
example.or().andLessThan("id", 41);
```

- fluent mybatis代码
``` java
new UserUpdate()
    .set
    .age().is(34).end()
    .where
    .userName().eq("myname").end();
```
    可以直接引用字段 age()和 userName()
    
**_区别点_** : 通过上面的3段代码，可以看出几点不同
1. fluent mybatis的段落感比较强，同时在IDE有语法渲染效果
2. 没有字段的硬编码，不容易书写错误
3. 流式接口，减少记忆成本
4. 当字段发生变更时，IDE也可以第一时间发现语法错误。
    
fluent mybatis代码IDE语法渲染效果

![](10/compare.png)   
    
## 区别二： 对mybatis类的重写
- mybatis-plus重写了mybatis有十来个类。
- fluent mybatis仅仅继承了mybatis一个类: SqlSessionFactoryBean
```java
@Accessors(chain = true)
public class FluentMybatisSessionFactoryBean extends SqlSessionFactoryBean implements ApplicationContextAware {
    // ...
}
```
    更少的修改，以保持对mybatis的无侵入和兼容
## 区别三：对分页查询的支持
- mybatis-plus和TK-MyBatis引入了 com.github.pagehelper:pagehelper 分页支持
- fluent-mybatis根据数据库类型，直接生成limit分页语法


其他
- [MyBatis Enhance](https://gitee.com/hengboy/mybatis-enhance)
- [fastmybatis](https://durcframework.gitee.io/fastmybatis/#/)