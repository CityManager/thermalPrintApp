package ind.xwm.gui.service;

import com.alibaba.fastjson.JSON;
import ind.xwm.gui.AppTest;
import ind.xwm.gui.model.OrderDetail;
import ind.xwm.gui.model.ProductOrder;
import ind.xwm.gui.repository.OrderDetailRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XuWeiman on 2017/9/30.
 * OrderService 单元测试
 */
public class ProductOrderServiceTest extends AppTest {
    private static Logger logger = LogManager.getLogger(ProductOrderServiceTest.class);
    @Resource
    private ProductOrderService productOrderService;

    @Resource
    private OrderDetailRepository orderDetailDao;

    @Test
    public void save() throws Exception {
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String todayStr = df.format(new Date());
        ProductOrder productOrder = new ProductOrder();
        productOrder.setCustomerName("许维满");
        productOrder.setCustomerPhone("13928882435");
        productOrder.setOrderTime(todayStr);
        productOrder.setPayStatus(0);
        productOrder.setPrintStatus(0);
        productOrder.setOrderId(todayStr);

        for (int i = 0; i < 10; i++) {
            OrderDetail detail = new OrderDetail();
            String index = String.valueOf(i);
            detail.setProductCount(index);
            detail.setProductPrice(String.valueOf(50.00 + i));
            detail.setProductUnit("件");
            detail.setAmount(String.valueOf((50.00 + i) * i));
            detail.setProductName("商品" + index);
            detail.setProductOrder(productOrder);
            //orderDetailDao.save(detail);
            orderDetails.add(detail);
        }


        productOrder.setOrderDetails(orderDetails);
        logger.info("保存订单：{}", JSON.toJSONString(productOrderService.save(productOrder)));
    }

    @Test
    public void findAll() throws Exception {
        logger.info("全部订单详情-{}", JSON.toJSONString(productOrderService.findAll()));
    }

    @Test
    public void findAllPage() {
        Pageable pageable = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "orderTime"));
        logger.info("订单分页-{}", JSON.toJSONString(productOrderService.findAllPage(pageable).getContent()));
    }

    @Test
    public void searchOrders() {
        Pageable pageable = new PageRequest(1, 1, new Sort(Sort.Direction.DESC, "orderTime"));
        logger.info("订单分页-是不是页数{}", pageable.getOffset());
        logger.info("订单分页-{}", JSON.toJSONString(productOrderService.searchOrders(pageable, "0002").getContent()));
        logger.info("订单分页-是不是页数{}", pageable.getOffset());
    }
}