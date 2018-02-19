package ind.xwm.gui.ui.listener;

import ind.xwm.gui.model.Product;
import ind.xwm.gui.service.ProductService;
import ind.xwm.gui.utils.SpringBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductEditorListener implements ActionListener {
    private static Logger logger = LogManager.getLogger(ProductEditorListener.class);

    private JTable table;


    public ProductEditorListener(JTable table) {
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JComboBox actionJCombobox = (JComboBox) e.getSource();
        String productName = (String) actionJCombobox.getSelectedItem();
        if (productName == null) {
            return;
        }
        SwingWorker worker = new SwingWorker<Product, Void>() {
            @Override
            protected Product doInBackground() {
                ProductService productService = SpringBeanUtil.getBean(ProductService.class);
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
    }
}
