package rolling.redisspringdatademo.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import rolling.redisspringdatademo.controller.Response;
import rolling.redisspringdatademo.controller.dto.LoginFormDto;
import rolling.redisspringdatademo.repository.UserPo;
import rolling.redisspringdatademo.repository.UserRepository;
import rolling.redisspringdatademo.utils.RandomUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static rolling.redisspringdatademo.utils.RedisConstants.LOGIN_PHONE_KEY;
import static rolling.redisspringdatademo.utils.RedisConstants.LOGIN_USER_KEY;
import static rolling.redisspringdatademo.utils.RedisConstants.LOGIN_USER_TTL;

@Service
public class UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserRepository userRepository;

    public Response getVerifyCode(String phone) {

        if (phone.length() != 11) return Response.fail("phone number is invalid");

        String verifyCode = RandomUtils.randomNumber(6);

        stringRedisTemplate.opsForValue().set(LOGIN_PHONE_KEY + phone, verifyCode, 15, TimeUnit.MINUTES);

        return Response.ok();
    }

    public Response login(LoginFormDto loginFormDto, HttpSession session) {
        if (loginFormDto.getPhone().length() != 11) return Response.fail("phone number is invalid");

        String verifyCode = stringRedisTemplate.opsForValue().get(LOGIN_PHONE_KEY + loginFormDto.getPhone());

        if (verifyCode == null || !verifyCode.equals(loginFormDto.getVerifyCode())) {
            return Response.fail("verify code is wrong");
        }

        // get user info
        Optional<UserPo> userPo = userRepository.findByPhone(loginFormDto.getPhone());
        UserPo user;

        if (userPo.isEmpty()) {
            user = createUserWithPhone(loginFormDto);
        } else {
            user = userPo.get();
            if (!loginFormDto.getPassword().equals(user.getPassword())) {
                return Response.fail("password is wrong");
            }
        }

        // generate token
        String token = UUID.randomUUID().toString();

        Map<String, Object> userMap = BeanUtil.beanToMap(user, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> BeanUtil.isEmpty(fieldValue) ? "" : fieldValue.toString()));

        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY + token, userMap);
        stringRedisTemplate.expire(LOGIN_USER_KEY + token, LOGIN_USER_TTL, TimeUnit.HOURS);

        User u = new User();
        BeanUtil.copyProperties(user, u);
        session.setAttribute("user", u);

        return Response.ok(token);
    }

    private UserPo createUserWithPhone(LoginFormDto loginFormDto) {
        UserPo newUser = new UserPo();
        newUser.setPhone(loginFormDto.getPhone());
        newUser.setPassword(loginFormDto.getPassword());

        userRepository.save(newUser);

        return newUser;
    }
}
