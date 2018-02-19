package ind.xwm.gui.ui;

import ind.xwm.gui.ui.initializer.MainPanelInitializer;
import ind.xwm.gui.ui.initializer.ProductPanelInitializer;
import ind.xwm.gui.ui.initializer.RecordPanelInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * Created by XuWeiman on 2017/10/2.
 * UI启动器
 */
@Component
public class UIStarter {
    private static Logger logger = LogManager.getLogger(UIStarter.class);


    /**
     * 应用程序界面初始化
     */
    public void init() {
        PrintForm form = new PrintForm();
        form.getTabbedPane().addChangeListener(e -> {
            JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex == 0) {
                MainPanelInitializer.init(form);
            } else if (selectedIndex == 1) {
                RecordPanelInitializer.init(form);
            } else if (selectedIndex == 2) {
                ProductPanelInitializer.init(form);
            }
        });
        MainPanelInitializer.init(form);

        JFrame frame = new JFrame("金碣路干洗店");
        frame.setContentPane(form.getPanel1());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setPreferredSize(new Dimension(800, 500));
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
