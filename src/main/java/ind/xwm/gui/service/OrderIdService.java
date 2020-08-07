package ind.xwm.gui.service;

import ind.xwm.gui.model.OrderId;
import ind.xwm.gui.repository.OrderIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by XuWeiman on 2017/9/30.
 * OrderIdService
 */
@Service
public class OrderIdService {
    private static String keyID = "orderid";
    @Autowired
    private OrderIdRepository orderIdDao;

    @Transactional
    public String getOrderId() {
        String id;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String todayStr = df.format(new Date());
        OrderId orderId = orderIdDao.findOne(keyID);
        if (orderId == null) {
            id = todayStr + "0001";
            OrderId newOrderId = new OrderId();
            newOrderId.setId(keyID);
            newOrderId.setOrderId(id);
            newOrderId.setStatus(1);
            orderIdDao.save(newOrderId);
        } else if (orderId.getStatus().equals(1)) {
            id = orderId.getOrderId();
        } else {
            id = String.valueOf(Long.valueOf(orderId.getOrderId()) + 1);
        }

        OrderId newOrderId = new OrderId();
        newOrderId.setId(keyID);
        newOrderId.setOrderId(id);
        newOrderId.setStatus(1);
        orderIdDao.save(newOrderId);
        return id;
    }

    @Transactional
    public void usedOrderId(String id) {
        OrderId orderId = orderIdDao.findOne(keyID);
        if (orderId != null && orderId.getOrderId().equals(id)) {
            orderId.setStatus(0);
            orderIdDao.save(orderId);
        }
    }
}
