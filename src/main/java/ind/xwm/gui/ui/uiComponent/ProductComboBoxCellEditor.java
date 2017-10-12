package ind.xwm.gui.ui.uiComponent;

import ind.xwm.gui.model.Product;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * Created by XuWeiman on 2017/10/3.
 * 商品 table编辑器
 */
public class ProductComboBoxCellEditor  extends AbstractCellEditor implements TableCellEditor {
    private JComboBox comboBox;
    private Vector<Product> products;

    private ActionListener listener;

    private ProductComboBoxCellEditor() {

    }

    public void addActionListener(ActionListener l) {
        this.listener = l;
    }

    public ProductComboBoxCellEditor(Vector<Product> products) {
        this.products = products;

    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.comboBox = new JComboBox();
        this.comboBox.setFont(new Font("Default", Font.PLAIN, 16));
        if(value != null && StringUtils.isNotBlank(String.valueOf(value))) {
            this.comboBox.addItem(value);
        }
        for(Product product: products) {
            if(!product.getName().equals(value)) {
                this.comboBox.addItem(product.getName());
            }
        }
        this.comboBox.setEditable(true);
        this.comboBox.addActionListener(this.listener);
        return this.comboBox;
    }

    @Override
    public Object getCellEditorValue() {
        return this.comboBox.getEditor().getItem();
    }



    public JComboBox getComboBox() {
        return comboBox;
    }

    public void setComboBox(JComboBox comboBox) {
        this.comboBox = comboBox;
    }
}
