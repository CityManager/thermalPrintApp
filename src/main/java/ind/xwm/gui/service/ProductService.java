package ind.xwm.gui.service;

import ind.xwm.gui.model.Product;
import ind.xwm.gui.model.Unit;
import ind.xwm.gui.repository.ProductRepository;
import ind.xwm.gui.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Vector;

/**
 * Created by XuWeiman on 2017/10/3.
 * product service
 */
@Service
public class ProductService {
    @Autowired
    private ProductRepository productDao;

    @Autowired
    private UnitRepository unitDao;

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Vector<Product> findAllVector() {
        List<Product> products = productDao.findAll();
        Vector<Product> vector = new Vector<>();
        vector.addAll(products);
        return vector;
    }

    public Product findByName(String name) {
        return productDao.findByName(name);
    }

    @Transactional
    public void save(Product product) {
        Product productInDb = productDao.findByName(product.getName());
        if (productInDb != null) {
            productInDb.setUnit(product.getUnit());
            productInDb.setPrice(product.getPrice());
        } else {
            productInDb = product;
        }
        product = productDao.save(productInDb);
        String unitName = product.getUnit();
        Unit unit = unitDao.findByName(unitName);
        if (unit == null) {
            unit = new Unit();
            unit.setName(unitName);
            unitDao.save(unit);
        }
    }


    @Transactional
    public void delete(String name) {
        productDao.deleteByName(name);
    }
}
