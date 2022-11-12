package rolling.redisspringdatademo.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisLock implements ILock{

    private String lockName;
    private StringRedisTemplate redis;

    public RedisLock(String lockName, StringRedisTemplate redis) {
        this.lockName = lockName;
        this.redis = redis;
    }

    private static final String KEY_PREFIX="lock:";

    @Override
    public boolean tryLock(long timeoutSec) {
        // 获取线程标识
        long threadId = Thread.currentThread().getId();
        Boolean success = redis.opsForValue().setIfAbsent(KEY_PREFIX + lockName, threadId + "", timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success); // 这里避免拆箱会有空指针报错！！ 细节！！
    }

    @Override
    public void unlock() {
        redis.opsForValue().getAndDelete(KEY_PREFIX+lockName);
    }
}
