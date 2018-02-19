package ind.xwm.gui.print;

import ind.xwm.gui.model.OrderDetail;
import ind.xwm.gui.model.ProductOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.time.LocalDateTime;
import java.util.Vector;

/**
 * Created by XuWeiman on 2017/10/3.
 * 58 热敏机打印实体
 */
public class P58Model implements Printable {
    private static Logger logger = LogManager.getLogger(P58Model.class);

    private String header;
    private String footer;
    private String remind;
    private Vector<String> mainContents;
    private int height;

    public P58Model() {
        this.header = "金碣干洗店取件单";
        this.footer = "本店电话：xxxxxxxx\r\n地址：陆丰金碣路xx号";
        this.remind = "凭单取件\r\n开单后3至5个工作日可取\r\n祝您生活愉快！";

        this.mainContents = new Vector<>();
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        drawContent(graphics);
        return Printable.PAGE_EXISTS;
    }

    private void drawContent(Graphics graphics) {
        int x = 0;
        int y = 50;
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setFont(new Font("Default", Font.BOLD, 16));
        g2d.drawString(this.header, 3, y);
        y += 10;
        g2d.drawString("--------------------------------------------", x, y);
        y += 20;
        g2d.setFont(new Font("Default", Font.PLAIN, 12));
        for (String content : this.mainContents) {
            g2d.drawString(content, x, y);
            y += 16;
        }

        g2d.drawString("--------------------------------------------", x, y);
        y += 10;
        g2d.setFont(new Font("Default", Font.PLAIN, 12));
        String[] footerItems = this.footer.split("\r\n");
        for (String item : footerItems) {
            g2d.drawString(item, x, y);
            y += 16;
        }
        String[] remindItems = this.remind.split("\r\n");
        if (remindItems.length > 0) {
            g2d.drawString("温馨提示：", x, y);
            y += 16;
        }
        for (String item : remindItems) {
            g2d.drawString(item, 2, y);
            y += 16;
        }


        this.height += y;
    }

    public void setOrderContent(ProductOrder order) {
        this.mainContents.add("客户：" + order.getCustomerName());
        this.mainContents.add("电话：" + order.getCustomerPhone());
        this.mainContents.add("..................................................");
        this.mainContents.add("单号：" + order.getOrderId().substring(8));
        this.mainContents.add("订单时间：" + order.getOrderTime());
        this.mainContents.add("打印时间：" + LocalDateTime.now());
        this.mainContents.add("..................................................");
        this.mainContents.add("名称  数量 单价   金额      ");

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            String detailStr = "";
            if (orderDetail.getProductName().length() > 3) {
                detailStr += orderDetail.getProductName().substring(0, 3);
            } else {
                detailStr += orderDetail.getProductName();
                for (int i = 0; i < 4 - orderDetail.getProductName().length(); i++) {
                    detailStr += " ";
                }

            }
            detailStr += orderDetail.getProductCount();
            detailStr += "  ";
            detailStr += "￥";
            if (orderDetail.getProductPrice().length() == 4) {
                detailStr += orderDetail.getProductPrice();
            } else {
                detailStr += orderDetail.getProductPrice();
                for (int i = 0; i < 4 - orderDetail.getProductPrice().length(); i++) {
                    detailStr += " ";
                }
            }
            detailStr += " ";
            detailStr += "￥";
            detailStr += orderDetail.getAmount();
            this.mainContents.add(detailStr);
        }
        this.mainContents.add("..................................................");
        this.mainContents.add("总数：" + order.getTotalCount());
        this.mainContents.add("总额：" + order.getTotalPrice());
        if (order.getPayStatus() == 1) {
            this.mainContents.add("收款：已收款");
        } else {
            this.mainContents.add("收款：未收款");
        }

        if (order.getDeliverStatus() == 1) {
            this.mainContents.add("取件：已取件");
        } else {
            this.mainContents.add("取件：未取件");
        }

    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public Vector<String> getMainContents() {
        return mainContents;
    }

    public void setMainContents(Vector<String> mainContents) {
        this.mainContents = mainContents;
    }
}
