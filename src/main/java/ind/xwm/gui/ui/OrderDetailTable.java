package ind.xwm.gui.ui;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.math.BigDecimal;
import java.util.Vector;

/**
 * Created by XuWeiman on 2017/10/2.
 * 订单列表
 */
public class OrderDetailTable extends JTable {
    private static Logger logger = LogManager.getLogger(OrderDetailTable.class);
    private static String[] tableHeader = new String[]{"序号", "商品", "数量", "单位", "单价", "金额", "操作"};
    private boolean editable = true;
    private DefaultTableModel tableModel;

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        super.changeSelection(rowIndex, columnIndex, toggle, extend);
        if (columnIndex == 6) {
            ((DefaultTableModel) this.getModel()).removeRow(rowIndex);
            return;
        }
        CellEditor editor = this.getCellEditor();
        if (editor == null || editor.stopCellEditing()) {
            String valueAt = String.valueOf(this.tableModel.getValueAt(rowIndex, columnIndex));
            if (valueAt == null) {
                valueAt = "";
            }
            super.editCellAt(rowIndex, columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return editable && column != 0 && column != 5 && column != 6 && super.isCellEditable(row, column);
    }

    private OrderDetailTable() {
        this.tableModel = new DefaultTableModel(null, tableHeader);
        this.setModel(this.tableModel);
        this.setRowHeight(30);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public void addTableModelListener(TableModelListener listener) {
        this.tableModel.addTableModelListener(listener);
    }

    public void setCellEditor(int columnIndex, TableCellEditor editor) {
        TableColumn tableColumn = this.getColumnModel().getColumn(columnIndex);
        tableColumn.setCellEditor(editor);
    }

    public void setCellRenderer(int columnIndex, TableCellRenderer renderer) {
        TableColumn tableColumn = this.getColumnModel().getColumn(columnIndex);
        tableColumn.setCellRenderer(renderer);
    }

    public void addRowData() {
        String index = String.valueOf(tableModel.getDataVector().size() + 1);
        String[] rowData = new String[]{index, "", "1", "", "", "", ""};
        tableModel.addRow(rowData);
    }

    public static OrderDetailTable getInstance(JTextField totalCountField, JTextField totalPriceField) {
        OrderDetailTable table = new OrderDetailTable();
        DefaultTableModel tableModel = table.getTableModel();
        table.setCellRenderer(6, (table1, value, isSelected, hasFocus, row, column) -> new JButton("删除"));


        tableModel.addTableModelListener(e -> {
            DefaultTableModel model = (DefaultTableModel) e.getSource();
            int row = e.getFirstRow();
            int col = e.getColumn();

            logger.info("修改行{}-{}", row, col);
            if ((col == 2 || col == 4) && e.getType() == TableModelEvent.UPDATE) {
                String numValue = (String) model.getValueAt(row, 2);
                String priceValue = (String) model.getValueAt(row, 4);
                if (!StringUtils.isAllBlank(numValue, priceValue)) {
                    if (StringUtils.isNumeric(numValue) && StringUtils.isNumeric(priceValue.replace(".", ""))) {
                        Integer num = Integer.valueOf(numValue);
                        Float price = Float.valueOf(priceValue);
                        BigDecimal priceDecimal = new BigDecimal(num * price);
                        model.setValueAt(priceDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(), row, 5);
                    } else {
                        model.setValueAt("", row, 5);
                    }
                } else {
                    model.setValueAt("", row, 5);
                }
            }

            Vector<Vector> dataRows = model.getDataVector();
            double total = 0.0;
            int count = 0;
            for (Vector dataRow : dataRows) {
                String rowPrice = String.valueOf(dataRow.get(5));
                String rowCount = String.valueOf(dataRow.get(2));
                if (StringUtils.isNotBlank(rowPrice) && StringUtils.isNumeric(rowPrice.replace(".", ""))) {
                    total += Float.valueOf(rowPrice);
                }
                if (StringUtils.isNotBlank(rowCount) && StringUtils.isNumeric(rowCount)) {
                    count += Integer.valueOf(rowCount);
                }

            }
            logger.info("总计:数量-{}, 金额-{}", count, total);
            BigDecimal totalDecimal = new BigDecimal(total);
            totalCountField.setText(String.valueOf(count));
            totalPriceField.setText("￥" + totalDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        });

        return table;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
