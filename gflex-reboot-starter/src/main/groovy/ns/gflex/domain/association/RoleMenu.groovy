package ns.gflex.domain.association

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import ns.gflex.domain.*;

@Entity
@ToString(includes = ['role', 'menu'])
@EqualsAndHashCode(includes = ['role', 'menu'])
class RoleMenu implements Serializable {
    Role role
    Menu menu

    static mapping = {
        role fetch: 'join', lazy: false
        menu fetch: 'join', lazy: false
    }

    static constraints = {
        menu(unique: 'role')
    }
}
