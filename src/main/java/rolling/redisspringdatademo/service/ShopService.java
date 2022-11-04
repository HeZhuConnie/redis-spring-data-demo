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
import java.util.Optional;

@Service
public class ShopService {

    @Resource
    private ShopRepository shopRepository;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Response getShop(String id) {
        String key = "cache:shop" + id;
        String shop = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotBlank(shop)) {
            return Response.ok(JSONUtil.toBean(shop, ShopPo.class));
        }

        Optional<ShopPo> shopFromDb = shopRepository.findById(id);

        if (shopFromDb.isEmpty()) {
            stringRedisTemplate.opsForValue().set(key, "");
            return Response.ok();
        }
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shopFromDb.get()));
        return Response.ok(shopFromDb.get());
    }

    public void create(Shop shop) {
        shopRepository.save(BeanUtil.copyProperties(shop, ShopPo.class));
    }
}
