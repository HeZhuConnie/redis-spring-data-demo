package rolling.redisspringdatademo.controller.dto;

import lombok.Data;

@Data
public class LoginFormDto {
    private String phone;
    private String verifyCode;
    private String password;
}
