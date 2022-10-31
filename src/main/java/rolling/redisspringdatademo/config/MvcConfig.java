package rolling.redisspringdatademo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import rolling.redisspringdatademo.config.interceptor.LoginInterceptor;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/user/verify-code",
                        "/shop-type/**",
                        "/user/login"
                ).order(1);

        registry.addInterceptor(new RefreshTokenInterceptor()).addPathPatterns("**").order(0);
    }
}
