package ns.gflex.config.data

import ns.gflex.domain.Department
import ns.gflex.domain.Role
import ns.gflex.domain.User
import ns.gflex.domain.association.UserRole
import ns.gflex.util.EncoderUtil
import org.springframework.core.annotation.Order

/**
 * Created by Neo on 2017-08-22.
 */
@Order(300)
class UserInitializer extends AbstractDataInitializer implements DataInitializer {

    @Override
    boolean isInited() {
        return (generalRepository.count(User, null) > 0)
    }

    void doInit() {
        def dept = generalRepository.findFirst(Department, [eq: [['name', '总部']]])
        def admin = save(new User(account: 'admin', name: '系统管理员', dept: dept, editable: false, password: EncoderUtil.md5('admin')))
        def anonymous = save(new User(account: 'anonymous', name: '匿名帐号', dept: dept, editable: false, password: EncoderUtil.md5('anonymous')))

        def roleAdmin = generalRepository.findFirst(Role, [eq: [['roleCode', 'Administrators']]])
        def rolePublic = generalRepository.findFirst(Role, [eq: [['roleCode', 'Public']]])

        save(new UserRole(user: admin, role: roleAdmin))
        save(new UserRole(user: admin, role: rolePublic))
        save(new UserRole(user: anonymous, role: rolePublic))
    }
}
