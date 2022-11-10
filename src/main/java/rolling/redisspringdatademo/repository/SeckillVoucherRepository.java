package rolling.redisspringdatademo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeckillVoucherRepository extends JpaRepository<SeckillVoucherPo, Long> {
}
