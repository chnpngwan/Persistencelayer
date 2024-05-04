package cn.org.formservice.demo.test;

import cn.org.formservice.demo.config.AppSpringConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.test4j.integration.spring.SpringContext;
import org.test4j.junit5.Test4J;
import org.test4j.module.database.proxy.DataSourceCreator;

import javax.sql.DataSource;

@SpringContext(
    classes = {TestSpringConfig.class, AppSpringConfig.class},
    basePackages = {"cn.org.formservice.demo.service"}
)
public abstract class BaseTest extends Test4J {
}

@Configuration
class TestSpringConfig {
    @Bean("dataSource")
    public DataSource newDataSource() {
        return DataSourceCreator.create("dataSource");
    }
}