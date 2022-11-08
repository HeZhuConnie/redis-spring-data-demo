package rolling.redisspringdatademo.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import rolling.redisspringdatademo.controller.Response;
import rolling.redisspringdatademo.repository.ShopPo;
import rolling.redisspringdatademo.repository.ShopRepository;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static rolling.redisspringdatademo.utils.RedisConstants.CACHE_SHOP_KEY;
import static rolling.redisspringdatademo.utils.RedisConstants.CACHE_SHOP_NULL_TTL;
import static rolling.redisspringdatademo.utils.RedisConstants.CACHE_SHOP_TTL;
import static rolling.redisspringdatademo.utils.RedisConstants.LOCK_SHOP_KEY;

@Service
public class ShopService {

    @Resource
    private ShopRepository shopRepository;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Response getShop(String id) {
        // 缓存穿透
//        ShopPo shop = queryWithPassThrough(id);

        // 互斥锁解决缓存击穿
//        ShopPo shop = queryWithMutex(id);

        // 逻辑国旗解决缓存击穿
        ShopPo shop = queryWithLogicalExpire(id);

        return Response.ok(shop);
    }

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    public ShopPo queryWithLogicalExpire(String id) {
        String key = CACHE_SHOP_KEY + id;
        String redisData = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isBlank(redisData)) {
            // 要做预热，所以肯定在redis里的
                Optional<ShopPo> shopFromDb = shopRepository.findById(id);
                if (shopFromDb.isPresent()) {
                    RedisLogicalExpireData rebuildedRedisData = shopToRedisData(shopFromDb.get());
                    stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(rebuildedRedisData), 10, TimeUnit.DAYS);
                }
            return null;
        }

        // check expire, if expired, then open a new thread to refresh redis
        ShopPo shop = JSONUtil.toBean((JSONObject) JSONUtil.toBean(redisData, RedisLogicalExpireData.class).getData(), ShopPo.class);

        LocalDateTime expire = JSONUtil.toBean(redisData, RedisLogicalExpireData.class).getExpire();
        if (expire.isAfter(LocalDateTime.now())) {
            return shop;
        }

        String lockKey = LOCK_SHOP_KEY + id;
        boolean isLock = tryLock(lockKey);
        if (isLock) {
            CACHE_REBUILD_EXECUTOR.submit(
                    () -> {
                        try {
                            Optional<ShopPo> shopFromDb = shopRepository.findById(id);
                            if (shopFromDb.isPresent()) {
                                RedisLogicalExpireData rebuildedRedisData = shopToRedisData(shopFromDb.get());
                                stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(rebuildedRedisData), 10, TimeUnit.DAYS);
                            }
                        } catch (Exception e) {
                            throw e;
                        } finally {
                            unlock(lockKey);
                        }
                    }
            );
        }

        return shop;
    }

    private RedisLogicalExpireData shopToRedisData(ShopPo shop) {
        RedisLogicalExpireData redisData = new RedisLogicalExpireData();
        redisData.setData(shop);
        redisData.setExpire(LocalDateTime.now().plusSeconds(2));
        return redisData;
    }

    public ShopPo queryWithMutex(String id) {
        String key = CACHE_SHOP_KEY + id;
        String shop = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotBlank(shop)) {
            return JSONUtil.toBean(shop, ShopPo.class);
        }

        Optional<ShopPo> shopFromDb = null;

        try {
            boolean isLock = tryLock(LOCK_SHOP_KEY + id);
            if(!isLock) {
                Thread.sleep(50);
                queryWithMutex(id);
            }

            shopFromDb = shopRepository.findById(id);
             Thread.sleep(200); // 用于测试

            if (shopFromDb.isEmpty()) {
                stringRedisTemplate.opsForValue().set(key, "", CACHE_SHOP_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shopFromDb.get()), CACHE_SHOP_TTL, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock(LOCK_SHOP_KEY + id);

        }

        return shopFromDb.get();
    }

    public ShopPo queryWithPassThrough(String id) {
        String key = CACHE_SHOP_KEY + id;
        String shop = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotBlank(shop)) {
            return JSONUtil.toBean(shop, ShopPo.class);
        }

        Optional<ShopPo> shopFromDb = shopRepository.findById(id);

        if (shopFromDb.isEmpty()) {
            stringRedisTemplate.opsForValue().set(key, "", CACHE_SHOP_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shopFromDb.get()), CACHE_SHOP_TTL, TimeUnit.MINUTES);
        return shopFromDb.get();
    }

    public void create(Shop shop) {
        shopRepository.save(BeanUtil.copyProperties(shop, ShopPo.class));
    }

    @Transactional
    public Response updateShop(Shop shop) {
        ShopPo shopPo = new ShopPo();
        BeanUtil.copyProperties(shop, shopPo, false);
        shopRepository.save(shopPo);

        stringRedisTemplate.delete(CACHE_SHOP_KEY + shop.getId());

        return Response.ok();
    }

    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);

        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key) {
        stringRedisTemplate.opsForValue().getAndDelete(key);
    }
}
