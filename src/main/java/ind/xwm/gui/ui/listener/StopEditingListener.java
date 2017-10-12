package ind.xwm.gui.ui.listener;

import ind.xwm.gui.ui.OrderDetailTable;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by XuWeiman on 2017/10/3.
 * 用于停止 商品表格 的编辑状态
 */
public class StopEditingListener implements MouseListener {
    private OrderDetailTable table;

    public StopEditingListener(OrderDetailTable table) {
        this.table = table;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        CellEditor editor = this.table.getCellEditor();
        if(editor != null) {
            editor.stopCellEditing();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
