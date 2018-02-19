package ind.xwm.gui.utils;

import ind.xwm.gui.model.ProductOrder;
import ind.xwm.gui.print.P58Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.print.*;

/**
 * Created by XuWeiman on 2017/10/3.
 * 打印工具类
 */
public class PrintUtil {
    private static Logger logger = LogManager.getLogger(PrintUtil.class);

    public static boolean print58(P58Model p58Model) {
        int height = 1000 + p58Model.getHeight();
        Book book = new Book();

        PageFormat pf = new PageFormat();
        pf.setOrientation(PageFormat.PORTRAIT);

        Paper p = new Paper();
        p.setSize(240, height);
        p.setImageableArea(0, 0, 240, height);
        pf.setPaper(p);

        book.append(p58Model, pf);
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(book);
        try {
            logger.info("================开始打印");
            job.print();
        } catch (PrinterException e) {
           logger.info("================打印出现异常");
            return false;
//            return true; // 调试
        }

        return true;
    }

    public static boolean print58(ProductOrder order) {
        P58Model p58Model = new P58Model();
        p58Model.setOrderContent(order);
        return print58(p58Model);
    }
}
