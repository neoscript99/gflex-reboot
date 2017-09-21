package ns.gflex.domain.association

import grails.gorm.annotation.Entity

import ns.gflex.domain.*;

@Entity
class UserRole implements Serializable{

	Role role
	User user

	static mapping = {
		role fetch:'join',lazy:false
		user fetch:'join',lazy:false
	}
	
	static constraints = {
		role(unique:'user')
	}
}
