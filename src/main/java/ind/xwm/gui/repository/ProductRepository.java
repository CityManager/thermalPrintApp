package ind.xwm.gui.repository;

import ind.xwm.gui.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by XuWeiman on 2017/10/3.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByName(String name);
    void deleteByName(String name);
}
