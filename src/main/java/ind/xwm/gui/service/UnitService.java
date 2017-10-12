package ind.xwm.gui.service;

import ind.xwm.gui.model.Unit;
import ind.xwm.gui.repository.UnitRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Vector;

/**
 * Created by XuWeiman on 2017/10/3.
 * 单位 service
 */
@Service
public class UnitService {
    @Resource
    private UnitRepository unitDao;

    public Vector<Unit> findAll() {
        Vector<Unit> vector = new Vector<>();
        vector.addAll(unitDao.findAll());
        return vector;
    }

    public Unit findByName(String name) {
        return unitDao.findByName(name);
    }

    public void save(Unit unit) {
        unitDao.save(unit);
    }

    public void save(String name) {
        if(StringUtils.isBlank(name)) {
            return;
        }
        Unit unit = unitDao.findByName(name);
        if(unit == null) {
            unit = new Unit();
            unit.setName(name);
            unitDao.save(unit);
        }
    }
}
