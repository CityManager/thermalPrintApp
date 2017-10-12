package ind.xwm.gui.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by XuWeiman on 2017/9/30.
 * 订单明细
 */
@Entity
public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 334L;

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ORDER_ID")
    private ProductOrder productOrder;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_PRICE")
    private String productPrice;

    @Column(name = "PRODUCT_UNIT")
    private String productUnit;

    @Column(name = "PRODUCT_COUNT")
    private String productCount;

    @Column(name = "AMOUNT")
    private String amount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }
}
