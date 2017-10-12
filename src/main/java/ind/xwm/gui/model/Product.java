package ind.xwm.gui.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by XuWeiman on 2017/10/3.
 * 产品信息
 */
@Entity
public class Product implements Serializable {
    private static final long serialVersionUID = 334L;
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "PRICE")
    private String price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
