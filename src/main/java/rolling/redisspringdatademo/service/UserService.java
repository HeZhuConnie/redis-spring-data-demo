package rolling.redisspringdatademo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import rolling.redisspringdatademo.controller.Response;
import rolling.redisspringdatademo.utils.RandomUtils;

import java.util.concurrent.TimeUnit;

import static rolling.redisspringdatademo.utils.RedisConstants.LOGIN_PHONE_KEY;

@Service
public class UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Response getVerifyCode(String phone) {

        if (phone.length() != 11) return Response.fail("phone number is invalid");

        String verifyCode = RandomUtils.randomNumber(6);

        stringRedisTemplate.opsForValue().set(LOGIN_PHONE_KEY + phone, verifyCode, 15, TimeUnit.MINUTES);

        return Response.ok();
    }
}
