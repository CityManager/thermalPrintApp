package ind.xwm.gui.ui.listener;

import ind.xwm.gui.model.OrderDetail;
import ind.xwm.gui.model.ProductOrder;
import ind.xwm.gui.print.P58Model;
import ind.xwm.gui.service.OrderIdService;
import ind.xwm.gui.service.ProductOrderService;
import ind.xwm.gui.ui.PrintForm;
import ind.xwm.gui.ui.initializer.MainPanelInitializer;
import ind.xwm.gui.utils.BaseUtil;
import ind.xwm.gui.utils.PrintUtil;
import ind.xwm.gui.utils.SpringBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class PrintButtonListener implements ActionListener {
    private static Logger logger = LogManager.getLogger(PrintButtonListener.class);

    private JTable table;
    private PrintForm form;

    public PrintButtonListener(PrintForm form, JTable table) {
        this.table = table;
        this.form = form;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CellEditor editor = table.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
        JTextField msgField = form.getMessageField();
        String name = form.getNameField().getText().trim();
        String phone = form.getPhoneField().getText().trim();
        String orderTime = form.getOrderTimeTxtField().getText().trim();
        String count = form.getOrderCountTxtField().getText().trim();
        String totalPrice = form.getOrderTotalTxtField().getText();
        Vector<Vector> tableData = ((DefaultTableModel) table.getModel()).getDataVector();
        boolean payed = form.getOrderPayChBox().isSelected();

        if (StringUtils.isBlank(name)) {
            msgField.setCaretColor(Color.RED);
            msgField.setText("请输入姓名");
            return;
        } else if (StringUtils.isBlank(phone)) {
            msgField.setCaretColor(Color.RED);
            msgField.setText("请输入号码");
            return;
        }

        SwingWorker saveWork = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                OrderIdService orderIdService = SpringBeanUtil.getBean(OrderIdService.class);
                ProductOrderService productOrderService = SpringBeanUtil.getBean(ProductOrderService.class);
                ProductOrder order = new ProductOrder();
                String orderId = orderIdService.getOrderId();

                order.setOrderId(orderId);
                order.setCustomerName(name);
                order.setCustomerPhone(phone);
                order.setOrderTime(orderTime);
                order.setPayStatus(payed ? 1 : 0);
                order.setTotalCount(count);
                order.setTotalPrice(totalPrice);
                Vector<OrderDetail> orderDetails = BaseUtil.getOrderDetails(tableData);
                order.setOrderDetails(new ArrayList<>(orderDetails));

                for (OrderDetail orderDetail : orderDetails) {
                    orderDetail.setProductOrder(order);
                }
                order = productOrderService.save(order);  // 订单入库
                orderIdService.usedOrderId(orderId);
                if (PrintUtil.print58(order)) {  // 订单打印
                    order.setPrintStatus(1);
                    return true;
                }
                return false;
            }

            @Override
            protected void done() {
                super.done();
                try {
                    boolean isDone = get();
                    if (isDone) {
                        MainPanelInitializer.init(form);  // 直接重新初始化，不知道可不可以
                    } else {
                        form.getMessageField().setCaretColor(Color.RED);
                        form.getMessageField().setText("打印异常，请重新打印");
                    }
                } catch (Exception e) {
                    logger.info("销售保存打印异常-", e);
                }
            }
        };
        saveWork.execute();
    }
}
