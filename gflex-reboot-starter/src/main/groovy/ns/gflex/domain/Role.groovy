package ns.gflex.domain

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import ns.gflex.config.initialize.InitializeDomian

/**
 * 创建时间 Dec 14, 2010
 * @author wangchu
 */
@Entity
@TupleConstructor
@ToString(includePackage = false, includes = 'roleName')
@EqualsAndHashCode(includes = 'roleCode')
@InitializeDomian
class Role {
    static final Role ADMINISTRATORS = new Role(roleName: 'Administrators', roleCode: 'Administrators',
            editable: false, description: 'System Administrators.')

    static final Role PUBLIC = new Role(roleName: 'Public', roleCode: 'Public',
            editable: false, description: 'Public role associate with public menus.')

    String roleName
    String roleCode
    String description
    Boolean editable = true
    Boolean enabled = true

    static constraints = {
        description nullable: true, maxSize: 128
        roleCode unique: true
    }

    static initList = [ADMINISTRATORS, PUBLIC]
}
