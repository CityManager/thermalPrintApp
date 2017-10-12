package ind.xwm.gui.ui;

import ind.xwm.gui.model.OrderDetail;
import ind.xwm.gui.model.ProductOrder;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Created by XuWeiman on 2017/10/4.
 */
public class SearchItemForm {
    private JPanel searchItemPanel;
    private JTextField orderNoField;
    private JTextField nameField;
    private JTextField orderTimeField;
    private JTextField phoneField;
    private JButton reprintBtn;
    private JLabel payLabel;
    private JPanel orderDetailPanel;
    private JLabel orderCountLable;
    private JLabel orderAmountLabel;


    public JPanel getSearchItemPanel() {
        return searchItemPanel;
    }

    public void setSearchItemPanel(JPanel searchItemPanel) {
        this.searchItemPanel = searchItemPanel;
    }

    public JTextField getOrderNoField() {
        return orderNoField;
    }

    public void setOrderNoField(JTextField orderNoField) {
        this.orderNoField = orderNoField;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public void setNameField(JTextField nameField) {
        this.nameField = nameField;
    }

    public JTextField getOrderTimeField() {
        return orderTimeField;
    }

    public void setOrderTimeField(JTextField orderTimeField) {
        this.orderTimeField = orderTimeField;
    }

    public JTextField getPhoneField() {
        return phoneField;
    }

    public void setPhoneField(JTextField phoneField) {
        this.phoneField = phoneField;
    }

    public JButton getReprintBtn() {
        return reprintBtn;
    }

    public void setReprintBtn(JButton reprintBtn) {
        this.reprintBtn = reprintBtn;
    }

    public JLabel getPayLabel() {
        return payLabel;
    }

    public void setPayLabel(JLabel payLabel) {
        this.payLabel = payLabel;
    }

    public JPanel getOrderDetailPanel() {
        return orderDetailPanel;
    }

    public void setOrderDetailPanel(JPanel orderDetailPanel) {
        this.orderDetailPanel = orderDetailPanel;
    }

    public JLabel getOrderCountLable() {
        return orderCountLable;
    }

    public void setOrderCountLable(JLabel orderCountLable) {
        this.orderCountLable = orderCountLable;
    }

    public JLabel getOrderAmountLabel() {
        return orderAmountLabel;
    }

    public void setOrderAmountLabel(JLabel orderAmountLabel) {
        this.orderAmountLabel = orderAmountLabel;
    }

    public void setOrderContent(ProductOrder order) {
        String orderTime = order.getOrderTime();
        String name = order.getCustomerName();
        String phone = order.getCustomerPhone();
        Integer payStatus = order.getPayStatus();
        String count = order.getTotalCount();
        String amount = order.getTotalPrice();

        if(StringUtils.isNotBlank(order.getOrderId()) && order.getOrderId().length() > 8) {
            String orderNo = order.getOrderId().substring(8);
            this.getOrderNoField().setText(orderNo);
        }

        if(StringUtils.isNotBlank(orderTime)) {
            this.getOrderTimeField().setText(orderTime);
        }

        if(StringUtils.isNotBlank(name)) {
            this.getNameField().setText(name);
        }

        if(StringUtils.isNotBlank(phone)) {
            this.getPhoneField().setText(phone);
        }

        if(payStatus == 1) {
            this.getPayLabel().setText("已付款");
        } else {
            this.getPayLabel().setText("未付款");
        }

        if(StringUtils.isNotBlank(count)) {
            this.getOrderCountLable().setText("总数：" + count);
        } else {
            this.getOrderCountLable().setText("总数：0");
        }

        if(StringUtils.isNotBlank(amount)) {
            this.getOrderAmountLabel().setText("总额：" + amount);
        } else {
            this.getOrderAmountLabel().setText("总额：");
        }


        JTable jTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel(null, new String[]{"商品", "数量", "单位", "单价", "金额"});
        for(OrderDetail orderDetail: order.getOrderDetails()) {
            Object[] data = new Object[] {
                    orderDetail.getProductName(),
                    orderDetail.getProductCount(),
                    orderDetail.getProductUnit(),
                    orderDetail.getProductPrice(),
                    orderDetail.getAmount()
            };
            tableModel.addRow(data);
        }
        jTable.setModel(tableModel);
        jTable.setEnabled(false);
        jTable.setFont(new Font("Default", Font.PLAIN,14));
        jTable.setBorder(BorderFactory.createEtchedBorder());
        jTable.setRowHeight(25);
        jTable.setBackground(new Color(238, 238, 238));
        this.getOrderDetailPanel().setLayout(new BoxLayout(this.getOrderDetailPanel(), BoxLayout.Y_AXIS));
        this.getOrderDetailPanel().setBorder(BorderFactory.createTitledBorder("明细"));
        this.getOrderDetailPanel().add(jTable);
        this.getOrderDetailPanel().validate();
        this.getOrderDetailPanel().repaint();
    }


}
