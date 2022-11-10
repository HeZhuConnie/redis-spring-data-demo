package rolling.redisspringdatademo.service;

import lombok.Data;

import javax.persistence.Transient;
import java.time.LocalDateTime;

@Data
public class Voucher {
    private Long id;

    private Long shopId;

    private String title;

    private String subTitle;

    private String rules;

    private Long pay_value; // unit: 分 = 0.01元

    private Long actual_value; // unit: 分 = 0.01元

    private Integer type;

    private Integer status;

    private Integer stock;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
