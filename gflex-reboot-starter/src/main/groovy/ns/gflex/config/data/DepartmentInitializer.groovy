package ns.gflex.config.data

import ns.gflex.domain.*
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * Created by Neo on 2017-08-22.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class DepartmentInitializer extends AbstractDataInitializer implements DataInitializer {

    @Override
    boolean isInited() {
        return (generalRepository.count(Department, null) > 0)
    }

    void doInit() {
        save(new Department(name: '总部', seq: 1))
    }
}
