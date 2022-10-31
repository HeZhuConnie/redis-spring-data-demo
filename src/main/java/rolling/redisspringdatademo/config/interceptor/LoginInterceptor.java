package rolling.redisspringdatademo.config.interceptor;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import rolling.redisspringdatademo.controller.dto.UserDto;
import rolling.redisspringdatademo.utils.UserHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static rolling.redisspringdatademo.utils.RedisConstants.LOGIN_USER_KEY;

public class LoginInterceptor implements HandlerInterceptor {

//    LoginInterceptor这个类不是Spring帮我们创建的，所以内部不能用autowire注解引入任何类，引不到
//    @Autowired

    private StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取 token
        String token = request.getHeader("authorization");

        // 2。获取redis中的用户
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(LOGIN_USER_KEY + token);
        // 3。判断用户是否存在
        if (userMap.isEmpty()) {
            // 4。不存在，拦截
            response.setStatus(401);
            return false;
        }

        // 5。存在，保存用户信息到ThreadLocal，放行
        UserDto user = BeanUtil.fillBeanWithMap(userMap, new UserDto(), false);
        UserHolder.saveUser(user);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 执行完之后移除用户，避免内存泄漏
        UserHolder.removeUser();
    }
}
