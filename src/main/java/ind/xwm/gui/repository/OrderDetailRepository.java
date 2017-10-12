package ind.xwm.gui.repository;

import ind.xwm.gui.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by XuWeiman on 2017/9/30.
 * OrderDetail dao
 */
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
}
