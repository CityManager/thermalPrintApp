package ind.xwm.gui.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by XuWeiman on 2017/9/30.
 * 订单记录表
 */
@Entity
public class ProductOrder {
    @Id
    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_PHONE")
    private String customerPhone;

    @Column(name = "ORDER_TIME")
    private String orderTime;

    /**
     * 1 -- 已付款
     */
    @Column(name = "PAY_STATUS")
    private Integer payStatus;

    /**
     * 1 -- 已取件
     */
    @Column(name = "DELIVER_STATUS")
    private Integer deliverStatus;

    /**
     * 1 -- 已打印
     */
    @Column(name = "PRINT_STATUS")
    private Integer printStatus;

    @Column(name = "TOTAL_COUNT")
    private String totalCount;

    @Column(name = "TOTAL_PRICE")
    private String totalPrice;


    @OneToMany(mappedBy = "productOrder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderDetail> orderDetails;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(Integer printStatus) {
        this.printStatus = printStatus;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Integer getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(Integer deliverStatus) {
        this.deliverStatus = deliverStatus;
    }
}
