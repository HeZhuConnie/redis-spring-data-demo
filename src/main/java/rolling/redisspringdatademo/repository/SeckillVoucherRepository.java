package rolling.redisspringdatademo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SeckillVoucherRepository extends JpaRepository<SeckillVoucherPo, Long> {

    @Modifying
    @Transactional
    @Query(
        value = "UPDATE tb_seckill_voucher SET stock = stock - 1 WHERE voucher_id = ?1",
        nativeQuery = true
    )
    public void reduceStock(Long voucherId);
}
