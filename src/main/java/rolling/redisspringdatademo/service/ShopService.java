package rolling.redisspringdatademo.service;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rolling.redisspringdatademo.controller.Response;
import rolling.redisspringdatademo.repository.ShopPo;
import rolling.redisspringdatademo.repository.ShopRepository;

import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    public Response getShop(String id) {
        Optional<ShopPo> shop = shopRepository.findById(id);

        if (shop.isEmpty()) {
            return Response.ok();
        }
        return Response.ok(shop);
    }

    public void create(Shop shop) {
        shopRepository.save(BeanUtil.copyProperties(shop, ShopPo.class));
    }
}
