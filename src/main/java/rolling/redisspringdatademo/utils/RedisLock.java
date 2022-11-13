package rolling.redisspringdatademo.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class RedisLock implements ILock{

    private String lockName;
    private StringRedisTemplate redis;

    public RedisLock(String lockName, StringRedisTemplate redis) {
        this.lockName = lockName;
        this.redis = redis;
    }

    private static final String KEY_PREFIX="lock:";
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    @Override
    public boolean tryLock(long timeoutSec) {
        // 获取线程标识
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        Boolean success = redis.opsForValue().setIfAbsent(KEY_PREFIX + lockName, threadId + "", timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success); // 这里避免拆箱会有空指针报错！！ 细节！！
    }

    @Override
    public void unlock() {
        redis.execute(
            UNLOCK_SCRIPT,
            Collections.singletonList(KEY_PREFIX + lockName),
            ID_PREFIX + Thread.currentThread().getId()
        );
    }

//    @Override
//    public void unlock() {
//        String threadId = ID_PREFIX + Thread.currentThread().getId();
//
//        String id = redis.opsForValue().get(KEY_PREFIX+lockName);
//
//        if (id.equals(threadId)) {
//            redis.opsForValue().getAndDelete(KEY_PREFIX+lockName);
//        }
//    }
}
