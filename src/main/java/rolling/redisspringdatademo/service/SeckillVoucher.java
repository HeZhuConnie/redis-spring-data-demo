package rolling.redisspringdatademo.service;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SeckillVoucher {
    private Long voucherId;

    private Integer stock;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
