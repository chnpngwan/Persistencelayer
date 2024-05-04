
## 使用fluent mybatis生成Entity文件
增加生成代码的jar包依赖
```xml
<dependency>
    <groupId>com.github.atool</groupId>
    <artifactId>fluent-mybatis-processor</artifactId>
    <version>${fluent.mybatis.version}</version>
    <scope>provided</scope>
</dependency>
```

### 使用注解定义方式
```java
public class EntityGeneratorDemo {
    public static final String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";

    public static void main(String[] args) throws Exception {
        FileGenerator.build(Empty.class);
    }

    @Tables(
        // 设置数据库连接信息
        url = url, username = "root", password = "password",
        // 设置entity类生成src目录, 相对于 user.dir
        srcDir = "src/main/java",
        // 设置entity类的package值
        basePack = "cn.org.atool.fluent.mybatis.demo3",
        // 设置dao接口和实现的src目录, 相对于 user.dir
        daoDir = "src/main/java",
        // 设置哪些表要生成Entity文件
        tables = {@Table(value = {"hello_world"})}
    )
    static class Empty {
    }
}
```