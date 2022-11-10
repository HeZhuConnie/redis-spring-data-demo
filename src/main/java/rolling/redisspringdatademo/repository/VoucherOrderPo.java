package rolling.redisspringdatademo.repository;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_voucher_order")
public class VoucherOrderPo {
    @Id
    private Long id;

    private String userId;

    private Long voucherId;

    private Integer payType = 1;

    private Integer status = 1;

    private LocalDateTime payTime;

    private LocalDateTime useTime;

    private LocalDateTime refundTime;

}
