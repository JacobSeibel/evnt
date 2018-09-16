package mailapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("application.properties")
@PropertySource("secure.properties")
@Configuration
//@ImportResource({"classpath*:applicationContext.xml"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}