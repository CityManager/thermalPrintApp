package ind.xwm.gui.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by XuWeiman on 2017/9/30.
 * 每日订单列表
 */
@Entity
public class OrderId  implements Serializable {
    private static final long serialVersionUID = 334L;
    /**
     * 固定为 orderid
     */
    @Id
    private String id;

    @Column(name = "ORDER_ID")
    private String orderId;

    /**
     * 1 -- enable
     * 0 -- used
     */
    @Column(name = "STATUS")
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
