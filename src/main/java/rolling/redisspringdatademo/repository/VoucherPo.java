package rolling.redisspringdatademo.repository;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_voucher")
public class VoucherPo {
    @Id
    private Long id;

    private Long shopId;

    private String title;

    private String subTitle;

    private String rules;

    private Long pay_value; // unit: 分 = 0.01元

    private Long actual_value; // unit: 分 = 0.01元

    private Integer type;

    private Integer status;

    @Transient
    private Integer stock;

    @Transient
    private LocalDateTime beginTime;

    @Transient
    private LocalDateTime endTime;

    @Transient
    public Integer getStock() {
        return stock;
    }

    @Transient
    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    @Transient
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
