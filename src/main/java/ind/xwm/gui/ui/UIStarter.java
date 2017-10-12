package ind.xwm.gui.ui;

import ind.xwm.gui.model.OrderDetail;
import ind.xwm.gui.model.Product;
import ind.xwm.gui.model.ProductOrder;
import ind.xwm.gui.print.P58Model;
import ind.xwm.gui.service.OrderIdService;
import ind.xwm.gui.service.ProductOrderService;
import ind.xwm.gui.service.ProductService;
import ind.xwm.gui.service.UnitService;
import ind.xwm.gui.ui.listener.StopEditingListener;
import ind.xwm.gui.ui.uiComponent.JSpinnerCellEditor;
import ind.xwm.gui.ui.uiComponent.ProductComboBoxCellEditor;
import ind.xwm.gui.ui.uiComponent.UnitComboBoxCellEditor;
import ind.xwm.gui.utils.BaseUtil;
import ind.xwm.gui.utils.PrintUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Created by XuWeiman on 2017/10/2.
 * UI启动器
 */
@Component
public class UIStarter {
    private static Logger logger = LogManager.getLogger(UIStarter.class);

    @Resource
    private OrderIdService orderIdService;

    @Resource
    private ProductService productService;

    @Resource
    private UnitService unitService;

    @Resource
    private ProductOrderService productOrderService;


    private void mainPanelInit(PrintForm form) {
        String orderId = orderIdService.getOrderId();
        form.getOrderNoTxtField().setText(orderId.substring(8));

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String todayStr = df.format(new Date());
        form.getOrderTimeTxtField().setText(todayStr);
        form.getOrderCountTxtField().setText("");
        form.getOrderTotalTxtField().setText("");
        form.getNameField().setText("");
        form.getPhoneField().setText("");

        OrderDetailTable table = OrderDetailTable.getInstance(form.getOrderCountTxtField(), form.getOrderTotalTxtField());
        table.setFont(new Font("Default", Font.PLAIN, 16));

        form.getjScrollPane().setViewportView(table);
        form.getjScrollPane().addMouseListener(new StopEditingListener(table));

        ProductComboBoxCellEditor productEditor = new ProductComboBoxCellEditor(productService.findAllVector());
        UnitComboBoxCellEditor unitEditor = new UnitComboBoxCellEditor(unitService.findAll());
        productEditor.addActionListener(e -> {
            JComboBox actionJCombobox = (JComboBox) e.getSource();
            String productName = (String) actionJCombobox.getSelectedItem();
            if (productName == null) {
                return;
            }
            SwingWorker worker = new SwingWorker<Product, Void>() {
                @Override
                protected Product doInBackground() throws Exception {
                    return productService.findByName(productName);
                }

                @Override
                protected void done() {
                    super.done();
                    try {
                        int row = table.getSelectedRow();
                        Product product = get();
                        if (product == null) {
                            return;
                        }
                        String unitName = product.getUnit();
                        String productPrice = product.getPrice();
                        if (StringUtils.isNotBlank(unitName)) {
                            table.getModel().setValueAt(unitName, row, 3);
                        }
                        if (productPrice != null && StringUtils.isNumeric(productPrice.replace(".", ""))) {
                            table.getModel().setValueAt(productPrice, row, 4);
                        }
                    } catch (Exception e) {
                        logger.info("商品{}联动异常-", productName, e);
                    }
                }
            };
            worker.execute();
        });
        table.setCellEditor(1, productEditor);
        table.setCellEditor(2, new JSpinnerCellEditor());
        table.setCellEditor(3, unitEditor);

        JButton recordBtn = form.getOrderRecordBtn();
        for (ActionListener l : recordBtn.getActionListeners()) {
            recordBtn.removeActionListener(l);
        }
        form.getOrderRecordBtn().addActionListener(e -> table.addRowData());
        form.getOrderPayChBox().setSelected(false);


        JCheckBox payCheckBox = form.getOrderPayChBox();
        for(ChangeListener l: payCheckBox.getChangeListeners()) {
            payCheckBox.removeChangeListener(l);
        }
        payCheckBox.addChangeListener(e -> {
            JCheckBox jcb = (JCheckBox) e.getSource();
            if(jcb.isSelected()) {
                jcb.setText("已收款");
            } else {
                jcb.setText("未收款");
            }
        });

        JButton printBtn = form.getOrderPrintBtn();
        for (ActionListener l : printBtn.getActionListeners()) {
            printBtn.removeActionListener(l);
        }
        printBtn.addActionListener(e -> {
            CellEditor editor = table.getCellEditor();
            if(editor != null) {
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
                    order = productOrderService.save(order);
                    orderIdService.usedOrderId(orderId);
                    P58Model p58Model = new P58Model();
                    p58Model.setOrderContent(order);
                    if (PrintUtil.print58(p58Model)) {
                        order.setPrintStatus(1);
//                        productOrderService.save(order);
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
                            mainPanelInit(form);  // 直接重新初始化，不知道可不可以
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
        });
    }

    private void recordPanelInit(PrintForm form) {
        JTextField searchTxtField = form.getSearchField();

        java.util.List<ProductOrder> products = productOrderService.findAll();
        form.getSearchResultPanel().getViewport().add(this.initRecordPanel(products));
        form.getSearchResultPanel().validate();
        form.getSearchResultPanel().repaint();

        JButton searchBtn = form.getSearchBtn();
        for (ActionListener l : searchBtn.getActionListeners()) {
            searchBtn.removeActionListener(l);
        }
        searchBtn.addActionListener(e -> {
            String searchKey = searchTxtField.getText();
            java.util.List<ProductOrder> searchOrders = productOrderService.searchOrders(searchKey);
            form.getSearchResultPanel().getViewport().add(this.initRecordPanel(searchOrders));
            form.getSearchResultPanel().validate();
            form.getSearchResultPanel().repaint();
        });
    }

    private void productPanelInit(PrintForm form) {
        JTextField proNameField = form.getProductNameField();
        JTextField proUnitField = form.getProductUnitField();
        JTextField proPriceField = form.getProductPriceField();
        proNameField.setText("");
        proUnitField.setText("");
        proPriceField.setText("");
        java.util.List<Product> products = productService.findAll();
        JTable productsTable = new JTable() {
            @Override
            public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, toggle, extend);
                String proName = String.valueOf(this.getModel().getValueAt(rowIndex, 0));
                String proUnit = String.valueOf(this.getModel().getValueAt(rowIndex, 1));
                String proPrice = String.valueOf(this.getModel().getValueAt(rowIndex, 2));
                if (StringUtils.isNotBlank(proName)) {
                    proNameField.setText(proName);
                }
                if (StringUtils.isNotBlank(proUnit)) {
                    proUnitField.setText(proUnit);
                }
                if (StringUtils.isNotBlank(proPrice)) {
                    proPriceField.setText(proPrice);
                }
            }
        };
        DefaultTableModel tableModel = new DefaultTableModel(null, new String[]{"商品名称", "单位", "单价"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Product product : products) {
            tableModel.addRow(new String[]{product.getName(), product.getUnit(), product.getPrice()});
        }
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productsTable.setFont(new Font("Default", Font.PLAIN, 16));
        productsTable.setModel(tableModel);
        productsTable.setRowHeight(25);

        form.getProductScroll().setViewportView(productsTable);
        JButton productDelBtn = form.getProductDelBtn();
        for (ActionListener l : productDelBtn.getActionListeners()) {
            productDelBtn.removeActionListener(l);
        }
        productDelBtn.addActionListener(e -> {
            int selectRow = productsTable.getSelectedRow();
            if (selectRow != -1) {
                String proName = String.valueOf(tableModel.getValueAt(selectRow, 0));
                if (StringUtils.isNotBlank(proName)) { // 没有启动swingworker
                    productService.delete(proName);
                }
                tableModel.removeRow(selectRow);
            }
        });
        JButton productSaveBtn = form.getProductSaveBtn();
        for(ActionListener l: productSaveBtn.getActionListeners()) {
            productSaveBtn.removeActionListener(l);
        }
        productSaveBtn.addActionListener(e -> {
            String proName = proNameField.getText();
            String proUnit = proUnitField.getText();
            String proPrice = proPriceField.getText();
            if (StringUtils.isBlank(proName)) {
                return;
            }
            if (StringUtils.isBlank(proUnit)) {
                proUnit = "";
            }
            if (StringUtils.isBlank(proPrice)) {
                proPrice = "";
            }

            Product product = new Product();
            product.setName(proName);
            product.setUnit(proUnit);
            product.setPrice(proPrice);
            productService.save(product);
            Vector<Vector> rowDatas = tableModel.getDataVector();
            boolean isExit = false;
            for (int i = 0; i < rowDatas.size(); i++) {
                String n = String.valueOf(rowDatas.get(i).get(0));
                if (product.getName().equals(n)) {
                    tableModel.setValueAt(proName, i, 0);
                    tableModel.setValueAt(proUnit, i, 1);
                    tableModel.setValueAt(proPrice, i, 2);
                    isExit = true;
                }
            }
            if (!isExit) {
                tableModel.addRow(new String[]{proName, proUnit, proPrice});
            }
        });

    }

    private JPanel initRecordPanel(java.util.List<ProductOrder> products) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(""));
        for (ProductOrder order : products) {
            SearchItemForm searchItemForm = new SearchItemForm();
            searchItemForm.setOrderContent(order);
            JPanel itemPanel = searchItemForm.getSearchItemPanel();
            itemPanel.setBorder(BorderFactory.createTitledBorder(""));
            searchItemForm.getReprintBtn().addActionListener(e -> {
                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        P58Model p58Model = new P58Model();
                        p58Model.setOrderContent(order);
                        PrintUtil.print58(p58Model);
                        return null;
                    }
                };
                worker.execute();
            });
            panel.add(itemPanel);
        }
        panel.validate();
        panel.repaint();
        return panel;
    }


    public void init() {
        PrintForm form = new PrintForm();
        form.getTabbedPane().addChangeListener(e -> {
            JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex == 0) {
                mainPanelInit(form);
            } else if (selectedIndex == 1) {
                recordPanelInit(form);
            } else if (selectedIndex == 2) {
                productPanelInit(form);
            }
        });
        mainPanelInit(form);
        // recordPanelInit(form);
        // productPanelInit(form);
        JFrame frame = new JFrame("金碣路干洗店");
        frame.setContentPane(form.getPanel1());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setPreferredSize(new Dimension(800, 500));
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
