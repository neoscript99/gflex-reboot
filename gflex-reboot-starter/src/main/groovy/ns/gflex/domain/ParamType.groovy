package ns.gflex.domain

import grails.gorm.annotation.Entity

/**
 * Functions
 * @since Dec 23, 2010
 * @author wangchu
 */
@Entity
class ParamType {
	String code
	String name
	Integer seq

	static mapping={
		id name:'code',generator:'assigned'
		sort 'seq'
	}
}
