package ind.xwm.gui.repository;

import ind.xwm.gui.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by XuWeiman on 2017/9/30.
 * ProductOrder dao
 */
@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, String> {
    @Query("select o from ProductOrder o where o.customerName like %:key% or o.customerPhone like %:key% or o.orderId like %:key%")
    List<ProductOrder> searchOrders(@Param("key") String key);
}
