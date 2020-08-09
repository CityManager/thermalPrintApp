package ind.xwm.gui.ui.uiComponent;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * Created by XuWeiman on 2017/10/3.
 * 商品数量
 */
public class JSpinnerCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JSpinner jSpinner;

    public JSpinnerCellEditor() {
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        int count = 1;
        if (table != null && value != null) {
            count = Integer.parseInt(String.valueOf(value));
        }
        this.jSpinner = new JSpinner();
        jSpinner.setValue(count);
        return jSpinner;
    }

    @Override
    public Object getCellEditorValue() {
        return String.valueOf(jSpinner.getValue());
    }
}
