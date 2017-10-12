package ind.xwm.gui.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by XuWeiman on 2017/10/3.
 * 单位表
 */
@Entity
public class Unit implements Serializable {
    private static final long serialVersionUID = 334L;
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "NAME")
    private String name;

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
}
