package ind.xwm.gui.ui.uiComponent;

import ind.xwm.gui.model.Unit;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.Vector;

/**
 * Created by XuWeiman on 2017/10/3.
 */
public class UnitComboBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JComboBox comboBox;
    private Vector<Unit> units;

    public UnitComboBoxCellEditor(Vector<Unit> vector) {
        this.units = vector;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.comboBox = new JComboBox();
        if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
            this.comboBox.addItem(value);
        }
        for (Unit unit : units) {
            if (!unit.getName().equals(value)) {
                this.comboBox.addItem(unit.getName());
            }
        }
        this.comboBox.setEditable(true);
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
