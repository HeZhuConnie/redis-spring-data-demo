package rolling.redisspringdatademo.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisWorker {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final long INITIAL_TIMESTAMP =
            LocalDateTime.of(2022, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
    private static final int COUNT_BIT = 32;

    public long getAutoId(String keyPrefix) {
        // timestamp
        long initial = LocalDateTime.of(2022, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
        long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - initial;

        // incremented id
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long incrementId = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);

        return timestamp << COUNT_BIT | incrementId;
    }
}
