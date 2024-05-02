import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ProjectName:  Persistencelayer
 * PackageName:  PACKAGE_NAME
 * ClassName:    MyBatisGenerator
 *
 * @Author chnpngwng
 * @Date 2024 05 02 11 49
 **/
public class MyBatisGeneratorApp {
    private static final Logger logger = LogManager.getLogger(MyBatisGeneratorApp.class);

    public static void main(String[] args) {
        try {
            // 使用类加载器加载配置文件
            InputStream inputStream = MyBatisGeneratorApp.class.getResourceAsStream("/generatorConfig.xml");

            // 创建 MyBatis Generator 对象
            ConfigurationParser cp = new ConfigurationParser(null);
            Configuration config = cp.parseConfiguration(inputStream);
            DefaultShellCallback callback = new DefaultShellCallback(true);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, new ArrayList<>());

            // 执行代码生成任务
            myBatisGenerator.generate(null);

            logger.info("代码生成成功！");
        } catch (IOException | XMLParserException | InvalidConfigurationException | SQLException |
                 InterruptedException e) {
            logger.error("代码生成失败！", e);
        }
    }

}
