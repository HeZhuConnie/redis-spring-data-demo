package rolling.redisspringdatademo.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import rolling.redisspringdatademo.controller.Response;
import rolling.redisspringdatademo.repository.ShopPo;
import rolling.redisspringdatademo.repository.ShopRepository;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static rolling.redisspringdatademo.utils.RedisConstants.CACHE_SHOP_KEY;
import static rolling.redisspringdatademo.utils.RedisConstants.CACHE_SHOP_NULL_TTL;
import static rolling.redisspringdatademo.utils.RedisConstants.CACHE_SHOP_TTL;

@Service
public class ShopService {

    @Resource
    private ShopRepository shopRepository;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Response getShop(String id) {
        String key = CACHE_SHOP_KEY + id;
        String shop = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotBlank(shop)) {
            return Response.ok(JSONUtil.toBean(shop, ShopPo.class));
        }

        Optional<ShopPo> shopFromDb = shopRepository.findById(id);

        if (shopFromDb.isEmpty()) {
            stringRedisTemplate.opsForValue().set(key, "", CACHE_SHOP_NULL_TTL, TimeUnit.MINUTES);
            return Response.ok();
        }
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shopFromDb.get()), CACHE_SHOP_TTL, TimeUnit.MINUTES);
        return Response.ok(shopFromDb.get());
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
}
