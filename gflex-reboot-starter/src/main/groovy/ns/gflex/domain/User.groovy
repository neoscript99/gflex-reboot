package ns.gflex.domain

import grails.gorm.annotation.Entity

@Entity
class User {
	String account
	String password
	String name
	Boolean editable = true
	Boolean enabled = true
	Department dept;
	static mapping = {
		dept fetch:'join',lazy:false
	}
	static constraints = { account unique:true }
}
