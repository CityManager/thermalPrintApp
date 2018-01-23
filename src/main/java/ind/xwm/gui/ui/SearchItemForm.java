package ind.xwm.gui.ui;

import ind.xwm.gui.model.OrderDetail;
import ind.xwm.gui.model.ProductOrder;
import ind.xwm.gui.print.P58Model;
import ind.xwm.gui.utils.PrintUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
    private JPanel orderDetailPanel;
    private JLabel orderCountLabel;
    private JLabel orderAmountLabel;
    private JRadioButton deliveredRadio;
    private JButton detailBtn;
    private JPanel orderInfoPanel;
    private JRadioButton payRadio;
    private JButton editBtn;
    private JLabel payLabel;
    private JLabel deliveredLabel;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;

    private java.util.List<ActionListener> actionListeners = new ArrayList<>();

    private boolean detailShow = false;
    private boolean editing = false;
    private ProductOrder order;

    private static Color[] colors = new Color[]{new Color(197, 165, 175), new Color(121, 178, 197)};

    public SearchItemForm(ProductOrder order) {
        if (order != null) {
            this.order = order;
            this.displayInit();
            this.actionInit();
        }
    }

    public SearchItemForm(ProductOrder order, boolean isOdd) {
        this(order);
        this.setBackGroundColor(isOdd ? colors[0] : colors[1]);
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

    public JPanel getOrderInfoPanel() {
        return orderInfoPanel;
    }

    public void setOrderInfoPanel(JPanel orderInfoPanel) {
        this.orderInfoPanel = orderInfoPanel;
    }

    public JRadioButton getPayRadio() {
        return payRadio;
    }

    public void setPayRadio(JRadioButton payRadio) {
        this.payRadio = payRadio;
    }

    public JButton getEditBtn() {
        return editBtn;
    }

    public void setEditBtn(JButton editBtn) {
        this.editBtn = editBtn;
    }


    public JPanel getOrderDetailPanel() {
        return orderDetailPanel;
    }

    public void setOrderDetailPanel(JPanel orderDetailPanel) {
        this.orderDetailPanel = orderDetailPanel;
    }

    public JLabel getOrderCountLabel() {
        return orderCountLabel;
    }

    public void setOrderCountLabel(JLabel orderCountLabel) {
        this.orderCountLabel = orderCountLabel;
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

    public ProductOrder getOrder() {
        return order;
    }

    public void setOrder(ProductOrder order) {
        this.order = order;
    }

    public JLabel getPayLabel() {
        return payLabel;
    }

    public void setPayLabel(JLabel payLabel) {
        this.payLabel = payLabel;
    }

    public JLabel getDeliveredLabel() {
        return deliveredLabel;
    }

    public void setDeliveredLabel(JLabel deliveredLabel) {
        this.deliveredLabel = deliveredLabel;
    }

    public boolean isDetailShow() {
        return detailShow;
    }

    public void setDetailShow(boolean detailShow) {
        this.detailShow = detailShow;
    }

    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    public List<ActionListener> getActionListeners() {
        return actionListeners;
    }

    public void setActionListeners(List<ActionListener> actionListeners) {
        this.actionListeners = actionListeners;
    }

    private void displayInit() {
        String orderTime = order.getOrderTime();
        String name = order.getCustomerName();
        String phone = order.getCustomerPhone();
        Integer payStatus = order.getPayStatus();
        Integer deliverStatus = order.getDeliverStatus();
        deliverStatus = (deliverStatus == null) ? 0 : deliverStatus;
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
        orderCountLabel.setText(StringUtils.isNotBlank(count) ? "总数：" + count : "总数：0");
        orderAmountLabel.setText(StringUtils.isNotBlank(amount) ? "总额：" + amount : "总额：");

        payLabel.setText((payStatus == 1) ? "已付款" : "未付款");
        payRadio.setText((payStatus == 1) ? "已付款" : "未付款");
        payRadio.setSelected(payStatus == 1);
        payRadio.setVisible(false);
        deliveredLabel.setText((deliverStatus == 1) ? "已取件" : "未取件");
        deliveredRadio.setText((deliverStatus == 1) ? "已取件" : "未取件");
        deliveredRadio.setSelected(deliverStatus == 1);
        deliveredRadio.setVisible(false);

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

        // 订单详情面板
        orderDetailPanel.setLayout(new BoxLayout(this.getOrderDetailPanel(), BoxLayout.Y_AXIS));
        orderDetailPanel.setBorder(BorderFactory.createTitledBorder("明细"));
        orderDetailPanel.add(jTable);
        orderDetailPanel.setVisible(detailShow);
        repaintComponent(orderDetailPanel);
    }

    private void actionInit() {
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
        detailBtn.addActionListener(e -> {
            detailShow = !detailShow;
            detailBtn.setText(detailShow ? "隐藏明细" : "显示明细");
            orderDetailPanel.setVisible(detailShow);
            orderDetailPanel.validate();
            orderDetailPanel.repaint();
        });

        editBtn.addActionListener(e -> {
            editing = !editing;
            editBtn.setText(editing ? "保存" : "修改");
            payLabel.setVisible(!editing);
            deliveredLabel.setVisible(!editing);
            payRadio.setVisible(editing);
            deliveredRadio.setVisible(editing);
            if(!editing) { // 从编辑状态点击保存进入到非编辑状态，进行订单保存
                for(ActionListener listener: actionListeners) {
                    listener.actionPerformed(e);
                }
            }
        });

        payRadio.addChangeListener(e -> {
            if(payRadio.isSelected()) {
                order.setPayStatus(1);
            } else {
                order.setPayStatus(0);
            }
        });

        deliveredRadio.addChangeListener(e -> {
            if(deliveredRadio.isSelected()) {
                order.setDeliverStatus(1);
            } else {
                order.setDeliverStatus(0);
            }
        });
    }

    public void setBackGroundColor(Color color) {
        searchItemPanel.setBackground(color);
        orderInfoPanel.setBackground(color);
        orderDetailPanel.setBackground(color);
        panel1.setBackground(color);
        panel2.setBackground(color);
        panel3.setBackground(color);
        for (Component component : orderDetailPanel.getComponents()) {
            if (component instanceof JTable) {
                component.setBackground(color);
            }
        }
    }

    private void repaintComponent(Component component) {
        component.validate();
        component.repaint();
    }

    public void repainEditComponent() {
        Integer payStatus = order.getPayStatus();
        payLabel.setText((payStatus == 1) ? "已付款" : "未付款");
        payRadio.setText((payStatus == 1) ? "已付款" : "未付款");
        payRadio.setSelected(payStatus == 1);
        repaintComponent(payLabel);
        repaintComponent(payRadio);
        Integer deliverStatus = order.getDeliverStatus();
        deliveredLabel.setText((deliverStatus == 1) ? "已取件" : "未取件");
        deliveredRadio.setText((deliverStatus == 1) ? "已取件" : "未取件");
        deliveredRadio.setSelected(deliverStatus == 1);
        repaintComponent(deliveredLabel);
        repaintComponent(deliveredRadio);
    }

}
