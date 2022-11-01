package rolling.redisspringdatademo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rolling.redisspringdatademo.service.Shop;
import rolling.redisspringdatademo.service.ShopService;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/{id}")
    private Response getShop(@PathVariable("id") String id) {
        return shopService.getShop(id);
    }

    @PostMapping
    private Response createShop(@RequestBody Shop shop) {
        shopService.create(shop);

        return Response.ok(shop.getId());
    }
}
