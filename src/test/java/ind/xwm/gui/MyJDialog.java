package ind.xwm.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by XuWeiman on 2017/10/4.
 */
public class MyJDialog extends JDialog {
    private JPanel panel;
    private JButton next = new JButton("next");

    public MyJDialog() {
        this.setTitle("MyJDialog");
        this.setSize(300, 300);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.add(next, BorderLayout.SOUTH);
        defaultPanel();// 默认界面
        this.setVisible(true);
        next.addActionListener(new ActionListener() {
            // 点击next按钮后，界面更新
            public void actionPerformed(ActionEvent evt) {
                nextPanel(evt);
            }
        });
    }

    private void defaultPanel() {
        panel = new StepOnePanel();
        this.add(panel, BorderLayout.CENTER);
    }

    private void nextPanel(ActionEvent evt) {
        this.remove(panel);
        panel = new StepTwoPanel();
        this.add(panel, BorderLayout.CENTER);
        this.validate();
        this.repaint();
    }

    private class StepOnePanel extends JPanel {
        public StepOnePanel() {
            this.setLayout(new BorderLayout());
            JLabel label = new JLabel("This is StepOnePanel!");
            this.add(label, BorderLayout.CENTER);
        }
    }

    private class StepTwoPanel extends JPanel {
        public StepTwoPanel() {
            this.setLayout(new BorderLayout());
            JLabel label = new JLabel("This is StepTwoPanel!This is StepTwoPanel!");
            this.add(label, BorderLayout.CENTER);
        }
    }

    public static void main(String[] args) {
        MyJDialog dialog = new MyJDialog();
    }
}
