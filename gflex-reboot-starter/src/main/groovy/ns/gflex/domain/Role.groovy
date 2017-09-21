package ns.gflex.domain

import grails.gorm.annotation.Entity

/**
 * 创建时间 Dec 14, 2010
 * @author wangchu
 */
@Entity
class Role {
	String roleName
	String roleCode
	String description
	Boolean editable = true
	Boolean enabled = true

	static constraints = {
		description nullable:true, maxSize:128
		roleCode unique:true
	}
}
