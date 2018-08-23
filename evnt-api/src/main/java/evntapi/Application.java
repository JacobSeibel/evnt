package evntapi;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import javax.sql.DataSource;
import java.util.Arrays;

@SpringBootApplication
@Configuration
@MapperScan(basePackages = "evntapi.rest.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
@ImportResource({"classpath*:applicationContext.xml"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private javax.sql.DataSource dataSource;
    @Autowired
    private ResourceLoader resourceLoader;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage("src.main.java.domain");
        sessionFactory.setMapperLocations(ResourcePatternUtils.getResourcePatternResolver(resourceLoader).
                getResources("classpath:persistence/mapper/*.xml"));
        return sessionFactory.getObject();
    }
}