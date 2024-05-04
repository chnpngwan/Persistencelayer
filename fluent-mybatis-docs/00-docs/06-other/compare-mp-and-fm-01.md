## 剖析Mybatis Plus实现动态SQL语句的机理

### 入口类：MybatisSqlSessionFactoryBuilder
通过在入口类 MybatisSqlSessionFactoryBuilder#build方法中, 在应用启动时, 将mybatis plus(简称MP)自定义的动态配置xml文件注入到Mybatis中。
```java
public class MybatisSqlSessionFactoryBuilder extends SqlSessionFactoryBuilder {
    public SqlSessionFactory build(Configuration configuration) {
            // ... 省略若干行 
            if (globalConfig.isEnableSqlRunner()) {
                new SqlRunnerInjector().inject(configuration);
            }
            // ... 省略若干行 
            return sqlSessionFactory;
        }
}
```

这里涉及到2个MP2个重要的功能类

1. 扩展继承自Mybatis的MybatisConfiguration类: MP动态脚本构建，注册，及其它逻辑判断。
2. SqlRunnerInjector: MP默认插入一些动态方法的xml 脚本方法。

### MybatisConfiguration类
这里我们重点剖析MybatisConfiguration类，在MybatisConfiguration中，MP初始化了其自身的MybatisMapperRegistry，而MybatisMapperRegistry是MP加载自定义的SQL方法的注册器。

#### MybatisConfiguration中很多方法是使用MybatisMapperRegistry进行重写实现
其中有3个重载方法addMapper实现了注册MP动态脚本的功能。

```java
public class MybatisConfiguration extends Configuration {
    /**
     * Mapper 注册
     */
    protected final MybatisMapperRegistry mybatisMapperRegistry = new MybatisMapperRegistry(this);
    // ....

    /**
     * 初始化调用
     */
    public MybatisConfiguration() {
        super();
        this.mapUnderscoreToCamelCase = true;
        languageRegistry.setDefaultDriverClass(MybatisXMLLanguageDriver.class);
    }

    /**
     * MybatisPlus 加载 SQL 顺序：
     * <p> 1、加载 XML中的 SQL </p>
     * <p> 2、加载 SqlProvider 中的 SQL </p>
     * <p> 3、XmlSql 与 SqlProvider不能包含相同的 SQL </p>
     * <p>调整后的 SQL优先级：XmlSql > sqlProvider > CurdSql </p>
     */
    @Override
    public void addMappedStatement(MappedStatement ms) {
        // ...
    }
    
    // ... 省略若干行 
    /**
     * 使用自己的 MybatisMapperRegistry
     */
    @Override
    public <T> void addMapper(Class<T> type) {
        mybatisMapperRegistry.addMapper(type);
    }
    // .... 省略若干行 
}
```

#### 在MybatisMapperRegistry中，MP将mybatis的MapperAnnotationBuilder替换为MP自己的MybatisMapperAnnotationBuilder
```java
public class MybatisMapperRegistry extends MapperRegistry {
    @Override
    public <T> void addMapper(Class<T> type) {
        // ... 省略若干行 
        MybatisMapperAnnotationBuilder parser = new MybatisMapperAnnotationBuilder(config, type);
        parser.parse();
        // ... 省略若干行 
    }
}
```

在MybatisMapperRegistry类的addMapper方法中，才真正进入到MP的核心类MybatisMapperAnnotationBuilder，MybatisMapperAnnotationBuilder这个类是MP实现动态脚本的关键类。

#### MybatisMapperAnnotationBuilder动态构造
在MP的核心类MybatisMapperAnnotationBuilder的parser方法中，MP逐一遍历要加载的Mapper类，加载的方法包括下面几个
```java
public class MybatisMapperAnnotationBuilder extends MapperAnnotationBuilder {
    @Override
    public void parse() {
        //... 省略若干行 
        for (Method method : type.getMethods()) {
            /** for循环代码, MP判断method方法是否是@Select @Insert等mybatis注解方法**/
            parseStatement(method);
            InterceptorIgnoreHelper.initSqlParserInfoCache(cache, mapperName, method);
            SqlParserHelper.initSqlParserInfoCache(mapperName, method);
        }
        /** 这2行代码, MP注入默认的方法列表**/
        if (GlobalConfigUtils.isSupperMapperChildren(configuration, type)) {
            GlobalConfigUtils.getSqlInjector(configuration).inspectInject(assistant, type);
        }
        //... 省略若干行 
    }

    @Override
    public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
        Class<?> modelClass = extractModelClass(mapperClass);
        //... 省略若干行 
        List<AbstractMethod> methodList = this.getMethodList(mapperClass);
        TableInfo tableInfo = TableInfoHelper.initTableInfo(builderAssistant, modelClass);
        // 循环注入自定义方法
        methodList.forEach(m -> m.inject(builderAssistant, mapperClass, modelClass, tableInfo));
        mapperRegistryCache.add(className);
    }
}
public class DefaultSqlInjector extends AbstractSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        return Stream.of(
            new Insert(),
            //... 省略若干行 
            new SelectPage()
        ).collect(toList());
    }
}
```

在MybatisMapperAnnotationBuilder中，MP真正将框架自定义的动态的SQL语句注册到Mybatis引擎中。而AbstractMethod则履行了具体方法的SQL语句构造。

#### 具体的AbstractMethod实例类，构造具体的方法SQL语句
已 SelectById 这个类为例说明下
```java
/**
 * 根据ID 查询一条数据
 */
public class SelectById extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        /** 定义 mybatis xml method id, 对应 <id="xyz"> **/
        SqlMethod sqlMethod = SqlMethod.SELECT_BY_ID;
        /** 构造id对应的具体xml片段 **/
        SqlSource sqlSource = new RawSqlSource(configuration, String.format(sqlMethod.getSql(),
            sqlSelectColumns(tableInfo, false),
            tableInfo.getTableName(), tableInfo.getKeyColumn(), tableInfo.getKeyProperty(),
            tableInfo.getLogicDeleteSql(true, true)), Object.class);
        /** 将xml method方法添加到mybatis的MappedStatement中 **/
        return this.addSelectMappedStatementForTable(mapperClass, getMethod(sqlMethod), sqlSource, tableInfo);
    }
}
```

至此，MP完成了在启动时加载自定义的方法xml配置的过程，后面的就是mybatis ${变量} #{变量}的动态替换和预编译，已经进入mybatis自有功能。

### 总结一下
MP总共改写和替换了mybatis的十多个类，主要在入下图所示:
![-w300](../../images/mybatis-plus-01.png)

总体上来说，略显繁琐和不够直观，其实根据MybatisMapperAnnotationBuilder构造出自定义方法的xml文件，将其转换为mybatis的Resource资源，完全可以只需要修改一个Sql
比如如下：
```java
public class YourSqlSessionFactoryBean extends SqlSessionFactoryBean implements ApplicationContextAware {

    private Resource[] mapperLocations;

    @Override
    public void setMapperLocations(Resource... mapperLocations) {
        super.setMapperLocations(mapperLocations);
        /** 暂存使用mybatis原生定义的mapper xml文件路径**/
        this.mapperLocations = mapperLocations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        /** 只需要通过将自定义的方法构造成xml resource和原生定义的Resource一起注入到mybatis中即可, 这样就可以实现MP的自定义动态SQL和原生SQL的共生关系**/
        this.setMapperLocations(InjectMapper.getMapperResource(this.dbType, beanFactory, this.mapperLocations));
        super.afterPropertiesSet();
        if (banner) {
            System.out.println(MybatisUtil.getVersionBanner());
        }
    }
}
```

在这边文章中，简单介绍了MP实现动态语句的实现过程，并且给出一个更便捷的方法。在后续文章中，将会介绍fluent mybatis是如何实现不重写mybatis任何系统方法达到动态语句的机制。