package ind.xwm.gui.ui.initializer;

import com.alibaba.fastjson.JSON;
import ind.xwm.gui.model.ProductOrder;
import ind.xwm.gui.service.ProductOrderService;
import ind.xwm.gui.ui.PrintForm;
import ind.xwm.gui.ui.SearchItemForm;
import ind.xwm.gui.utils.SpringBeanUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;

public class RecordPanelInitializer {
    private static Logger logger = LogManager.getLogger(RecordPanelInitializer.class);

    public static void init(PrintForm form) {
        ProductOrderService productOrderService = SpringBeanUtil.getBean(ProductOrderService.class);
        final JTextField searchTxtField = form.getSearchField();
        final JScrollPane searchResultPanel = form.getSearchResultPanel();
        java.util.List<ProductOrder> products = productOrderService.findAll();  // 不符合规范
        searchResultPanel.getViewport().add(initRecordPanel(products));
        searchResultPanel.validate();
        searchResultPanel.repaint();

        JButton searchBtn = form.getSearchBtn();
        for (ActionListener l : searchBtn.getActionListeners()) {
            searchBtn.removeActionListener(l);
        }
        searchBtn.addActionListener(e -> {
            String searchKey = searchTxtField.getText();
            java.util.List<ProductOrder> searchOrders = productOrderService.searchOrders(searchKey); // 不符合规范
            searchResultPanel.getViewport().add(initRecordPanel(searchOrders));
            searchResultPanel.validate();
            searchResultPanel.repaint();
        });
    }


    /**
     * 订单列表信息渲染到JPanel中，并返回该JPanel
     *
     * @param products 订单列表
     * @return 订单信息JPanel
     */
    private static JPanel initRecordPanel(java.util.List<ProductOrder> products) {
        ProductOrderService productOrderService = SpringBeanUtil.getBean(ProductOrderService.class);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(""));
        boolean isOdd = false;
        for (ProductOrder order : products) {
            SearchItemForm searchItemForm = new SearchItemForm(order, isOdd);
            searchItemForm.addActionListener(e -> {
                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() {
                        ProductOrder savedOrder = productOrderService.save(searchItemForm.getOrder());
                        logger.info(JSON.toJSONString(savedOrder));
                        return null;
                    }

                    @Override
                    protected void done() {
                        searchItemForm.repainEditComponent();
                    }
                };
                worker.execute();
            });
            isOdd = !isOdd;
            JPanel itemPanel = searchItemForm.getSearchItemPanel();
            itemPanel.setBorder(BorderFactory.createTitledBorder(""));
            panel.add(itemPanel);
        }
        panel.validate();
        panel.repaint();
        return panel;
    }
}
