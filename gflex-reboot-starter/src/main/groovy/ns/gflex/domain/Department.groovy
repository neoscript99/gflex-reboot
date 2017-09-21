package ns.gflex.domain

import grails.gorm.annotation.Entity

@Entity
class Department {
	String name;
	Integer seq;
	Boolean enabled = true

	static init = {
		new Department(name:'总部',seq:1).save()
	}
}
