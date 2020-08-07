package ind.xwm.gui.service;

import ind.xwm.gui.AppTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by XuWeiman on 2017/9/30.
 * OrderIdService 单元测试
 */
public class OrderIdServiceTest extends AppTest {
    private static Logger logger = LogManager.getLogger(OrderIdServiceTest.class);

    @Autowired
    private OrderIdService orderIdService;

    @Test
    public void getOrderId() throws Exception {
        logger.info("订单id-{}", orderIdService.getOrderId());
    }

    @Test
    public void usedOrderId() throws Exception {
        orderIdService.usedOrderId("201709300000");
    }

}