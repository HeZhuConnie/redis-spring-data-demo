package rolling.redisspringdatademo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rolling.redisspringdatademo.controller.dto.LoginFormDto;
import rolling.redisspringdatademo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/verify-code")
    public Response getVerifyCode(@RequestParam String phone) {
        return userService.getVerifyCode(phone);
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginFormDto loginFormDto) {
        return userService.login(loginFormDto);
    }
}
