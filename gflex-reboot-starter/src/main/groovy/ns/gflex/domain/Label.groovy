package ns.gflex.domain

import grails.gorm.annotation.Entity

@Entity
class Label {
	String ownerId
	String label
	
	static mapping = {
		id generator:'increment'
		ownerId index:"idx_label_owner" }
	
	static constraints = {
		ownerId(unique:'label')
		label empty:false
	}
}
