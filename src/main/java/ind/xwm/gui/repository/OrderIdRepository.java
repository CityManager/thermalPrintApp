package ind.xwm.gui.repository;

import ind.xwm.gui.model.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by XuWeiman on 2017/9/30.
 * orderid 表 dao
 */
@Repository
public interface OrderIdRepository extends JpaRepository<OrderId, String> {
}
