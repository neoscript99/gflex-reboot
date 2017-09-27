package ns.gflex.domain.association

import grails.gorm.annotation.Entity
import ns.gflex.config.initialize.InitializeDomian
import ns.gflex.domain.*;

@Entity
@InitializeDomian(depends = [User, Role])
class UserRole implements Serializable {

    Role role
    User user

    static mapping = {
        role fetch: 'join', lazy: false
        user fetch: 'join', lazy: false
    }

    static constraints = {
        role(unique: 'user')
    }
    static initList = [
            (new UserRole(user: User.ADMIN, role: Role.ADMINISTRATORS)),
            (new UserRole(user: User.ADMIN, role: Role.PUBLIC)),
            (new UserRole(user: User.ANONYMOUS, role: Role.PUBLIC)),]
}
