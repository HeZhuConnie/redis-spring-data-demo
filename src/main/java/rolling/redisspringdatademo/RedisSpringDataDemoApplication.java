package rolling.redisspringdatademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class RedisSpringDataDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisSpringDataDemoApplication.class, args);
	}

}
