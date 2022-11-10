package rolling.redisspringdatademo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rolling.redisspringdatademo.service.VoucherOrderService;

@RestController
@RequestMapping("/voucher-order")
public class VoucherOrderController {

    @Autowired
    private VoucherOrderService service;

    @PostMapping("/seckill/{id}")
    public Response seckillVoucher(@PathVariable("id") Long voucherId) {
        return Response.ok();
    }

}
