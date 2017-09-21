package ns.gflex.domain.association

import grails.gorm.annotation.Entity

import ns.gflex.domain.*;

@Entity
class RoleMenu implements Serializable{
	Role role
	Menu menu

	static mapping = {
		role fetch:'join',lazy:false
		menu fetch:'join',lazy:false
	}
	
	static constraints = {
		menu(unique:'role')
	}
}
