package ind.xwm.gui.ui.initializer;

import ind.xwm.gui.model.Product;
import ind.xwm.gui.service.ProductService;
import ind.xwm.gui.ui.PrintForm;
import ind.xwm.gui.utils.SpringBeanUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Vector;

public class ProductPanelInitializer {
    public static void init(PrintForm form) {
        ProductService productService = SpringBeanUtil.getBean(ProductService.class);
        JTextField proNameField = form.getProductNameField();
        JTextField proUnitField = form.getProductUnitField();
        JTextField proPriceField = form.getProductPriceField();
        proNameField.setText("");
        proUnitField.setText("");
        proPriceField.setText("");
        java.util.List<Product> products = productService.findAll();  // 不符合规范
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
                    productService.delete(proName);  // 不符合规范
                }
                tableModel.removeRow(selectRow);
            }
        });
        JButton productSaveBtn = form.getProductSaveBtn();
        for (ActionListener l : productSaveBtn.getActionListeners()) { // 先清理掉已有的多有监听事件
            productSaveBtn.removeActionListener(l);
        }
        productSaveBtn.addActionListener(e -> { // 重新添加监听事件
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
}
