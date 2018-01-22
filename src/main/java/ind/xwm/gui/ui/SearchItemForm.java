package ind.xwm.gui.ui;

import ind.xwm.gui.model.OrderDetail;
import ind.xwm.gui.model.ProductOrder;
import ind.xwm.gui.print.P58Model;
import ind.xwm.gui.utils.PrintUtil;
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
    private JRadioButton deliveredRadio;
    private JButton detailBtn;
    private JPanel orderInfoPanel;

    private boolean detailShow = false;
    private ProductOrder order;

    public SearchItemForm(ProductOrder order) {
        if (order != null) {
            this.order = order;
            this.init();
        }
    }

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

    public JRadioButton getDeliveredRadio() {
        return deliveredRadio;
    }

    public void setDeliveredRadio(JRadioButton deliveredRadio) {
        this.deliveredRadio = deliveredRadio;
    }

    public JButton getDetailBtn() {
        return detailBtn;
    }

    public void setDetailBtn(JButton detailBtn) {
        this.detailBtn = detailBtn;
    }

    public boolean isDetailShow() {
        return detailShow;
    }

    public void setDetailShow(boolean detailShow) {
        this.detailShow = detailShow;
    }

    public ProductOrder getOrder() {
        return order;
    }

    public void setOrder(ProductOrder order) {
        this.order = order;
    }

    private void init() {
        String orderTime = order.getOrderTime();
        String name = order.getCustomerName();
        String phone = order.getCustomerPhone();
        Integer payStatus = order.getPayStatus();
        String count = order.getTotalCount();
        String amount = order.getTotalPrice();


        // 订单面板控件数据填充
        if (StringUtils.isNotBlank(order.getOrderId()) && order.getOrderId().length() > 8) {
            String orderNo = order.getOrderId().substring(8);
            orderNoField.setText(orderNo);
        }
        orderTimeField.setText(StringUtils.isNotBlank(orderTime) ? orderTime : "");
        nameField.setText(StringUtils.isNotBlank(name) ? name : "");
        phoneField.setText(StringUtils.isNotBlank(phone) ? phone : "");
        payLabel.setText((payStatus == 1) ? "已付款" : "未付款");
        orderCountLable.setText(StringUtils.isNotBlank(count) ? "总数：" + count : "总数：0");
        orderAmountLabel.setText(StringUtils.isNotBlank(amount) ? "总额：" + amount : "总额：");

        // 订单详情表格
        JTable jTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel(null, new String[]{"商品", "数量", "单位", "单价", "金额"});
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            Object[] data = new Object[]{
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
        jTable.setFont(new Font("Default", Font.PLAIN, 14));
        jTable.setBorder(BorderFactory.createEtchedBorder());
        jTable.setRowHeight(25);
        jTable.setBackground(new Color(238, 238, 238));

        // 订单详情面板
        orderDetailPanel.setLayout(new BoxLayout(this.getOrderDetailPanel(), BoxLayout.Y_AXIS));
        orderDetailPanel.setBorder(BorderFactory.createTitledBorder("明细"));
        orderDetailPanel.add(jTable);
        orderDetailPanel.setVisible(detailShow);
        orderDetailPanel.validate();
        orderDetailPanel.repaint();

        // 打印按钮
        reprintBtn.addActionListener(e -> {
            SwingWorker worker = new SwingWorker() {
                @Override
                protected Object doInBackground() {
                    P58Model p58Model = new P58Model();
                    p58Model.setOrderContent(order);
                    PrintUtil.print58(p58Model);
                    return null;
                }

            };
            worker.execute();
        });

        // 详情隐藏或者显示按钮
        detailBtn.setText("显示明细");
        detailBtn.addActionListener(e -> {
            detailShow = !detailShow;
            detailBtn.setText(detailShow ? "隐藏明细" : "显示明细");
            orderDetailPanel.setVisible(detailShow);
            orderDetailPanel.validate();
            orderDetailPanel.repaint();
        });
    }

    public void setBackGroundColor(Color color) {
        searchItemPanel.setBackground(color);
        orderInfoPanel.setBackground(color);
        orderDetailPanel.setBackground(color);
    }

}
