package ind.xwm.gui;

import ind.xwm.gui.ui.UIStarter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring.xml"});
        UIStarter uiStarter = context.getBean(UIStarter.class);
        SwingUtilities.invokeLater(uiStarter::init);
    }
}
