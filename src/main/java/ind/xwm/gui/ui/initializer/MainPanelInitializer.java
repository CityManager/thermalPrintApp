package ind.xwm.gui.ui.initializer;

import ind.xwm.gui.service.OrderIdService;
import ind.xwm.gui.service.ProductService;
import ind.xwm.gui.service.UnitService;
import ind.xwm.gui.ui.OrderDetailTable;
import ind.xwm.gui.ui.PrintForm;
import ind.xwm.gui.ui.listener.PrintButtonListener;
import ind.xwm.gui.ui.listener.ProductEditorListener;
import ind.xwm.gui.ui.listener.StopEditingListener;
import ind.xwm.gui.ui.uiComponent.JSpinnerCellEditor;
import ind.xwm.gui.ui.uiComponent.ProductComboBoxCellEditor;
import ind.xwm.gui.ui.uiComponent.UnitComboBoxCellEditor;
import ind.xwm.gui.utils.SpringBeanUtil;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainPanelInitializer {

    public static void init(PrintForm form) {
        OrderIdService orderIdService = SpringBeanUtil.getBean(OrderIdService.class);
        ProductService productService = SpringBeanUtil.getBean(ProductService.class);
        UnitService unitService = SpringBeanUtil.getBean(UnitService.class);

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
        productEditor.addActionListener(new ProductEditorListener(table));
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
        for (ChangeListener l : payCheckBox.getChangeListeners()) {
            payCheckBox.removeChangeListener(l);
        }
        payCheckBox.addChangeListener(e -> {
            JCheckBox jcb = (JCheckBox) e.getSource();
            if (jcb.isSelected()) {
                jcb.setText("已收款");
            } else {
                jcb.setText("未收款");
            }
        });

        JButton printBtn = form.getOrderPrintBtn();
        for (ActionListener l : printBtn.getActionListeners()) {
            printBtn.removeActionListener(l);
        }
        printBtn.addActionListener(new PrintButtonListener(form, table));
    }
}
