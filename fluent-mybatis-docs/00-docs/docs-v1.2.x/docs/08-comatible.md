# 和MyBatis原有实现一起玩耍
fluent mybatis并没有更改mybatis的代码，只是按照一定规则，在应用启动时，
在内存中生成了默认的fluent mybatis默认方法的xml文件，并自动加载到mybatis管理器中。
具体逻辑可以阅读FluentMybatisSessionFactoryBean的实现代码。

所以，如果你的工程有老的mybatis实现代码，包括Mapper和XML文件，也是可以简单升级使用fluent mybatis。

你要做的仅仅1个小动作：
1. 原有定义MybatisSessionFactoryBean替换为FluentMybatisSessionFactoryBean
2. 在定义FluentMybatisSessionFactoryBean时，保持原有的mapper xml资源加载。

``` java
@Bean
public FluentMybatisSessionFactoryBean sqlSessionFactoryBean() throws Exception {
    FluentMybatisSessionFactoryBean bean = new FluentMybatisSessionFactoryBean();
    bean.setDataSource(yourDataSource());
    bean.setMapperLocations(new ClassPathResource("你原有mapper xml文件"));
    return bean;
}
```
Mapper Bean的扫描路径包含生成的代码路径和原有的代码路径
``` java
@MapperScan({"fluent mybatis生成的Mapper路径", "你原有的Mapper路径"})
```

## 自定义数据操作方法
如果fluent mybatis提供的通用数据操作方法没法满足你的需要，或者你有比较复杂的多表关联操作，
你可以自定义Mapper方法和xml文件，通过上述方式加载。