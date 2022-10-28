package rolling.redisspringdatademo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import rolling.redisspringdatademo.service.User;

@SpringBootTest
class RedisSpringDataDemoApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RedisTemplate<String, Object> updatedRedisTemplate;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	void testString() {
		redisTemplate.opsForValue().set("name", "Jack");

		Object name = redisTemplate.opsForValue().get("name");
		System.out.print("name = " + name);
	}

	@Test
	void testCustomized() {
		updatedRedisTemplate.opsForValue().set("name", "Jack");

		Object name = updatedRedisTemplate.opsForValue().get("name");
		System.out.print("name = " + name);
	}

	@Test
	void testStringRedisTemplate() throws JsonProcessingException {
		User user = new User();
		user.setName("Jack");
		user.setAge(50);

		ObjectMapper mapper = new ObjectMapper();
		String s = mapper.writeValueAsString(user);

		stringRedisTemplate.opsForValue().set("user:200", s);

		Object user200 = stringRedisTemplate.opsForValue().get("user:200");
		System.out.print("user = " + user200);
	}

}
