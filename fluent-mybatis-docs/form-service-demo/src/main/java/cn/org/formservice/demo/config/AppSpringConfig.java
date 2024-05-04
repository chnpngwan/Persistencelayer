package cn.org.formservice.demo.config;

import cn.org.atool.fluent.form.FormKit;
import cn.org.atool.fluent.form.annotation.FormServiceScan;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import cn.org.formservice.demo.shared.entity.StudentEntity;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {
    "cn.org.formservice.demo.shared.mapper"
}, markerInterface = IMapper.class)
@FormServiceScan({"cn.org.formservice.demo.service"})
public class AppSpringConfig {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLazyLoadingEnabled(true);
        configuration.setMapUnderscoreToCamelCase(true);
        bean.setConfiguration(configuration);
        return bean;
    }

    @Bean
    public MapperFactory mapperFactory() {
        return new MapperFactory()
            .initializer(() -> FormKit.mapping("student", StudentEntity.class));
    }
}