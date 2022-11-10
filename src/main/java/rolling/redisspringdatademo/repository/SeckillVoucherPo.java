package rolling.redisspringdatademo.repository;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_seckill_voucher")
public class SeckillVoucherPo {
    @Id
    private Long voucherId;

    private Integer stock;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}
