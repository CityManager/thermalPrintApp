package ind.xwm.gui.repository;

import ind.xwm.gui.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by XuWeiman on 2017/10/3.
 * Unit dao
 */
@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {
    Unit findByName(String name);
}
