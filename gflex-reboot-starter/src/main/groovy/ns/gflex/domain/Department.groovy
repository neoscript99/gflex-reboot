package ns.gflex.domain

import grails.gorm.annotation.Entity
import groovy.transform.ToString
import ns.gflex.config.data.InitializeDomian

@Entity
@ToString(includePackage = false, includes = 'name')
@InitializeDomian('deptList')
class Department {
    static final Department HEAD_OFFICE = new Department(name: '总部', seq: 1);
    String name;
    Integer seq;
    Boolean enabled = true

    static deptList = [HEAD_OFFICE]
}
