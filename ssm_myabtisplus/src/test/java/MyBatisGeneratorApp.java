import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;

/**
 * ProjectName:  Persistencelayer
 * PackageName:  PACKAGE_NAME
 * ClassName:    MyBatisGeneratorApp
 *
 * @Author chnpngwng
 * @Date 2024 05 03 09 17
 **/
public class MyBatisGeneratorApp {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/db_persistencelayer?useUnicode=true&characterEncoding=utf8", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("chnpngwng") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\Github\\Persistencelayer\\ssm_myabtisplus\\src\\main\\java"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder -> {
                    builder.parent("com.student");       // 设置父包名
//                            .moduleName("sys")          //父包模块名
//                            .entity("po")               // Entity 包名
//                            .service("service")         // Service 包名
//                            .serviceImpl("service.impl") //Service Impl 包名
//                            .mapper("mapper")            //Mapper 包名
//                            .xml("mapper.xml")           // Mapper XML 包名
//                            .controller("controller")    //Controller 包名
//                            .build();
                })
/*      parent(String)	    父包名	            默认值:com.baomidou
        moduleName(String)	父包模块名	        默认值:无
        entity(String)	    Entity 包名	        默认值:entity
        service(String)	    Service 包名	        默认值:service
        serviceImpl(String)	Service Impl 包名	默认值:service.impl
        mapper(String)	    Mapper 包名	        默认值:mapper
        xml(String)	        Mapper XML 包名	    默认值:mapper.xml
        controller(String)	Controller 包名	    默认值:controller
        */
                .strategyConfig(builder -> {
                    builder.addInclude("student"); // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
