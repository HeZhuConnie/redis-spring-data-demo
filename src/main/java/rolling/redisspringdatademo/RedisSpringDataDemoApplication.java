package rolling.redisspringdatademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class RedisSpringDataDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisSpringDataDemoApplication.class, args);
	}

}
