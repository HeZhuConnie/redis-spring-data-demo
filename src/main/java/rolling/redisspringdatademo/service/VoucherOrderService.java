package rolling.redisspringdatademo.service;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import rolling.redisspringdatademo.controller.Response;
import rolling.redisspringdatademo.repository.SeckillVoucherPo;
import rolling.redisspringdatademo.repository.SeckillVoucherRepository;
import rolling.redisspringdatademo.repository.VoucherOrderPo;
import rolling.redisspringdatademo.repository.VoucherOrderRepository;
import rolling.redisspringdatademo.utils.RedisLock;
import rolling.redisspringdatademo.utils.RedisWorker;
import rolling.redisspringdatademo.utils.UserHolder;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VoucherOrderService {

    @Autowired
    private SeckillVoucherRepository seckillVoucherRepository;
    @Autowired
    private VoucherOrderRepository voucherOrderRepository;
    @Autowired
    private RedisWorker redisWorker;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    public Response seckillVoucher(Long voucherId) {
        // 查询优惠券
        Optional<SeckillVoucherPo> seckillVoucherPo = seckillVoucherRepository.findById(voucherId);
        if (seckillVoucherPo.isEmpty()) return null;

        // 判断是否开始 & 判断是否结束
        if (
            seckillVoucherPo.get().getBeginTime().isAfter(LocalDateTime.now()) ||
                seckillVoucherPo.get().getEndTime().isBefore(LocalDateTime.now())
        ) return Response.fail("不在规定使用时间范围内");

        // 查看库存
        if (seckillVoucherPo.get().getStock() < 1) {
            return Response.fail("库存不足");
        }

        String userId = UserHolder.getUser().getId();

        String lockName = "lock:order:" + userId;
        //  RedisLock redisLock = new RedisLock(lockName, stringRedisTemplate);
        RLock redisLock = redissonClient.getLock(lockName);

        boolean isLock = redisLock.tryLock();

        if (!isLock) {
            return Response.fail("每人只能下一单");
        }

        try {
            // 获取代理对象，因为Transactional注解对this.createVoucherOrder是不起作用的
            VoucherOrderService proxy = (VoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        } finally {
            redisLock.unlock();
        }
    }

    @Transactional
    public Response createVoucherOrder(Long voucherId) {
        // 每人只能下一单
        String userId = UserHolder.getUser().getId();
        if (voucherOrderRepository.findFirstByUserId(userId) != null)
        {
            return Response.fail("每人只能下一单");
        };

        // 减少库存
        try {
            seckillVoucherRepository.reduceStock(voucherId);
        } catch (Exception e) {
            return Response.fail("Ohhh 库存不足了！");
        }

        // 创建订单
        VoucherOrderPo voucherOrderPo = new VoucherOrderPo();
        voucherOrderPo.setId(redisWorker.getAutoId("order"));
        voucherOrderPo.setVoucherId(voucherId);
        voucherOrderPo.setUserId(userId);

        // 返回订单id
        return Response.ok(voucherOrderRepository.save(voucherOrderPo));
    }
}
