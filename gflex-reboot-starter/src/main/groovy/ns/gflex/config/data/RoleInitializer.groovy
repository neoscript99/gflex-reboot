package ns.gflex.config.data

import ns.gflex.domain.Role
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * Created by Neo on 2017-08-22.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class RoleInitializer extends AbstractDataInitializer implements DataInitializer {

    @Override
    boolean isInited() {
        return (generalRepository.count(Role, null) > 0)
    }

    void doInit() {

        save(new Role(roleName: 'Administrators', roleCode: 'Administrators', editable: false, description: 'System Administrators.'))
        save(new Role(roleName: 'Public', roleCode: 'Public', editable: false, description: 'Public role associate with public menus.'))
    }
}
