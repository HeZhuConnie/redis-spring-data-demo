package rolling.redisspringdatademo.service;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedisLogicalExpireData {
    private LocalDateTime expire;
    private Object data;
}
