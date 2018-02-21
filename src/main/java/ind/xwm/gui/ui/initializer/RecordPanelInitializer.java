package ind.xwm.gui.ui.initializer;

import ind.xwm.gui.model.ProductOrder;
import ind.xwm.gui.service.ProductOrderService;
import ind.xwm.gui.ui.PrintForm;
import ind.xwm.gui.ui.SearchItemForm;
import ind.xwm.gui.utils.SpringBeanUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.swing.*;
import java.awt.event.ActionListener;

public class RecordPanelInitializer {
    private static Logger logger = LogManager.getLogger(RecordPanelInitializer.class);
    private static int totalPageCount = 1;
    private static int currentPage = 0;

    public static void init(PrintForm form) {
        clearListener(form);
        currentPage = 0;
        Pageable pageable = new PageRequest(currentPage, 10, new Sort(Sort.Direction.DESC, "orderTime"));
        doSearch(form, pageable);

        form.getSearchBtn().addActionListener(e -> {
            currentPage = 0;
            Pageable pageRequest = new PageRequest(currentPage, 10, new Sort(Sort.Direction.DESC, "orderTime"));
            doSearch(form, pageRequest);
        });

        form.getRecordPageUpBtn().addActionListener(e -> {
            if (currentPage > 0) {
                Pageable pageRequest = new PageRequest(--currentPage, 10, new Sort(Sort.Direction.DESC, "orderTime"));
                doSearch(form, pageRequest);
            }

        });

        form.getRecordPageDownBtn().addActionListener(e -> {

            if (currentPage + 1 < totalPageCount) {
                Pageable pageRequest = new PageRequest(++currentPage, 10, new Sort(Sort.Direction.DESC, "orderTime"));
                doSearch(form, pageRequest);
            }

        });
    }


    private static void doSearch(PrintForm form, Pageable pageable) {
        String searchKey = form.getSearchField().getText();
        SwingWorker<Page<ProductOrder>, Void> worker = new SwingWorker<Page<ProductOrder>, Void>() {
            @Override
            protected Page<ProductOrder> doInBackground() throws Exception {
                ProductOrderService productOrderService = SpringBeanUtil.getBean(ProductOrderService.class);
                return productOrderService.searchOrders(pageable, searchKey);
            }

            @Override
            protected void done() {
                try {
                    Page<ProductOrder> page = get();
                    totalPageCount = page.getTotalPages();
                    JScrollPane searchResultPanel = form.getSearchResultPanel();
                    searchResultPanel.getViewport().add(initRecordPanel(page.getContent()));
                    searchResultPanel.validate();
                    searchResultPanel.repaint();
                    form.getRecordPageLabel().setText((currentPage + 1) + "/" + totalPageCount);
                } catch (Exception e) {
                    logger.info("销售记录列表异常-", e);
                }
            }
        };
        worker.execute();
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
                        productOrderService.save(searchItemForm.getOrder());
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

    private static void clearListener(PrintForm form) {
        JButton searchBtn = form.getSearchBtn();
        for (ActionListener l : searchBtn.getActionListeners()) {
            searchBtn.removeActionListener(l);
        }
    }
}
