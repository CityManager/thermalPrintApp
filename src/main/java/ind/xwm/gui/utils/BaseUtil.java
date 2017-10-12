package ind.xwm.gui.utils;

import com.alibaba.fastjson.JSON;
import ind.xwm.gui.model.OrderDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Vector;

/**
 * Created by XuWeiman on 2017/10/3.
 * 工具集合
 */
public class BaseUtil {
    private static Logger logger = LogManager.getLogger(BaseUtil.class);

    public static Vector<OrderDetail> getOrderDetails(Vector<Vector> tableData) {
        Vector<OrderDetail> orderDetails = new Vector<>();
        for(Vector rowData: tableData) {
            OrderDetail orderDetail = new OrderDetail();
            String productName = String.valueOf(rowData.get(1));
            String productCount = String.valueOf(rowData.get(2));
            String productUnit = String.valueOf(rowData.get(3));
            String productPrice = String.valueOf(rowData.get(4));
            String productAmount = String.valueOf(rowData.get(5));

            orderDetail.setProductName(productName);
            orderDetail.setProductUnit(productUnit);
            orderDetail.setProductPrice(productPrice);
            orderDetail.setProductCount(productCount);
            orderDetail.setAmount(productAmount);

            orderDetails.add(orderDetail);
        }
        logger.info("订单详情转换结果-{}", JSON.toJSONString(orderDetails));
        return orderDetails;
    }
}
