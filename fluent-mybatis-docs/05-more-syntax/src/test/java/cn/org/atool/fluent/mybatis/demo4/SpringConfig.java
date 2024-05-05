package cn.org.atool.fluent.mybatis.demo4;


import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"cn.org.atool.fluent.mybatis.demo4"})
@MapperScan({"cn.org.atool.fluent.mybatis.demo4.mapper"})
public class SpringConfig {
    @Bean("dataSource")
    public DataSource newDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3307/db_fluent_mybatis?useUnicode=true&characterEncoding=utf8");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean;
    }

    @Bean
    public MapperFactory mapperFactory() {
        return new MapperFactory();
    }
}
