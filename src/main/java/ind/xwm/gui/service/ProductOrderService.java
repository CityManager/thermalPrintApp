package ind.xwm.gui.service;

import ind.xwm.gui.model.OrderDetail;
import ind.xwm.gui.model.Product;
import ind.xwm.gui.model.ProductOrder;
import ind.xwm.gui.model.Unit;
import ind.xwm.gui.repository.ProductOrderRepository;
import ind.xwm.gui.repository.ProductRepository;
import ind.xwm.gui.repository.UnitRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by XuWeiman on 2017/9/30.
 * ProductOrder service
 */
@Service
public class ProductOrderService {
    private static Logger logger = LogManager.getLogger(ProductOrderService.class);

    @Autowired
    private ProductOrderRepository orderDao;

    @Autowired
    private ProductRepository productDao;

    @Autowired
    private UnitRepository unitDao;

    @Transactional
    public ProductOrder save(ProductOrder productOrder) {
        return orderDao.save(productOrder);
    }

    @Transactional
    public ProductOrder saveProductOrder(ProductOrder productOrder) {
        List<OrderDetail> orderDetails = productOrder.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            Product product = productDao.findByName(orderDetail.getProductName());
            if (product == null) {
                product = new Product();
            }
            product.setName(orderDetail.getProductName());
            product.setUnit(orderDetail.getProductUnit());
            product.setPrice(orderDetail.getProductPrice());
            productDao.save(product);

            Unit unit = unitDao.findByName(orderDetail.getProductUnit());
            if (unit == null) {
                unit = new Unit();
                unit.setName(orderDetail.getProductUnit());
                unitDao.save(unit);
            }
        }

        return orderDao.save(productOrder);
    }

    public List<ProductOrder> findAll() {
        List<ProductOrder> orders = orderDao.findAll();
        sortOrder(orders);
        return orders;
    }

    public Page<ProductOrder> findAllPage(Pageable pageable) {
        return orderDao.findAll(pageable);
    }

    public List<ProductOrder> searchOrders(String key) {
        if (StringUtils.isBlank(key)) {
            return this.findAll();
        }
        List<ProductOrder> orders = orderDao.searchOrders(key);
        sortOrder(orders);
        return orders;
    }

    public Page<ProductOrder> searchOrders(Pageable pageable, String key) {
        ProductOrder order = new ProductOrder();
        order.setOrderId(key);
        order.setCustomerName(key);
        order.setCustomerPhone(key);
        ExampleMatcher.GenericPropertyMatcher matcher = ExampleMatcher.GenericPropertyMatchers.contains();
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("customerName", matcher)
                .withMatcher("customerPhone", matcher)
                .withMatcher("orderId", matcher).withIgnoreCase();

        Example<ProductOrder> example = Example.of(order, exampleMatcher);
        return orderDao.findAll(example, pageable);
    }


    private void sortOrder(List<ProductOrder> orders) {
        orders.sort((o1, o2) -> {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                return df.parse(o2.getOrderTime()).compareTo(df.parse(o1.getOrderTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }
}
